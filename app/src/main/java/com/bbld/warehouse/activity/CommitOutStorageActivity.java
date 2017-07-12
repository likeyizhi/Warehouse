package com.bbld.warehouse.activity;

import android.Manifest;
import android.app.AlertDialog;
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
import com.bbld.warehouse.bean.StorageDetails;
import com.bbld.warehouse.db.UserDataBaseOperate;
import com.bbld.warehouse.db.UserSQLiteOpenHelper;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.utils.UploadUserInformationByPostService;
import com.bbld.warehouse.zxing.android.CaptureActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 其他订单出库
 * Created by likey on 2017/7/12.
 */

public class CommitOutStorageActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_orderNumber)
    TextView tvOrderNumber;
    @BindView(R.id.lv_fahuo)
    ListView lv_fahuo;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tv_name_phone)
    TextView tvNamePhone;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.btn_out)
    Button btnOut;

    private String orderCount;
    private int type;
    private String storageId;
    private List<StorageDetails.StorageDetailsInfo.StorageDetailsProductList> products;
    private UserSQLiteOpenHelper mUserSQLiteOpenHelper;
    private UserDataBaseOperate mUserDataBaseOperate;
    private String storageID;
    private CommitOutAdapter adapter;
    private String request;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    showToast(""+request);
                    //出库成功清空数据库，释放当前acticity
                    mUserDataBaseOperate.deleteAll();
                    ActivityManagerUtil.getInstance().finishActivity(CommitOutStorageActivity.this);
                    break;
                case 222:
                    showToast(""+request);
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(CommitOutStorageActivity.this);
        mUserDataBaseOperate = new UserDataBaseOperate(mUserSQLiteOpenHelper.getWritableDatabase());
        loadData();
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBackDialog();
            }
        });
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
//                            x.setSerialNumber(sqlProducts.get(q).getSerialNumber()+"");
//                            x.setBatchNumber(sqlProducts.get(q).getBatchNumber()+"");
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
                                request= UploadUserInformationByPostService.commitOutStorage(new MyToken(CommitOutStorageActivity.this).getToken()+""
                                        ,storageId+"",codejson);
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

    private void loadData() {
        Call<StorageDetails> call= RetrofitService.getInstance().storageDetails(type, new MyToken(CommitOutStorageActivity.this).getToken()+"", storageId);
        call.enqueue(new Callback<StorageDetails>() {
            @Override
            public void onResponse(Response<StorageDetails> response, Retrofit retrofit) {
                if (response.body()==null){
                    showToast("服务器错误");
                    return;
                }
                if (response.body().getStatus()==0){
                    StorageDetails.StorageDetailsInfo info = response.body().getInfo();
                    setData(info);
                }else{
                    showToast(response.body().getMes()+"");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setData(StorageDetails.StorageDetailsInfo info) {
        storageID=info.getStorageID();
        tvType.setText(info.getTypeName()+"");
        tvNamePhone.setText(info.getLinkName()+"("+info.getLinkPhone()+")");
        tvRemark.setText(info.getRemark()+"");
        tvOrderNumber.setText("订单号："+info.getStorageNumber()+"");
        products=info.getProductList();
        setAdapter();
    }

    private void setAdapter() {
        adapter=new CommitOutAdapter();
        lv_fahuo.setAdapter(adapter);
    }

    class CommitOutAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public StorageDetails.StorageDetailsInfo.StorageDetailsProductList getItem(int i) {
            return products.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            CommitOutHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_commit_out,null);
                holder=new CommitOutHolder();
                holder.ivProductImg=(ImageView)view.findViewById(R.id.ivProductImg);
                holder.tvProductName=(TextView)view.findViewById(R.id.tvProductName);
                holder.tvShouldCount=(TextView)view.findViewById(R.id.tvShouldCount);
                holder.tvSacnCount=(TextView)view.findViewById(R.id.tvSacnCount);
                holder.tvProductSpec=(TextView)view.findViewById(R.id.tvProductSpec);
                holder.btn_info=(Button)view.findViewById(R.id.btn_info);
                holder.btn_scan=(Button)view.findViewById(R.id.btn_scan);
                view.setTag(holder);
            }
            holder= (CommitOutHolder) view.getTag();
            final StorageDetails.StorageDetailsInfo.StorageDetailsProductList product = getItem(i);
            Glide.with(getApplicationContext()).load(product.getProductImg()).error(R.mipmap.xiuzhneg).into(holder.ivProductImg);
            holder.tvProductName.setText(product.getProductName()+"");
            holder.tvShouldCount.setText(product.getProductCount()+"");
            holder.tvSacnCount.setText(setScanCount(product.getProductID()+""));
            holder.tvProductSpec.setText(product.getProductSpec()+"");
            holder.btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("productId", product.getProductID()+"");
                    bundle.putString("productName",product.getProductName()+"");
                    bundle.putString("needCount", product.getProductCount()+"");
                    readyGo(CaptureFinishActivity.class, bundle);
                }
            });
            holder.btn_scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toScan(product.getProductID(),product.getProductName(),product.getProductCount(),type+"");
                }

                private void toScan(String productID, String productName, String productCount,String type) {
                    if (Build.VERSION.SDK_INT >= 23){
                        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                        if (cameraPermission != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(CommitOutStorageActivity.this, new String[]{Manifest.permission.CAMERA}, 123);
                            return;
                        }else{
                            Bundle bundle=new Bundle();
                            bundle.putString("productId", productID);
                            bundle.putString("productName",productName);
                            bundle.putString("type", type+"");
                            bundle.putString("needCount", productCount+"");
                            bundle.putString("storage", "yes");
                            bundle.putString("other", "yes");
                            readyGo(CaptureActivity.class, bundle);
                        }
                    }else{
                        Bundle bundle=new Bundle();
                        bundle.putString("productId", productID);
                        bundle.putString("productName",productName);
                        bundle.putString("type", type+"");
                        bundle.putString("needCount", productCount+"");
                        bundle.putString("storage", "yes");
                        bundle.putString("other", "yes");
                        readyGo(CaptureActivity.class, bundle);
                    }
                }
            });
            return view;
        }

        class CommitOutHolder{
            ImageView ivProductImg;
            TextView tvProductName;
            TextView tvShouldCount;
            TextView tvSacnCount;
            TextView tvProductSpec;
            Button btn_info;
            Button btn_scan;
        }
    }

    private String setScanCount(String s) {
        List<CartSQLBean> thisPros = mUserDataBaseOperate.findUserById(s);
        int scanCount = 0;
        for (int i=0;i<thisPros.size();i++){
            scanCount=scanCount+thisPros.get(i).getProCount();
        }
        return scanCount+"";
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            showBackDialog();
        }
        return false;
    }
    private void showBackDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(CommitOutStorageActivity.this);
        builder.setMessage("退出将清空已扫的产品");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mUserDataBaseOperate.deleteAll();
                dialogInterface.dismiss();
                ActivityManagerUtil.getInstance().finishActivity(CommitOutStorageActivity.this);
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
    protected void onRestart() {
        super.onRestart();
        loadData();
    }
    @Override
    protected void getBundleExtras(Bundle extras) {
        orderCount=extras.getString("typeId");
        type=extras.getInt("type");
        storageId=extras.getString("storageId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_commit_out_storage;
    }
}
