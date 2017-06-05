package com.bbld.warehouse.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.OrderList;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 待出库=1/已出库=2/待收货=3
 * Created by likey on 2017/5/23.
 */

public class BackOrderActivity extends BaseActivity{
    @BindView(R.id.lv_backOrder)
    ListView lvBackOrder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.srl_order)
    SwipeRefreshLayout srlOrder;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private List<OrderList.OrderListList> orderlist;
    private BackOrderAdapter backOrderAdapter;
    private int status;
    private int page=1;
    public static BackOrderActivity boActivity=null;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    srlOrder.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void initViewsAndEvents() {
        boActivity=this;
        if (status==1){
            tvTitle.setText("待出库订单");
        }else if (status==2){
            tvTitle.setText("已出库订单");
        }else{
            tvTitle.setText("待收货订单");
        }

        loadData(false);

        srlOrder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        page=1;
                        loadData(false);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
        lvBackOrder.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(BackOrderActivity.this);
            }
        });
    }

    private void loadData(final boolean loadMore) {
        Call<OrderList> call= RetrofitService.getInstance().orderList(new MyToken(BackOrderActivity.this).getToken()+"", status, page, 10);
        call.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Response<OrderList> response, Retrofit retrofit) {
                if (response.body()== null){
                    showToast("服务器出错");
                    return;
                }
                if (response.body().getStatus()==0){
                    if (loadMore){
                        List<OrderList.OrderListList> orderlistAdd = response.body().getList();
                        orderlist.addAll(orderlistAdd);
                        backOrderAdapter.notifyDataSetChanged();
                    }else{
                        orderlist = response.body().getList();
                        setAdapter();
                    }
                }else{
                    showToast(response.body().getMes()+"");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                showToast(throwable+"");
            }
        });
    }

    private void setAdapter() {
        backOrderAdapter=new BackOrderAdapter();
        lvBackOrder.setAdapter(backOrderAdapter);
        setOnItemClickSetInfo(new OnItemClickSetInfo() {
            @Override
            public void OnItemClickSetInfo(RecyclerView rv_item_order, String tag) {
                showToast(tag);
                if (rv_item_order.getVisibility()==View.VISIBLE){
                    rv_item_order.setVisibility(View.GONE);
                }else{
                    rv_item_order.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        status = extras.getInt("status", 0);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_back_order;
    }

    class BackOrderAdapter extends BaseAdapter{
        BackOrderHolder holder=null;
        @Override
        public int getCount() {
            return orderlist.size();
        }

        @Override
        public OrderList.OrderListList getItem(int i) {
            return orderlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(orderlist.get(i).getOrderID()+"");
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_backorder,null);
                holder=new BackOrderHolder();
                holder.tv_item_orderid=(TextView)view.findViewById(R.id.tv_item_orderid);
                holder.tv_item_order_state=(TextView)view.findViewById(R.id.tv_item_order_state);
                holder.tv_channelName=(TextView)view.findViewById(R.id.tv_channelName);
                holder.tv_dealerName=(TextView)view.findViewById(R.id.tv_dealerName);
                holder.tv_product=(TextView)view.findViewById(R.id.tv_product);
                holder.tv_productCount=(TextView)view.findViewById(R.id.tv_productCount);
                holder.tv_date=(TextView)view.findViewById(R.id.tv_date);
                holder.rv_item_order=(RecyclerView) view.findViewById(R.id.rv_item_order);
                holder.btn_info=(Button)view.findViewById(R.id.btn_info);
                holder.btn_goOut=(Button)view.findViewById(R.id.btn_goOut);
                holder.btn_track=(Button)view.findViewById(R.id.btn_track);
                holder.btn_writTrack=(Button)view.findViewById(R.id.btn_writTrack);
                holder.iv_open=(ImageView)view.findViewById(R.id.iv_open);
                view.setTag(holder);
            }
            holder= (BackOrderHolder) view.getTag();
            final OrderList.OrderListList order = getItem(i);
            holder.tv_item_orderid.setText("订单号:"+order.getOrderNumber()+"");
            holder.tv_item_order_state.setText("待发货");
            holder.tv_channelName.setText(order.getChannelName()+"");
            holder.tv_dealerName.setText(order.getDealerName()+"");
            holder.tv_product.setText(order.getProductTypeCount()+"");
            holder.tv_productCount.setText("类"+order.getProductCount()+"盒");
            holder.tv_date.setText(order.getDate()+"");
            if (status==1){
                holder.btn_info.setVisibility(View.VISIBLE);
                holder.btn_goOut.setVisibility(View.VISIBLE);
                holder.btn_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle=new Bundle();
                        bundle.putString("OrderID",order.getOrderID()+"");
                        bundle.putString("OrderCount",getCount()+"");
                        readyGo(BackOrderInfoActivity.class,bundle);
                    }
                });
                holder.btn_goOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        showToast("出库");
                        Bundle bundle=new Bundle();
                        bundle.putString("OrderID",order.getOrderID()+"");
                        bundle.putString("OrderCount",getCount()+"");
                        readyGo(OrderDeliveryActivity.class,bundle);
                    }
                });
            }else if(status==2){
                holder.btn_info.setVisibility(View.VISIBLE);
                if (Integer.parseInt(order.getLogisticsCount())>0){
                    holder.btn_track.setVisibility(View.VISIBLE);
                }else{
                    holder.btn_track.setVisibility(View.GONE);
                }
                holder.btn_writTrack.setVisibility(View.VISIBLE);
                holder.btn_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle=new Bundle();
                        bundle.putString("OrderID",order.getOrderID()+"");
                        bundle.putString("OrderCount",getCount()+"");
                        readyGo(BackOrderInfoActivity.class,bundle);
                    }
                });
                holder.btn_track.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        showToast("物流跟踪");
                        Bundle bundle=new Bundle();
                        bundle.putString("OrderID()",order.getOrderID()+"");
                        readyGo(LogisticsTrackingActivity.class,bundle);

                    }
                });
                holder.btn_writTrack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        showToast("录入物流信息");
                        Bundle bundle=new Bundle();
                        bundle.putString("OrderID()",order.getOrderID()+"");
                        bundle.putString("ChannelName()",order.getChannelName()+"");
                        bundle.putString("DealerName()",order.getDealerName()+"");
                        bundle.putString("Count()",getCount()+"");
                        bundle.putString("ProductCount()","类"+order.getProductCount()+"盒");
                        bundle.putString("Date()",order.getDate()+"");

                        readyGo(LogisticsNumberActivity.class,bundle);

                    }
                });
            }else{
                holder.btn_track.setVisibility(View.VISIBLE);
                holder.btn_track.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showToast("物流跟踪");
                        Bundle bundle=new Bundle();
                        bundle.putString("OrderID()",order.getOrderID()+"");
                        readyGo(LogisticsTrackingActivity.class,bundle);

                    }
                });
            }
            holder.iv_open.setTag(""+i);
            if (order!=null){
                holder.iv_open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickSetInfo != null) {
                            onItemClickSetInfo.OnItemClickSetInfo(holder.rv_item_order, holder+""+i);
                        }
                    }
                });
            }
