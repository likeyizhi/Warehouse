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
import com.bbld.warehouse.bean.InventoryList;
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
 * 库存盘点
 * Created by likey on 2017/6/7.
 */

public class StockingActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.lv_stocking)
    ListView lvStocking;
    @BindView(R.id.srl_stocking)
    SwipeRefreshLayout srlStocking;
    @BindView(R.id.btn_addStocking)
    Button btnAddStocking;
    private List<InventoryList.InventoryListlist> inventoryList;
    private StockingAdapter adapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    srlStocking.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        loadData(false);
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(StockingActivity.this);
            }
        });
        btnAddStocking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(AddStockingActivity.class);
            }
        });
        srlStocking.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
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
    }

    private void loadData(boolean isLoadMore) {
        Call<InventoryList> call= RetrofitService.getInstance().inventoryList(new MyToken(StockingActivity.this).getToken());
        call.enqueue(new Callback<InventoryList>() {
            @Override
            public void onResponse(Response<InventoryList> response, Retrofit retrofit) {
                if (response==null){
                    showToast("数据获取失败");
                    return;
                }
                if (response.body().getStatus()==0){
                    inventoryList = response.body().getList();
                    setAdapter();
                }else{
                    showToast(""+response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setAdapter() {
        adapter=new StockingAdapter();
        lvStocking.setAdapter(adapter);
    }

    class StockingAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return inventoryList.size();
        }

        @Override
        public InventoryList.InventoryListlist getItem(int i) {
            return inventoryList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(inventoryList.get(i).getInventoryID());
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            StockingHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_stocking,null);
                holder=new StockingHolder();
                holder.tvInventoryNumber=(TextView)view.findViewById(R.id.tvInventoryNumber);
                holder.tvInventoryStatus=(TextView)view.findViewById(R.id.tvInventoryStatus);
                holder.tvInventoryRemark=(TextView)view.findViewById(R.id.tvInventoryRemark);
                holder.tvProduct=(TextView)view.findViewById(R.id.tvProduct);
                holder.tvProductCount=(TextView)view.findViewById(R.id.tvProductCount);
                holder.tvInventoryDate=(TextView)view.findViewById(R.id.tvInventoryDate);
                holder.btnInfo=(Button) view.findViewById(R.id.btnInfo);
                view.setTag(holder);
            }
            holder= (StockingHolder) view.getTag();
            final InventoryList.InventoryListlist inventory = getItem(i);
            holder.tvInventoryNumber.setText("盘点单号"+inventory.getInventoryNumber());
            holder.tvInventoryStatus.setText(inventory.getInventoryStatus()+"");
            holder.tvInventoryRemark.setText(inventory.getInventoryRemark()+"");
            holder.tvProduct.setText("无字段");
            holder.tvProductCount.setText("类"+inventory.getProductCount()+"盒");
            holder.tvInventoryDate.setText(inventory.getInventoryDate()+"");
            holder.btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("InventoryId",inventory.getInventoryID()+"");
                    readyGo(StockingInfoActivity.class, bundle);
                }
            });
            return view;
        }

        class StockingHolder{
            TextView tvInventoryNumber;
            TextView tvInventoryStatus;
            TextView tvInventoryRemark;
            TextView tvProduct;
            TextView tvProductCount;
            TextView tvInventoryDate;
            Button btnInfo;
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
        return R.layout.activity_stocking;
    }
}
