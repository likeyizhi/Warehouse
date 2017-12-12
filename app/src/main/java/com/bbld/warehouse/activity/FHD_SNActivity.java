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
 * 发货单-省内
 * Created by likey on 2017/11/17.
 */

public class FHD_SNActivity extends BaseActivity{
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
    @BindView(R.id.lvSCFHD_sn)
    ListView lvSCFHD_sn;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tvOrderNumber)
    TextView tvOrderNumber;
    @BindView(R.id.tvAddress)
    TextView tvAddress;

    private int isEdit;
    private String token;
    private Dialog loading;
    private int fhdId;
    private FHDCreateFHD createFHD;
    private FHDGetCurrentProviceFHDInfo fhd;
    private List<DCandDGC> dcdgcs;
    private List<FHDGetCurrentProviceFHDInfo.FHDFhdProList> proList;
    private String warehouseName;
    private FHDAdapter adapter;
    private List<FHDGetCurrentProviceFHDInfo.FHDDealerWarehouseList> warehouseList;
    private String[] items;
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

    private void CreateFHD() {
        upLoading=WeiboDialogUtils.createLoadingDialog(this,"上传中...");
        Gson gson=new Gson();
        final String fhdJson=gson.toJson(createFHD);
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
        Call<FHDGetCurrentProviceFHDInfo> call= RetrofitService.getInstance().fhdGetCurrentProviceFHDInfo(token,fhdId);
        call.enqueue(new Callback<FHDGetCurrentProviceFHDInfo>() {
            @Override
            public void onResponse(Response<FHDGetCurrentProviceFHDInfo> response, Retrofit retrofit) {
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
                    dcdgcs=new ArrayList<DCandDGC>();
                    createFHD=new FHDCreateFHD();
                    createFHD.setFhdId(fhdId);
                    createFHD.setOrderId(fhd.getOrderId());
                    createFHD.setRemark(fhd.getFhdRemark()+"");
                    createFHD.setWarehouseId(fhd.getDealerWarehouseId());
                    createFHD.setIsProvice(0);//0=本省发货单，1=外省发货单
                    createFHD.setDealerId(fhd.getDealerId());
                    createFHD.setDeliveryId(fhd.getDeliveryId());
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
        tvAddress.setText(fhd.getAddress()+"");
        for (int i=0;i<fhd.getDealerWarehouseList().size();i++){
            if (fhd.getDealerWarehouseList().get(i).getId()==fhd.getDealerWarehouseId()){
                warehouseName=fhd.getDealerWarehouseList().get(i).getName();
            }
        }
        tvDealerWarehouse.setText(warehouseName+"");
        etRemark.setText(fhd.getFhdRemark()+"");
        setFHDAdapter();
    }

    private void setFHDAdapter() {
        adapter=new FHDAdapter();
        lvSCFHD_sn.setAdapter(adapter);
    }

    class FHDAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return proList.size();
        }

        @Override
        public FHDGetCurrentProviceFHDInfo.FHDFhdProList getItem(int i) {
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
            final FHDGetCurrentProviceFHDInfo.FHDFhdProList item = getItem(i);
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
        return R.layout.activity_scfhd_sn;
    }
}
