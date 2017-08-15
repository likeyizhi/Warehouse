package com.bbld.warehouse.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.CusInvoiceReceiptList;
import com.bbld.warehouse.bean.CusInvoiceSendList;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 到货确认
 * Created by likey on 2017/8/15.
 */

public class DHQRActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.srl_order)
    SwipeRefreshLayout srl_order;
    @BindView(R.id.lv_dhqr)
    ListView lv_dhqr;

    private String token;
    private List<CusInvoiceReceiptList.CusInvoiceReceiptListlist> cusList;
    private DHQRAdapter adapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    srl_order.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };

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
                finish();
            }
        });
        srl_order.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
    }

    private void loadData() {
        Call<CusInvoiceReceiptList> call= RetrofitService.getInstance().getCusInvoiceReceiptList(token);
        call.enqueue(new Callback<CusInvoiceReceiptList>() {
            @Override
            public void onResponse(Response<CusInvoiceReceiptList> response, Retrofit retrofit) {
                if (response==null){
                    return;
                }
                if (response.body().getStatus()==0){
                    cusList=response.body().getList();
                    setAdapter();
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setAdapter() {
        adapter=new DHQRAdapter();
        lv_dhqr.setAdapter(adapter);
    }

    class DHQRAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cusList.size();
        }

        @Override
        public CusInvoiceReceiptList.CusInvoiceReceiptListlist getItem(int i) {
            return cusList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(cusList.get(i).getStorageID()+"");
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            DHQRHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_dhqr, null);
                holder=new DHQRHolder();
                holder.tv_item_orderid=(TextView)view.findViewById(R.id.tv_item_orderid);
                holder.tv_item_order_state=(TextView)view.findViewById(R.id.tv_item_order_state);
                holder.tv_channelName=(TextView)view.findViewById(R.id.tv_channelName);
                holder.tv_phone=(TextView)view.findViewById(R.id.tv_phone);
                holder.tv_product=(TextView)view.findViewById(R.id.tv_product);
                holder.tv_productCount=(TextView)view.findViewById(R.id.tv_productCount);
                holder.tv_date=(TextView)view.findViewById(R.id.tv_date);
                holder.btn_info=(Button) view.findViewById(R.id.btn_info);
                view.setTag(holder);
            }
            holder= (DHQRHolder) view.getTag();
            final CusInvoiceReceiptList.CusInvoiceReceiptListlist item = getItem(i);
            holder.tv_item_orderid.setText("订单号:"+item.getStorageNumber());
            holder.tv_item_order_state.setText("待出库");
            holder.tv_channelName.setText(item.getCusName()+"");
            holder.tv_phone.setText(item.getRemark()+"");
            holder.tv_product.setText(item.getProductTypeCount()+"");
            holder.tv_productCount.setText("类"+item.getProductCount()+"盒");
            holder.tv_date.setText(item.getDate()+"");
            holder.btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("customerInvoiceId",item.getStorageID()+"");
                    readyGo(DHQRInfoActivity.class, bundle);
                }
            });
            return view;
        }

        class DHQRHolder{
            TextView tv_item_orderid;
            TextView tv_item_order_state;
            TextView tv_channelName;
            TextView tv_phone;
            TextView tv_product;
            TextView tv_productCount;
            TextView tv_date;
            Button btn_info;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_dhqr;
    }
}
