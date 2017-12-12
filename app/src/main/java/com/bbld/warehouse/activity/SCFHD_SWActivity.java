package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.DCandDGC;
import com.bbld.warehouse.bean.FHDCreateFHD;
import com.bbld.warehouse.bean.FHDGetAddOtherProviceFHDInfo;
import com.bbld.warehouse.bean.FHDGetDealerDeliveryList;
import com.bbld.warehouse.bean.FHDGetOrderProductList;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.utils.UploadUserInformationByPostService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 生成发货单-省外
 * Created by likey on 2017/11/14.
 */

public class SCFHD_SWActivity extends BaseActivity{
    @BindView(R.id.tvOrderCode)
    TextView tvOrderCode;
    @BindView(R.id.tvDealerName)
    TextView tvDealerName;
    @BindView(R.id.tvDealerRemark)
    TextView tvDealerRemark;
    @BindView(R.id.tvHeaderRemark)
    TextView tvHeaderRemark;
    @BindView(R.id.tvDealerWarehouse)
    TextView tvDealerWarehouse;
    @BindView(R.id.tvWarehouseName)
    TextView tvWarehouseName;
    @BindView(R.id.tvWarehousePerson)
    TextView tvWarehousePerson;
    @BindView(R.id.tvWarehousePhone)
    TextView tvWarehousePhone;
    @BindView(R.id.tvWarehouseAddr)
    TextView tvWarehouseAddr;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.lvSCFHD_sw)
    ListView lvSCFHD_sw;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tvOrderNumber)
    TextView tvOrderNumber;

    private String token;
    private Dialog loading;
    private int orderId;
    private FHDGetAddOtherProviceFHDInfo fhdInfo;
    private List<FHDGetAddOtherProviceFHDInfo.FHDGetAddOtherProviceFHDInfoD> warehouseList;
    private String[] itemsW;
    private int warehouseId;
    private List<FHDGetAddOtherProviceFHDInfo.FHDGetAddOtherProviceFHDInfoR> receiveDealerList;
    private String[] itemsR;
    private List<FHDGetDealerDeliveryList.FHDGetDealerDeliveryListlist> ddlList;
    private String[] itemsD;
    private int deliveryId;
    private int dealerId;
    private List<FHDGetOrderProductList.FHDGetOrderProductListlist> bottomList;
    private ArrayList<DCandDGC> dcdgcs;
    private BottomAdapter bottomAdapter;
    private Dialog upLoading;
    private String request;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    WeiboDialogUtils.closeDialog(upLoading);
                    showToast(""+request);
                    SCFHDActivity.scfhdActivity.finish();
                    readyGo(FHDActivity.class);
                    finish();
                    break;
                case 222:
                    WeiboDialogUtils.closeDialog(upLoading);
                    showToast(""+request);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        loadBottomData();
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvDealerWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WarehouseDialog();
            }
        });
        tvWarehouseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReceiveDialog();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count=0;
                for (int i=0;i<dcdgcs.size();i++){
                    if (dcdgcs.get(i).getDeliverGiveCount()==0 && dcdgcs.get(i).getDeliveryCount()==0){
                        count=1;
                    }
                }
                if (count==1){
                    showToast("本次发货数、配赠数不能都为 0 ");
                }else{
                    if (warehouseId==0){
                        showToast("请选择发货仓库");
                    }else{
                        CreateFHD();
                    }
                }
            }
        });
    }

    private void CreateFHD() {
        upLoading=WeiboDialogUtils.createLoadingDialog(this,"上传中...");
        FHDCreateFHD creatFHD = new FHDCreateFHD();
        creatFHD.setFhdId(0);
        creatFHD.setOrderId(orderId);
        creatFHD.setRemark(etRemark.getText()+"");
        creatFHD.setWarehouseId(warehouseId);
        creatFHD.setIsProvice(1);
        creatFHD.setDealerId(dealerId);
        creatFHD.setDeliveryId(deliveryId);
        creatFHD.setFhdProductList(dcdgcs);
        Gson gson=new Gson();
        final String fhdJson=gson.toJson(creatFHD);
//        Log.i("fhdJson","fhdJson="+fhdJson);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    request= UploadUserInformationByPostService.createFHD(token,
                            fhdJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (request.contains("成功")) { // 请求成功
                    Message message=new Message();
                    message.what=111;
                    handler.sendMessage(message);
                } else { // 请求失败
                    Message message=new Message();
                    message.what=222;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private void loadData() {
        loading= WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        Call<FHDGetAddOtherProviceFHDInfo> call= RetrofitService.getInstance().fhdGetAddOtherProviceFHDInfo(token,orderId);
        call.enqueue(new Callback<FHDGetAddOtherProviceFHDInfo>() {
            @Override
            public void onResponse(Response<FHDGetAddOtherProviceFHDInfo> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    fhdInfo=response.body();
                    warehouseList=response.body().getDealerWarehouseList();
                    itemsW=new String[warehouseList.size()];
                    for (int i=0;i<warehouseList.size();i++){
                        itemsW[i]=warehouseList.get(i).getName();
                    }
                    receiveDealerList=response.body().getReceiveDealerList();
                    itemsR=new String[receiveDealerList.size()];
                    for (int i=0;i<receiveDealerList.size();i++){
                        itemsR[i]=receiveDealerList.get(i).getName();
                    }
                    setData();
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

    private void setData() {
        tvOrderCode.setText(fhdInfo.getOrderCode()+"");
        tvDealerName.setText(fhdInfo.getDealerName()+"");
        tvDealerRemark.setText(fhdInfo.getDealerRemark()+"");
        tvHeaderRemark.setText(fhdInfo.getHeaderRemark()+"");
    }

    private void loadBottomData() {
        final Dialog loadingBottom = WeiboDialogUtils.createLoadingDialog(this, "加载中...");
        Call<FHDGetOrderProductList> call=RetrofitService.getInstance().fhdGetOrderProductList(token,orderId);
        call.enqueue(new Callback<FHDGetOrderProductList>() {
            @Override
            public void onResponse(Response<FHDGetOrderProductList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loadingBottom);
                    return;
                }
                if (response.body().getStatus()==0){
                    bottomList=response.body().getList();
                    dcdgcs=new ArrayList<DCandDGC>();
                    for (int i=0;i<bottomList.size();i++){
                        DCandDGC dcdgc = new DCandDGC(bottomList.get(i).getProductId(),
                                bottomList.get(i).getProductAmount()-bottomList.get(i).getDeliveryCount(),
                                bottomList.get(i).getGiveAmount()-bottomList.get(i).getDeliveryGiveCount());
                        dcdgcs.add(dcdgc);
                    }
                    setBottomAdapter();
                    WeiboDialogUtils.closeDialog(loadingBottom);
                }else{
                    WeiboDialogUtils.closeDialog(loadingBottom);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(loadingBottom);
            }
        });
    }

    private void setBottomAdapter() {
        bottomAdapter=new BottomAdapter();
        lvSCFHD_sw.setAdapter(bottomAdapter);
    }

    class BottomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return bottomList.size();
        }

        @Override
        public FHDGetOrderProductList.FHDGetOrderProductListlist getItem(int i) {
            return bottomList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            BottomHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_scfhd_sn,null);
                holder=new BottomHolder();
                holder.img=(ImageView)view.findViewById(R.id.img);
                holder.tvName=(TextView) view.findViewById(R.id.tvName);
                holder.tvSpec=(TextView) view.findViewById(R.id.tvSpec);
                holder.tvProductAmount=(TextView) view.findViewById(R.id.tvProductAmount);
                holder.tvGiveAmount=(TextView) view.findViewById(R.id.tvGiveAmount);
                holder.tvCurrentProductAmount=(TextView) view.findViewById(R.id.tvCurrentProductAmount);
                holder.tvCurrentGiveAmount=(TextView) view.findViewById(R.id.tvCurrentGiveAmount);
                holder.ivDelete=(ImageView) view.findViewById(R.id.ivDelete);
                view.setTag(holder);
            }
            holder= (BottomHolder) view.getTag();
            final FHDGetOrderProductList.FHDGetOrderProductListlist item = getItem(i);
            Glide.with(getApplicationContext()).load(item.getLogo()).error(R.mipmap.xiuzhneg).into(holder.img);
            holder.tvName.setText(item.getName()+"");
            holder.tvSpec.setText("规格："+item.getProSpecifications()+"");
            holder.tvProductAmount.setText(Html.fromHtml("订货数："+"<font color=\"#FF9900\">"+item.getProductAmount()+"（未发："+(item.getProductAmount()-item.getDeliveryCount())+"）"+"</font>"));
            holder.tvGiveAmount.setText(Html.fromHtml("配赠数："+"<font color=\"#00CC66\">"+item.getGiveAmount()+"（未发："+(item.getGiveAmount()-item.getDeliveryGiveCount())+"）"+"</font>"));
            holder.tvCurrentProductAmount.setText(dcdgcs.get(i).getDeliveryCount()+"");
            holder.tvCurrentGiveAmount.setText(dcdgcs.get(i).getDeliverGiveCount()+"");
            holder.tvCurrentProductAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInputDialog(1,i,(item.getProductAmount()-item.getDeliveryCount()));
                }
            });
            holder.tvCurrentGiveAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInputDialog(2,i,(item.getGiveAmount()-item.getDeliveryGiveCount()));
                }
            });
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteDialog(i);
                }
            });
            return view;
        }

        class BottomHolder{
            ImageView img;
            TextView tvName,tvSpec,tvProductAmount,tvGiveAmount,tvCurrentProductAmount,tvCurrentGiveAmount;
            ImageView ivDelete;
        }
    }

    private void showDeleteDialog(final int i) {
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("删除此条？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dcdgcs.remove(i);
                        bottomList.remove(i);
                        bottomAdapter.notifyDataSetChanged();
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

    private void showInputDialog(final int whichC, final int i, final int maxCount) {
        final EditText et = new EditText(this);
        et.setBackgroundResource(R.drawable.bg_batch);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setHint("输入数量");
        et.setMaxLines(1);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setSingleLine(true);
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("请输入数量")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int input = Integer.parseInt((et.getText() + "").trim());
                        if (whichC==1){
                            if (input>maxCount){
                                showToast("输入量超出");
                            }else{
                                dcdgcs.get(i).setDeliveryCount(input);
                                bottomAdapter.notifyDataSetChanged();
                            }
                        }else{
                            if (input>maxCount){
                                showToast("输入量超出");
                            }else {
                                dcdgcs.get(i).setDeliverGiveCount(input);
                                bottomAdapter.notifyDataSetChanged();
                            }
                        }
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

    //选择发货仓库Dialoh
    private void WarehouseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setItems(itemsW, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvDealerWarehouse.setText(itemsW[which]);
                warehouseId=warehouseList.get(which).getId();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    //选择收货经销商Dialog
    private void ReceiveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setItems(itemsR, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvWarehouseName.setText(itemsR[which]);
                dealerId=receiveDealerList.get(which).getId();
                getDealerDeliveryList();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    //获取收货人列表
    private void getDealerDeliveryList() {
        final Dialog ddlLoading = WeiboDialogUtils.createLoadingDialog(this, "加载中...");
        Call<FHDGetDealerDeliveryList> call=RetrofitService.getInstance().fhdGetDealerDeliveryList(token,dealerId);
        call.enqueue(new Callback<FHDGetDealerDeliveryList>() {
            @Override
            public void onResponse(Response<FHDGetDealerDeliveryList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(ddlLoading);
                    return;
                }
                if (response.body().getStatus()==0){
                    ddlList=response.body().getList();
                    itemsD=new String[ddlList.size()];
                    for (int i=0;i<ddlList.size();i++){
                        itemsD[i]=ddlList.get(i).getName();
                    }
                    tvWarehousePerson.setText("选择");
                    tvWarehousePhone.setText("");
                    tvWarehouseAddr.setText("");
                    deliveryId=0;
                    setWPClickListeners();
                    WeiboDialogUtils.closeDialog(ddlLoading);
                }else{
                    WeiboDialogUtils.closeDialog(ddlLoading);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(ddlLoading);
            }
        });
    }
    //选择收货人点击事件
    private void setWPClickListeners() {
        tvWarehousePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WarehousePersonDialog();
            }
        });
    }
    //选择收货人Dialog
    private void WarehousePersonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setItems(itemsD, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvWarehousePerson.setText(itemsD[which]);
                deliveryId=ddlList.get(which).getId();
                tvWarehousePhone.setText(ddlList.get(which).getPhone()+"");
                tvWarehouseAddr.setText(ddlList.get(which).getAreas()+ddlList.get(which).getAddress()+"");
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    @Override
    protected void getBundleExtras(Bundle extras) {
        orderId=extras.getInt("orderId", 0);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_scfhd_sw;
    }
}
