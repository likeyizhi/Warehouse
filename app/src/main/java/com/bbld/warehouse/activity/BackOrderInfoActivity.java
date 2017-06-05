package com.bbld.warehouse.activity;

import android.os.Bundle;
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
import com.bbld.warehouse.bean.OrderDetails;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 待发货、已出库、待收货的订单详情
 * Created by likey on 2017/5/24.
 */

public class BackOrderInfoActivity extends BaseActivity{
    @BindView(R.id.tv_channelName)
    TextView tvChannelName;
    @BindView(R.id.tv_dealerName)
    TextView tvDealerName;
    @BindView(R.id.tv_name_phone)
    TextView tvNamePhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.lv_backOrderInfo)
    ListView lvBackOrderInfo;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_orderNumber)
    TextView tvOrderNumber;
    @BindView(R.id.tv_invoiceCode)
    TextView tvInvoiceCode;
    @BindView(R.id.btn_out)
    Button btnOut;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private String invoiceid;
    private String orderCount;

    @Override
    protected void initViewsAndEvents() {
        loadData();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(BackOrderInfoActivity.this);
            }
        });
    }

    private void loadData() {
        Call<OrderDetails> call= RetrofitService.getInstance().orderDetails(new MyToken(BackOrderInfoActivity.this).getToken()+"", Integer.parseInt(invoiceid+""));
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

    private void setData(OrderDetails.OrderDetailsInfo info) {
        tvChannelName.setText(info.getChannelName()+"");
        tvDealerName.setText(info.getDealerName()+"");
        tvNamePhone.setText(info.getDeliveryName()+"("+info.getDeliveryPhone()+")");
        tvAddress.setText(info.getDeliveryAddress()+"");
        tvRemark.setText(info.getRemark()+"");
        tvStatus.setText(info.getStatus()+"");
        tvMore.setText("发货商品共"+orderCount+"类"+info.getProductCount()+"盒"+",查看更多");
        tvDate.setText(info.getDate()+"");
        tvOrderNumber.setText(info.getOrderNumber()+"");
        tvInvoiceCode.setText(info.getInvoiceCode()+"");
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("发货出库");
            }
        });
        lvBackOrderInfo.setAdapter(new OrderInfoAdapter(info.getProductList()));
    }
    class OrderInfoAdapter extends BaseAdapter{
        private List<OrderDetails.OrderDetailsInfo.OrderDetailsProductList> productList;
        public OrderInfoAdapter(List<OrderDetails.OrderDetailsInfo.OrderDetailsProductList> productList){
            super();
            this.productList=productList;
        }
        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public OrderDetails.OrderDetailsInfo.OrderDetailsProductList getItem(int i) {
            return productList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            OrderInfoHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_back_order_info,null);
                holder=new OrderInfoHolder();
                holder.iv_ProductImg=(ImageView)view.findViewById(R.id.iv_ProductImg);
                holder.tv_ProductName=(TextView)view.findViewById(R.id.tv_ProductName);
                holder.tv_ProductSpec=(TextView)view.findViewById(R.id.tv_ProductSpec);
                holder.tv_Unit=(TextView)view.findViewById(R.id.tv_Unit);
                holder.tv_ProductCount=(TextView)view.findViewById(R.id.tv_ProductCount);
                view.setTag(holder);
            }
            holder= (OrderInfoHolder) view.getTag();
            OrderDetails.OrderDetailsInfo.OrderDetailsProductList product = getItem(i);
            Glide.with(getApplicationContext()).load(product.getProductImg()).error(R.mipmap.cha).into(holder.iv_ProductImg);
            holder.tv_ProductName.setText(product.getProductName()+"");
            holder.tv_ProductSpec.setText(product.getProductSpec()+"");
            holder.tv_Unit.setText(product.getUnit()+"");
            holder.tv_ProductCount.setText(product.getProductCount()+"");
            return view;
        }
        class OrderInfoHolder{
            ImageView iv_ProductImg;
            TextView tv_ProductName;
            TextView tv_ProductSpec;
            TextView tv_Unit;
            TextView tv_ProductCount;
        }
    }
    @Override
    protected void getBundleExtras(Bundle extras) {
        invoiceid=extras.getString("OrderID");
        orderCount=extras.getString("OrderCount");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_back_order_info;
    }
}
