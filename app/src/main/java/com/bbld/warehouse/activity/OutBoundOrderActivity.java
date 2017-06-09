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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.StorageList;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 产品出库，产品入库
 * Created by likey on 2017/5/27.
 */

public class OutBoundOrderActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.srl_outBound)
    SwipeRefreshLayout srlOutBound;
    @BindView(R.id.lv_outBound)
    ListView lvOutBound;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_addOutBound)
    Button btnAddOutBound;
    @BindView(R.id.ll_kong)
    LinearLayout llKong;

    private String title;
    private int type;
    private int typeid;
    private int page=1;
    private List<StorageList.StorageListList> storageList;
    private OutBoundAdapter outBoundAdapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    srlOutBound.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        tvTitle.setText(title);
        if (type==1){
            btnAddOutBound.setText("添加出库单");
        }else{
            btnAddOutBound.setText("添加入库单");
        }
        loadData(false);
        setListeners();
    }

    private void setListeners() {
        srlOutBound.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

        lvOutBound.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                ActivityManagerUtil.getInstance().finishActivity(OutBoundOrderActivity.this);
            }
        });

        btnAddOutBound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("type",type+"");
                readyGo(AddOutBoundOrderActivity.class, bundle);
            }
        });
    }

    private void loadData(final boolean isLoadMore) {
        Call<StorageList> call= RetrofitService.getInstance().storageList(type, typeid, new MyToken(OutBoundOrderActivity.this).getToken(), page, 10);
        call.enqueue(new Callback<StorageList>() {
            @Override
            public void onResponse(Response<StorageList> response, Retrofit retrofit) {
                if (response==null){
                    showToast("服务器错误");
                    return;
                }
                if (response.body().getStatus()==0){
                    if (isLoadMore){
                        List<StorageList.StorageListList> storageListAdd = response.body().getList();
                        storageList.addAll(storageListAdd);
                        outBoundAdapter.notifyDataSetChanged();
                    }else{
                        storageList=response.body().getList();
                        if(storageList.isEmpty()){
                            llKong.setBackgroundResource(R.mipmap.kong);
                        }else{
                            setAdapter();
                        }
                    }
                }else{
                    showToast(response.body().getMes()+"");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setAdapter() {
        outBoundAdapter=new OutBoundAdapter();
        lvOutBound.setAdapter(outBoundAdapter);
    }

    class OutBoundAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return storageList.size();
        }

        @Override
        public StorageList.StorageListList getItem(int i) {
            return storageList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(storageList.get(i).getStorageID()+"");
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            OutBoundHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_outbound_order, null);
                holder=new OutBoundHolder();
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
            holder= (OutBoundHolder) view.getTag();
            final StorageList.StorageListList item = getItem(i);
            holder.tv_item_orderid.setText("订单号:"+item.getStorageNumber());
            holder.tv_item_order_state.setText(item.getTypeName()+"");
            holder.tv_channelName.setText(item.getLinkName()+"");
            holder.tv_phone.setText(item.getLinkPhone()+"");
            holder.tv_product.setText("2"/*item.getProductTypeCount()+""*/);
            holder.tv_productCount.setText("类"+item.getProductCount()+"盒");
            holder.tv_date.setText(item.getDate()+"");
            holder.btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putInt("type",type);
                    bundle.putString("storageId",item.getStorageID()+"");
                    bundle.putString("OrderCount",getCount()+"");
                    readyGo(OutBoundOrderInfoActivity.class,bundle);
                }
            });
            return view;
        }

        class OutBoundHolder{
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
    protected void getBundleExtras(Bundle extras) {
        title=extras.getString("title");
        type=extras.getInt("type");
        typeid=extras.getInt("typeid");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_outbound_order;
    }
}
