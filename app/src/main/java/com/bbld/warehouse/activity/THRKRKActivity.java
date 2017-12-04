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
import com.bbld.warehouse.bean.RefundGetRefundDetail;
import com.bbld.warehouse.db.UserDataBaseOperate;
import com.bbld.warehouse.db.UserSQLiteOpenHelper;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.scancodenew_thrk.scan.CaptureActivity;
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
 * 退货入库-入库
 * Created by likey on 2017/9/7.
 */

public class THRKRKActivity extends BaseActivity{
    @BindView(R.id.tvSHZT)
    TextView tvSHZT;
    @BindView(R.id.tvSHBZ)
    TextView tvSHBZ;
    @BindView(R.id.tv_name_phone)
    TextView tvNamePhone;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_orderNumber)
    TextView tvOrderNumber;
    @BindView(R.id.lv_THSQ)
    ListView lv_THSQ;
    @BindView(R.id.btn_out)
    Button btnSubmit;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private String id;
    private String token;
    private RefundGetRefundDetail.RefundGetRefundDetailInfo info;
    private List<RefundGetRefundDetail.RefundGetRefundDetailInfo.RefundGetRefundDetailProductList> pros;
    private String uuid;
    private Dialog baocunDialog;
    private UserSQLiteOpenHelper mUserSQLiteOpenHelper;
    private UserDataBaseOperate mUserDataBaseOperate;
    private String request;
    private Dialog loading;
    private List<MyAppInfo> appInfos;
    private boolean is_iData;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    WeiboDialogUtils.closeDialog(baocunDialog);
                    btnSubmit.setClickable(true);
                    showToast(""+request);
                    //出库成功清空数据库，释放当前acticity
                    mUserDataBaseOperate.deleteAll();
                    ActivityManagerUtil.getInstance().finishActivity(THRKRKActivity.this);
                    break;
                case 222:
                    WeiboDialogUtils.closeDialog(baocunDialog);
                    btnSubmit.setClickable(true);
                    showToast(""+request);
                    break;
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
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(THRKRKActivity.this);
        mUserDataBaseOperate = new UserDataBaseOperate(mUserSQLiteOpenHelper.getWritableDatabase());
        token=new MyToken(this).getToken();
        uuid= UUID.randomUUID().toString();
        loadData();
        setListeners();
        initAppList();
    }

    private void initAppList(){
        loading=WeiboDialogUtils.createLoadingDialog(THRKRKActivity.this,"加载中...");
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                appInfos = ApkTool.scanLocalInstallAppList(THRKRKActivity.this.getPackageManager());
                handler.sendEmptyMessage(1101);
            }
        }.start();
    }

    private void setListeners() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<pros.size();i++){
                    String proId = pros.get(i).getProductID();
                    List<CartSQLBean> dbPros = mUserDataBaseOperate.findUserById(proId);
                    int dbCount = 0;
                    for (int b=0;b<dbPros.size();b++){
                        dbCount=dbCount+dbPros.get(b).getProCount();
                    }
                    if (pros.get(i).getProductCount().equals(dbCount+"")){
                    }else{
                        showToast(pros.get(i).getProductName()+"未扫描完成");
                        return;
                    }
                }
                btnSubmit.setClickable(false);
                baocunDialog= WeiboDialogUtils.createLoadingDialog(THRKRKActivity.this,getString(R.string.caozuo_ing));
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
                            request= UploadUserInformationByPostService.refundReceiveRefund(new MyToken(THRKRKActivity.this).getToken()+"",
                                    id);
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
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBackDialog();
            }
        });
    }

    private void loadData() {
        Call<RefundGetRefundDetail> call= RetrofitService.getInstance().refundGetRefundDetail(token,id);
        call.enqueue(new Callback<RefundGetRefundDetail>() {
            @Override
            public void onResponse(Response<RefundGetRefundDetail> response, Retrofit retrofit) {
                if (response==null){
                    showToast("数据获取失败,请重试");
                    return;
                }
                if (response.body().getStatus()==0){
                    info=response.body().getInfo();
                    pros=info.getProductList();
                    setData();
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                showToast("数据获取失败,请重试");
            }
        });
    }

    private void setData() {
        tvSHZT.setText(info.getRefundStatus());
        tvSHBZ.setText(info.getAuditRemark());
        tvNamePhone.setText(info.getDealerName()+"("+info.getContactPhone()+")");
        tvRemark.setText(info.getRemark());
        tvDate.setText(info.getDate());
        tvOrderNumber.setText(info.getRefundCode());
        lv_THSQ.setAdapter(new THSQInfoAdapter());
    }

    class THSQInfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pros.size();
        }

        @Override
        public RefundGetRefundDetail.RefundGetRefundDetailInfo.RefundGetRefundDetailProductList getItem(int i) {
            return pros.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            THSQInfoHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_thrk_rk,null);
                holder=new THSQInfoHolder();
                holder.iv_productImg=(ImageView)view.findViewById(R.id.iv_productImg);
                holder.tv_productName=(TextView)view.findViewById(R.id.tv_productName);
                holder.tv_productCount=(TextView)view.findViewById(R.id.tv_productCount);
                holder.btn_toInfo=(Button)view.findViewById(R.id.btn_toInfo);
                holder.btn_toScan=(Button)view.findViewById(R.id.btn_toScan);
                view.setTag(holder);
            }
            final RefundGetRefundDetail.RefundGetRefundDetailInfo.RefundGetRefundDetailProductList item = getItem(i);
            holder= (THSQInfoHolder) view.getTag();
            Glide.with(getApplicationContext()).load(item.getProductImg()).error(R.mipmap.xiuzhneg).into(holder.iv_productImg);
            holder.tv_productName.setText(item.getProductName()+"");
            holder.tv_productCount.setText("数量："+item.getProductCount()+"");
            holder.btn_toInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("id", id);
                    bundle.putString("productId", item.getProductID()+"");
                    readyGo(THSQInfoMxActivity.class,bundle);
                }
            });
            holder.btn_toScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (is_iData){
                        Bundle bundle=new Bundle();
                        bundle.putString("productId", item.getProductID());
                        bundle.putString("productName",item.getProductName());
                        bundle.putString("type", 3+"");
                        bundle.putString("needCount", item.getProductCount());
                        bundle.putString("storage", "yes");
                        bundle.putString("other", "yes");
                        bundle.putString("uuid", uuid);
                        readyGo(IDataScanTHRKActivity.class, bundle);
                    }else{
                        toScan(item.getProductID(),item.getProductName(),3,item.getProductCount());
                    }
                }
                private void toScan(String productID, String productName, int type, String productCount) {
                    if (Build.VERSION.SDK_INT >= 23){
                        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                        if (cameraPermission != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(THRKRKActivity.this, new String[]{Manifest.permission.CAMERA}, 123);
                            return;
                        }else{
                            Bundle bundle=new Bundle();
                            bundle.putString("productId", productID);
                            bundle.putString("refundId", id);
                            bundle.putString("productName",productName);
                            bundle.putString("needCount", productCount);
                            bundle.putString("storage", "yes");
                            bundle.putString("other", "yes");
                            bundle.putString("uuid", uuid);
                            readyGo(CaptureActivity.class, bundle);
                        }
                    }else{
                        Bundle bundle=new Bundle();
                        bundle.putString("productId", productID);
                        bundle.putString("refundId", id);
                        bundle.putString("productName",productName);
                        bundle.putString("needCount", productCount);
                        bundle.putString("storage", "yes");
                        bundle.putString("other", "yes");
                        bundle.putString("uuid", uuid);
                        readyGo(CaptureActivity.class, bundle);
                    }
                }
            });
            return view;
        }

        class THSQInfoHolder{
            ImageView iv_productImg;
            TextView tv_productName,tv_productCount;
            Button btn_toInfo,btn_toScan;
        }
    }

    private void showBackDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(THRKRKActivity.this);
        builder.setMessage("退出将清空已扫的产品");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mUserDataBaseOperate.deleteAll();
                dialogInterface.dismiss();
                ActivityManagerUtil.getInstance().finishActivity(THRKRKActivity.this);
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
    protected void getBundleExtras(Bundle extras) {
        id=extras.getString("id");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_thrk_rk;
    }
}
