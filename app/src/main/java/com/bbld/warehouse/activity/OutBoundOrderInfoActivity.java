package com.bbld.warehouse.activity;

import android.os.Bundle;
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
import com.bbld.warehouse.bean.StorageCodeList;
import com.bbld.warehouse.bean.StorageDetails;
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

import static android.R.attr.type;

/**
 * 获取出/入库单详情
 * Created by likey on 2017/5/24.
 */

public class OutBoundOrderInfoActivity extends BaseActivity{
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
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lvOther)
    ListView lvOther;


    private int type;
    private String storageId;
    private String orderCount;
    private String token;
    private List<StorageCodeList.StorageCodeListInfo.StorageCodeListCodeList> codeList;
    private OtherAdapter otherAdapter;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        loadOtherData();
        setTitle();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(OutBoundOrderInfoActivity.this);
            }
        });
    }

    private void loadOtherData() {
        Call<StorageCodeList> call=RetrofitService.getInstance().storageCodeList(token,type+"",storageId);
        call.enqueue(new Callback<StorageCodeList>() {
            @Override
            public void onResponse(Response<StorageCodeList> response, Retrofit retrofit) {
                if (response==null){
                    return;
                }
                if (response.body().getStatus()==0){
                    codeList=response.body().getInfo().getCodeList();
                    setOtherAdapter();
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setOtherAdapter() {
        otherAdapter=new OtherAdapter();
        lvOther.setAdapter(otherAdapter);
    }

    class OtherAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return codeList.size();
        }

        @Override
        public StorageCodeList.StorageCodeListInfo.StorageCodeListCodeList getItem(int i) {
            return codeList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            OtherHolder holder=null;
            if (view==null){
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_other_info,null);
                holder=new OtherHolder();
                holder.tvSPName=(TextView)view.findViewById(R.id.tvSPName);
                holder.tvSPCode=(TextView)view.findViewById(R.id.tvSPCode);
                holder.tvSPSerialNumber=(TextView)view.findViewById(R.id.tvSPSerialNumber);
                holder.tvSPBatchNumber=(TextView)view.findViewById(R.id.tvSPBatchNumber);
                view.setTag(holder);
            }
            holder= (OtherHolder) view.getTag();
            StorageCodeList.StorageCodeListInfo.StorageCodeListCodeList item = getItem(i);
            holder.tvSPName.setText(item.getName()+"");
            holder.tvSPCode.setText(item.getBarCode()+"");
            holder.tvSPSerialNumber.setText(item.getSerialNumber()+"");
            holder.tvSPBatchNumber.setText(item.getBatchNumber()+"");
            return view;
        }

        class OtherHolder{
            TextView tvSPName,tvSPCode,tvSPSerialNumber,tvSPBatchNumber;
        }
    }

    private void setTitle() {
        if (type==1){
            tvTitle.setText("出库单详情");
        }else{
            tvTitle.setText("入库单详情");
        }
    }

    private void loadData() {
        Call<StorageDetails> call= RetrofitService.getInstance().storageDetails(type, new MyToken(OutBoundOrderInfoActivity.this).getToken()+"", storageId);
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
        tvChannelName.setText(info.getTypeName()+"");
//        tvDealerName.setText(info.getDealerName()+"");
        tvNamePhone.setText(info.getLinkName()+"("+info.getLinkPhone()+")");
//        tvAddress.setText(info.getDeliveryAddress()+"");
        tvRemark.setText(info.getRemark()+"");
//        tvStatus.setText(info.getStatus()+"");
//        tvMore.setText("发货商品共"+orderCount+"类"+info.getProductCount()+"盒"+",查看更多");
        tvDate.setText(info.getDate()+"");
        tvOrderNumber.setText(info.getStorageNumber()+"");
//        tvInvoiceCode.setText(info.getInvoiceCode()+"");
        lvBackOrderInfo.setAdapter(new OrderInfoAdapter(info.getProductList()));
    }
    class OrderInfoAdapter extends BaseAdapter{
        private List<StorageDetails.StorageDetailsInfo.StorageDetailsProductList> productList;
        public OrderInfoAdapter(List<StorageDetails.StorageDetailsInfo.StorageDetailsProductList> productList){
            super();
            this.productList=productList;
        }
        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public StorageDetails.StorageDetailsInfo.StorageDetailsProductList getItem(int i) {
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
            StorageDetails.StorageDetailsInfo.StorageDetailsProductList product = getItem(i);
            Glide.with(getApplicationContext()).load(product.getProductImg()).error(R.mipmap.xiuzhneg).into(holder.iv_ProductImg);
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
        orderCount=extras.getString("typeId");
        type=extras.getInt("type");
        storageId=extras.getString("storageId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_outbound_order_info;
    }
}
