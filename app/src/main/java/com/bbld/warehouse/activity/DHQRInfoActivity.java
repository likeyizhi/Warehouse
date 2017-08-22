package com.bbld.warehouse.activity;

import android.app.Dialog;
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
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.CusInvoiceConfirm;
import com.bbld.warehouse.bean.CusInvoiceInfo;
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
 * 到货确认详情
 * Created by likey on 2017/8/11.
 */

public class DHQRInfoActivity extends BaseActivity{
    @BindView(R.id.tv_channelName)
    TextView tvChannelName;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_orderNumber)
    TextView tvOrderNumber;
    @BindView(R.id.lv_backOrderInfo)
    ListView lvInfo;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.ib_back)
    ImageButton ibBack;


    private String customerInvoiceId;
    private String token;
    private CusInvoiceInfo.CusInvoiceInfoInfo cusInfo;
    private List<CusInvoiceInfo.CusInvoiceInfoInfo.CusInvoiceInfoProductList> cusPros;
    private CusInfoAdapter adapter;
    private Dialog loading;
    private String request;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    WeiboDialogUtils.closeDialog(loading);
                    showToast(""+request);
                    finish();
                    break;
                case 222:
                    WeiboDialogUtils.closeDialog(loading);
                    showToast(""+request);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        setListeners();
    }

    private void setListeners() {
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading=WeiboDialogUtils.createLoadingDialog(DHQRInfoActivity.this,"加载中...");
                sure();
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sure() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    request= UploadUserInformationByPostService.cusInvoiceConfirm(token,customerInvoiceId);
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
        Call<CusInvoiceInfo> call= RetrofitService.getInstance().getCusInvoiceInfo(token, customerInvoiceId);
        call.enqueue(new Callback<CusInvoiceInfo>() {
            @Override
            public void onResponse(Response<CusInvoiceInfo> response, Retrofit retrofit) {
                if (response==null){
                    return;
                }
                if (response.body().getStatus()==0){
                    cusInfo = response.body().getInfo();
                    cusPros = cusInfo.getProductList();
                    setData();
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setData() {
        tvChannelName.setText(cusInfo.getCusName()+"");
        tvRemark.setText(cusInfo.getRemark()+"");
        tvDate.setText(cusInfo.getDate()+"");
        tvOrderNumber.setText(cusInfo.getStorageNumber());
        setAdapter();
    }

    private void setAdapter() {
        adapter=new CusInfoAdapter();
        lvInfo.setAdapter(adapter);
    }

    class CusInfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cusPros.size();
        }

        @Override
        public CusInvoiceInfo.CusInvoiceInfoInfo.CusInvoiceInfoProductList getItem(int i) {
            return cusPros.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            CusProsHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_dhqr_info,null);
                holder=new CusProsHolder();
                holder.iv_ProductImg=(ImageView)view.findViewById(R.id.iv_ProductImg);
                holder.tv_ProductName=(TextView)view.findViewById(R.id.tv_ProductName);
                holder.tv_ProductSpec=(TextView)view.findViewById(R.id.tv_ProductSpec);
                holder.tv_Unit=(TextView)view.findViewById(R.id.tv_Unit);
                holder.tv_ProductCount=(TextView)view.findViewById(R.id.tv_ProductCount);
                holder.lvCodes=(ListView) view.findViewById(R.id.lvCodes);
                view.setTag(holder);
            }
            holder= (CusProsHolder) view.getTag();
            CusInvoiceInfo.CusInvoiceInfoInfo.CusInvoiceInfoProductList product = getItem(i);
            Glide.with(getApplicationContext()).load(product.getProductImg()).error(R.mipmap.xiuzhneg).into(holder.iv_ProductImg);
            holder.tv_ProductName.setText(product.getProductName()+"");
            holder.tv_ProductSpec.setText(product.getProductSpec()+"");
            holder.tv_Unit.setText(product.getUnit()+"");
            holder.tv_ProductCount.setText(product.getProductCount()+"");
            holder.lvCodes.setAdapter(new CodesAdapter(product.getCodeList()));
            return view;
        }
        class CusProsHolder{
            ImageView iv_ProductImg;
            TextView tv_ProductName;
            TextView tv_ProductSpec;
            TextView tv_Unit;
            TextView tv_ProductCount;
            ListView lvCodes;
        }
    }

    class CodesAdapter extends BaseAdapter{
        private List<CusInvoiceInfo.CusInvoiceInfoInfo.CusInvoiceInfoProductList.CusInvoiceInfoCodeList> codeList;
        public CodesAdapter(List<CusInvoiceInfo.CusInvoiceInfoInfo.CusInvoiceInfoProductList.CusInvoiceInfoCodeList> codeList) {
            super();
            this.codeList=codeList;
        }

        @Override
        public int getCount() {
            return codeList.size();
        }

        @Override
        public CusInvoiceInfo.CusInvoiceInfoInfo.CusInvoiceInfoProductList.CusInvoiceInfoCodeList getItem(int i) {
            return codeList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            CodesHolder holder=null;
            if (view==null){
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_dhqr_info_codes,null);
                holder=new CodesHolder();
                holder.tvCode=(TextView)view.findViewById(R.id.tvCode);
                holder.tvSerialNumber=(TextView)view.findViewById(R.id.tvSerialNumber);
                holder.tvBatchNumber=(TextView)view.findViewById(R.id.tvBatchNumber);
                view.setTag(holder);
            }
            CusInvoiceInfo.CusInvoiceInfoInfo.CusInvoiceInfoProductList.CusInvoiceInfoCodeList item = getItem(i);
            holder= (CodesHolder) view.getTag();
            holder.tvCode.setText("条码："+item.getCode());
            holder.tvSerialNumber.setText("序列号："+item.getSerialNumber());
            holder.tvBatchNumber.setText("批次号："+item.getBatchNumber());
            return view;
        }

        class CodesHolder{
            TextView tvCode,tvSerialNumber,tvBatchNumber;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        customerInvoiceId=extras.getString("customerInvoiceId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_dhqr_info;
    }
}
