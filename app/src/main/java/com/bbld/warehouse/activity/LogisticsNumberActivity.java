package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.AddOrderLogisticsInfo;
import com.bbld.warehouse.bean.GetLogisticsList;
import com.bbld.warehouse.bean.GetOrderLogisticsInfo;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.ApkTool;
import com.bbld.warehouse.utils.MyAppInfo;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.zxinglogistics.android.CaptureActivity;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by zzy on 2017/5/24.
 * 物流编号录入
 */

public class LogisticsNumberActivity extends BaseActivity {
    @BindView(R.id.tv_channelname)
    TextView tvChannelName;
    @BindView(R.id.tv_dealername)
    TextView tvDealerName;
    @BindView(R.id.tv_orderid)
    TextView tvOrderID;
    @BindView(R.id.lv_add_logistics)
    ListView lvAddLogistics;
    @BindView(R.id.tv_data)
    TextView tvData;
    @BindView(R.id.tv_ordercount)
    TextView tvOrderCount;
    @BindView(R.id.tv_productcount)
    TextView tvProductCount;
    @BindView(R.id.ed_logisticsid)
    EditText edLogisticsID;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.tv_logistics)
    TextView tvLogistics;
    @BindView(R.id.tv_yincangid)
    TextView tvYincangID;
    @BindView(R.id.tv_yincangname)
    TextView tvYincangName;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.ivScan)
    ImageView ivScan;

    private  int  invoiceid;
    private int logisticsId;
    private String number;
    private String[] items;
    private  String OrderID;
    private  String ChannelName;
    private  String DealerName;
    private  String Count;
    private  String ProductCount;
    private  String Date;
    private List<GetLogisticsList.GetLogisticsListList> getLogisticsList;
    private List<GetOrderLogisticsInfo.GetOrderLogisticsInfoList> logisticsInfoList;
    private GetOrderLogisticsInfoAdapter getOrderLogisticsInfoAdapter;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private List<MyAppInfo> appInfos;
    private Dialog loading;
    private boolean is_iData;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1101:
                    for (int i=0;i<appInfos.size();i++) {
                        if (appInfos.get(i).getAppName().equals("com.android.auto.iscan")) {
                            is_iData=true;
                            WeiboDialogUtils.closeDialog(loading);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        tvOrderID.setText("订单号"+OrderID);
        tvChannelName.setText(ChannelName);
        tvData.setText(Date);
        tvDealerName.setText(DealerName);
        tvProductCount.setText(ProductCount);
        tvOrderCount.setText(Count);
        loadData();
        setListeners();
        initAppList();
    }
    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvLogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseTypeIdDialog();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
                edLogisticsID.setText("");
                edLogisticsID.clearFocus();
                hideInputMethod();
            }
        });
        ivScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_iData){
                    showToast("iData终端，可直接扫取");
                }else{
                    readyGoForResult(CaptureActivity.class,REQUEST_CODE_SCAN);
                }
            }
        });
    }

    private void initAppList(){
        loading=WeiboDialogUtils.createLoadingDialog(LogisticsNumberActivity.this,"加载中...");
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                appInfos = ApkTool.scanLocalInstallAppList(LogisticsNumberActivity.this.getPackageManager());
                handler.sendEmptyMessage(1101);
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                edLogisticsID.setText(content+"");
            }
        }
    }
    @Override
    protected void getBundleExtras(Bundle extras) {
        OrderID= extras.getString("OrderNumber");
        ChannelName=  extras.getString("ChannelName");
        DealerName=   extras.getString("DealerName");
        Count=  extras.getString("Count");
        ProductCount= extras.getString("ProductCount");
        Date=  extras.getString("Date");
        invoiceid = Integer.parseInt(extras.getString("OrderID"));
    }
    private void loadData() {
        /**
         * 物流信息查询接口
         */
        Call<GetOrderLogisticsInfo> call1 = RetrofitService.getInstance().getOrderLogisticsInfo(new MyToken(LogisticsNumberActivity.this).getToken() + "",  invoiceid);
        call1.enqueue(new Callback<GetOrderLogisticsInfo>() {
            @Override
            public void onResponse(Response<GetOrderLogisticsInfo> response, Retrofit retrofit) {
                if (response.body() == null) {
                    showToast("服务器出错");
                    return;
                }
                if (response.body().getStatus() == 0) {
                    logisticsInfoList=response.body().getList();
                    setAdapter();

                }else {
                    showToast(""+response.body().getMes());
                }
            }
            @Override
            public void onFailure(Throwable throwable) {
            }
        });
        /**
         * 获取物流公司字典接口
         */
        Call<GetLogisticsList> call2 = RetrofitService.getInstance().getLogisticsList(new MyToken(LogisticsNumberActivity.this).getToken()+"");
        call2.enqueue(new Callback<GetLogisticsList>() {
            @Override
            public void onResponse(Response<GetLogisticsList> response, Retrofit retrofit) {
                if (response.body() == null) {
                    showToast("出入库类型列表获取失败");
                    return;
                }
                if (response.body().getStatus() == 0) {
                    getLogisticsList = response.body().getList();
                    if (!getLogisticsList.isEmpty()){
                        logisticsId=getLogisticsList.get(0).getLogisticsID();
                        tvLogistics.setText(getLogisticsList.get(0).getLogisticsName()+"");
                    }
                    items = new String[getLogisticsList.size()];
                    for (int t=0;t<getLogisticsList.size();t++){
                        items[t]=getLogisticsList.get(t).getLogisticsName();
                    }
                }else {
                    showToast(""+response.body().getMes());
                }

            }
            @Override
            public void onFailure(Throwable throwable) {
            }
        });


    }
    private void setData(){
        /**
         * 增加物流信息接口
         */
        number=edLogisticsID.getText().toString();
        if(number==null||number.trim().equals("")){
            showToast("请添加物流编号");
        }else{
            Call<AddOrderLogisticsInfo> call3 = RetrofitService.getInstance().addOrderLogisticsInfo(new MyToken(LogisticsNumberActivity.this).getToken() + "",invoiceid,logisticsId,number);
//            showToast("货号"+invoiceid+"物流id"+logisticsId+"物流单号："+number+"");
            call3.enqueue(new Callback<AddOrderLogisticsInfo>() {
                @Override
                public void onResponse(Response<AddOrderLogisticsInfo> response, Retrofit retrofit) {
                    if (response.body() == null) {
                        showToast("服务器出错");
                        return;
                    }
                    if (response.body().getStatus() == 0) {
                        showToast(response.body().getMes()+"");
                        loadData();

                    }else{
                        showToast(response.body().getMes()+"");
                    }

                }

                @Override
                public void onFailure(Throwable throwable) {
                }
            });

        }

    }

    private void setAdapter() {
        getOrderLogisticsInfoAdapter = new GetOrderLogisticsInfoAdapter();
        lvAddLogistics.setAdapter(getOrderLogisticsInfoAdapter);
    }

    class GetOrderLogisticsInfoAdapter extends BaseAdapter {
        GetOrderLogisticsInfoHolder holder = null;

        @Override
        public int getCount() {
            return logisticsInfoList.size();
        }

        @Override
        public GetOrderLogisticsInfo.GetOrderLogisticsInfoList getItem(int i) {
            return logisticsInfoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_logistics_number, null);
                holder = new GetOrderLogisticsInfoHolder();
                holder.tvLogisticsNmae = (TextView) view.findViewById(R.id.tv_logisticsname);
                holder.tvLogisticsNumber= (TextView) view.findViewById(R.id.tv_logisticsnumber);
                view.setTag(holder);
            }
            holder = (GetOrderLogisticsInfoHolder) view.getTag();
            final GetOrderLogisticsInfo.GetOrderLogisticsInfoList list = getItem(i);
            holder.tvLogisticsNmae.setText(list.getLogisticsName()+"");

            holder.tvLogisticsNumber.setText(list.getLogisticsNumber()+"");
            return view;
        }
        class GetOrderLogisticsInfoHolder {

            TextView tvLogisticsNmae;
            TextView tvLogisticsNumber;
        }
    }
    private void chooseTypeIdDialog() {
        AlertDialog alertDialog=new AlertDialog.Builder(LogisticsNumberActivity.this)
                .setTitle("请选择物流")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logisticsId=getLogisticsList.get(i).getLogisticsID();
                        tvLogistics.setText(getLogisticsList.get(i).getLogisticsName());
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }
    @Override
    public int getContentView() {
        return R.layout.activity_logistics_number;
    }
}
