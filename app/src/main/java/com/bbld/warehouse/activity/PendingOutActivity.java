package com.bbld.warehouse.activity;

import android.graphics.Color;
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
import com.bbld.warehouse.bean.PendingOutStorageList;
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
 * 其他出库
 * Created by likey on 2017/7/12.
 */

public class PendingOutActivity extends BaseActivity{
    @BindView(R.id.lv_backOrder)
    ListView lvBackOrder;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.srl_order)
    SwipeRefreshLayout srlOrder;

    private String token;
    private int page=1;
    private int pagesize=10;
    private List<PendingOutStorageList.PendingOutStorageListList> storageList;
    private OutBoundAdapter outBoundAdapter;
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
        token = new MyToken(this).getToken();
        loadData(false);
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(PendingOutActivity.this);
            }
        });
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
    }

    private void loadData(final boolean isLoadMore) {
        Call<PendingOutStorageList> call= RetrofitService.getInstance().getPendingOutStorageList(token,page,pagesize);
        call.enqueue(new Callback<PendingOutStorageList>() {
            @Override
            public void onResponse(Response<PendingOutStorageList> response, Retrofit retrofit) {
                if (response==null){
                    showToast("数据获取失败");
                    return;
                }
                if (response.body().getStatus()==0){
                    if (isLoadMore){
                        List<PendingOutStorageList.PendingOutStorageListList> storageListAdd = response.body().getList();
                        storageList.addAll(storageListAdd);
                        outBoundAdapter.notifyDataSetChanged();
                    }else{
                        storageList=response.body().getList();
                        if(storageList.isEmpty()){
//                            llKong.setBackgroundResource(R.mipmap.kong);
                            setAdapter();
                        }else{
//                            llKong.setBackgroundColor(Color.WHITE);
                            setAdapter();
                        }
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
        outBoundAdapter=new OutBoundAdapter();
        lvBackOrder.setAdapter(outBoundAdapter);
    }
    class OutBoundAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return storageList.size();
        }

        @Override
        public PendingOutStorageList.PendingOutStorageListList getItem(int i) {
            return storageList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(storageList.get(i).getStorageID() + "");
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            PendingOutHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_peding_out, null);
                holder = new PendingOutHolder();
                holder.tv_item_orderid = (TextView) view.findViewById(R.id.tv_item_orderid);
                holder.tv_item_order_state = (TextView) view.findViewById(R.id.tv_item_order_state);
                holder.tv_channelName = (TextView) view.findViewById(R.id.tv_channelName);
                holder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
                holder.tv_product = (TextView) view.findViewById(R.id.tv_product);
                holder.tv_productCount = (TextView) view.findViewById(R.id.tv_productCount);
                holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
                holder.btn_info = (Button) view.findViewById(R.id.btn_info);
                holder.btn_out = (Button) view.findViewById(R.id.btn_out);
                view.setTag(holder);
            }
            holder = (PendingOutHolder) view.getTag();
            final PendingOutStorageList.PendingOutStorageListList item = getItem(i);
            holder.tv_item_orderid.setText("订单号:" + item.getStorageNumber());
            holder.tv_item_order_state.setText(item.getTypeName() + "");
            holder.tv_channelName.setText(item.getLinkName() + "");
            holder.tv_phone.setText(item.getLinkPhone() + "");
            holder.tv_product.setText(item.getProductTypeCount() + "");
            holder.tv_productCount.setText("类" + item.getProductCount() + "盒");
            holder.tv_date.setText(item.getDate() + "");
            holder.btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putInt("type",1);
                    bundle.putString("storageId",item.getStorageID()+"");
                    bundle.putString("OrderCount",getCount()+"");
                    readyGo(OutBoundOrderInfoActivity.class,bundle);
                }
            });
            holder.btn_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putInt("type",1);
                    bundle.putString("storageId",item.getStorageID()+"");
                    bundle.putString("OrderCount",getCount()+"");
                    readyGo(CommitOutStorageActivity.class,bundle);
                }
            });
            return view;
        }

        class PendingOutHolder {
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
        page=1;
        loadData(false);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_pending_out;
    }
}
