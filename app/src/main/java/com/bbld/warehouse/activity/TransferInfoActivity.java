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
import com.bbld.warehouse.bean.HandoverInfo;
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
 * 市场交接--详情
 * Created by likey on 2017/6/8.
 */

public class TransferInfoActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tv_jjr)
    TextView tvJjr;
    @BindView(R.id.tv_zcr)
    TextView tvZcr;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.lv_transferInfo)
    ListView lvTransferInfo;

    private String handoverId;
    private List<HandoverInfo.HandoverInfoInfo.HandoverInfoProductList> productList;
    private TransferInfoAdapter adapter;

    @Override
    protected void initViewsAndEvents() {
        loadData();
        setListeners();
    }
    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(TransferInfoActivity.this);
            }
        });

    }
    private void loadData() {
        Call<HandoverInfo> call= RetrofitService.getInstance().handoverInfo(new MyToken(TransferInfoActivity.this).getToken(), handoverId);
        call.enqueue(new Callback<HandoverInfo>() {
            @Override
            public void onResponse(Response<HandoverInfo> response, Retrofit retrofit) {
                if (response==null){
                    showToast(getResources().getString(R.string.get_data_fail));
                    return;
                }
                if (response.body().getStatus()==0){
                    HandoverInfo.HandoverInfoInfo handoverInfo = response.body().getInfo();
                    setData(handoverInfo);
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setData(HandoverInfo.HandoverInfoInfo handoverInfo) {
        tvNumber.setText("交接单号："+handoverInfo.getHandoverCode());
        tvJjr.setText(""+handoverInfo.getJjr());
        tvZcr.setText(""+handoverInfo.getZcr());
        tvRemark.setText(handoverInfo.getRemark()+"");
        tvState.setText(handoverInfo.getStatus()+"");
        productList = handoverInfo.getProductList();
        setAdapter();
    }

    private void setAdapter() {
        adapter=new TransferInfoAdapter();
        lvTransferInfo.setAdapter(adapter);
    }

    class TransferInfoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public HandoverInfo.HandoverInfoInfo.HandoverInfoProductList getItem(int i) {
            return productList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TransferInfoHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_transfer_info, null);
                holder=new TransferInfoHolder();
                holder.iv_productImg=(ImageView)view.findViewById(R.id.iv_productImg);
                holder.tv_productName=(TextView)view.findViewById(R.id.tv_productName);
                holder.tv_productSpec=(TextView)view.findViewById(R.id.tv_productSpec);
                holder.tv_count=(TextView)view.findViewById(R.id.tv_count);
                view.setTag(holder);
            }
            holder= (TransferInfoHolder) view.getTag();
            HandoverInfo.HandoverInfoInfo.HandoverInfoProductList handover = getItem(i);
            Glide.with(getApplicationContext()).load(handover.getProductImg()+"").error(R.mipmap.xiuzhneg).into(holder.iv_productImg);
            holder.tv_productName.setText(handover.getProductName()+"");
            holder.tv_productSpec.setText(handover.getProductSpec()+"");
            holder.tv_count.setText("盘点数量："+handover.getProductCount()+"(盒)");
            return view;
        }

        class TransferInfoHolder{
            ImageView iv_productImg;
            TextView tv_productName, tv_productSpec, tv_count;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        handoverId=extras.getString("handoverId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_transfer_info;
    }
}
