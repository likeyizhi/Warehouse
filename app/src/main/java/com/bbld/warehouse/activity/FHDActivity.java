package com.bbld.warehouse.activity;

import android.app.Dialog;
import android.os.Bundle;
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
import com.bbld.warehouse.bean.DCGetChildOrderList;
import com.bbld.warehouse.bean.FHDGetFHDList;
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
 * 发货单
 * Created by likey on 2017/11/17.
 */

public class FHDActivity extends BaseActivity{
    @BindView(R.id.lvFHD)
    ListView lvFHD;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private String token;
    private int page=1;
    private int pagesize=10;
    private Dialog loading;
    private List<FHDGetFHDList.FHDList> fhdList;
    private FHDAdapter adapter;

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
    }

    private void loadData(boolean isLoadMore) {
        loading = WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        Call<FHDGetFHDList> call= RetrofitService.getInstance().fhdGetFHDList(token,page,pagesize);
        call.enqueue(new Callback<FHDGetFHDList>() {
            @Override
            public void onResponse(Response<FHDGetFHDList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    fhdList=response.body().getFhdlist();
                    setAdapter();
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
        adapter=new FHDAdapter();
        lvFHD.setAdapter(adapter);
    }

    class FHDAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return fhdList.size();
        }

        @Override
        public FHDGetFHDList.FHDList getItem(int i) {
            return fhdList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            FHDHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_fhd,null);
                holder=new FHDHolder();
                holder.tvTitle=(TextView)view.findViewById(R.id.tvTitle);
                holder.tvState=(TextView)view.findViewById(R.id.tvState);
                holder.tvName=(TextView)view.findViewById(R.id.tvName);
                holder.tvPerson=(TextView)view.findViewById(R.id.tvPerson);
                holder.tv_product=(TextView)view.findViewById(R.id.tv_product);
                holder.tv_productCount=(TextView)view.findViewById(R.id.tv_productCount);
                holder.tv_date=(TextView)view.findViewById(R.id.tv_date);
                holder.btn_edit=(Button) view.findViewById(R.id.btn_edit);
                holder.btn_logistics=(Button) view.findViewById(R.id.btn_logistics);
                view.setTag(holder);
            }
            holder= (FHDHolder) view.getTag();
            final FHDGetFHDList.FHDList item = getItem(i);
            holder.tvTitle.setText("订单号："+item.getOrderCode());
            holder.tvState.setText(item.getStatusMessage()+"");
            holder.tvName.setText(item.getDealerName()+"");
            holder.tvPerson.setText(item.getPhone()+"");
            holder.tv_product.setText(item.getProTCount()+"");
            holder.tv_productCount.setText("类"+item.getProTotal()+"盒");
            if (item.getAddDate().contains("T")){
                holder.tv_date.setText(item.getAddDate().substring(0,item.getAddDate().indexOf("T"))+"");
            }else{
                holder.tv_date.setText(item.getAddDate()+"");
            }
            if (item.getStatus()==1){
                holder.btn_edit.setText("编辑");
                holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.getIsProvice()==0){//0=本省，1=外省
                            Bundle bundleSN=new Bundle();
                            bundleSN.putInt("fhdId", item.getId());
                            bundleSN.putInt("isEdit",1);
                            readyGo(FHD_SNActivity.class,bundleSN);
                        }else{
                            Bundle bundleSW=new Bundle();
                            bundleSW.putInt("fhdId", item.getId());
                            bundleSW.putInt("isEdit",1);
                            readyGo(FHD_SWActivity.class,bundleSW);
                        }
                    }
                });
            }else{
                holder.btn_edit.setText("详情");
                holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.getIsProvice()==0){//0=本省，1=外省
                            Bundle bundleSN=new Bundle();
                            bundleSN.putInt("fhdId", item.getId());
                            bundleSN.putInt("isEdit",0);
                            readyGo(FHD_SNActivity.class,bundleSN);
                        }else{
                            Bundle bundleSW=new Bundle();
                            bundleSW.putInt("fhdId", item.getId());
                            bundleSW.putInt("isEdit",0);
                            readyGo(FHD_SWActivity.class,bundleSW);
                        }
                    }
                });
            }
            if (item.getStatus()==2 || item .getStatus()==3){
                holder.btn_logistics.setVisibility(View.VISIBLE);
                holder.btn_logistics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle=new Bundle();
                        bundle.putInt("invoiceId", item.getId());
                        readyGo(FHD_WLActivity.class,bundle);
                    }
                });

            }else{
                holder.btn_logistics.setVisibility(View.GONE);
            }
            return view;
        }

        class FHDHolder{
            TextView tvTitle,tvState,tvName,tvPerson,tv_product,tv_productCount,tv_date;
            Button btn_edit,btn_logistics;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_fhd;
    }
}
