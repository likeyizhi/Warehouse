package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.CloseOrder;
import com.bbld.warehouse.bean.GetEndCustomerInfo;
import com.bbld.warehouse.bean.GetParentDealerForEndCustomer;
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
 * 终端录入（终端客户）--添加/编辑
 * Created by likey on 2017/12/4.
 */

public class ZDKHAdd_EditActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ib_back;
    @BindView(R.id.tvDealerName)
    TextView tvDealerName;
    @BindView(R.id.etContacts)
    EditText etContacts;
    @BindView(R.id.etShopName)
    EditText etShopName;
    @BindView(R.id.etContactPhone)
    EditText etContactPhone;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.tvSetXY)
    TextView tvSetXY;
    @BindView(R.id.tvX)
    TextView tvX;
    @BindView(R.id.tvY)
    TextView tvY;
    @BindView(R.id.btnAdd)
    Button btnAdd;

    private int id;
    private String token;
    private Dialog loading;
    private List<GetEndCustomerInfo.GetEndCustomerInfoDealerList> dList;
    private String[] dItems;
    private String name="";
    private String contacts="";
    private String contactphone="";
    private String address="";
    private int dealerId=0;
    private String x="";
    private String y="";
    private List<GetParentDealerForEndCustomer.GetParentDealerForEndCustomerDealerList> dListAdd;
    private String[] dItemsAdd;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        if (id!=0){
            loadData();
        }else{
            loadDealerData();
        }
        setListeners();
    }

    private void loadDealerData() {
        final Dialog loadingDealer = WeiboDialogUtils.createLoadingDialog(this, "加载中...");
        Call<GetParentDealerForEndCustomer> call=RetrofitService.getInstance().getParentDealerForEndCustomer(token);
        call.enqueue(new Callback<GetParentDealerForEndCustomer>() {
            @Override
            public void onResponse(Response<GetParentDealerForEndCustomer> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loadingDealer);
                    return;
                }
                if (response.body().getStatus()==0){
                    dListAdd=response.body().getDealerList();
                    dItemsAdd=new String[dListAdd.size()];
                    for (int i=0;i<dListAdd.size();i++){
                        dItemsAdd[i]=dListAdd.get(i).getName();
                    }
                    WeiboDialogUtils.closeDialog(loadingDealer);
                }else{
                    showToast(response.body().getMes());
                    WeiboDialogUtils.closeDialog(loadingDealer);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(loadingDealer);
            }
        });
    }

    private void setListeners() {
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvDealerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id!=0){
                    DWDialog();
                }else{
                    DW2Dialog();
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dealerId==0){
                    showToast("请选择经销商");
                }else if ((etContacts.getText()+"").trim()==null || (etContacts.getText()+"").trim().equals("")){
                    showToast("请输入联系人");
                } else if ((etContactPhone.getText()+"").trim()==null || (etContacts.getText()+"").trim().equals("")){
                    showToast("请输入联系人电话");
                } else if ((etShopName.getText()+"").trim()==null || (etContacts.getText()+"").trim().equals("")){
                    showToast("请输入店铺名称");
                } else if ((etAddress.getText()+"").trim()==null || (etContacts.getText()+"").trim().equals("")){
                    showToast("请输入详细地址");
                } else if (x==null||x.equals("")){
                    showToast("请设置位置");
                } else if (y==null||y.equals("")){
                    showToast("请设置位置");
                }else{
                    add();
                }
            }
        });
        tvSetXY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("x",x);
                bundle.putString("y",y);
                readyGoForResult(SetXYActivity.class,2010,bundle);
            }
        });
    }

    private void add() {
        name=(etShopName.getText()+"").trim();
        contacts=(etContacts.getText()+"").trim();
        contactphone=(etContactPhone.getText()+"").trim();
        address=(etAddress.getText()+"").trim();
        final Dialog loadingSure = WeiboDialogUtils.createLoadingDialog(this, "加载中...");
        Call<CloseOrder> call=RetrofitService.getInstance().addEndCustomer(token, id, name, contacts, contactphone, address, dealerId, x, y);
        Log.i("cs","cs="+token+","+id+","+name+","+contacts+","+contactphone+","+address+","+dealerId+","+x+","+y);
        call.enqueue(new Callback<CloseOrder>() {
            @Override
            public void onResponse(Response<CloseOrder> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loadingSure);
                    return;
                }
                if (response.body().getStatus()==0){
                    showToast(response.body().getMes());
                    finish();
                    WeiboDialogUtils.closeDialog(loadingSure);
                }else{
                    showToast(response.body().getMes());
                    WeiboDialogUtils.closeDialog(loadingSure);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(loadingSure);
            }
        });
    }

    private void DWDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setItems(dItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dealerId=dList.get(which).getId();
                tvDealerName.setText(dList.get(which).getName());
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void DW2Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setItems(dItemsAdd, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dealerId=dListAdd.get(which).getId();
                tvDealerName.setText(dListAdd.get(which).getName());
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void loadData() {
        loading = WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        Call<GetEndCustomerInfo> call=RetrofitService.getInstance().getEndCustomerInfo(token,id);
        call.enqueue(new Callback<GetEndCustomerInfo>() {
            @Override
            public void onResponse(Response<GetEndCustomerInfo> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    dList=response.body().getDealerList();
                    dItems=new String[dList.size()];
                    for (int i=0;i<dList.size();i++){
                        dItems[i]=dList.get(i).getName();
                    }
                    name=response.body().getName();
                    contacts=response.body().getContacts();
                    contactphone=response.body().getContactPhone();
                    address=response.body().getAddress();
                    dealerId=response.body().getDealerId();
                    x=response.body().getX();
                    y=response.body().getY();
                    setData();
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

    private void setData() {
        for (int i=0;i<dList.size();i++){
            if (dList.get(i).getId()==dealerId){
                tvDealerName.setText(dList.get(i).getName());
            }
        }
        etContacts.setText(contacts);
        etContactPhone.setText(contactphone);
        etShopName.setText(name);
        etAddress.setText(address);
        tvX.setText(x);
        tvY.setText(y);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2010 && resultCode!=0){
            x=data.getDoubleExtra("_X",0)+"";
            y=data.getDoubleExtra("_Y",0)+"";
            tvX.setText(x);
            tvY.setText(y);
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        id=extras.getInt("id",0);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_zdkh_add_edit;
    }
}
