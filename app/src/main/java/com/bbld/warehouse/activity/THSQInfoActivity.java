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
import com.bbld.warehouse.bean.RefundGetRefundDetail;
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
 * 退货申请-详情
 * Created by likey on 2017/9/7.
 */

public class THSQInfoActivity extends BaseActivity{
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
    @BindView(R.id.ib_back)
    ImageButton ibBack;


    private String id;
    private String token;
    private RefundGetRefundDetail.RefundGetRefundDetailInfo info;
    private List<RefundGetRefundDetail.RefundGetRefundDetailInfo.RefundGetRefundDetailProductList> pros;

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
                ActivityManagerUtil.getInstance().finishActivity(THSQInfoActivity.this);
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

    class THSQInfoAdapter extends BaseAdapter{

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
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_thsq_info,null);
                holder=new THSQInfoHolder();
                holder.iv_productImg=(ImageView)view.findViewById(R.id.iv_productImg);
                holder.tv_productName=(TextView)view.findViewById(R.id.tv_productName);
                holder.tv_productCount=(TextView)view.findViewById(R.id.tv_productCount);
                holder.btn_toInfo=(Button)view.findViewById(R.id.btn_toInfo);
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
            return view;
        }

        class THSQInfoHolder{
            ImageView iv_productImg;
            TextView tv_productName,tv_productCount;
            Button btn_toInfo;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        id=extras.getString("id");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_thsq_info;
    }
}
