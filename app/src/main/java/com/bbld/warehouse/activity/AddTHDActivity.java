package com.bbld.warehouse.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.AddOutBoundProduct;
import com.bbld.warehouse.bean.CartSQLBean;
import com.bbld.warehouse.bean.ClearScanCode;
import com.bbld.warehouse.bean.CodeJson;
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
 * 添加退货单
 * Created by likey on 2017/9/6.
 */

public class AddTHDActivity extends BaseActivity{
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_addProduct)
    TextView tvAddProduct;
    @BindView(R.id.lv_addTHD)
    ListView lvAddTHD;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.et_remark)
    EditText etRemark;

    private String uuid;
    private String token;
    private Dialog loading;
    private List<MyAppInfo> appInfos;
    private boolean is_iData;
    private UserSQLiteOpenHelper mUserSQLiteOpenHelper;
    private UserDataBaseOperate mUserDataBaseOperate;
    private ArrayList<AddOutBoundProduct> productList;
    private boolean isInit=false;
    private AddTHDAdapter adapter;
    private Dialog thdDialog;
    private String request;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    WeiboDialogUtils.closeDialog(thdDialog);
                    btnSubmit.setClickable(true);
                    showToast(""+request);
                    //出库成功清空数据库，释放当前acticity
                    mUserDataBaseOperate.deleteAll();
                    ActivityManagerUtil.getInstance().finishActivity(AddTHDActivity.this);
                    break;
                case 222:
                    WeiboDialogUtils.closeDialog(thdDialog);
                    btnSubmit.setClickable(true);
                    showToast(""+request);
                    break;
                case 1101:
                    for (int i=0;i<appInfos.size();i++) {
                        if (appInfos.get(i).getAppName().equals("com.android.auto.iscan")) {
                            is_iData=true;
                        }
                    }
                    WeiboDialogUtils.closeDialog(loading);
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        uuid= UUID.randomUUID().toString();
        mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(this);
        mUserDataBaseOperate = new UserDataBaseOperate(mUserSQLiteOpenHelper.getWritableDatabase());
        productList=new ArrayList<AddOutBoundProduct>();
        initAppList();
        setNumber();
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBackDialog();
            }
        });
        tvAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoForResult(SelectGoodsActivity.class, 1020);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubmit.setClickable(false);
                thdDialog=WeiboDialogUtils.createLoadingDialog(AddTHDActivity.this,getString(R.string.caozuo_ing));
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
                            request= UploadUserInformationByPostService.refundCommitRefund(new MyToken(AddTHDActivity.this).getToken()+"",
                                    ""+etNumber.getText(),
                                    ""+uuid,
                                    ""+etRemark.getText(),
                                    codejson);
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

    private void initAppList(){
        loading= WeiboDialogUtils.createLoadingDialog(AddTHDActivity.this,"加载中...");
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                appInfos = ApkTool.scanLocalInstallAppList(AddTHDActivity.this.getPackageManager());
                handler.sendEmptyMessage(1101);
            }
        }.start();
    }

    private void setNumber() {
        etNumber.setText("THD"+getTime());
        etNumber.setFocusable(false);
    }

    public String getTime(){
        long time=System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳
        String  str=String.valueOf(time);
        return str;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1020){
            if (data.getStringExtra("init").equals("yes")){
                String productid = data.getStringExtra("productid");
                String productimg = data.getStringExtra("productimg");
                String productName = data.getStringExtra("productName");
                String productSpec = data.getStringExtra("productSpec");
                AddOutBoundProduct product = new AddOutBoundProduct();
                product.setId(productid);
                product.setImg(productimg);
                product.setName(productName);
                product.setSpec(productSpec);
                product.setScanCount(0);
                for (int p=0;p<productList.size();p++){
                    if (productList.get(p).getId().equals(product.getId())){
                        isInit=true;
                        showToast("该商品已经存在");
                    }
                }
                if (!isInit){
                    productList.add(product);
                }
                isInit=false;
            }
            setAdapter();
        }
    }

    private void setAdapter() {
        adapter=new AddTHDAdapter();
        lvAddTHD.setAdapter(adapter);
    }

    class AddTHDAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public AddOutBoundProduct getItem(int i) {
            return productList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            AddTHDHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_add_outbound_order, null);
                holder=new AddTHDHolder();
                holder.iv_img=(ImageView)view.findViewById(R.id.iv_img);
                holder.tv_title=(TextView)view.findViewById(R.id.tv_title);
                holder.tv_spec=(TextView)view.findViewById(R.id.tv_spec);
                holder.tv_scanCount=(TextView)view.findViewById(R.id.tv_scanCount);
                holder.tv_toScan=(TextView)view.findViewById(R.id.tv_toScan);
                holder.tv_toInfo=(TextView)view.findViewById(R.id.tv_toInfo);
                view.setTag(holder);
            }
            holder= (AddTHDHolder) view.getTag();
            final AddOutBoundProduct product = getItem(i);
            Glide.with(getApplicationContext()).load(product.getImg()).error(R.mipmap.xiuzhneg).into(holder.iv_img);
            holder.tv_title.setText(product.getName()+"");
            holder.tv_spec.setText(product.getSpec()+"");
            holder.tv_scanCount.setText("扫码数量:"+product.getScanCount()+"(盒)");
            holder.tv_toScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (is_iData){
                        Bundle bundle=new Bundle();
                        bundle.putString("productId", product.getId());
                        bundle.putString("productName",product.getName());
                        bundle.putString("type", 3+"");
                        bundle.putString("needCount", 1000000+"");
                        bundle.putString("storage", "yes");
                        bundle.putString("other", "no");
                        bundle.putString("uuid", uuid);
                        bundle.putString("showBS", "yes");
                        readyGo(IDataScanTHDActivity.class, bundle);
                    }else{
                        toScan(product.getId(),product.getName(),3,100000);
                    }
                }

                private void toScan(String productID, String productName, int type, int productCount) {
                    if (Build.VERSION.SDK_INT >= 23){
                        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                        if (cameraPermission != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(AddTHDActivity.this, new String[]{Manifest.permission.CAMERA}, 123);
                            return;
                        }else{
                            Bundle bundle=new Bundle();
                            bundle.putString("productId", productID);
                            bundle.putString("productName",productName);
                            bundle.putString("type", type+"");
                            bundle.putString("needCount", productCount+"");
                            bundle.putString("storage", "yes");
                            bundle.putString("other", "no");
                            bundle.putString("uuid", uuid);
                            bundle.putString("showBS", "yes");
                            readyGo(CaptureActivity.class, bundle);
                        }
                    }else{
                        Bundle bundle=new Bundle();
                        bundle.putString("productId", productID);
                        bundle.putString("productName",productName);
                        bundle.putString("type", type+"");
                        bundle.putString("needCount", productCount+"");
                        bundle.putString("storage", "yes");
                        bundle.putString("other", "no");
                        bundle.putString("uuid", uuid);
                        bundle.putString("showBS", "yes");
                        readyGo(CaptureActivity.class, bundle);
                    }
                }
            });
            holder.tv_toInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    showToast("明细"+product.getId());
                    Bundle bundle=new Bundle();
                    bundle.putString("productId", product.getId()+"");
                    bundle.putString("productName",product.getName()+"");
                    bundle.putString("needCount", 1000000+"");
                    bundle.putString("showBS", "yes");
                    readyGo(CaptureFinishActivity.class, bundle);
                }
            });
            return view;
        }

        class AddTHDHolder{
            ImageView iv_img;
            TextView tv_title;
            TextView tv_spec;
            TextView tv_scanCount;
            TextView tv_toScan;
            TextView tv_toInfo;
        }
    }

    private void showBackDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AddTHDActivity.this);
        builder.setMessage("退出将清空已扫的产品");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cleanAll();
                dialogInterface.dismiss();
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

    private void cleanAll() {
        Call<ClearScanCode> clearCall= RetrofitService.getInstance().clearScanCodeRefund(new MyToken(AddTHDActivity.this).getToken(),uuid);
        clearCall.enqueue(new Callback<ClearScanCode>() {
            @Override
            public void onResponse(Response<ClearScanCode> response, Retrofit retrofit) {
                if (response==null){
                    showToast("操作失败，请重试");
                    return;
                }
                if (response.body().getStatus()==0){
                    mUserDataBaseOperate.deleteAll();
                    ActivityManagerUtil.getInstance().finishActivity(AddTHDActivity.this);
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                showToast("操作失败,请重试");
            }
        });
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

    }

    @Override
    public int getContentView() {
        return R.layout.activity_add_thd;
    }
}
