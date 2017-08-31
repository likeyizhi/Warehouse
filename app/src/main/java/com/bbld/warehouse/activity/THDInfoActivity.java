package com.bbld.warehouse.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.CusInvoiceInfo;
import com.bbld.warehouse.bean.RefundDetail;
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
 * 退货单-详情
 * Created by likey on 2017/8/28.
 */

public class THDInfoActivity extends BaseActivity{
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tv_name_phone)
    TextView tvNamePhone;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_orderNumber)
    TextView tvOrderNumber;
    @BindView(R.id.lvOrderInfo)
    ListView lvOrderInfo;
    @BindView(R.id.ib_back)
    ImageButton ibBack;


    private String token;
    private String refundId;
    private RefundDetail.RefundDetailInfo info;
    private List<RefundDetail.RefundDetailInfo.RefundDetailProductList> pros;
    private THDInfoAdapter adapter;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(THDInfoActivity.this);
            }
        });
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
        tvName.setText(info.getRefundDealerName()+"");
        tvNamePhone.setText(info.getLinkName()+"("+info.getLinkPhone()+")");
        tvDate.setText(info.getAddDate()+"");
        tvOrderNumber.setText(info.getRefundCode()+"");
        pros=info.getProductList();
        setAdapter();
    }

    private void setAdapter() {
        adapter=new THDInfoAdapter();
        lvOrderInfo.setAdapter(adapter);
    }

    class THDInfoAdapter extends BaseAdapter {

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
            THDInfoHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_thd_info,null);
                holder=new THDInfoHolder();
                holder.iv_ProductImg=(ImageView)view.findViewById(R.id.iv_ProductImg);
                holder.tv_ProductName=(TextView)view.findViewById(R.id.tv_ProductName);
                holder.tv_ProductSpec=(TextView)view.findViewById(R.id.tv_ProductSpec);
                holder.tv_Unit=(TextView)view.findViewById(R.id.tv_Unit);
                holder.tv_ProductCount=(TextView)view.findViewById(R.id.tv_ProductCount);
                view.setTag(holder);
            }
            holder= (THDInfoHolder) view.getTag();
            RefundDetail.RefundDetailInfo.RefundDetailProductList product = getItem(i);
            Glide.with(getApplicationContext()).load(product.getProductImg()).error(R.mipmap.xiuzhneg).into(holder.iv_ProductImg);
            holder.tv_ProductName.setText(product.getProductName()+"");
            holder.tv_ProductSpec.setText(product.getProductSpec()+"");
            holder.tv_Unit.setText(product.getUnit()+"");
            holder.tv_ProductCount.setText(product.getProductAmount()+"");
            return view;
        }
        class THDInfoHolder{
            ImageView iv_ProductImg;
            TextView tv_ProductName;
            TextView tv_ProductSpec;
            TextView tv_Unit;
            TextView tv_ProductCount;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        refundId=extras.getString("RefundId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_thd_info;
    }
}
