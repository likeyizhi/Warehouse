package com.bbld.warehouse.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.DCOGetProductCirculationStatistics;
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
 * 条码流转
 * Created by likey on 2017/11/22.
 */

public class TMLZActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.etKey)
    EditText etKey;
    @BindView(R.id.lvTmlz)
    ListView lvTmlz;
    @BindView(R.id.btnSearch)
    Button btnSearch;

    private int page=1;
    private int pagesize=10;
    private Dialog loading;
    private String token;
    private List<DCOGetProductCirculationStatistics.DCOGetProductCirculationStatisticslist> tmlzList;
    private TMLZAdapter adapter;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page=1;
                loadData(false);
            }
        });
        lvTmlz.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        String key = (etKey.getText() + "").trim();
        if (key.equals("") || key==null){
            showToast("请输入条码/序列号");
        }else{
            loading= WeiboDialogUtils.createLoadingDialog(this,"加载中...");
            Call<DCOGetProductCirculationStatistics> call= RetrofitService.getInstance().dcoGetProductCirculationStatistics(token,key,page,pagesize);
            call.enqueue(new Callback<DCOGetProductCirculationStatistics>() {
                @Override
                public void onResponse(Response<DCOGetProductCirculationStatistics> response, Retrofit retrofit) {
                    if (response==null){
                        WeiboDialogUtils.closeDialog(loading);
                        return;
                    }
                    if (response.body().getStatus()==0){
                        if (isLoadMore){
                            List<DCOGetProductCirculationStatistics.DCOGetProductCirculationStatisticslist> tmlzListAdd = response.body().getList();
                            tmlzList.addAll(tmlzListAdd);
                            adapter.notifyDataSetChanged();
                            WeiboDialogUtils.closeDialog(loading);
                        }else{
                            tmlzList=response.body().getList();
                            setAdapter();
                            WeiboDialogUtils.closeDialog(loading);
                        }
                    }else{
                        showToast(response.body().getMes());
                        WeiboDialogUtils.closeDialog(loading);
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    WeiboDialogUtils.closeDialog(loading);
                }
            });
        }
    }

    private void setAdapter() {
        adapter=new TMLZAdapter();
        lvTmlz.setAdapter(adapter);
    }

    class TMLZAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return tmlzList.size();
        }

        @Override
        public DCOGetProductCirculationStatistics.DCOGetProductCirculationStatisticslist getItem(int i) {
            return tmlzList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TMLZHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_tmlz,null);
                holder=new TMLZHolder();
                holder.tvName=(TextView)view.findViewById(R.id.tvName);
                holder.tvType=(TextView)view.findViewById(R.id.tvType);
                holder.tvBarCode=(TextView)view.findViewById(R.id.tvBarCode);
                holder.tvBatchNumber=(TextView)view.findViewById(R.id.tvBatchNumber);
                holder.tvSerialNumber=(TextView)view.findViewById(R.id.tvSerialNumber);
                holder.tvDealerName=(TextView)view.findViewById(R.id.tvDealerName);
                holder.tvWarehouseName=(TextView)view.findViewById(R.id.tvWarehouseName);
                holder.tvAddDate=(TextView)view.findViewById(R.id.tvAddDate);
                view.setTag(holder);
            }
            holder= (TMLZHolder) view.getTag();
            DCOGetProductCirculationStatistics.DCOGetProductCirculationStatisticslist item = getItem(i);
            holder.tvName.setText(item.getName()+"");
            if (item.getTypeId()==1){//1=出库，2=入库
                holder.tvType.setText("出库");
                holder.tvType.setTextColor(Color.rgb(255,107,1));
            }else{
                holder.tvType.setText("入库");
                holder.tvType.setTextColor(Color.rgb(0,163,217));
            }
            holder.tvBarCode.setText("条码："+item.getBarCode());
            holder.tvBatchNumber.setText("批次号："+item.getBatchNumber());
            holder.tvSerialNumber.setText("序列号："+item.getSerialNumber());
            holder.tvDealerName.setText("经销商："+item.getDealerName());
            holder.tvWarehouseName.setText("仓库："+item.getWarehouseName());
            holder.tvAddDate=(TextView)view.findViewById(R.id.tvAddDate);
            if (item.getAddDate().contains("T")){
                holder.tvAddDate.setText("创建时间："+item.getAddDate().substring(0,item.getAddDate().indexOf("T"))+"");
            }else{
                holder.tvAddDate.setText("创建时间："+item.getAddDate()+"");
            }
            return view;
        }

        class TMLZHolder{
            TextView tvName,tvType,tvBarCode,tvBatchNumber,tvSerialNumber,tvDealerName,tvWarehouseName,tvAddDate;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_tmlz;
    }
}
