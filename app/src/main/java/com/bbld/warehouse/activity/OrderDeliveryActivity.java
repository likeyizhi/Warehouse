package com.bbld.warehouse.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.base.Constants;
import com.bbld.warehouse.bean.CartSQLBean;
import com.bbld.warehouse.bean.CodeJson;
import com.bbld.warehouse.bean.OrderDetails;
import com.bbld.warehouse.db.UserDataBaseOperate;
import com.bbld.warehouse.db.UserSQLiteOpenHelper;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.ApkTool;
import com.bbld.warehouse.utils.MyAppInfo;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.utils.SettingImage;
import com.bbld.warehouse.utils.UploadUserInformationByPostService;
import com.bbld.warehouse.scancodenew.scan.CaptureActivity;
import com.bbld.warehouse.utils.UploadUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 待出库--出库，待收货--确认收货
 * Created by likey on 2017/5/24.
 */

public class OrderDeliveryActivity extends BaseActivity{
    @BindView(R.id.lv_fahuo)
    ListView lvFahuo;
    @BindView(R.id.tv_orderNumber)
    TextView tvOrderNumber;
    @BindView(R.id.tvChannelName)
    TextView tvChannelName;
    @BindView(R.id.tvDealerName)
    TextView tvDealerName;
    @BindView(R.id.tv_name_phone)
    TextView tvNamePhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.btn_out)
    TextView btnOut;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.iv01)
    ImageView iv01;
    @BindView(R.id.iv02)
    ImageView iv02;
    @BindView(R.id.iv03)
    ImageView iv03;
    @BindView(R.id.iv04)
    ImageView iv04;
    @BindView(R.id.iv05)
    ImageView iv05;
    @BindView(R.id.iv06)
    ImageView iv06;
    @BindView(R.id.iv07)
    ImageView iv07;
    @BindView(R.id.iv08)
    ImageView iv08;
    @BindView(R.id.iv09)
    ImageView iv09;
    @BindView(R.id.llSaoMaSH)
    LinearLayout llSaoMaSH;
    @BindView(R.id.glWrong)
    GridLayout glWrong;
    @BindView(R.id.lvNoPP)
    ListView lvNoPP;
    @BindView(R.id.tvWrongCount)
    TextView tvWrongCount;

    private String invoiceid;
    private String orderCount;
    private String orderId;
    private UserSQLiteOpenHelper mUserSQLiteOpenHelper;
    private UserDataBaseOperate mUserDataBaseOperate;
    private String request;
    private String doType;
    private String type;
    private int isNeedBatch;
    private Dialog loadDialog;
    private Dialog loading;
    private List<MyAppInfo> appInfos;
    private boolean is_iData;
    private List<OrderDetails.OrderDetailsInfo.OrderDetailsProductList> products;
    private ArrayList<String> codes;
    private ArrayList<String> wrongCodes;
    private WrongCodeAdapter wcAdaper;
    private String[] items = new String[]{"选择本地图片", "拍照"};
    private static final int SELECT_PIC_KITKAT = 49;
    private static final int IMAGE_REQUEST_CODE = 50;
    private static final int CAMERA_REQUEST_CODE = 51;
    private static final int RESULT_REQUEST_CODE = 52;
    private String checkedPosition;
    //    private static final String IMAGE_FILE_NAME = "file_img.jpg";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    WeiboDialogUtils.closeDialog(loadDialog);
                    btnOut.setClickable(true);
                    showToast(""+request);
                    //出库成功清空数据库，释放当前acticity
                    mUserDataBaseOperate.deleteAll();
                    ActivityManagerUtil.getInstance().finishActivity(OrderDeliveryActivity.this);
                    if (doType.equals("out")){
                        BackOrderActivity.boActivity.finish();
                        Bundle bundle=new Bundle();
                        bundle.putInt("status", 2);
                        readyGo(BackOrderActivity.class, bundle);
                    }
                    break;
                case 222:
                    WeiboDialogUtils.closeDialog(loadDialog);
                    btnOut.setClickable(true);
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
    private String file_imgPath01="";
    private String file_imgPath02="";
    private String file_imgPath03="";
    private String file_imgPath04="";
    private String file_imgPath05="";
    private String file_imgPath06="";
    private String file_imgPath07="";
    private String file_imgPath08="";
    private String file_imgPath09="";
    private static final int REQUEST_EXTERNAL_STORAGE = 321;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    private String requestImg;

    @Override
    protected void initViewsAndEvents() {
        mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(OrderDeliveryActivity.this);
        mUserDataBaseOperate = new UserDataBaseOperate(mUserSQLiteOpenHelper.getWritableDatabase());
        loadData();
        setListeners();
        setText();
        initAppList();
    }

    private void initAppList(){
        loading=WeiboDialogUtils.createLoadingDialog(OrderDeliveryActivity.this,"加载中...");
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                appInfos = ApkTool.scanLocalInstallAppList(OrderDeliveryActivity.this.getPackageManager());
                handler.sendEmptyMessage(1101);
            }
        }.start();
    }

    private void setText() {
        if (doType.equals("sure")){
            type="2";
            tvTitle.setText("订单收货");
            btnOut.setText("确认收货");
        }else{//out
            type="1";
            tvTitle.setText("订单发货");
            btnOut.setText("发货出库");
        }
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBackDialog();
            }
        });
    }

    public void addImage(View v){
        switch (v.getId()){
            case R.id.iv01:
//                showToast("01");
                checkedPosition="01";
                break;
            case R.id.iv02:
//                showToast("02");
                checkedPosition="02";
                break;
            case R.id.iv03:
//                showToast("03");
                checkedPosition="03";
                break;
            case R.id.iv04:
//                showToast("04");
                checkedPosition="04";
                break;
            case R.id.iv05:
//                showToast("05");
                checkedPosition="05";
                break;
            case R.id.iv06:
//                showToast("06");
                checkedPosition="06";
                break;
            case R.id.iv07:
//                showToast("07");
                checkedPosition="07";
                break;
            case R.id.iv08:
//                showToast("08");
                checkedPosition="08";
                break;
            case R.id.iv09:
//                showToast("09");
                checkedPosition="09";
                break;
        }
        showAddImgDialog();
        verifyStoragePermissions(this);
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void showBackDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(OrderDeliveryActivity.this);
        builder.setMessage("退出将清空已扫的产品");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mUserDataBaseOperate.deleteAll();
                dialogInterface.dismiss();
                ActivityManagerUtil.getInstance().finishActivity(OrderDeliveryActivity.this);
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

    private void loadData() {
        Call<OrderDetails> call= RetrofitService.getInstance().orderDetails(new MyToken(OrderDeliveryActivity.this).getToken()+"", Integer.parseInt(invoiceid+""));
        call.enqueue(new Callback<OrderDetails>() {
            @Override
            public void onResponse(Response<OrderDetails> response, Retrofit retrofit) {
                if (response.body()==null){
                    showToast("服务器错误");
                    return;
                }
                if (response.body().getStatus()==0){
                    OrderDetails.OrderDetailsInfo info = response.body().getInfo();
                    products=info.getProductList();
                    codes=new ArrayList<String>();
                    for (int i=0;i<products.size();i++){
                        for (int p=0;p<products.get(i).getCodeList().size();p++){
                            codes.add(products.get(i).getCodeList().get(p).getCode());
                        }
                    }
                    if (doType.equals("sure")){
                        setWrongCodeAdapter();
                    }else{
                        llSaoMaSH.setVisibility(View.GONE);
                    }
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

    private void setData(final OrderDetails.OrderDetailsInfo info) {
        isNeedBatch=info.getSendNeedBatchNumber();
        orderId=info.getOrderID()+"";
        tvOrderNumber.setText("订单号:"+info.getOrderNumber()+"");
        tvChannelName.setText(info.getChannelName()+"");
        tvDealerName.setText(info.getDealerName()+"");
        tvNamePhone.setText(info.getDeliveryName()+"("+info.getDeliveryPhone()+")");
        tvAddress.setText(info.getDeliveryAddress()+"");
        tvRemark.setText(info.getRemark()+"");
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOut.setClickable(false);
                loadDialog=WeiboDialogUtils.createLoadingDialog(OrderDeliveryActivity.this,getString(R.string.caozuo_ing));
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
                            x.setSerialNumber(sqlProducts.get(q).getSerialNumber()+"");
                            x.setBatchNumber(sqlProducts.get(q).getBatchNumber()+"");
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
                if (!doType.equals("sure")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                request= UploadUserInformationByPostService.save(new MyToken(OrderDeliveryActivity.this).getToken()+""
                                        ,invoiceid+"",codejson);
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
                }else{
                    //收货
                    uploadImg(codejson);
                }
            }
        });
        lvFahuo.setAdapter(new OrderDelAdapter(info.getProductList()));
    }

    private void uploadImg(final String codejson) {
        if (llSaoMaSH.getVisibility()==View.GONE){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        request= UploadUserInformationByPostService.orderReceipt(new MyToken(OrderDeliveryActivity.this).getToken()+""
                                ,invoiceid+"",codejson);
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

//            final Map<String, String> params = new HashMap<String, String>();
//            params.put("token", new MyToken(OrderDeliveryActivity.this).getToken());
//            params.put("invoiceid", invoiceid);
//            params.put("codejson", codejson);
//            final Map<String, File> files = new TreeMap<String, File>();
//            Log.i("params", "params="+params);
//            Log.i("files", "files="+files);
//            final String requestURL = Constants.BASE_URL + "Order/OrderReceipt";
//            Log.i("requestURL", "requestURL="+requestURL);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        request = UploadUtil.post(requestURL, params, files);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    if ((request+"").contains("成功")) { // 请求成功
//                        Message message=new Message();
//                        message.what=111;
//                        handler.sendMessage(message);
//                    } else { // 请求失败
//                        Message message=new Message();
//                        message.what=222;
//                        handler.sendMessage(message);
//                    }
//                }
//            }).start();
        }else{
//            if (file_imgPath01.equals("") || file_imgPath01==null){
//                showToast("请上传异常条码产品");
//                WeiboDialogUtils.closeDialog(loadDialog);
//            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            request= UploadUserInformationByPostService.orderReceipt(new MyToken(OrderDeliveryActivity.this).getToken()+""
                                    ,invoiceid+"",codejson);
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

                final Map<String, String> params = new HashMap<String, String>();
                params.put("token", new MyToken(OrderDeliveryActivity.this).getToken());
//                params.put("invoiceid", invoiceid);
//                params.put("codejson", codejson);
                final Map<String, File> files = new TreeMap<String, File>();
                if (!file_imgPath01.equals("")){
                    files.put("image01",new File(file_imgPath01));
                }
                if (!file_imgPath02.equals("")){
                    files.put("image02",new File(file_imgPath02));
                }
                if (!file_imgPath03.equals("")){
                    files.put("image03",new File(file_imgPath03));
                }
                if (!file_imgPath04.equals("")){
                    files.put("image04",new File(file_imgPath04));
                }
                if (!file_imgPath05.equals("")){
                    files.put("image05",new File(file_imgPath05));
                }
                if (!file_imgPath06.equals("")){
                    files.put("image06",new File(file_imgPath06));
                }
                if (!file_imgPath07.equals("")){
                    files.put("image07",new File(file_imgPath07));
                }
                if (!file_imgPath08.equals("")){
                    files.put("image08",new File(file_imgPath08));
                }
                if (!file_imgPath09.equals("")){
                    files.put("image09",new File(file_imgPath09));
                }
                Log.i("params", "params="+params);
                Log.i("files", "files="+files);
                final String requestURL = Constants.BASE_URL + "Order/OrderCommitError";
                Log.i("requestURL", "requestURL="+requestURL);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            requestImg = UploadUtil.post(requestURL, params, files);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        if ((request+"").contains("成功")) { // 请求成功
//                            Message message=new Message();
//                            message.what=111;
//                            handler.sendMessage(message);
//                        } else { // 请求失败
//                            Message message=new Message();
//                            message.what=222;
//                            handler.sendMessage(message);
//                        }
                    }
                }).start();
//            }
        }
    }

    class OrderDelAdapter extends BaseAdapter{
        private List<OrderDetails.OrderDetailsInfo.OrderDetailsProductList> orders;
        public OrderDelAdapter(List<OrderDetails.OrderDetailsInfo.OrderDetailsProductList> orders){
            super();
            this.orders=orders;
        }

        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public OrderDetails.OrderDetailsInfo.OrderDetailsProductList getItem(int i) {
            return orders.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(orders.get(i).getProductID());
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            OrderDelHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_order_delivery,null);
                holder=new OrderDelHolder();
                holder.ivProductImg=(ImageView)view.findViewById(R.id.ivProductImg);
                holder.tvProductName=(TextView)view.findViewById(R.id.tvProductName);
                holder.tvShouldCount=(TextView)view.findViewById(R.id.tvShouldCount);
                holder.tvSacnCount=(TextView)view.findViewById(R.id.tvSacnCount);
                holder.tvProductSpec=(TextView)view.findViewById(R.id.tvProductSpec);
                holder.btn_info=(Button)view.findViewById(R.id.btn_info);
                holder.btn_scan=(Button)view.findViewById(R.id.btn_scan);
                view.setTag(holder);
            }
            holder= (OrderDelHolder) view.getTag();
            final OrderDetails.OrderDetailsInfo.OrderDetailsProductList product = getItem(i);
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
                    if (doType.equals("sure")){
                        bundle.putString("showBS", "yes");
                    }
                    readyGo(CaptureFinishActivity.class, bundle);
                }
            });
            holder.btn_scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (is_iData){
//                        showToast("iData终端");
                        Bundle bundle=new Bundle();
                        bundle.putString("productId", product.getProductID());
                        bundle.putString("productName",product.getProductName());
                        bundle.putString("orderId", orderId);
                        bundle.putString("needCount", product.getProductCount());
                        bundle.putString("storage", "no");
                        bundle.putString("type", type+"");
                        bundle.putInt("NeedBatch", isNeedBatch);
                        if (doType.equals("sure")){
                            bundle.putString("showBS", "yes");
                        }
                        readyGo(IDataScanActivity.class, bundle);
                    }else{
                        toScan(product.getProductID(),product.getProductName(),orderId,product.getProductCount(),type);
                    }
                }

                private void toScan(String productID, String productName, String orderId, String productCount,String type) {
                    if (Build.VERSION.SDK_INT >= 23){
                        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                        if (cameraPermission != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(OrderDeliveryActivity.this, new String[]{Manifest.permission.CAMERA}, 123);
                            return;
                        }else{
                            Bundle bundle=new Bundle();
                            bundle.putString("productId", productID);
                            bundle.putString("productName",productName);
                            bundle.putString("orderId", orderId);
                            bundle.putString("needCount", productCount);
                            bundle.putString("storage", "no");
                            bundle.putString("type", type+"");
                            bundle.putInt("NeedBatch", isNeedBatch);
                            readyGo(CaptureActivity.class, bundle);
                        }
                    }else{
                        Bundle bundle=new Bundle();
                        bundle.putString("productId", productID);
                        bundle.putString("productName",productName);
                        bundle.putString("orderId", orderId);
                        bundle.putString("needCount", productCount);
                        bundle.putString("storage", "no");
                        bundle.putString("type", type+"");
                        bundle.putInt("NeedBatch", isNeedBatch);
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

        class OrderDelHolder{
            ImageView ivProductImg;
            TextView tvProductName;
            TextView tvShouldCount;
            TextView tvSacnCount;
            TextView tvProductSpec;
            Button btn_info;
            Button btn_scan;
        }
    }

    private void setWrongCodeAdapter() {
        List<CartSQLBean> allCodes = mUserDataBaseOperate.findAll();
        List<String> allCodesOne = new ArrayList<String>();
        for (int i=0;i<allCodes.size();i++){
            allCodesOne.add(allCodes.get(i).getProductCode());
        }
        wrongCodes=new ArrayList<String>();
        for (int i=0;i<allCodesOne.size();i++){
            if (!codes.contains(allCodesOne.get(i))){
                wrongCodes.add(allCodesOne.get(i));
            }
        }
        tvWrongCount.setText("不匹配条码("+wrongCodes.size()+")");
        wcAdaper=new WrongCodeAdapter(wrongCodes);
        lvNoPP.setAdapter(wcAdaper);
        if (wrongCodes.isEmpty() || wrongCodes.size()==0){
            glWrong.setVisibility(View.GONE);
            llSaoMaSH.setVisibility(View.GONE);
        }else{
            glWrong.setVisibility(View.VISIBLE);
            llSaoMaSH.setVisibility(View.VISIBLE);
        }
    }


    class WrongCodeAdapter extends BaseAdapter{
        private ArrayList<String> wrongCodes;
        public WrongCodeAdapter(ArrayList<String> wrongCodes) {
            super();
            this.wrongCodes=wrongCodes;
        }

        @Override
        public int getCount() {
            return wrongCodes.size();
        }

        @Override
        public String getItem(int i) {
            return wrongCodes.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            WrongHolder holder=null;
            if (view==null){
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_wrong_code,null);
                holder=new WrongHolder();
                holder.tvWrong=(TextView)view.findViewById(R.id.tvWrong);
                view.setTag(holder);
            }
            String item = getItem(i);
            holder= (WrongHolder) view.getTag();
            holder.tvWrong.setText(item+"");
            return view;
        }
        class WrongHolder{
            TextView tvWrong;
        }
    }

    /**
     * 添加图片
     * 显示选择对话框
     */
    private void showAddImgDialog() {
        new AlertDialog.Builder(this)
                .setTitle("修改方式")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0 :
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                                intentFromGallery.addCategory(Intent.CATEGORY_OPENABLE);
                                intentFromGallery.setType("image/jpeg"); // 设置文件类型
                                if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){
                                    startActivityForResult(intentFromGallery, SELECT_PIC_KITKAT);
                                }else{
                                    startActivityForResult(intentFromGallery,IMAGE_REQUEST_CODE);
                                }
                                break;
                            case 1 :
                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                String state = Environment
                                        .getExternalStorageState();
                                if (state.equals(Environment.MEDIA_MOUNTED)) {
                                    File path = Environment
                                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                    File file = new File(path, "wearhouse"+checkedPosition+".jpg");
                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(file));
                                }
                                startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public String getTime(){
        long time=System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳
        String str=String.valueOf(time);
        return str;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE :
//                    startPhotoZoom(data.getData());
                    getImageToView(data.getData(),"图库");
                    break;
                case SELECT_PIC_KITKAT :
//                    startPhotoZoom(data.getData());
                    getImageToView(data.getData(),"图库");
                    break;
                case CAMERA_REQUEST_CODE :
                    // 判断存储卡是否可以用，可用进行存储
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File path = Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path, "wearhouse"+checkedPosition+".jpg");
//                        startPhotoZoom(Uri.fromFile(tempFile));
                        getImageToView(Uri.fromFile(tempFile),"相机");
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RESULT_REQUEST_CODE : // 图片缩放完成后
                    if (data != null) {
//                        getImageToView(data);
                    }
                    break;
            }
        }
    }

    private void getImageToView(Uri data, String type){
        if (data != null) {
            Bitmap photo;
            if (type.equals("相机")){
                File file = null;
                if (null != data && data != null) {
                    file = getFileFromMediaUri(OrderDeliveryActivity.this, data);
                }
                Bitmap photoBmp = null;
                try {
                    photoBmp = getBitmapFormUri(OrderDeliveryActivity.this, Uri.fromFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int degree = getBitmapDegree(file.getAbsolutePath());
                /**
                 * 把图片旋转为正的方向
                 */
                photo = rotateBitmapByDegree(photoBmp, degree);
            }else{
                photo = null;
                try {
                    photo = getBitmapFormUri(OrderDeliveryActivity.this, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            switch (checkedPosition){
                case "01":
                    SettingImage settingImage01 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath01=settingImage01.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath01).error(R.mipmap.xiuzhneg).into(iv01);
                    iv02.setVisibility(View.VISIBLE);
                    break;
                case "02":
                    SettingImage settingImage02 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath02=settingImage02.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath02).error(R.mipmap.xiuzhneg).into(iv02);
                    iv03.setVisibility(View.VISIBLE);
                    break;
                case "03":
                    SettingImage settingImage03 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath03=settingImage03.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath03).error(R.mipmap.xiuzhneg).into(iv03);
                    iv04.setVisibility(View.VISIBLE);
                    break;
                case "04":
                    SettingImage settingImage04 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath04=settingImage04.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath04).error(R.mipmap.xiuzhneg).into(iv04);
                    iv05.setVisibility(View.VISIBLE);
                    break;
                case "05":
                    SettingImage settingImage05 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath05=settingImage05.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath05).error(R.mipmap.xiuzhneg).into(iv05);
                    iv06.setVisibility(View.VISIBLE);
                    break;
                case "06":
                    SettingImage settingImage06 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath06=settingImage06.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath06).error(R.mipmap.xiuzhneg).into(iv06);
                    iv07.setVisibility(View.VISIBLE);
                    break;
                case "07":
                    SettingImage settingImage07 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath07=settingImage07.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath07).error(R.mipmap.xiuzhneg).into(iv07);
                    iv08.setVisibility(View.VISIBLE);
                    break;
                case "08":
                    SettingImage settingImage08 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath08=settingImage08.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath08).error(R.mipmap.xiuzhneg).into(iv08);
                    iv09.setVisibility(View.VISIBLE);
                    break;
                case "09":
                    SettingImage settingImage09 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath09=settingImage09.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath09).error(R.mipmap.xiuzhneg).into(iv09);
                    break;
            }
