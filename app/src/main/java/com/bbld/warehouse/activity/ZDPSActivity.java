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
 * 终端配送
 * Created by likey on 2017/8/11.
 */

public class ZDPSActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.srl_order)
    SwipeRefreshLayout srl_order;
    @BindView(R.id.lv_zdps)
    ListView lv_zdps;

    private String token;
    private List<CusInvoiceSendList.CusInvoiceSendListlist> cus;
    private ZDPSAdapter adapter;
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
        Call<CusInvoiceSendList> call= RetrofitService.getInstance().getCusInvoiceSendList(token);
        call.enqueue(new Callback<CusInvoiceSendList>() {
            @Override
            public void onResponse(Response<CusInvoiceSendList> response, Retrofit retrofit) {
                if (response==null){
                    showToast("获取失败");
                    return;
                }
                if (response.body().getStatus()==0){
                    cus = response.body().getList();
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
        adapter=new ZDPSAdapter();
        lv_zdps.setAdapter(adapter);
    }

    class ZDPSAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cus.size();
        }

        @Override
        public CusInvoiceSendList.CusInvoiceSendListlist getItem(int i) {
            return cus.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(cus.get(i).getStorageID()+"");
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ZDPSHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_zdps, null);
                holder=new ZDPSHolder();
                holder.tv_item_orderid=(TextView)view.findViewById(R.id.tv_item_orderid);
                holder.tv_item_order_state=(TextView)view.findViewById(R.id.tv_item_order_state);
                holder.tv_channelName=(TextView)view.findViewById(R.id.tv_channelName);
                holder.tv_phone=(TextView)view.findViewById(R.id.tv_phone);
                holder.tv_product=(TextView)view.findViewById(R.id.tv_product);
                holder.tv_productCount=(TextView)view.findViewById(R.id.tv_productCount);
                holder.tv_date=(TextView)view.findViewById(R.id.tv_date);
                holder.btn_info=(Button) view.findViewById(R.id.btn_info);
                holder.btn_out=(Button) view.findViewById(R.id.btn_out);
                view.setTag(holder);
            }
            holder= (ZDPSHolder) view.getTag();
            final CusInvoiceSendList.CusInvoiceSendListlist item = getItem(i);
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
                    readyGo(ZDPSInfoActivity.class, bundle);
                }
            });
            holder.btn_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("customerInvoiceId",item.getStorageID());
                    readyGo(ZDPSOutActivity.class, bundle);
                }
            });
            return view;
        }

        class ZDPSHolder{
            TextView tv_item_orderid;
            TextView tv_item_order_state;
            TextView tv_channelName;
            TextView tv_phone;
            TextView tv_product;
            TextView tv_productCount;
            TextView tv_date;
            Button btn_info;
            Button btn_out;
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
        return R.layout.activity_zdps;
    }
}
