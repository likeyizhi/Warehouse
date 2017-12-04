package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.FHDDeleteLogistics;
import com.bbld.warehouse.bean.FHDGetCboLogistics;
import com.bbld.warehouse.bean.FHDGetInvoiceLogisticsList;
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
 * 发货单-物流设置
 * Created by likey on 2017/11/22.
 */

public class FHD_WLActivity extends BaseActivity{
    @BindView(R.id.lvFhdWl)
    ListView lvFhdWl;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tvAdd)
    TextView tvAdd;

    private int invoiceId;
    private Dialog loading;
    private String token;
    private List<FHDGetInvoiceLogisticsList.FHDGetInvoiceLogisticsListlist> list;
    private WLAdapter adapter;
    private String[] items;
    private List<FHDGetCboLogistics.FHDGetCboLogisticslist> cbs;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        setListners();
    }

    private void setListners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCboLogistics();
            }
        });
    }
    //获取--获取经销商物流公司(下拉使用)
    private void getCboLogistics() {
        final Dialog getLoading = WeiboDialogUtils.createLoadingDialog(this, "");
        Call<FHDGetCboLogistics> call=RetrofitService.getInstance().fhdGetCboLogistics(token);
        call.enqueue(new Callback<FHDGetCboLogistics>() {
            @Override
            public void onResponse(Response<FHDGetCboLogistics> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(getLoading);
                    return;
                }
                if (response.body().getStatus()==0){
                    cbs=response.body().getList();
                    items=new String[cbs.size()];
                    for (int i=0;i<cbs.size();i++){
                        items[i]=cbs.get(i).getName();
                    }
                    showAddInfoLogistics();
                    WeiboDialogUtils.closeDialog(getLoading);
                }else{
                    showToast(response.body().getMes()+"");
                    WeiboDialogUtils.closeDialog(getLoading);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(getLoading);
            }
        });
    }
    //选择物流公司Dialog
    private void showAddInfoLogistics() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int logisticsId = cbs.get(which).getId();
                showAddInfoInput(logisticsId);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    //添加物流信息Dialog
    private void showAddInfoInput(final int logisticsId) {
        LayoutInflater factory = LayoutInflater.from(this);
        View view=factory.inflate(R.layout.two_edittext_dialog02,null);
        final EditText et1=(EditText)view.findViewById(R.id.et1);
        final EditText et2=(EditText)view.findViewById(R.id.et2);
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("请输入物流单号及备注")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String number = (et1.getText() + "").trim();
                        String remark = (et2.getText() + "").trim();
                        addInfo(logisticsId,number,remark);
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
    }
    //添加物流信息
    private void addInfo(int logisticsId, String number, String remark) {
        final Dialog addLoading = WeiboDialogUtils.createLoadingDialog(this, "添加中...");
        Call<FHDDeleteLogistics> call=RetrofitService.getInstance().fhdAddInvoiceLogisticsInfo(token,invoiceId,logisticsId,number,remark);
        call.enqueue(new Callback<FHDDeleteLogistics>() {
            @Override
            public void onResponse(Response<FHDDeleteLogistics> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(addLoading);
                    return;
                }
                if (response.body().getStatus()==0){
                    loadData();
                    WeiboDialogUtils.closeDialog(addLoading);
                }else{
                    showToast(response.body().getMes()+"");
                    WeiboDialogUtils.closeDialog(addLoading);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(addLoading);
            }
        });
    }

    private void loadData() {
        loading = WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        Call<FHDGetInvoiceLogisticsList> call= RetrofitService.getInstance().fhdGetInvoiceLogisticsList(token,invoiceId);
        call.enqueue(new Callback<FHDGetInvoiceLogisticsList>() {
            @Override
            public void onResponse(Response<FHDGetInvoiceLogisticsList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    list=response.body().getList();
                    setAdapter();
                    WeiboDialogUtils.closeDialog(loading);
                }else {
                    showToast(response.body().getMes()+"");
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
        adapter=new WLAdapter();
        lvFhdWl.setAdapter(adapter);
    }

    class WLAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public FHDGetInvoiceLogisticsList.FHDGetInvoiceLogisticsListlist getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            WLHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_fhd_wl,null);
                holder=new WLHolder();
                holder.tvName=(TextView)view.findViewById(R.id.tvName);
                holder.tvNumber=(TextView)view.findViewById(R.id.tvNumber);
                view.setTag(holder);
            }
            holder= (WLHolder) view.getTag();
            final FHDGetInvoiceLogisticsList.FHDGetInvoiceLogisticsListlist item = getItem(i);
            holder.tvName.setText(item.getLogisticsName()+"");
            holder.tvNumber.setText("物流单号："+item.getLogisticalCode());
            if (view!=null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle=new Bundle();
                        bundle.putInt("id",item.getId());
                        readyGo(FHD_WL_InfoActivity.class,bundle);
                    }
                });
            }
            return view;
        }

        class WLHolder{
            TextView tvName,tvNumber;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        invoiceId=extras.getInt("invoiceId",0);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_fhd_wl;
    }
}
