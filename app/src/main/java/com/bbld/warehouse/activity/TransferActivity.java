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
import com.bbld.warehouse.bean.HandoverList;
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
 * 市场交接
 * Created by likey on 2017/6/8.
 */

public class TransferActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.lv_transfer)
    ListView lvTransfer;
    @BindView(R.id.srl_transfer)
    SwipeRefreshLayout srlTransfer;


    private List<HandoverList.HandoverListlist> handoverList;
    private TransferAdapter adapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    srlTransfer.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void initViewsAndEvents() {
        loadData();
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(TransferActivity.this);
            }
        });
        srlTransfer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
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

    private void loadData() {
        Call<HandoverList> call= RetrofitService.getInstance().handoverList(new MyToken(TransferActivity.this).getToken());
        call.enqueue(new Callback<HandoverList>() {
            @Override
            public void onResponse(Response<HandoverList> response, Retrofit retrofit) {
                if (response==null){
                    showToast(getResources().getString(R.string.get_data_fail));
                    return;
                }
                if (response.body().getStatus()==0){
                    handoverList = response.body().getList();
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
        adapter=new TransferAdapter();
        lvTransfer.setAdapter(adapter);
    }

    class TransferAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return handoverList.size();
        }

        @Override
        public HandoverList.HandoverListlist getItem(int i) {
            return handoverList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Integer.parseInt(handoverList.get(i).getHandOverID()+"");
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TransferHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_transfer,null);
                holder=new TransferHolder();
                holder.tvNumber=(TextView)view.findViewById(R.id.tvNumber);
                holder.tvState=(TextView)view.findViewById(R.id.tvState);
                holder.tv_jjr=(TextView)view.findViewById(R.id.tv_jjr);
                holder.tv_zcr=(TextView)view.findViewById(R.id.tv_zcr);
                holder.tv_date=(TextView)view.findViewById(R.id.tv_date);
                holder.tv_product=(TextView)view.findViewById(R.id.tv_product);
                holder.tv_productCount=(TextView)view.findViewById(R.id.tv_productCount);
                holder.btn_operation=(Button) view.findViewById(R.id.btn_operation);
                view.setTag(holder);
            }
            holder= (TransferHolder) view.getTag();
            final HandoverList.HandoverListlist handover = getItem(i);
            holder.tvNumber.setText(handover.getHandoverCode()+"");
            holder.tvState.setText("无字段");
            holder.tv_jjr.setText("监交人："+handover.getJjr());
            holder.tv_zcr.setText("转出人："+handover.getZcr());
            holder.tv_date.setText(handover.getDate()+"");
            holder.tv_product.setText("无字段");
            holder.tv_productCount.setText("无字段");
            holder.btn_operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle =new Bundle();
                    bundle.putString("handoverId", handover.getHandOverID());
                    switch (handover.getEdit()){
                        case 0:
                            readyGo(TransferInfoActivity.class, bundle);
                            break;
                        case 1:
                            readyGo(TransferEditActivity.class, bundle);
                            break;
                        default:
                            break;
                    }
                }
            });
            return view;
        }

        class TransferHolder{
            TextView tvNumber, tvState, tv_jjr, tv_zcr, tv_date, tv_product, tv_productCount;
            Button btn_operation;
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
        return R.layout.activity_transfer;
    }
}
