package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.CloseOrder;
import com.bbld.warehouse.bean.GetOrderTbList;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 订单提报
 * Created by likey on 2017/11/1.
 */

public class DDTBActivity extends BaseActivity{
    @BindView(R.id.srl_order)
    SwipeRefreshLayout srlOrder;
    @BindView(R.id.lvOrder)
    ListView lvOrder;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private Dialog loading;
    private String token;
    private int page;
    private List<GetOrderTbList.GetOrderTbListlist> tbList;
    private DDTBAdapter adapter;
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
        token=new MyToken(this).getToken();
        page=1;
        loadData(false);
        setListeners();
    }

    private void setListeners() {
        lvOrder.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        srlOrder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("orderId", 0+"");
                readyGo(DDTBAddActivity.class,bundle);
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadData(final boolean isLoadMore) {
        loading= WeiboDialogUtils.createLoadingDialog(DDTBActivity.this,"加载中...");
        Call<GetOrderTbList> call= RetrofitService.getInstance().getOrderTbList(token,page,10);
        call.enqueue(new Callback<GetOrderTbList>() {
            @Override
            public void onResponse(Response<GetOrderTbList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    if (isLoadMore){
                        List<GetOrderTbList.GetOrderTbListlist> tbListAdd = response.body().getList();
                        tbList.addAll(tbListAdd);
                        adapter.notifyDataSetChanged();
                        WeiboDialogUtils.closeDialog(loading);
                    }else{
                        tbList = response.body().getList();
                        setAdapter();
                        WeiboDialogUtils.closeDialog(loading);
                    }
                }else{
                    showToast("数据获取失败");
                    WeiboDialogUtils.closeDialog(loading);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                showToast("数据获取失败");
                WeiboDialogUtils.closeDialog(loading);
            }
        });
    }

    private void setAdapter() {
        adapter=new DDTBAdapter();
        lvOrder.setAdapter(adapter);
    }

    class DDTBAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return tbList.size();
        }

        @Override
        public GetOrderTbList.GetOrderTbListlist getItem(int i) {
            return tbList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            DDTBHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_ddtb, null);
                holder=new DDTBHolder();
                holder.tvTitle=(TextView)view.findViewById(R.id.tvTitle);
                holder.tvState=(TextView)view.findViewById(R.id.tvState);
                holder.tvName=(TextView)view.findViewById(R.id.tvName);
                holder.tvPerson=(TextView)view.findViewById(R.id.tvPerson);
                holder.tv_product=(TextView)view.findViewById(R.id.tv_product);
                holder.tv_productCount=(TextView)view.findViewById(R.id.tv_productCount);
                holder.tv_date=(TextView)view.findViewById(R.id.tv_date);
                holder.btn_close=(Button) view.findViewById(R.id.btn_close);
                holder.btn_edit=(Button) view.findViewById(R.id.btn_edit);
                view.setTag(holder);
            }
            holder= (DDTBHolder) view.getTag();
            final GetOrderTbList.GetOrderTbListlist item = getItem(i);
            holder.tvTitle.setText("订单号："+item.getOrderCode());
            holder.tvState.setText(item.getOrderStatusMessage()+"");
            holder.tvName.setText(item.getName()+"");
            holder.tvPerson.setText(item.getPhone()+"");
            holder.tv_product.setText(item.getProductCategoryCount()+"");
            holder.tv_productCount.setText("类"+item.getProductTotal()+"盒");
            if (item.getAddDate().contains("T")){
                holder.tv_date.setText(item.getAddDate().substring(0,item.getAddDate().indexOf("T"))+"");
            }else{
                holder.tv_date.setText(item.getAddDate()+"");
            }
            if (item.getOrderStatus()==1){
                holder.btn_close.setVisibility(View.VISIBLE);
                holder.btn_edit.setVisibility(View.VISIBLE);
                holder.btn_edit.setText("编辑");
                holder.btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showCloseDialog(item.getOrderId()+"");
                    }
                });
                holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle=new Bundle();
                        bundle.putString("orderId", item.getOrderId()+"");
                        readyGo(DDTBAddActivity.class,bundle);
                    }
                });
            }else{
                holder.btn_close.setVisibility(View.GONE);
                holder.btn_edit.setVisibility(View.VISIBLE);
                holder.btn_edit.setText("详情");
                holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle=new Bundle();
                        bundle.putString("orderId", item.getOrderId()+"");
                        readyGo(DDTBAddActivity.class,bundle);
                    }
                });
            }
            return view;
        }

        class DDTBHolder {
            TextView tvTitle, tvState, tvName, tvPerson, tv_product, tv_productCount, tv_date;
            Button btn_close, btn_edit;
        }
    }

    private void showCloseDialog(final String orderId) {
        final EditText et = new EditText(this);
        et.setBackgroundResource(R.drawable.bg_batch);
        et.setHint("输入备注");
        et.setMaxLines(1);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setSingleLine(true);
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("关闭此单？")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        closeOrder(et.getText()+"",orderId);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
        setDialogWindowAttr(serialDialog);
    }
    //在dialog.show()之后调用
    public void setDialogWindowAttr(Dialog dlg){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = 4*(width/5);//宽高可设置具体大小
        lp.height = 2*(height/7);
        dlg.getWindow().setAttributes(lp);
    }

    private void closeOrder(String s, String orderId) {
        if (s.equals("")||s==null){
            showToast("请输入备注信息");
        }else{
            Call<CloseOrder> call=RetrofitService.getInstance().closeOrder(token, orderId, s);
            call.enqueue(new Callback<CloseOrder>() {
                @Override
                public void onResponse(Response<CloseOrder> response, Retrofit retrofit) {
                    if (response==null){
                        return;
                    }
                    if (response.body().getStatus()==0){
                        page=1;
                        loadData(false);
                    }else{
                        showToast("操作失败，请重试");
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    showToast("操作失败，请重试");
                }
            });
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
        return R.layout.activity_ddtb;
    }
}
