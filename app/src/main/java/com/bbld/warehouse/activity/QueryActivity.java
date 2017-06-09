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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.ProductCountList;
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
 * 库存查询
 * Created by likey on 2017/6/7.
 */

public class QueryActivity extends BaseActivity{
    @BindView(R.id.lv_productList)
    ListView lvProductList;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.srl_productList)
    SwipeRefreshLayout srlProductList;

    private int page=1;
    private int pagesize=10;
    private List<ProductCountList.ProductCountListList> products;
    private QueryAdapter adapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    srlProductList.setRefreshing(false);
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
                ActivityManagerUtil.getInstance().finishActivity(QueryActivity.this);
            }
        });
        srlProductList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        lvProductList.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        Call<ProductCountList> call= RetrofitService.getInstance().productCountList(new MyToken(QueryActivity.this).getToken(), page, pagesize);
        call.enqueue(new Callback<ProductCountList>() {
            @Override
            public void onResponse(Response<ProductCountList> response, Retrofit retrofit) {
                if (response==null){
                    showToast("数据接收错误");
                    return;
                }
                if (response.body().getStatus()==0){
                    if (isLoadMore){
                        List<ProductCountList.ProductCountListList> productsAdd = response.body().getList();
                        products.addAll(productsAdd);
                        adapter.notifyDataSetChanged();
                    }else{
                        products = response.body().getList();
                        setAdapter();
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
        adapter=new QueryAdapter();
        lvProductList.setAdapter(adapter);
    }

    class QueryAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public ProductCountList.ProductCountListList getItem(int i) {
            return products.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(products.get(i).getProductID());
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            QueryHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_query,null);
                holder=new QueryHolder();
                holder.iv_productImg=(ImageView)view.findViewById(R.id.iv_productImg);
                holder.tv_productName=(TextView)view.findViewById(R.id.tv_productName);
                holder.tv_productSpec=(TextView)view.findViewById(R.id.tv_productSpec);
                holder.tv_productCount=(TextView)view.findViewById(R.id.tv_productCount);
                holder.btn_productCodeInfo=(Button)view.findViewById(R.id.btn_productCodeInfo);
                view.setTag(holder);
            }
            holder= (QueryHolder) view.getTag();
            final ProductCountList.ProductCountListList product = getItem(i);
            Glide.with(getApplicationContext()).load(product.getProductImg()).error(R.mipmap.cha).into(holder.iv_productImg);
            holder.tv_productName.setText(product.getProductName()+"");
            holder.tv_productSpec.setText(product.getProductSpec()+"");
            holder.tv_productCount.setText("库存数量:"+product.getCount()+"(盒)");
            holder.btn_productCodeInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast("条码明细"+product.getProductID());
                }
            });
            return view;
        }

        class QueryHolder{
            ImageView iv_productImg;
            TextView tv_productName;
            TextView tv_productSpec;
            TextView tv_productCount;
            Button btn_productCodeInfo;
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
        return R.layout.activity_query;
    }
}
