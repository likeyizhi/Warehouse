package com.bbld.warehouse.activity;

import android.app.Dialog;
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
import com.bbld.warehouse.bean.GivebackGetGivebackForOutList;
import com.bbld.warehouse.loading.WeiboDialogUtils;
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
 * 还货出库
 * Created by likey on 2017/9/8.
 */

public class HHCKActivity extends BaseActivity{
    @BindView(R.id.lv_hhck)
    ListView lv_hhck;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.ib_back)
    ImageButton ibBack;


    private String token;
    private int page;
    private int size;
    private Dialog loading;
    private List<GivebackGetGivebackForOutList.GivebackGetGivebackForOutListlist> list;
    private HHCKAdapter adapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    srl.setRefreshing(false);
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
        size=10;
        loadData(false);
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(HHCKActivity.this);
            }
        });
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                loadData(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
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
        lv_hhck.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    private void loadData(boolean isLoadMore) {
        loading=WeiboDialogUtils.createLoadingDialog(HHCKActivity.this,"加载中...");
        Call<GivebackGetGivebackForOutList> call= RetrofitService.getInstance().givebackGetGivebackForOutList(token,page,size);
        call.enqueue(new Callback<GivebackGetGivebackForOutList>() {
            @Override
            public void onResponse(Response<GivebackGetGivebackForOutList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    list=response.body().getList();
                    setAdapter();
                }else {
                    showToast(response.body().getMes());
                }
                WeiboDialogUtils.closeDialog(loading);
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(loading);
            }
        });
    }

    private void setAdapter() {
        adapter=new HHCKAdapter();
        lv_hhck.setAdapter(adapter);
    }

    class HHCKAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public GivebackGetGivebackForOutList.GivebackGetGivebackForOutListlist getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            HHCKHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_hhck,null);
                holder=new HHCKHolder();
                holder.tvTHDH=(TextView)view.findViewById(R.id.tvTHDH);
                holder.tvTHZT=(TextView)view.findViewById(R.id.tvTHZT);
                holder.tvSQBZ=(TextView)view.findViewById(R.id.tvSQBZ);
                holder.tvSHBZ=(TextView)view.findViewById(R.id.tvSHBZ);
                holder.tv_product=(TextView)view.findViewById(R.id.tv_product);
                holder.tv_productCount=(TextView)view.findViewById(R.id.tv_productCount);
                holder.tv_date=(TextView)view.findViewById(R.id.tv_date);
                holder.tvDealer=(TextView)view.findViewById(R.id.tvDealer);
                holder.btn_info=(Button)view.findViewById(R.id.btn_info);
                holder.btn_scan=(Button)view.findViewById(R.id.btn_scan);
                view.setTag(holder);
            }
            final GivebackGetGivebackForOutList.GivebackGetGivebackForOutListlist item = getItem(i);
            holder= (HHCKHolder) view.getTag();
            holder.tvTHDH.setText("退货单号："+item.getReturnCode());
            holder.tvTHZT.setText(""+item.getReturnMessage());
            holder.tvSQBZ.setText(""+item.getRemark());
            holder.tvSHBZ.setText(""+item.getAuditRemark());
            holder.tv_product.setText(""+item.getProductTypeCount());
            holder.tv_productCount.setText("类"+item.getProductTotal()+"盒");
            holder.tvDealer.setText(item.getDealerName()+"("+item.getPhone()+")");
            holder.tv_date.setText(""+item.getDate());
            if (item.getReturnStatus()==1){
                holder.btn_info.setVisibility(View.VISIBLE);
                holder.btn_scan.setVisibility(View.VISIBLE);
            }else if (item.getReturnStatus()==2){
                holder.btn_info.setVisibility(View.VISIBLE);
                holder.btn_scan.setVisibility(View.GONE);
            }else{
                holder.btn_info.setVisibility(View.VISIBLE);
                holder.btn_scan.setVisibility(View.GONE);
            }
            holder.btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("id", item.getId()+"");
                    readyGo(HHCKInfoActivity.class,bundle);
                }
            });
            holder.btn_scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("id", item.getId()+"");
                    readyGo(HHCKCKActivity.class,bundle);
                }
            });
            return view;
        }

        class HHCKHolder{
            TextView tvTHDH,tvTHZT,tvSQBZ,tvSHBZ,tv_product,tv_productCount,tv_date,tvDealer;
            Button btn_info,btn_scan;
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
        return R.layout.activity_hhck;
    }
}