//            holder.iv_open.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (holder.rv_item_order.getVisibility()==View.VISIBLE){
//                        holder.rv_item_order.setVisibility(View.GONE);
//                    }else{
//                        holder.rv_item_order.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            holder.rv_item_order.setLayoutManager(mLayoutManager);
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.rv_item_order.setHasFixedSize(true);
            holder.rv_item_order.setAdapter(new BackOrderRecAdapter(order.getProductList()));

            return view;
        }

        class BackOrderHolder{
            TextView tv_item_orderid;
            TextView tv_item_order_state;
            TextView tv_channelName;
            TextView tv_dealerName;
            TextView tv_product;
            TextView tv_productCount;
            TextView tv_date;
            RecyclerView rv_item_order;
            Button btn_info;
            Button btn_goOut;
            Button btn_track;
            Button btn_writTrack;
            ImageView iv_open;
        }

    }
    public interface OnItemClickSetInfo{
        void OnItemClickSetInfo(RecyclerView rv_item_order, String tag);
    }
    private OnItemClickSetInfo onItemClickSetInfo;

    public void setOnItemClickSetInfo(OnItemClickSetInfo onItemClickSetInfo){
        this.onItemClickSetInfo=onItemClickSetInfo;
    }
    private class BackOrderRecAdapter extends RecyclerView.Adapter<BackOrderRecAdapter.RecHolder> {
        private List<OrderList.OrderListList.OrderListProductList> productList;

        public BackOrderRecAdapter(List<OrderList.OrderListList.OrderListProductList> productList) {
            super();
            this.productList=productList;
        }

        @Override
        public RecHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lv_backorder_item,parent,false);
            RecHolder recHolder = new RecHolder(view);
            return recHolder;
        }

        @Override
        public void onBindViewHolder(RecHolder holder, int position) {
            Glide.with(getApplicationContext()).load(productList.get(0).getProductImg()).error(R.mipmap.cha).into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast("productId="+productList.get(0).getProductID()+"添加处理事件");
                }
            });
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }
        class RecHolder extends RecyclerView.ViewHolder {
            public ImageView img;
            public RecHolder(View view){
                super(view);
                img = (ImageView) view.findViewById(R.id.iv_img);
            }
        }
    }
}
