package com.bbld.warehouse.activity;

import android.app.Dialog;
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
import com.bbld.warehouse.bean.StorageGetSaleList;
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
 * 销售查询
 * Created by likey on 2017/11/22.
 */

public class XSCXActivity extends BaseActivity{
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.btnSearch)
    Button btnSearch;
    @BindView(R.id.lvXscx)
    ListView lvXscx;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private Dialog loading;
    private String token;
    private int page=1;
    private int pagesize=10;
    private List<StorageGetSaleList.StorageGetSaleListlist> xscxList;
    private XSCXAdapter adapter;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        page=1;
        pagesize=10;
        loadData(false);
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lvXscx.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page=1;
                loadData(false);
            }
        });
    }

    private void loadData(final boolean isLoadMore) {
        loading = WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        String searchName = etName.getText() + "";
        String searchCode = etCode.getText() + "";
        Call<StorageGetSaleList> call= RetrofitService.getInstance().storageGetSaleList(token,searchName,searchCode,page,pagesize);
        call.enqueue(new Callback<StorageGetSaleList>() {
            @Override
            public void onResponse(Response<StorageGetSaleList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    if (isLoadMore){
                        List<StorageGetSaleList.StorageGetSaleListlist> xscxListAdd = response.body().getList();
                        xscxList.addAll(xscxListAdd);
                        adapter.notifyDataSetChanged();
                        WeiboDialogUtils.closeDialog(loading);
                    }else{
                        xscxList=response.body().getList();
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

    private void setAdapter() {
        adapter=new XSCXAdapter();
        lvXscx.setAdapter(adapter);
    }

    class XSCXAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return xscxList.size();
        }

        @Override
        public StorageGetSaleList.StorageGetSaleListlist getItem(int i) {
            return xscxList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            XSCXHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_xscx,null);
                holder=new XSCXHolder();
                holder.tvName=(TextView)view.findViewById(R.id.tvName);
                holder.tvBarCode=(TextView)view.findViewById(R.id.tvBarCode);
                holder.tvLink=(TextView)view.findViewById(R.id.tvLink);
                holder.tvBatchNumber=(TextView)view.findViewById(R.id.tvBatchNumber);
                holder.tvSerialNumber=(TextView)view.findViewById(R.id.tvSerialNumber);
                holder.tvAddDate=(TextView)view.findViewById(R.id.tvAddDate);
                view.setTag(holder);
            }
            holder= (XSCXHolder) view.getTag();
            StorageGetSaleList.StorageGetSaleListlist item = getItem(i);
            holder.tvName.setText(item.getName()+"");
            holder.tvBarCode.setText("条码："+item.getBarCode());
            holder.tvLink.setText("联系方式："+item.getLinkName()+"("+item.getLinkPhone()+")");
            holder.tvBatchNumber.setText("批次号："+item.getBatchNumber());
            holder.tvSerialNumber.setText("序列号："+item.getSerialNumber());
            if (item.getAddDate().contains("T")){
                holder.tvAddDate.setText(item.getAddDate().substring(0,item.getAddDate().indexOf("T"))+"");
            }else{
                holder.tvAddDate.setText(item.getAddDate()+"");
            }
            return view;
        }

        class XSCXHolder{
            TextView tvName,tvBarCode,tvLink,tvBatchNumber,tvSerialNumber,tvAddDate;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_xscx;
    }
}
