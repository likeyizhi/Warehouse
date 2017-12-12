package com.bbld.warehouse.activity;

import android.app.Dialog;
import android.os.Bundle;
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
import com.bbld.warehouse.bean.CusInvoiceGetCusInvoiceList;
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
 * 终端发货
 * Created by likey on 2017/11/29.
 */

public class ZDFHActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.lvZdfh)
    ListView lvZdfh;
    @BindView(R.id.btnAdd)
    Button btnAdd;

    private Dialog loading;
    private String token;
    private int page;
    private int pagesize;
    private List<CusInvoiceGetCusInvoiceList.CusInvoiceGetCusInvoiceListlist> list;
    private ZdfhAdapter adapter;

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
        lvZdfh.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(ZDFHAddActivity.class);
            }
        });
    }

    private void loadData(final boolean isLoadMore) {
        loading= WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        Call<CusInvoiceGetCusInvoiceList> call= RetrofitService.getInstance().cusInvoiceGetCusInvoiceList(token,page,pagesize);
        call.enqueue(new Callback<CusInvoiceGetCusInvoiceList>() {
            @Override
            public void onResponse(Response<CusInvoiceGetCusInvoiceList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    if (isLoadMore){
                        List<CusInvoiceGetCusInvoiceList.CusInvoiceGetCusInvoiceListlist> listAdd = response.body().getList();
                        listAdd.addAll(listAdd);
                        adapter.notifyDataSetChanged();
                    }else{
                        list=response.body().getList();
                        setAdapter();
                    }
                    WeiboDialogUtils.closeDialog(loading);
                }else{
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
        adapter=new ZdfhAdapter();
        lvZdfh.setAdapter(adapter);
    }

    class ZdfhAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CusInvoiceGetCusInvoiceList.CusInvoiceGetCusInvoiceListlist getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ZdfhHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_zdfh,null);
                holder=new ZdfhHolder();
                holder.tvTitle=(TextView)view.findViewById(R.id.tvTitle);
                holder.tvName=(TextView)view.findViewById(R.id.tvName);
                holder.tvPerson=(TextView)view.findViewById(R.id.tvPerson);
                holder.tvState=(TextView)view.findViewById(R.id.tvState);
                holder.tv_product=(TextView)view.findViewById(R.id.tv_product);
                holder.tv_productCount=(TextView)view.findViewById(R.id.tv_productCount);
                holder.tv_date=(TextView)view.findViewById(R.id.tv_date);
                holder.btnEdit=(Button) view.findViewById(R.id.btnEdit);
                holder.btnInfo=(Button) view.findViewById(R.id.btnInfo);
                holder.btnCK=(Button) view.findViewById(R.id.btnCK);
                view.setTag(holder);
            }
            holder= (ZdfhHolder) view.getTag();
            final CusInvoiceGetCusInvoiceList.CusInvoiceGetCusInvoiceListlist item = getItem(i);
            holder.tvTitle.setText("单号："+item.getCustomerInvoiceCode());
            holder.tvState.setText(item.getStatusMessage()+"");
            holder.tvName.setText(item.getName()+"");
            holder.tvPerson.setText(item.getContactPhone()+"");
            holder.tv_product.setText(item.getTypeTotal()+"");
            holder.tv_productCount.setText("类"+item.getProductTotal()+"盒");
            if (item.getAddDate().contains("T")){
                holder.tv_date.setText(item.getAddDate().substring(0,item.getAddDate().indexOf("T"))+"");
            }else{
                holder.tv_date.setText(item.getAddDate()+"");
            }
            if (item.getStatus()==1){
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnCK.setVisibility(View.VISIBLE);
                holder.btnInfo.setVisibility(View.GONE);
                holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        showToast("编辑"+item.getId());
                        Bundle bundleEdit=new Bundle();
                        bundleEdit.putInt("id",item.getId());
                        bundleEdit.putInt("isEdit",1);
                        readyGo(ZDFHEditActivity.class,bundleEdit);
                    }
                });
                holder.btnCK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle=new Bundle();
                        bundle.putString("customerInvoiceId",item.getId()+"");
                        readyGo(ZDPSOutActivity.class, bundle);
                    }
                });
            }else{
                holder.btnEdit.setVisibility(View.GONE);
                holder.btnCK.setVisibility(View.GONE);
                holder.btnInfo.setVisibility(View.VISIBLE);
                holder.btnInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        showToast("详情"+item.getId());
                        Bundle bundleEdit=new Bundle();
                        bundleEdit.putInt("id",item.getId());
                        bundleEdit.putInt("isEdit",0);
                        readyGo(ZDFHEditActivity.class,bundleEdit);
                    }
                });
            }
            return view;
        }

        class ZdfhHolder{
            TextView tvTitle,tvState,tvName,tvPerson,tv_product,tv_productCount,tv_date;
            Button btnEdit,btnInfo,btnCK;
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
        return R.layout.activity_zdfh;
    }
}
