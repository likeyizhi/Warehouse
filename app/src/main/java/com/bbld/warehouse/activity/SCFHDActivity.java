package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
 * 生成发货单
 * Created by likey on 2017/11/8.
 */

public class SCFHDActivity extends BaseActivity{
    @BindView(R.id.lvSCFHD)
    ListView lvSCFHD;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private Dialog loading;
    private String token;
    private int page;
    private int pagesize;
    private List<DCGetChildOrderList.DCGetChildOrderListlist> list;
    private SCFHDAdapter adapter;
    private String[] items={"省内","省外"};
    public static SCFHDActivity scfhdActivity=null;

    @Override
    protected void initViewsAndEvents() {
        scfhdActivity=this;
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
        loading= WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        Call<DCGetChildOrderList> call= RetrofitService.getInstance().dcGetChildOrderList(token,page,pagesize);
        call.enqueue(new Callback<DCGetChildOrderList>() {
            @Override
            public void onResponse(Response<DCGetChildOrderList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    list=response.body().getList();
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
        adapter=new SCFHDAdapter();
        lvSCFHD.setAdapter(adapter);
    }

    class SCFHDAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public DCGetChildOrderList.DCGetChildOrderListlist getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            SCFHDHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_scfhd,null);
                holder=new SCFHDHolder();
                holder.tvTitle=(TextView)view.findViewById(R.id.tvTitle);
                holder.tvState=(TextView)view.findViewById(R.id.tvState);
                holder.tvName=(TextView)view.findViewById(R.id.tvName);
                holder.tvPerson=(TextView)view.findViewById(R.id.tvPerson);
                holder.tv_product=(TextView)view.findViewById(R.id.tv_product);
                holder.tv_productCount=(TextView)view.findViewById(R.id.tv_productCount);
                holder.tv_date=(TextView)view.findViewById(R.id.tv_date);
                holder.btn_edit=(Button) view.findViewById(R.id.btn_edit);
                view.setTag(holder);
            }
            holder= (SCFHDHolder) view.getTag();
            final DCGetChildOrderList.DCGetChildOrderListlist item = getItem(i);
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
                holder.btn_edit.setText("新订单处理");
                holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundleNew=new Bundle();
                        bundleNew.putInt("orderId", item.getOrderId());
                        bundleNew.putInt("isShow",1);
                        readyGo(SCFHD_newActivity.class,bundleNew);
                    }
                });
            }else if (item.getOrderStatus()==2){
                holder.btn_edit.setText("生成发货单");
                holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showWhereDialog(item.getOrderId());
                    }
                });
            }else{
                holder.btn_edit.setText("详情");
                holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundleNew=new Bundle();
                        bundleNew.putInt("orderId", item.getOrderId());
                        bundleNew.putInt("isShow",2);
                        readyGo(SCFHD_newActivity.class,bundleNew);
                    }
                });
            }
            return view;
        }

        class SCFHDHolder {
            TextView tvTitle,tvState,tvName,tvPerson,tv_product,tv_productCount,tv_date;
            Button btn_edit;
        }
    }

    private void showWhereDialog(final int orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Bundle bundleSN=new Bundle();
                        bundleSN.putInt("orderId", orderId);
                        readyGo(SCFHD_SNActivity.class,bundleSN);
                        break;
                    case 1:
                        Bundle bundleSW=new Bundle();
                        bundleSW.putInt("orderId", orderId);
                        readyGo(SCFHD_SWActivity.class,bundleSW);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
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
        return R.layout.activity_scfhd;
    }
}
