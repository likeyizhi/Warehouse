package com.bbld.warehouse.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.TbGetOrderInfo;
import com.bbld.warehouse.bean.TbGetProductInfo;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 订单提报-商品详情
 * Created by likey on 2017/11/3.
 */

public class DDTBProInfoActivity extends BaseActivity{
    @BindView(R.id.tvCPBH)
    TextView tvCPBH;
    @BindView(R.id.tvCPMC)
    TextView tvCPMC;
    @BindView(R.id.tvCPGG)
    TextView tvCPGG;
    @BindView(R.id.tvCPDJ)
    TextView tvCPDJ;
    @BindView(R.id.tvCPDW)
    TextView tvCPDW;
    @BindView(R.id.etCPSL)
    EditText etCPSL;
    @BindView(R.id.etCHJS)
    EditText etCHJS;
    @BindView(R.id.etBZ)
    EditText etBZ;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private String token;
    private String proId;
    private Dialog loading;
    private TbGetProductInfo info;
    private int packSpecifications;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        setListeners();
    }

    private void setListeners() {
//        etCPSL.addTextChangedListener(watcher01);
//        etCHJS.addTextChangedListener(watcher02);
        etCPSL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    etCPSL.addTextChangedListener(watcher01);
                }else {
                    etCPSL.removeTextChangedListener(watcher01);
                }
            }
        });
        etCHJS.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    etCHJS.addTextChangedListener(watcher02);
                }else {
                    etCHJS.removeTextChangedListener(watcher02);
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((etCPSL.getText()+"").trim().equals("")){
                    showToast("请输入产品数量");
                }else if ((etCHJS.getText()+"").trim().equals("")){
                    showToast("请输入拆合件数");
                }else{
                    Intent intent=new Intent(DDTBProInfoActivity.this,DDTBProsActivity.class);
                    intent.putExtra("_id",info.getId());
                    intent.putExtra("_cpbh",tvCPBH.getText()+"");
                    intent.putExtra("_cpmc",tvCPMC.getText()+"");
                    intent.putExtra("_cpgg",tvCPGG.getText()+"");
                    intent.putExtra("_cpsl",etCPSL.getText()+"");
                    intent.putExtra("_chjs",etCHJS.getText()+"");
                    intent.putExtra("_bz",etBZ.getText()+"");
                    setResult(26,intent);
                    finish();
                }
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    TextWatcher watcher01=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            etCHJS.removeTextChangedListener(watcher02);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable==null || (editable+"").trim().equals("")){
                etCHJS.setText("");
            }else{
                if ((Integer.parseInt(editable+"")%packSpecifications)>0){
                    etCHJS.setText((Integer.parseInt(editable+"")/packSpecifications)+1+"");
                }else {
                    etCHJS.setText((Integer.parseInt(editable+"")/packSpecifications)+"");
                }
            }
        }
    };
    TextWatcher watcher02=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            etCPSL.removeTextChangedListener(watcher01);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable==null || (editable+"").trim().equals("")){
                etCPSL.setText("");
            }else {
                etCPSL.setText(packSpecifications*(Integer.parseInt(editable+""))+"");
            }
        }
    };

    private void loadData() {
        loading= WeiboDialogUtils.createLoadingDialog(DDTBProInfoActivity.this,"加载中...");
        Call<TbGetProductInfo> call= RetrofitService.getInstance().tbGetProductInfo(token,proId);
        call.enqueue(new Callback<TbGetProductInfo>() {
            @Override
            public void onResponse(Response<TbGetProductInfo> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    info=response.body();
                    packSpecifications = info.getPackSpecifications();
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
        tvCPBH.setText(info.getProductCode()+"");
        tvCPMC.setText(info.getProductName()+"");
        tvCPGG.setText(info.getProSpecifications()+"");
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        proId=extras.getString("proId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_ddtb_proinfo;
    }
}
