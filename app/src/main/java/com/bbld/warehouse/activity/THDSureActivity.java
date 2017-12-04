package com.bbld.warehouse.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.CartSQLBean;
import com.bbld.warehouse.bean.CodeJson;
import com.bbld.warehouse.bean.RefundDetail;
import com.bbld.warehouse.db.UserDataBaseOperate;
import com.bbld.warehouse.db.UserSQLiteOpenHelper;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.scancodenew_thd.scan.CaptureActivity;
import com.bbld.warehouse.utils.ApkTool;
import com.bbld.warehouse.utils.MyAppInfo;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.utils.UploadUserInformationByPostService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 退货单-退货
 * Created by likey on 2017/8/28.
 */

public class THDSureActivity extends BaseActivity{
    @BindView(R.id.tvDealerName)
    TextView tvDealerName;
    @BindView(R.id.tv_name_phone)
    TextView tvNamePhone;
    @BindView(R.id.lv_thd_sure)
    ListView lvThdSure;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_out)
    Button btnOut;

    private String refundId;
    private Dialog loading;
    private List<MyAppInfo> appInfos;
    private boolean is_iData;
    private UserSQLiteOpenHelper mUserSQLiteOpenHelper;
    private UserDataBaseOperate mUserDataBaseOperate;
    private String token;
    private RefundDetail.RefundDetailInfo info;
    private List<RefundDetail.RefundDetailInfo.RefundDetailProductList> pros;
    private THDAdapter adapter;
    private Dialog loadDialog;
    private String request;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1101:
                    for (int i=0;i<appInfos.size();i++) {
                        if (appInfos.get(i).getAppName().equals("com.android.auto.iscan")) {
                            is_iData=true;
                        }
                    }
                    String model = Build.MODEL;
                    if (model.contains("NLS-")){
                        is_iData=true;
                    }
                    WeiboDialogUtils.closeDialog(loading);
                    break;
                case 111:
                    WeiboDialogUtils.closeDialog(loadDialog);
                    btnOut.setClickable(true);
                    showToast(""+request);
                    //出库成功清空数据库，释放当前acticity
                    mUserDataBaseOperate.deleteAll();
                    ActivityManagerUtil.getInstance().finishActivity(THDSureActivity.this);
                    break;
                case 222:
                    WeiboDialogUtils.closeDialog(loadDialog);
                    btnOut.setClickable(true);
                    showToast(""+request);
                    break;
            }
        }
    };
    private String uuid;

    @Override
    protected void initViewsAndEvents() {
        uuid= UUID.randomUUID().toString();
        mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(this);
        mUserDataBaseOperate = new UserDataBaseOperate(mUserSQLiteOpenHelper.getWritableDatabase());
        token=new MyToken(this).getToken();
        loadData();
        initAppList();
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBackDialog();
            }
        });
    }

    private void initAppList(){
        loading= WeiboDialogUtils.createLoadingDialog(THDSureActivity.this,"加载中...");
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                appInfos = ApkTool.scanLocalInstallAppList(THDSureActivity.this.getPackageManager());
                handler.sendEmptyMessage(1101);
            }
        }.start();
    }

    private void loadData() {
        Call<RefundDetail> call= RetrofitService.getInstance().getRefundDetail(token,refundId);
        call.enqueue(new Callback<RefundDetail>() {
            @Override
            public void onResponse(Response<RefundDetail> response, Retrofit retrofit) {
                if (response==null){
                    showToast("数据获取失败");
                    return;
                }
                if (response.body().getStatus()==0){
                    info=response.body().getInfo();
                    pros=info.getProductList();
                    setData();
                }else{
                    showToast(response.body().getMes()+"");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setData() {
        tvDealerName.setText(info.getRefundDealerName()+"");
        tvNamePhone.setText(info.getLinkName()+"("+info.getLinkPhone()+")");
        setAdapter();

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOut.setClickable(false);
                loadDialog= WeiboDialogUtils.createLoadingDialog(THDSureActivity.this,getString(R.string.caozuo_ing));
                List<CartSQLBean> sqlProducts = mUserDataBaseOperate.findAll();
                List<CodeJson.CodeJsonList> A = new ArrayList<CodeJson.CodeJsonList>();
                CodeJson B=new CodeJson();
                for (int j=0;j<sqlProducts.size();j++){
                    Gson gson=new Gson();
                    String AString=gson.toJson(A);
                    if (!(AString.contains(sqlProducts.get(j).getProductId()))){
                        CodeJson.CodeJsonList a = new CodeJson.CodeJsonList();
                        a.setProductID(Integer.parseInt(sqlProducts.get(j).getProductId()));
                        a.setCodeList(new LinkedList<CodeJson.CodeJsonList.CodeJsonCodeList>());
                        A.add(a);
                    }
                }
                for(int q=0;q<sqlProducts.size();q++){
                    for (int k=0;k<A.size();k++){
                        if (sqlProducts.get(q).getProductId().toString().equals(A.get(k).getProductID()+"")){
                            CodeJson.CodeJsonList.CodeJsonCodeList x=new CodeJson.CodeJsonList.CodeJsonCodeList();
                            x.setCode(sqlProducts.get(q).getProductCode()+"");
                            x.setCount(sqlProducts.get(q).getProCount()+"");
                            x.setType(sqlProducts.get(q).getProductType()+"");
                            x.setBatchNumber(sqlProducts.get(q).getBatchNumber()+"");
                            x.setSerialNumber(sqlProducts.get(q).getSerialNumber()+"");
                            A.get(k).getCodeList().add(x);
                            B.setList(A);
                        }
                    }
                }
                Gson gson=new Gson();
                String jsonString=gson.toJson(B);
//                showToast(jsonString);
                //需要参数：token,invoiceid(orderId),codejson
                final String codejson = jsonString;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            request=UploadUserInformationByPostService.commitRefund(token,refundId,codejson);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (request.contains("成功")) { // 请求成功
                            Message message=new Message();
                            message.what=111;
                            handler.sendMessage(message);
                        } else { // 请求失败
                            Message message=new Message();
                            message.what=222;
                            handler.sendMessage(message);
                        }
                    }
                }).start();
            }
        });

    }

    private void setAdapter() {
        adapter=new THDAdapter();
        lvThdSure.setAdapter(adapter);
    }

    class THDAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pros.size();
        }

        @Override
        public RefundDetail.RefundDetailInfo.RefundDetailProductList getItem(int i) {
            return pros.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            THDHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_thd_sure,null);
                holder=new THDHolder();
                holder.ivProductImg=(ImageView)view.findViewById(R.id.ivProductImg);
                holder.tvProductName=(TextView)view.findViewById(R.id.tvProductName);
                holder.tvShouldCount=(TextView)view.findViewById(R.id.tvShouldCount);
                holder.tvSacnCount=(TextView)view.findViewById(R.id.tvSacnCount);
                holder.tvProductSpec=(TextView)view.findViewById(R.id.tvProductSpec);
                holder.btn_info=(Button)view.findViewById(R.id.btn_info);
                holder.btn_scan=(Button)view.findViewById(R.id.btn_scan);
                view.setTag(holder);
            }
            holder= (THDHolder) view.getTag();
            final RefundDetail.RefundDetailInfo.RefundDetailProductList product = getItem(i);
            Glide.with(getApplicationContext()).load(product.getProductImg()).error(R.mipmap.xiuzhneg).into(holder.ivProductImg);
            holder.tvProductName.setText(product.getProductName()+"");
            holder.tvShouldCount.setText(product.getProductAmount()+"");
            holder.tvSacnCount.setText(setScanCount(product.getProductID()+""));
            holder.tvProductSpec.setText(product.getProductSpec()+"");
            holder.btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("productId", product.getProductID()+"");
                    bundle.putString("productName",product.getProductName()+"");
                    bundle.putString("needCount", product.getProductAmount()+"");
                    readyGo(CaptureFinishActivity.class, bundle);
                }
            });
            holder.btn_scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (is_iData){
                        Bundle bundle=new Bundle();
                        bundle.putString("productId", product.getProductID());
                        bundle.putString("productName",product.getProductName());
                        bundle.putString("orderId", info.getRefundCode());
                        bundle.putString("needCount", product.getProductAmount());
                        bundle.putString("storage", "yes");
                        bundle.putString("other", "yes");
                        bundle.putString("type", 1+"");
                        bundle.putInt("NeedBatch", 2);
                        bundle.putString("uuid", uuid);
                        readyGo(IDataScanActivity.class, bundle);
                    }else{
                        toScan(product.getProductID(), product.getProductName(), info.getRefundCode(), product.getProductAmount(), "1");
                    }
                }

                private void toScan(String productID, String productName, String orderId, String productCount,String type) {
                    if (Build.VERSION.SDK_INT >= 23){
                        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                        if (cameraPermission != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(THDSureActivity.this, new String[]{Manifest.permission.CAMERA}, 123);
                            return;
                        }else{
                            Bundle bundle=new Bundle();
                            bundle.putString("productId", productID);
                            bundle.putString("refundId", refundId);
                            bundle.putString("productName",productName);
                            bundle.putString("orderId", orderId);
                            bundle.putString("needCount", productCount);
                            bundle.putString("storage", "yes");
                            bundle.putString("other", "yes");
                            bundle.putString("type", type+"");
                            bundle.putInt("NeedBatch", 2);
                            bundle.putString("uuid", uuid);
                            readyGo(CaptureActivity.class, bundle);
                        }
                    }else{
                        Bundle bundle=new Bundle();
                        bundle.putString("productId", productID);
                        bundle.putString("refundId", refundId);
                        bundle.putString("productName",productName);
                        bundle.putString("orderId", orderId);
                        bundle.putString("needCount", productCount);
                        bundle.putString("storage", "yes");
                        bundle.putString("other", "yes");
                        bundle.putString("type", type+"");
                        bundle.putInt("NeedBatch", 2);
                        bundle.putString("uuid", uuid);
                        readyGo(CaptureActivity.class, bundle);
                    }
                }
            });
            return view;
        }
        private String setScanCount(String s) {
            List<CartSQLBean> thisPros = mUserDataBaseOperate.findUserById(s);
            int scanCount = 0;
            for (int i=0;i<thisPros.size();i++){
                scanCount=scanCount+thisPros.get(i).getProCount();
            }
            return scanCount+"";
        }

        class THDHolder{
            ImageView ivProductImg;
            TextView tvProductName;
            TextView tvShouldCount;
            TextView tvSacnCount;
            TextView tvProductSpec;
            Button btn_info;
            Button btn_scan;
        }
    }

    private void showBackDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(THDSureActivity.this);
        builder.setMessage("退出将清空已扫的产品");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mUserDataBaseOperate.deleteAll();
                dialogInterface.dismiss();
                ActivityManagerUtil.getInstance().finishActivity(THDSureActivity.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            showBackDialog();
        }
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        refundId=extras.getString("RefundId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_thd_sure;
    }
}
