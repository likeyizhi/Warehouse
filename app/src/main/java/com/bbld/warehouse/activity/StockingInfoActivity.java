package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.CancelInventory;
import com.bbld.warehouse.bean.FinishInventory;
import com.bbld.warehouse.bean.InventoryInfo;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.utils.UploadUserInformationByPostService;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 库存盘点详情
 * Created by likey on 2017/6/7.
 */

public class StockingInfoActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tvInventoryNumber)
    TextView tvInventoryNumber;
    @BindView(R.id.tvInventoryStatus)
    TextView tvInventoryStatus;
    @BindView(R.id.tvInventoryRemark)
    TextView tvInventoryRemark;
    @BindView(R.id.lvProductList)
    ListView lvProductList;
    @BindView(R.id.btnCancle)
    Button btnCancle;
    @BindView(R.id.btnEdit)
    Button btnEdit;
    @BindView(R.id.btnDecision)
    Button btnDecision;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;


    private String InventoryId;
    private List<InventoryInfo.InventoryInfoInfo.InventoryInfoProductList> productList;
    private StockingInfoAdapter adapter;
    private String request;
    private Dialog baocunDialog;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    WeiboDialogUtils.closeDialog(baocunDialog);
                    showToast(""+request);
                    break;
                case 222:
                    WeiboDialogUtils.closeDialog(baocunDialog);
                    showToast(""+request);
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        loadData();
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(StockingInfoActivity.this);
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancleDialog();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("InventoryId",InventoryId);
                readyGo(AddStockingActivity.class, bundle);
            }
        });
        btnDecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDecisionDialog();
            }
        });
    }

    private void cancle() {
        Call<CancelInventory> call=RetrofitService.getInstance().cancelInventory(new MyToken(StockingInfoActivity.this).getToken(),InventoryId);
        call.enqueue(new Callback<CancelInventory>() {
            @Override
            public void onResponse(Response<CancelInventory> response, Retrofit retrofit) {
                if (response==null){
                    showToast(getResources().getString(R.string.get_data_fail));
                    return;
                }
                if (response.body().getStatus()==0){
                    showToast("操作成功");
                    ActivityManagerUtil.getInstance().finishActivity(StockingInfoActivity.this);
                }else {
                    showToast(response.body().getMes()+"");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void loadData() {
        Call<InventoryInfo> call= RetrofitService.getInstance().inventoryInfo(new MyToken(StockingInfoActivity.this).getToken(), InventoryId);
        call.enqueue(new Callback<InventoryInfo>() {
            @Override
            public void onResponse(Response<InventoryInfo> response, Retrofit retrofit) {
                if (response==null){
                    showToast(getResources().getString(R.string.get_data_fail));
                    return;
                }
                if (response.body().getStatus()==0){
                    InventoryInfo.InventoryInfoInfo InventoryInfo = response.body().getInfo();
                    setData(InventoryInfo);
                }else{
                    showToast(response.body().getMes()+"");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setData(InventoryInfo.InventoryInfoInfo inventoryInfo) {
        tvInventoryNumber.setText("盘点单号:"+inventoryInfo.getInventoryNumber());
        tvInventoryStatus.setText(inventoryInfo.getInventoryStatus()+"");
        tvInventoryRemark.setText(inventoryInfo.getInventoryRemark()+"");
        productList = inventoryInfo.getProductList();
        if (inventoryInfo.getCanOperation()==0){
            llBottom.setVisibility(View.GONE);
        }else{
            llBottom.setVisibility(View.VISIBLE);
        }
        setAdapter();
    }

    private void setAdapter() {
        adapter=new StockingInfoAdapter();
        lvProductList.setAdapter(adapter);
    }

    class StockingInfoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public InventoryInfo.InventoryInfoInfo.InventoryInfoProductList getItem(int i) {
            return productList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(productList.get(i).getProductID()+"");
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            StockingInfoHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_stocking_info,null);
                holder=new StockingInfoHolder();
                holder.iv_productImg=(ImageView)view.findViewById(R.id.iv_productImg);
                holder.tv_productName=(TextView) view.findViewById(R.id.tv_productName);
                holder.tv_productSpec=(TextView) view.findViewById(R.id.tv_productSpec);
                holder.tv_productCount=(TextView) view.findViewById(R.id.tv_productCount);
                holder.btn_toInfo=(Button) view.findViewById(R.id.btn_toInfo);
                view.setTag(holder);
            }
            holder= (StockingInfoHolder) view.getTag();
            final InventoryInfo.InventoryInfoInfo.InventoryInfoProductList product = getItem(i);
            Glide.with(getApplicationContext()).load(product.getProductImg()).error(R.mipmap.xiuzhneg).into(holder.iv_productImg);
            holder.tv_productName.setText(product.getProductName()+"");
            holder.tv_productSpec.setText(product.getProductSpec()+"");
            holder.tv_productCount.setText("盘点数量："+product.getProductCount());
            holder.btn_toInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("InventoryId", InventoryId);
                    bundle.putInt("clickPosition", i);
                    readyGo(CodeInfoActivity.class, bundle);
                }
            });
            return view;
        }

        class StockingInfoHolder{
            ImageView iv_productImg;
            TextView tv_productName,tv_productSpec,tv_productCount;
            Button btn_toInfo;
        }
    }

    private void showCancleDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(StockingInfoActivity.this);
        builder.setMessage("作废此订单？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancle();
                ActivityManagerUtil.getInstance().finishActivity(StockingInfoActivity.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
    private void showDecisionDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(StockingInfoActivity.this);
        builder.setMessage("决策此订单？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                decision();
                ActivityManagerUtil.getInstance().finishActivity(StockingInfoActivity.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void decision() {
        baocunDialog= WeiboDialogUtils.createLoadingDialog(StockingInfoActivity.this,getString(R.string.caozuo_ing));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    request= UploadUserInformationByPostService.storageFinishInventory(new MyToken(StockingInfoActivity.this).getToken()+"", InventoryId);
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
//        Call<FinishInventory> call=RetrofitService.getInstance().finishInventory(new MyToken(StockingInfoActivity.this).getToken(), InventoryId);
//        call.enqueue(new Callback<FinishInventory>() {
//            @Override
//            public void onResponse(Response<FinishInventory> response, Retrofit retrofit) {
//                if (response==null){
//                    showToast(getResources().getString(R.string.get_data_fail));
//                    return;
//                }
//                if (response.body().getStatus()==0){
//                    showToast("操作成功");
//                }else{
//                    showToast(""+response.body().getMes());
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//
//            }
//        });
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        InventoryId=extras.getString("InventoryId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_stocking_info;
    }
}
