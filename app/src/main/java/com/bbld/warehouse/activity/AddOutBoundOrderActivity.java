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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.AddOutBoundProduct;
import com.bbld.warehouse.bean.CartSQLBean;
import com.bbld.warehouse.bean.CodeJson;
import com.bbld.warehouse.bean.GetTypeList;
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
 * 添加出库单
 * Created by likey on 2017/6/5.
 */

public class AddOutBoundOrderActivity extends BaseActivity{
    @BindView(R.id.tv_addProduct)
    TextView tvAddProduct;
    @BindView(R.id.lv_addOutBound)
    ListView lvAddOutBound;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_linkName)
    EditText etLinkName;
    @BindView(R.id.et_linkPhone)
    EditText etLinkPhone;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_typeId)
    TextView tvTypeId;

    private List<AddOutBoundProduct> productList;
    private AddOutBoundAdapter adapter;
    private UserSQLiteOpenHelper mUserSQLiteOpenHelper;
    private UserDataBaseOperate mUserDataBaseOperate;
    private boolean isInit=false;
    private String request;
    private String type;
    private String[] items;
    private List<GetTypeList.GetTypeListList> typeList;
    private String typeId;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    showToast(""+request);
                    //出库成功清空数据库，释放当前acticity
                    mUserDataBaseOperate.deleteAll();
                    ActivityManagerUtil.getInstance().finishActivity(AddOutBoundOrderActivity.this);
                    break;
                case 222:
                    showToast(""+request);
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(AddOutBoundOrderActivity.this);
        mUserDataBaseOperate = new UserDataBaseOperate(mUserSQLiteOpenHelper.getWritableDatabase());
        productList=new ArrayList<AddOutBoundProduct>();
        loadTypeId();
        setListeners();
    }

    private void loadTypeId() {
        Call<GetTypeList> typeListCall= RetrofitService.getInstance().getTypeList(Integer.parseInt(type));
        typeListCall.enqueue(new Callback<GetTypeList>() {
            @Override
            public void onResponse(Response<GetTypeList> response, Retrofit retrofit) {
                if (response==null){
                    showToast("出入库类型列表获取失败");
                    return;
                }
                if (response.body().getStatus()==0){
                    typeList = response.body().getList();
                    items = new String[typeList.size()];
                    for (int t=0;t<typeList.size();t++){
                        items[t]=typeList.get(t).getName();
                    }
                }else {
                    showToast(""+response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setListeners() {
        tvAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoForResult(SelectGoodsActivity.class, 1020);
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBackDialog();
            }
        });
        tvTypeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseTypeIdDialog();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
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
                            request= UploadUserInformationByPostService.saveStorage(new MyToken(AddOutBoundOrderActivity.this).getToken()+""
                                    ,type,
                                    ""+typeId,
                                    ""+etNumber.getText(),
                                    ""+etLinkName.getText(),
                                    ""+etLinkPhone.getText(),
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
        adapter=new AddOutBoundAdapter();
        lvAddOutBound.setAdapter(adapter);
    }
    class AddOutBoundAdapter extends BaseAdapter{

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
            AddOutBoundHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_add_outbound_order, null);
                holder=new AddOutBoundHolder();
                holder.iv_img=(ImageView)view.findViewById(R.id.iv_img);
                holder.tv_title=(TextView)view.findViewById(R.id.tv_title);
                holder.tv_spec=(TextView)view.findViewById(R.id.tv_spec);
                holder.tv_scanCount=(TextView)view.findViewById(R.id.tv_scanCount);
                holder.tv_toScan=(TextView)view.findViewById(R.id.tv_toScan);
                holder.tv_toInfo=(TextView)view.findViewById(R.id.tv_toInfo);
                view.setTag(holder);
            }
            holder= (AddOutBoundHolder) view.getTag();
            final AddOutBoundProduct product = getItem(i);
            Glide.with(getApplicationContext()).load(product.getImg()).error(R.mipmap.xiuzhneg).into(holder.iv_img);
            holder.tv_title.setText(product.getName()+"");
            holder.tv_spec.setText(product.getSpec()+"");
            holder.tv_scanCount.setText("扫码数量:"+product.getScanCount()+"(盒)");
            holder.tv_toScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    showToast("扫码"+product.getId());
                    toScan(product.getId(),product.getName(),Integer.parseInt(type),10000);
                }

                private void toScan(String productID, String productName, int type, int productCount) {
                    if (Build.VERSION.SDK_INT >= 23){
                        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                        if (cameraPermission != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(AddOutBoundOrderActivity.this, new String[]{Manifest.permission.CAMERA}, 123);
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
                    bundle.putString("needCount", 100+"");
                    readyGo(CaptureFinishActivity.class, bundle);
                }
            });
            return view;
        }

        class AddOutBoundHolder{
            ImageView iv_img;
            TextView tv_title;
            TextView tv_spec;
            TextView tv_scanCount;
            TextView tv_toScan;
            TextView tv_toInfo;
        }
    }
    @Override
    protected void getBundleExtras(Bundle extras) {
        type=extras.getString("type");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_add_outbound_order;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            showBackDialog();
        }
        return false;
    }
    private void showBackDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AddOutBoundOrderActivity.this);
        builder.setMessage("退出将清空已扫的产品");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mUserDataBaseOperate.deleteAll();
                dialogInterface.dismiss();
                ActivityManagerUtil.getInstance().finishActivity(AddOutBoundOrderActivity.this);
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

    private void chooseTypeIdDialog() {
        AlertDialog alertDialog=new AlertDialog.Builder(AddOutBoundOrderActivity.this)
                .setTitle("请选择类型")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        typeId=typeList.get(i).getID();
                        tvTypeId.setText(typeList.get(i).getName());
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }
}