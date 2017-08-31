package com.bbld.warehouse.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.CusInvoiceSendList;
import com.bbld.warehouse.bean.RefundList;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 退货单
 * Created by likey on 2017/8/28.
 */

public class THDActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.srl_order)
    SwipeRefreshLayout srl_order;
    @BindView(R.id.lv_thd)
    ListView lv_thd;

    private String token;
    private int page = 1;
    private int pagesize = 10;
    private List<RefundList.RefundListlist> refundList;
    private THDAdapter adapter;
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
        loadData(false);
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
                        loadData(false);
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
        lv_thd.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isBottom;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_FLING:
                        //Log.i("info", "SCROLL_STATE_FLING");
                        break;
                    case SCROLL_STATE_IDLE:
                        if (isBottom) {
                            page++;
                            loadData(true);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount){
                    //Log.i("info", "到底了....");
                    isBottom = true;
                }else{
                    isBottom = false;
                }
            }
        });
    }

    private void loadData(final boolean isLoadMore) {
        Call<RefundList> call= RetrofitService.getInstance().getRefundList(token,page,pagesize);
        call.enqueue(new Callback<RefundList>() {
            @Override
            public void onResponse(Response<RefundList> response, Retrofit retrofit) {
                if (response==null){
                    showToast("获取失败");
                    return;
                }
                if (response.body().getStatus()==0){
                    if (isLoadMore){
                        List<RefundList.RefundListlist> addRefundList = response.body().getList();
                        refundList.addAll(addRefundList);
                        adapter.notifyDataSetChanged();
                    }else{
                        refundList = response.body().getList();
                        setAdapter();
                    }
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
        adapter=new THDAdapter();
        lv_thd.setAdapter(adapter);
    }

    class THDAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return refundList.size();
        }

        @Override
        public RefundList.RefundListlist getItem(int i) {
            return refundList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(refundList.get(i).getId()+"");
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            THDHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_thd, null);
                holder=new THDHolder();
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
            holder= (THDHolder) view.getTag();
            final RefundList.RefundListlist item = getItem(i);
            holder.tv_item_orderid.setText("订单号:"+item.getRefundCode());
            holder.tv_item_order_state.setText("退货单");
            holder.tv_channelName.setText(item.getLinkName()+"");
            holder.tv_phone.setText(item.getLinkPhone()+"");
            holder.tv_product.setText(item.getProductTypeCount()+"");
            holder.tv_productCount.setText("类"+item.getProductCount()+"盒");
            holder.tv_date.setText(item.getAddDate()+"");
            holder.btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("RefundId",item.getId()+"");
                    readyGo(THDInfoActivity.class, bundle);
                }
            });
            holder.btn_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("RefundId",item.getId());
                    readyGo(THDSureActivity.class, bundle);
                }
            });
            return view;
        }

        class THDHolder{
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
        loadData(false);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_thd;
    }
}
