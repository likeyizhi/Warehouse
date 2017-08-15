package com.bbld.warehouse.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.CusInvoiceInfo;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 终端配送详情
 * Created by likey on 2017/8/11.
 */

public class ZDPSInfoActivity extends BaseActivity{
    @BindView(R.id.tv_channelName)
    TextView tvChannelName;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_orderNumber)
    TextView tvOrderNumber;
    @BindView(R.id.lv_backOrderInfo)
    ListView lvInfo;


    private String customerInvoiceId;
    private String token;
    private CusInvoiceInfo.CusInvoiceInfoInfo cusInfo;
    private List<CusInvoiceInfo.CusInvoiceInfoInfo.CusInvoiceInfoProductList> cusPros;
    private CusInfoAdapter adapter;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
    }

    private void loadData() {
        Call<CusInvoiceInfo> call= RetrofitService.getInstance().getCusInvoiceInfo(token, customerInvoiceId);
        call.enqueue(new Callback<CusInvoiceInfo>() {
            @Override
            public void onResponse(Response<CusInvoiceInfo> response, Retrofit retrofit) {
                if (response==null){
                    return;
                }
                if (response.body().getStatus()==0){
                    cusInfo = response.body().getInfo();
                    cusPros = cusInfo.getProductList();
                    setData();
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setData() {
        tvChannelName.setText(cusInfo.getCusName()+"");
        tvRemark.setText(cusInfo.getRemark()+"");
        tvDate.setText(cusInfo.getDate()+"");
        tvOrderNumber.setText(cusInfo.getStorageNumber());
        setAdapter();
    }

    private void setAdapter() {
        adapter=new CusInfoAdapter();
        lvInfo.setAdapter(adapter);
    }

    class CusInfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cusPros.size();
        }

        @Override
        public CusInvoiceInfo.CusInvoiceInfoInfo.CusInvoiceInfoProductList getItem(int i) {
            return cusPros.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            CusProsHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_zdps_info,null);
                holder=new CusProsHolder();
                holder.iv_ProductImg=(ImageView)view.findViewById(R.id.iv_ProductImg);
                holder.tv_ProductName=(TextView)view.findViewById(R.id.tv_ProductName);
                holder.tv_ProductSpec=(TextView)view.findViewById(R.id.tv_ProductSpec);
                holder.tv_Unit=(TextView)view.findViewById(R.id.tv_Unit);
                holder.tv_ProductCount=(TextView)view.findViewById(R.id.tv_ProductCount);
                view.setTag(holder);
            }
            holder= (CusProsHolder) view.getTag();
            CusInvoiceInfo.CusInvoiceInfoInfo.CusInvoiceInfoProductList product = getItem(i);
            Glide.with(getApplicationContext()).load(product.getProductImg()).error(R.mipmap.xiuzhneg).into(holder.iv_ProductImg);
            holder.tv_ProductName.setText(product.getProductName()+"");
            holder.tv_ProductSpec.setText(product.getProductSpec()+"");
            holder.tv_Unit.setText(product.getUnit()+"");
            holder.tv_ProductCount.setText(product.getProductCount()+"");
            return view;
        }
        class CusProsHolder{
            ImageView iv_ProductImg;
            TextView tv_ProductName;
            TextView tv_ProductSpec;
            TextView tv_Unit;
            TextView tv_ProductCount;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        customerInvoiceId=extras.getString("customerInvoiceId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_zdps_info;
    }
}
