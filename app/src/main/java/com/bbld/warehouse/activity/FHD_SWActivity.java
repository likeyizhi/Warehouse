package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bbld.warehouse.bean.FHDGetCurrentProviceFHDInfo;
import com.bbld.warehouse.bean.FHDGetDealerDeliveryList;
import com.bbld.warehouse.bean.FHDGetOtherProviceFHDInfo;
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
 * 发货单-省外
 * Created by likey on 2017/11/17.
 */

public class FHD_SWActivity extends BaseActivity{
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

    private int fhdId;
    private int isEdit;
    private String token;
    private Dialog loading;
    private FHDGetOtherProviceFHDInfo fhd;
    private List<FHDGetOtherProviceFHDInfo.FHDFhdProList> proList;
    private List<FHDGetOtherProviceFHDInfo.FHDDealerWarehouseList> warehouseList;
    private String[] items;
    private ArrayList<DCandDGC> dcdgcs;
    private FHDCreateFHD createFHD;
    private String warehouseName;
    private FHDAdapter adapter;
    private Dialog upLoading;
    private String request;
    private List<FHDGetOtherProviceFHDInfo.FHDReceiveDealerList> receiveDealerList;
    private String[] itemsR;
    private String[] itemsD;
    private List<FHDGetDealerDeliveryList.FHDGetDealerDeliveryListlist> ddlList;
    private int dealerId;
    private int deliveryId;
    private List<FHDGetOtherProviceFHDInfo.FHDReceiveDeliveryList> receiveDeliveryList;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    WeiboDialogUtils.closeDialog(upLoading);
                    showToast(""+request);
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
        setListeners();
        if (isEdit==0){
            btnSave.setVisibility(View.GONE);
        }else {
            btnSave.setVisibility(View.VISIBLE);
        }
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
                    CreateFHD();
                }
            }
        });
    }

    //选择收货经销商Dialog
    private void ReceiveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setItems(itemsR, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvWarehouseName.setText(itemsR[which]);
                dealerId=receiveDealerList.get(which).getId();
                createFHD.setDealerId(dealerId);
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
                createFHD.setDeliveryId(ddlList.get(which).getId());
                tvWarehousePhone.setText(ddlList.get(which).getPhone()+"");
                tvWarehouseAddr.setText(ddlList.get(which).getAreas()+ddlList.get(which).getAddress()+"");
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private void CreateFHD() {
        upLoading=WeiboDialogUtils.createLoadingDialog(this,"上传中...");
        Gson gson=new Gson();
        final String fhdJson=gson.toJson(createFHD);
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

    private void WarehouseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvDealerWarehouse.setText(items[which]);
                createFHD.setWarehouseId(warehouseList.get(which).getId());
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void loadData() {
        loading = WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        Call<FHDGetOtherProviceFHDInfo> call= RetrofitService.getInstance().fhdGetOtherProviceFHDInfo(token,fhdId);
        call.enqueue(new Callback<FHDGetOtherProviceFHDInfo>() {
            @Override
            public void onResponse(Response<FHDGetOtherProviceFHDInfo> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    fhd=response.body();
                    proList=fhd.getFhdProList();
                    warehouseList=fhd.getDealerWarehouseList();
                    items=new String[warehouseList.size()];
                    for (int i=0;i<warehouseList.size();i++){
                        items[i]=warehouseList.get(i).getName();
                    }
                    receiveDealerList=response.body().getReceiveDealerList();
                    itemsR=new String[receiveDealerList.size()];
                    for (int i=0;i<receiveDealerList.size();i++){
                        itemsR[i]=receiveDealerList.get(i).getName();
                    }
                    receiveDeliveryList=response.body().getReceiveDeliveryList();
                    dcdgcs=new ArrayList<DCandDGC>();
                    createFHD=new FHDCreateFHD();
                    createFHD.setFhdId(fhdId);
                    createFHD.setOrderId(fhd.getOrderId());
                    createFHD.setRemark(fhd.getFhdRemark()+"");
                    createFHD.setWarehouseId(fhd.getDealerWarehouseId());
                    createFHD.setIsProvice(0);//0=本省发货单，1=外省发货单
                    createFHD.setDealerId(fhd.getReceiveDealerId());
                    createFHD.setDeliveryId(fhd.getReceiveDeliveryId());
                    for (int i=0;i<proList.size();i++){
                        DCandDGC dcdgc = new DCandDGC(proList.get(i).getProductId(),
                                proList.get(i).getDeliveryAmount(),
                                proList.get(i).getDeliverGiveAmount());
                        dcdgcs.add(dcdgc);
                    }
                    createFHD.setFhdProductList(dcdgcs);
                    setData();
                    WeiboDialogUtils.closeDialog(loading);
                }else{
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

    private void setData() {
        tvOrderCode.setText(fhd.getOrderCode()+"");
        tvDealerName.setText(fhd.getDealerName()+"");
        tvDealerRemark.setText(fhd.getDealerRemark()+"");
        tvHeaderRemark.setText(fhd.getHeaderRemark()+"");
        tvOrderNumber.setText(fhd.getFhdCode()+"");
        for (int i=0;i<fhd.getDealerWarehouseList().size();i++){
            if (fhd.getDealerWarehouseList().get(i).getId()==fhd.getDealerWarehouseId()){
                warehouseName=fhd.getDealerWarehouseList().get(i).getName();
            }
        }
        tvDealerWarehouse.setText(warehouseName+"");
        for (int i=0;i<receiveDealerList.size();i++){
            if (receiveDealerList.get(i).getId()==fhd.getReceiveDealerId()){
                tvWarehouseName.setText(receiveDealerList.get(i).getName());
            }
        }
        for (int i=0;i<receiveDeliveryList.size();i++){
            if (receiveDeliveryList.get(i).getId()==fhd.getReceiveDeliveryId()){
                tvWarehousePerson.setText(receiveDeliveryList.get(i).getName()+"");
                tvWarehousePhone.setText(receiveDeliveryList.get(i).getPhone());
                tvWarehouseAddr.setText(receiveDeliveryList.get(i).getAreas()+receiveDeliveryList.get(i).getAddress());
            }
        }
        etRemark.setText(fhd.getFhdRemark()+"");
        setFHDAdapter();
    }

    private void setFHDAdapter() {
        adapter=new FHDAdapter();
        lvSCFHD_sw.setAdapter(adapter);
    }

    class FHDAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return proList.size();
        }

        @Override
        public FHDGetOtherProviceFHDInfo.FHDFhdProList getItem(int i) {
            return proList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            FHDHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_scfhd_sn,null);
                holder=new FHDHolder();
                holder.img=(ImageView)view.findViewById(R.id.img);
                holder.tvName=(TextView) view.findViewById(R.id.tvName);
                holder.tvSpec=(TextView) view.findViewById(R.id.tvSpec);
                holder.tvProductAmount=(TextView) view.findViewById(R.id.tvProductAmount);
                holder.tvGiveAmount=(TextView) view.findViewById(R.id.tvGiveAmount);
                holder.tvCurrentProductAmount=(TextView) view.findViewById(R.id.tvCurrentProductAmount);
                holder.tvCurrentGiveAmount=(TextView) view.findViewById(R.id.tvCurrentGiveAmount);
                view.setTag(holder);
            }
            holder= (FHDHolder) view.getTag();
            final FHDGetOtherProviceFHDInfo.FHDFhdProList item = getItem(i);
            Glide.with(getApplicationContext()).load(item.getLogo()+"").error(R.mipmap.xiuzhneg).into(holder.img);
            holder.tvName.setText(item.getName()+"");
            holder.tvSpec.setText("规格："+item.getProSpecifications()+"");
            holder.tvProductAmount.setText(Html.fromHtml("订货数："+"<font color=\"#FF9900\">"+item.getProductAmount()+"（未发："+(item.getProductAmount()-item.getDeliveryCount())+"）"+"</font>"));
            holder.tvGiveAmount.setText(Html.fromHtml("配赠数："+"<font color=\"#00CC66\">"+item.getGiveAmount()+"（未发："+(item.getGiveAmount()-item.getDeliveryGiveCount())+"）"+"</font>"));
            holder.tvCurrentProductAmount.setText(dcdgcs.get(i).getDeliveryCount()+"");
            holder.tvCurrentGiveAmount.setText(dcdgcs.get(i).getDeliverGiveCount()+"");
            holder.tvCurrentProductAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInputDialog(1,i,(item.getProductAmount()-item.getDeliveryCount()+item.getDeliveryAmount()));
                }
            });
            holder.tvCurrentGiveAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInputDialog(2,i,(item.getGiveAmount()-item.getDeliveryGiveCount()+item.getDeliverGiveAmount()));
                }
            });
            return view;
        }

        class FHDHolder{
            ImageView img;
            TextView tvName,tvSpec,tvProductAmount,tvGiveAmount,tvCurrentProductAmount,tvCurrentGiveAmount;
        }
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
                                adapter.notifyDataSetChanged();
                            }
                        }else{
                            if (input>maxCount){
                                showToast("输入量超出");
                            }else {
                                dcdgcs.get(i).setDeliverGiveCount(input);
                                adapter.notifyDataSetChanged();
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

    @Override
    protected void getBundleExtras(Bundle extras) {
        fhdId=extras.getInt("fhdId",0);
        isEdit=extras.getInt("isEdit",0);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_scfhd_sw;
    }
}