//            switch (checkedPosition) {
//                case "01":
//                    SettingImage settingImage1 = new SettingImage(photo, "file_img1");
//                    file_imgPath1 = settingImage1.imagePath();
//                    iv_img1.setImageBitmap(photo);
////                    Toast.makeText(OrderCommentActivity.this,""+file_imgPath1,Toast.LENGTH_SHORT).show();
//                    break;
//                case 2:
//                    SettingImage settingImage2 = new SettingImage(photo, "file_img2");
//                    file_imgPath2 = settingImage2.imagePath();
//                    iv_img2.setImageBitmap(photo);
////                    Toast.makeText(OrderCommentActivity.this,""+file_imgPath2,Toast.LENGTH_SHORT).show();
//                    break;
//                case 3:
//                    SettingImage settingImage3 = new SettingImage(photo, "file_img3");
//                    file_imgPath3 = settingImage3.imagePath();
//                    iv_img3.setImageBitmap(photo);
////                    Toast.makeText(OrderCommentActivity.this,""+file_imgPath3,Toast.LENGTH_SHORT).show();
//                    break;
//            }
        }
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(OrderDeliveryActivity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以800x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 800f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 通过Uri获取文件
     * @param ac
     * @param uri
     * @return
     */
    public static File getFileFromMediaUri(Context ac, Uri uri) {
        if(uri.getScheme().toString().compareTo("content") == 0){
            ContentResolver cr = ac.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
            if (cursor != null) {
                cursor.moveToFirst();
                String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路径
                cursor.close();
                if (filePath != null) {
                    return new File(filePath);
                }
            }
        }else if(uri.getScheme().toString().compareTo("file") == 0){
            return new File(uri.toString().replace("file://",""));
        }
        return null;
    }
    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

//    private void getImageToView(Intent data) {
//        Bundle extras = data.getExtras();
//        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            SettingImage settingImage = new SettingImage(photo, "wearhouse"+getTime());
//            switch (checkedPosition){
//                case "01":
//                    file_imgPath01=settingImage.imagePath();
//                    Glide.with(getApplicationContext()).load(file_imgPath01).error(R.mipmap.xiuzhneg).into(iv01);
//                    iv02.setVisibility(View.VISIBLE);
//                    break;
//                case "02":
//                    file_imgPath02=settingImage.imagePath();
//                    Glide.with(getApplicationContext()).load(file_imgPath02).error(R.mipmap.xiuzhneg).into(iv02);
//                    iv03.setVisibility(View.VISIBLE);
//                    break;
//                case "03":
//                    file_imgPath03=settingImage.imagePath();
//                    Glide.with(getApplicationContext()).load(file_imgPath03).error(R.mipmap.xiuzhneg).into(iv03);
//                    iv04.setVisibility(View.VISIBLE);
//                    break;
//                case "04":
//                    file_imgPath04=settingImage.imagePath();
//                    Glide.with(getApplicationContext()).load(file_imgPath04).error(R.mipmap.xiuzhneg).into(iv04);
//                    iv05.setVisibility(View.VISIBLE);
//                    break;
//                case "05":
//                    file_imgPath05=settingImage.imagePath();
//                    Glide.with(getApplicationContext()).load(file_imgPath05).error(R.mipmap.xiuzhneg).into(iv05);
//                    iv06.setVisibility(View.VISIBLE);
//                    break;
//                case "06":
//                    file_imgPath06=settingImage.imagePath();
//                    Glide.with(getApplicationContext()).load(file_imgPath06).error(R.mipmap.xiuzhneg).into(iv06);
//                    iv07.setVisibility(View.VISIBLE);
//                    break;
//                case "07":
//                    file_imgPath07=settingImage.imagePath();
//                    Glide.with(getApplicationContext()).load(file_imgPath07).error(R.mipmap.xiuzhneg).into(iv07);
//                    iv08.setVisibility(View.VISIBLE);
//                    break;
//                case "08":
//                    file_imgPath08=settingImage.imagePath();
//                    Glide.with(getApplicationContext()).load(file_imgPath08).error(R.mipmap.xiuzhneg).into(iv08);
//                    iv09.setVisibility(View.VISIBLE);
//                    break;
//                case "09":
//                    file_imgPath09=settingImage.imagePath();
//                    Glide.with(getApplicationContext()).load(file_imgPath09).error(R.mipmap.xiuzhneg).into(iv09);
//                    break;
//            }
//        }
//    }


    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
//            Log.i("tag", "The uri is not exist.");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url=getPath(OrderDeliveryActivity.this,uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/jpeg");
        }else{
            intent.setDataAndType(uri, "image/jpeg");
        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 100);
        intent.putExtra("aspectY", 100);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 640);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * <br>功能简述:4.4及以上获取图片的方法
     * <br>功能详细描述:
     * <br>注意:
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        invoiceid=extras.getString("OrderID");
        orderCount=extras.getString("OrderCount");
        doType=extras.getString("doType");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_order_delivery;
    }
}
