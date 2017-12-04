package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
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
import com.bbld.warehouse.bean.FHDGetLogisticsList;
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
 * 物流公司
 * Created by likey on 2017/11/22.
 */

public class WLGSActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.lvWlgs)
    ListView lvWlgs;
    @BindView(R.id.tvAdd)
    TextView tvAdd;

    private String token;
    private Dialog loading;
    private List<FHDGetLogisticsList.FHDGetLogisticsListlist> logisticsList;
    private WLGSAdapter adapter;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });
    }

    private void showAddDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        View view=factory.inflate(R.layout.two_edittext_dialog,null);
        final EditText et1=(EditText)view.findViewById(R.id.et1);
        final EditText et2=(EditText)view.findViewById(R.id.et2);
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("请输入物流信息")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = (et1.getText() + "").trim();
                        String remark = (et2.getText() + "").trim();
                        addLogistics(name,remark);
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

    private void addLogistics(String name, String remark) {
        final Dialog addLoading = WeiboDialogUtils.createLoadingDialog(this, "添加中...");
        Call<FHDDeleteLogistics> call=RetrofitService.getInstance().fhdAddLogistics(token,name,remark);
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
                    showToast(response.body().getMes());
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
        Call<FHDGetLogisticsList> call= RetrofitService.getInstance().fhdGetLogisticsList(token);
        call.enqueue(new Callback<FHDGetLogisticsList>() {
            @Override
            public void onResponse(Response<FHDGetLogisticsList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    logisticsList = response.body().getList();
                    setAdapter();
                    WeiboDialogUtils.closeDialog(loading);
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
        adapter=new WLGSAdapter();
        lvWlgs.setAdapter(adapter);
    }

    class WLGSAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return logisticsList.size();
        }

        @Override
        public FHDGetLogisticsList.FHDGetLogisticsListlist getItem(int i) {
            return logisticsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            WLGSHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_wlgs,null);
                holder=new WLGSHolder();
                holder.tvName=(TextView)view.findViewById(R.id.tvName);
                holder.tvInfo=(TextView)view.findViewById(R.id.tvInfo);
                holder.tvDelete=(TextView)view.findViewById(R.id.tvDelete);
                view.setTag(holder);
            }
            holder= (WLGSHolder) view.getTag();
            final FHDGetLogisticsList.FHDGetLogisticsListlist item = getItem(i);
            holder.tvName.setText(item.getLogisticsName()+"");
            holder.tvInfo.setText(item.getRemark()+"");
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteDialog(i,item.getNOIDTBLogistics());
                }
            });
            return view;
        }

        class WLGSHolder{
            TextView tvName,tvInfo,tvDelete;
        }
    }

    private void showDeleteDialog(final int i, final int LogisticsId) {
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("删除此物流公司？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteThis(i,LogisticsId);
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

    private void deleteThis(final int i, int logisticsId) {
        final Dialog deleteLoading = WeiboDialogUtils.createLoadingDialog(this, "删除中...");
        Call<FHDDeleteLogistics> call=RetrofitService.getInstance().fhdDeleteLogistics(token,logisticsId);
        call.enqueue(new Callback<FHDDeleteLogistics>() {
            @Override
            public void onResponse(Response<FHDDeleteLogistics> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(deleteLoading);
                    return;
                }
                if (response.body().getStatus()==0){
                    logisticsList.remove(i);
                    adapter.notifyDataSetChanged();
                    WeiboDialogUtils.closeDialog(deleteLoading);
                }else{
                    showToast(response.body().getMes());
                    WeiboDialogUtils.closeDialog(deleteLoading);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(deleteLoading);
            }
        });
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_wlgs;
    }
}
