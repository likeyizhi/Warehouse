package com.bbld.warehouse.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.DCOGetEndCustomerList;
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
 * 终端客户
 * Created by likey on 2017/11/21.
 */

public class ZDKHActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.lvZdkh)
    ListView lvZdkh;
    @BindView(R.id.tvAdd)
    TextView tvAdd;

    private Dialog loading;
    private String token;
    private int page=1;
    private int pagesize=10;
    private List<DCOGetEndCustomerList.DCOGetEndCustomerListlist> dcoList;
    private DCOAdapter adapter;

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
        lvZdkh.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("id",0);
                readyGo(ZDKHAdd_EditActivity.class,bundle);
            }
        });
    }

    private void loadData(final boolean isLoadMore) {
        loading = WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        Call<DCOGetEndCustomerList> call= RetrofitService.getInstance().dcoGetEndCustomerList(token,page,pagesize);
        call.enqueue(new Callback<DCOGetEndCustomerList>() {
            @Override
            public void onResponse(Response<DCOGetEndCustomerList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    if (isLoadMore){
                        List<DCOGetEndCustomerList.DCOGetEndCustomerListlist> dcoListAdd = response.body().getList();
                        dcoList.addAll(dcoListAdd);
                        adapter.notifyDataSetChanged();
                        WeiboDialogUtils.closeDialog(loading);
                    }else{
                        dcoList=response.body().getList();
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
        adapter=new DCOAdapter();
        lvZdkh.setAdapter(adapter);
    }

    class DCOAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return dcoList.size();
        }

        @Override
        public DCOGetEndCustomerList.DCOGetEndCustomerListlist getItem(int i) {
            return dcoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ZDKHHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_zdkh,null);
                holder=new ZDKHHolder();
                holder.tvName=(TextView)view.findViewById(R.id.tvName);
                holder.tvContact=(TextView)view.findViewById(R.id.tvContact);
                holder.tvAddress=(TextView)view.findViewById(R.id.tvAddress);
                holder.tvPName=(TextView)view.findViewById(R.id.tvPName);
                view.setTag(holder);
            }
            holder= (ZDKHHolder) view.getTag();
            final DCOGetEndCustomerList.DCOGetEndCustomerListlist item = getItem(i);
            holder.tvName.setText("姓名："+item.getName());
            holder.tvContact.setText("联系方式："+item.getContacts()+"("+item.getContactPhone()+")");
            holder.tvAddress.setText("地址："+item.getAddress());
            holder.tvPName.setText("上级名称："+item.getPName());
            if (view!=null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle=new Bundle();
                        bundle.putInt("id",item.getId());
                        readyGo(ZDKHAdd_EditActivity.class,bundle);
                    }
                });
            }
            return view;
        }

        class ZDKHHolder{
            TextView tvName,tvContact,tvAddress,tvPName;
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
        return R.layout.activity_zdkh;
    }
}
