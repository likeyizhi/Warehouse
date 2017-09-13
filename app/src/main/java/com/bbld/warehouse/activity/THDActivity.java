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
import com.bbld.warehouse.bean.RefundGetRefundList;
import com.bbld.warehouse.bean.RefundList;
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
 * 退货单
 * Created by likey on 2017/8/28.
 */

public class THDActivity extends BaseActivity{
    @BindView(R.id.btn_addTHD)
    Button btnAddTHD;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.srl_thd)
    SwipeRefreshLayout srl_thd;
    @BindView(R.id.lv_thd)
    ListView lv_thd;

    private String token;
    private int page;
    private int pagesize;
    private List<RefundGetRefundList.RefundGetRefundListlist> list;
    private THSQAdapter adapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    srl_thd.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        page=1;
        pagesize=10;
        loadData(false);
        setListeners();
    }

    private void loadData(final boolean isLoadMore) {
        Call<RefundGetRefundList> call=RetrofitService.getInstance().refundGetRefundList(token,page,pagesize);
        call.enqueue(new Callback<RefundGetRefundList>() {
            @Override
            public void onResponse(Response<RefundGetRefundList> response, Retrofit retrofit) {
                if (response==null){
                    showToast("数据获取失败,请重试");
                    return;
                }
                if (response.body().getStatus()==0){
                    if (isLoadMore){
                        List<RefundGetRefundList.RefundGetRefundListlist> listAdd = response.body().getList();
                        list.addAll(listAdd);
                        adapter.notifyDataSetChanged();
                    }else{
                        list = response.body().getList();
                        setAdapter();
                    }
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                showToast("数据获取失败,请重试");
            }
        });
    }

    private void setAdapter() {
        adapter=new THSQAdapter();
        lv_thd.setAdapter(adapter);
    }

    class THSQAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public RefundGetRefundList.RefundGetRefundListlist getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            THSQHolder holder=null;
            if (view==null){
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_thsq,null);
                holder=new THSQHolder();
                holder.tvTHDH=(TextView)view.findViewById(R.id.tvTHDH);
                holder.tvTHZT=(TextView)view.findViewById(R.id.tvTHZT);
                holder.tvSQBZ=(TextView)view.findViewById(R.id.tvSQBZ);
                holder.tvSHBZ=(TextView)view.findViewById(R.id.tvSHBZ);
                holder.tv_product=(TextView)view.findViewById(R.id.tv_product);
                holder.tv_productCount=(TextView)view.findViewById(R.id.tv_productCount);
                holder.tv_date=(TextView)view.findViewById(R.id.tv_date);
                holder.tvDealer=(TextView)view.findViewById(R.id.tvDealer);
                holder.btn_info=(Button)view.findViewById(R.id.btn_info);
                view.setTag(holder);
            }
            final RefundGetRefundList.RefundGetRefundListlist item = getItem(i);
            holder= (THSQHolder) view.getTag();
            holder.tvTHDH.setText("退货单号："+item.getRefundCode());
            holder.tvTHZT.setText(""+item.getRefundMessage());
            holder.tvSQBZ.setText(""+item.getRemark());
            holder.tvSHBZ.setText(""+item.getAuditRemark());
            holder.tv_product.setText(""+item.getProductTypeCount());
            holder.tv_productCount.setText("类"+item.getProductTotal()+"盒");
            holder.tvDealer.setText(item.getDealerName()+"("+item.getPhone()+")");
            holder.tv_date.setText(""+item.getDate());
            holder.btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("id", item.getId()+"");
                    readyGo(THSQInfoActivity.class,bundle);
                }
            });
            return view;
        }

        class THSQHolder {
            TextView tvTHDH,tvTHZT,tvSQBZ,tvSHBZ,tv_product,tv_productCount,tv_date,tvDealer;
            Button btn_info;
        }
    }

    private void setListeners() {
        btnAddTHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(AddTHDActivity.class);
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(THDActivity.this);
            }
        });
        srl_thd.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        return R.layout.activity_thd;
    }
}
