package com.bbld.warehouse.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbld.warehouse.R;
import com.bbld.warehouse.bean.SaleScanCode;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 销售出库--明细
 * Created by likey on 2017/8/22.
 */

public class XSCKMXActivity extends Activity{
    private String code;
    private ListView lvShowDesc;
    private RelativeLayout rlClose;
    private String token;
    private List<SaleScanCode.SaleScanCodeInfo.SaleScanCodeProductInfo.SaleScanCodeCodeList> codes;
    private CodeMxAdapter adapter;
    private Dialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xsck_mx);
        code=getIntent().getStringExtra("code");
        token=new MyToken(this).getToken();
        initView();
        loadData();
        setListeners();
    }

    private void setListeners() {
        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        lvShowDesc=(ListView)findViewById(R.id.lvShowDesc);
        rlClose=(RelativeLayout)findViewById(R.id.rlClose);
    }

    private void loadData() {
        loading=WeiboDialogUtils.createLoadingDialog(XSCKMXActivity.this,"加载中...");
        Call<SaleScanCode> call= RetrofitService.getInstance().saleScanCode(token,code);
        call.enqueue(new Callback<SaleScanCode>() {
            @Override
            public void onResponse(Response<SaleScanCode> response, Retrofit retrofit) {
                if (response==null){
                    Toast.makeText(XSCKMXActivity.this,"数据获取失败,请重试",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().getStatus()==0){
                    codes=response.body().getInfo().getProductInfo().getCodeList();
                    setAdapter();
                }else{
                    Toast.makeText(XSCKMXActivity.this,""+response.body().getMes(),Toast.LENGTH_SHORT).show();
                }
                WeiboDialogUtils.closeDialog(loading);
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(loading);
            }
        });
    }

    private void setAdapter() {
        adapter=new CodeMxAdapter();
        lvShowDesc.setAdapter(adapter);
    }

    class CodeMxAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return codes.size();
        }

        @Override
        public SaleScanCode.SaleScanCodeInfo.SaleScanCodeProductInfo.SaleScanCodeCodeList getItem(int i) {
            return codes.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            CodeMxHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_xsck_mx,null);
                holder=new CodeMxHolder();
                holder.tvCode=(TextView)view.findViewById(R.id.tvCode);
                holder.tvSerialNumber=(TextView)view.findViewById(R.id.tvSerialNumber);
                holder.tvBatchNumber=(TextView)view.findViewById(R.id.tvBatchNumber);
                view.setTag(holder);
            }
            SaleScanCode.SaleScanCodeInfo.SaleScanCodeProductInfo.SaleScanCodeCodeList item = getItem(i);
            holder= (CodeMxHolder) view.getTag();
            holder.tvCode.setText("条码："+item.getCode());
            holder.tvSerialNumber.setText("序列号："+item.getSerialNumber());
            holder.tvBatchNumber.setText("批次号："+item.getBatchNumber());
            return view;
        }

        class CodeMxHolder{
            TextView tvCode,tvSerialNumber,tvBatchNumber;
        }
    }
}
