package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.bbld.warehouse.bean.BarcodeConnectSave;
import com.bbld.warehouse.bean.BarcodeConnectScan;
import com.bbld.warehouse.bean.TbGetOrderInfo;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.utils.UploadUserInformationByPostService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 关联
 * Created by likey on 2017/11/16.
 */

public class BarcodeConnectActivity extends BaseActivity{
    @BindView(R.id.tvXM)
    TextView tvXM;
    @BindView(R.id.tvHM)
    TextView tvHM;
    @BindView(R.id.etInpur)
    EditText etInpur;
    @BindView(R.id.tvAllCount)
    TextView tvAllCount;
    @BindView(R.id.tvCurrentCount)
    TextView tvCurrentCount;
    @BindView(R.id.lvBarcode)
    ListView lvBarcode;
    @BindView(R.id.btnClear)
    Button btnClear;
    @BindView(R.id.btnCommite)
    Button btnCommite;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private BarcodeConnectSave save;
    private ArrayList<String> saveList;
    private String token;
    private CodeAdapter codeAdapter;
    private int scanCount=0;
    private String request;
    private Dialog upLoading;
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
        createSave();
        setListeners();
    }

    private void createSave() {
        save=new BarcodeConnectSave();
        saveList=new ArrayList<String>();
    }

    private void setListeners() {
        tvXM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
        etInpur.addTextChangedListener(watcher);
        tvHM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveList.clear();
                tvCurrentCount.setText("0");
                codeAdapter.notifyDataSetChanged();
            }
        });
        btnCommite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoading=WeiboDialogUtils.createLoadingDialog(BarcodeConnectActivity.this,"提交中...");
                save.setList(saveList);
                Gson gson=new Gson();
                final String codejson=gson.toJson(save);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            request= UploadUserInformationByPostService.saveBarCodeConnect(token,
                                    codejson);
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
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if ((editable+"").trim().equals("") || editable==null){

            }else{
                if (saveList.contains(editable+"")){
                    showToast("改条码已存在");
                }else{
                    getCodeInfo(editable+"");
                }
            }
        }
    };

    private void getCodeInfo(final String code) {
        final Dialog loading = WeiboDialogUtils.createLoadingDialog(this, "加载中...");
        Call<BarcodeConnectScan> call= RetrofitService.getInstance().barcodeConnectScan(token,code);
        call.enqueue(new Callback<BarcodeConnectScan>() {
            @Override
            public void onResponse(Response<BarcodeConnectScan> response, Retrofit retrofit) {
                if (response==null){
                    etInpur.setText("");
                    showToast("请重试");
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    BarcodeConnectScan.BarcodeConnectScanInfo codeInfo = response.body().getInfo();
                    int codeType = codeInfo.getType();//1=箱码，2=盒码
                    if (codeType==1){
                        if ((tvXM.getText()+"").trim().equals("") || tvXM.getText()==null){
                            save.setCode(codeInfo.getCode()+"");
                            save.setCount(codeInfo.getCount()+"");
                            tvXM.setText(codeInfo.getCode());
                            tvAllCount.setText(codeInfo.getCount()+"");
                        }else{
                            WarningDialog(codeInfo.getCode()+"",codeInfo.getCount()+"");
                        }
                    }else{
                        if ((tvXM.getText()+"").trim().equals("") || tvXM.getText()==null){
                            showToast("请先扫箱码");
                        }else{
                            int currentCount = Integer.parseInt(tvCurrentCount.getText() + "");
                            if (currentCount<Integer.parseInt(tvAllCount.getText()+"")){
                                saveList.add(code+"");
                                tvHM.setText(code+"");
                                tvCurrentCount.setText((currentCount+1)+"");
                                setCodeAdapter();
                            }else{
                                showToast("扫描完毕");
                            }
                        }
                    }
                    etInpur.setText("");
                    WeiboDialogUtils.closeDialog(loading);
                }else{
                    etInpur.setText("");
                    showToast(response.body().getMes()+"请重试");
                    WeiboDialogUtils.closeDialog(loading);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                etInpur.setText("");
                showToast("请重试");
                WeiboDialogUtils.closeDialog(loading);
            }
        });
    }

    private void setCodeAdapter() {
        Collections.reverse(saveList);
        codeAdapter=new CodeAdapter();
        lvBarcode.setAdapter(codeAdapter);
    }

    class CodeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return saveList.size();
        }

        @Override
        public String getItem(int i) {
            return saveList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            CodeHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_barcode_connect,null);
                holder=new CodeHolder();
                holder.ivDelete=(ImageView)view.findViewById(R.id.ivDelete);
                holder.tvCode=(TextView) view.findViewById(R.id.tvCode);
                view.setTag(holder);
            }
            holder= (CodeHolder) view.getTag();
            String item = getItem(i);
            holder.tvCode.setText(item+"");
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveList.remove(i);
                    codeAdapter.notifyDataSetChanged();
                }
            });
            return view;
        }

        class CodeHolder{
            ImageView ivDelete;
            TextView tvCode;
        }
    }


    private void showInputDialog() {
        final EditText et = new EditText(this);
        et.setBackgroundResource(R.drawable.bg_batch);
        et.setHint("输入条码");
        et.setMaxLines(1);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setSingleLine(true);
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("请输入条码")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getCodeInfo((et.getText()+"").trim());
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

    private void WarningDialog(final String code, final String count) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setMessage("将("+tvXM.getText()+")替换为("+code+")?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvXM.setText(code);
                tvHM.setText("");
                tvAllCount.setText(count);
                saveList.clear();
                codeAdapter.notifyDataSetChanged();
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

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_barcode_connect;
    }
}
