package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.bbld.warehouse.bean.OrderDetails;
import com.bbld.warehouse.db.UserDataBaseOperate;
import com.bbld.warehouse.db.UserSQLiteOpenHelper;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.utils.UploadUserInformationByPostService;
import com.bbld.warehouse.zxing.android.CaptureActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 待出库--出库
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

    private String invoiceid;
    private String orderCount;
    private String orderId;
    private UserSQLiteOpenHelper mUserSQLiteOpenHelper;
    private UserDataBaseOperate mUserDataBaseOperate;
    private boolean request;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    showToast("出库成功");

                    finish();
                    break;
                case 222:
                    showToast("出库失败");
                    break;
            }
        }
    };
    @Override
    protected void initViewsAndEvents() {
        mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(OrderDeliveryActivity.this);
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
                List<CartSQLBean> sqlProducts = mUserDataBaseOperate.findAll();
                List<CodeJson.CodeJsonList> A = new ArrayList<CodeJson.CodeJsonList>();
                CodeJson B=new CodeJson();
                for (int j=0;j<sqlProducts.size();j++){
                    if (!(A.toString().contains(sqlProducts.get(j).getProductId()))){
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
                showToast(jsonString);
                //需要参数：token,invoiceid(orderId),codejson
                final String codejson = jsonString;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            request= UploadUserInformationByPostService.save(new MyToken(OrderDeliveryActivity.this).getToken()+""
                                    ,orderId+"",codejson);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (request) { // 请求成功
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
        lvFahuo.setAdapter(new OrderDelAdapter(info.getProductList()));
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
            Glide.with(getApplicationContext()).load(product.getProductImg()).error(R.mipmap.cha).into(holder.ivProductImg);
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
                    Bundle bundle=new Bundle();
                    bundle.putString("productId", product.getProductID()+"");
                    bundle.putString("productName",product.getProductName()+"");
                    bundle.putString("orderId", orderId);
                    bundle.putString("needCount", product.getProductCount()+"");
                    readyGo(CaptureActivity.class, bundle);
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
    @Override
    protected void getBundleExtras(Bundle extras) {
        invoiceid=extras.getString("OrderID");
        orderCount=extras.getString("OrderCount");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_order_delivery;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }
}
