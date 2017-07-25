package com.bbld.warehouse.activity;

import android.Manifest;
import android.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.AddOutBoundProduct;
import com.bbld.warehouse.bean.CartSQLBean;
import com.bbld.warehouse.bean.CodeJson;
import com.bbld.warehouse.bean.HandOverSacnFinish;
import com.bbld.warehouse.bean.HandoverEdit;
import com.bbld.warehouse.db.UserDataBaseOperate;
import com.bbld.warehouse.db.UserSQLiteOpenHelper;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.utils.UploadUserInformationByPostService;
import com.bbld.warehouse.scancodenew.scan.CaptureActivity;
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
 * 市场交接--编辑
 * Created by likey on 2017/6/8.
 */

public class TransferEditActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tv_jjr)
    TextView tvJjr;
    @BindView(R.id.tv_zcr)
    TextView tvZcr;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_addProduct)
    TextView tvAddProduct;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.btn_keep)
    Button btnKeep;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.lv_transferEdit)
    ListView lvTransferEdit;

    private String handoverId;
    private List<HandoverEdit.HandoverEditInfo.HandoverEditProductList> netProductList;
    private UserSQLiteOpenHelper mUserSQLiteOpenHelper;
    private UserDataBaseOperate mUserDataBaseOperate;
    private ArrayList<AddOutBoundProduct> productList;
    private TransferEditAdapter adapter;
    private boolean isInit=false;
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
                    ActivityManagerUtil.getInstance().finishActivity(TransferEditActivity.this);
                    break;
                case 222:
                    showToast(""+request);
                    break;
            }
        }
    };
    @Override
    protected void initViewsAndEvents() {
        mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(TransferEditActivity.this);
        mUserDataBaseOperate = new UserDataBaseOperate(mUserSQLiteOpenHelper.getWritableDatabase());
        productList=new ArrayList<AddOutBoundProduct>();
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
        tvAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoForResult(SelectGoodsActivity.class, 1020);
            }
        });
        btnKeep.setOnClickListener(new View.OnClickListener() {
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
                            request= UploadUserInformationByPostService.commitHandOver(new MyToken(TransferEditActivity.this).getToken()+"",
                                    ""+handoverId,
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
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<HandOverSacnFinish> call=RetrofitService.getInstance().handOverSacnFinish(new MyToken(TransferEditActivity.this).getToken(), handoverId);
                call.enqueue(new Callback<HandOverSacnFinish>() {
                    @Override
                    public void onResponse(Response<HandOverSacnFinish> response, Retrofit retrofit) {
                        if (response==null){
                            showToast(getResources().getString(R.string.get_data_fail));
                            return;
                        }
                        if (response.body().getStatus()==0){
                            showToast("操作成功");
                            mUserDataBaseOperate.deleteAll();
                            ActivityManagerUtil.getInstance().finishActivity(TransferEditActivity.this);
                        }else{
                            showToast(response.body().getMes());
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
        });
    }

    private void loadData() {
        Call<HandoverEdit> call= RetrofitService.getInstance().handoverEdit(new MyToken(TransferEditActivity.this).getToken(),handoverId);
        call.enqueue(new Callback<HandoverEdit>() {
            @Override
            public void onResponse(Response<HandoverEdit> response, Retrofit retrofit) {
                if (response==null){
                    showToast(getResources().getString(R.string.get_data_fail));
                    return;
                }
                if (response.body().getStatus()==0){
                    HandoverEdit.HandoverEditInfo handoverEditInfo = response.body().getInfo();
                    setData(handoverEditInfo);
                }else {
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setData(HandoverEdit.HandoverEditInfo handoverEditInfo) {
        tvNumber.setText("交接单号："+handoverEditInfo.getHandoverCode());
        tvJjr.setText(handoverEditInfo.getJjr()+"");
        tvZcr.setText(handoverEditInfo.getZcr()+"");
        tvStatus.setText(handoverEditInfo.getStatus()+"");
        tvRemark.setText(handoverEditInfo.getRemark()+"");
        netProductList=handoverEditInfo.getProductList();
        intoLocationList();
        intoDB();
    }

    private void intoDB() {
        for (int n=0;n<netProductList.size();n++){
            for (int m=0;m<netProductList.get(n).getCodeList().size();m++){
                CartSQLBean sqlBean=new CartSQLBean();
                sqlBean.setProductId(netProductList.get(n).getProductID()+"");
                sqlBean.setProductCode(netProductList.get(n).getCodeList().get(m).getCode()+"");
                sqlBean.setProductType(netProductList.get(n).getCodeList().get(m).getCodeType()+"");
                sqlBean.setProCount(Integer.parseInt(netProductList.get(n).getCodeList().get(m).getCount()));
                mUserDataBaseOperate.insertToUser(sqlBean);
            }
        }
    }
    private void intoLocationList() {
        for (int p=0;p<netProductList.size();p++){
            AddOutBoundProduct product = new AddOutBoundProduct();
            product.setId(netProductList.get(p).getProductID()+"");
            product.setImg(netProductList.get(p).getProductImg()+"");
            product.setName(netProductList.get(p).getProductName()+"");
            product.setSpec(netProductList.get(p).getProductSpec()+"");
            product.setScanCount(Integer.parseInt(netProductList.get(p).getProductCount()+""));
            productList.add(product);
        }
        setAdapter();
    }

    private void setAdapter() {
        adapter=new TransferEditAdapter();
        lvTransferEdit.setAdapter(adapter);
    }

    class TransferEditAdapter extends BaseAdapter{

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
            TransferEditHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_add_outbound_order, null);
                holder=new TransferEditHolder();
                holder.iv_img=(ImageView)view.findViewById(R.id.iv_img);
                holder.tv_title=(TextView)view.findViewById(R.id.tv_title);
                holder.tv_spec=(TextView)view.findViewById(R.id.tv_spec);
                holder.tv_scanCount=(TextView)view.findViewById(R.id.tv_scanCount);
                holder.tv_toScan=(TextView)view.findViewById(R.id.tv_toScan);
                holder.tv_toInfo=(TextView)view.findViewById(R.id.tv_toInfo);
                view.setTag(holder);
            }
            holder= (TransferEditHolder) view.getTag();
            final AddOutBoundProduct product = getItem(i);
            Glide.with(getApplicationContext()).load(product.getImg()).error(R.mipmap.xiuzhneg).into(holder.iv_img);
            holder.tv_title.setText(product.getName()+"");
            holder.tv_spec.setText(product.getSpec()+"");
            holder.tv_scanCount.setText("扫码数量:"+product.getScanCount()+"(盒)");
            holder.tv_toScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    showToast("扫码"+product.getId());
                    toScan(product.getId(),product.getName(),4,10000);
                }

                private void toScan(String productID, String productName, int type, int productCount) {
                    if (Build.VERSION.SDK_INT >= 23){
                        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                        if (cameraPermission != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(TransferEditActivity.this, new String[]{Manifest.permission.CAMERA}, 123);
                            return;
                        }else{
                            Bundle bundle=new Bundle();
                            bundle.putString("productId", productID);
                            bundle.putString("productName",productName);
                            bundle.putString("type", type+"");
                            bundle.putString("needCount", productCount+"");
                            bundle.putString("storage", "yes");
                            readyGo(CaptureActivity.class, bundle);
                        }
                    }else{
                        Bundle bundle=new Bundle();
                        bundle.putString("productId", productID);
                        bundle.putString("productName",productName);
                        bundle.putString("type", type+"");
                        bundle.putString("needCount", productCount+"");
                        bundle.putString("storage", "yes");
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
                    bundle.putString("needCount", "");
                    readyGo(CaptureFinishActivity.class, bundle);
                }
            });
            return view;
        }

        class TransferEditHolder{
            ImageView iv_img;
            TextView tv_title;
            TextView tv_spec;
            TextView tv_scanCount;
            TextView tv_toScan;
            TextView tv_toInfo;
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if (!productList.isEmpty()){
            for (int i=0;i<productList.size();i++){
                List<CartSQLBean> sqlBean = mUserDataBaseOperate.findUserById(productList.get(i).getId() + "");
                if (!sqlBean.isEmpty()){
                    int boxSize=0;
                    for (int s=0;s<sqlBean.size();s++){
                        boxSize=boxSize+sqlBean.get(s).getProCount();
                        productList.get(i).setScanCount(boxSize);
//                        showToast(boxSize+"");
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
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
    private void showBackDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(TransferEditActivity.this);
        builder.setMessage("退出将清空已扫的产品");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mUserDataBaseOperate.deleteAll();
                dialogInterface.dismiss();
                ActivityManagerUtil.getInstance().finishActivity(TransferEditActivity.this);
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
        handoverId=extras.getString("handoverId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_transfer_operation;
    }
}
