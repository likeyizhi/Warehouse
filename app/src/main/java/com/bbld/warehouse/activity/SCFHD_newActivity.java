package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.DCOModifyOrder;
import com.bbld.warehouse.bean.DealerChildOrderInitOrderClose;
import com.bbld.warehouse.bean.DealerChildOrderInitOrderPass;
import com.bbld.warehouse.bean.TbGetOrderInfo;
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
 * 生成发货单-新订单处理
 * Created by likey on 2017/11/14.
 */

public class SCFHD_newActivity extends BaseActivity{
    @BindView(R.id.lvProduct)
    ListView lvProduct;
    @BindView(R.id.tvClose)
    TextView tvClose;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvPerson)
    TextView tvPerson;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvAddr)
    TextView tvAddr;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.btnOK)
    Button btnOK;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.etHeadRemark)
    EditText etHeadRemark;

    private int orderId;
    private Dialog loading;
    private TbGetOrderInfo res;
    private List<TbGetOrderInfo.TbGetOrderInfoProductList> pros;
    private NewAdapter adapter;
    private String token;
    private Dialog upLoading;
    private String request;
    private int isShow;
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
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count=0;
                for (int i=0;i<pros.size();i++){
                    if (pros.get(i).getProductAmount()==0 && pros.get(i).getGiveAmount()==0){
                        count=1;
                    }
                }
                if (count==1){
                    showToast("商品订货数、配赠数不能同时为0");
                }else{
                    save();
                }
            }
        });
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass();
            }
        });
    }

    private void close() {
        final Dialog closeLoading = WeiboDialogUtils.createLoadingDialog(this, "正在关闭");
        Call<DealerChildOrderInitOrderClose> call=RetrofitService.getInstance().dealerChildOrderInitOrderClose(token,orderId,etHeadRemark.getText()+"");
        call.enqueue(new Callback<DealerChildOrderInitOrderClose>() {
            @Override
            public void onResponse(Response<DealerChildOrderInitOrderClose> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(closeLoading);
                    showToast("请重试");
                    return;
                }
                if (response.body().getStatus()==0){
                    finish();
                    WeiboDialogUtils.closeDialog(closeLoading);
                }else {
                    WeiboDialogUtils.closeDialog(closeLoading);
                    showToast("请重试");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                showToast("请重试");
                WeiboDialogUtils.closeDialog(closeLoading);
            }
        });
    }

    private void pass() {
        final Dialog passLoading = WeiboDialogUtils.createLoadingDialog(this, "上传中...");
        Call<DealerChildOrderInitOrderPass> call=RetrofitService.getInstance().dealerChildOrderInitOrderPass(token,orderId,etHeadRemark.getText()+"");
        call.enqueue(new Callback<DealerChildOrderInitOrderPass>() {
            @Override
            public void onResponse(Response<DealerChildOrderInitOrderPass> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(passLoading);
                    showToast("请重试");
                    return;
                }
                if (response.body().getStatus()==0){
                    WeiboDialogUtils.closeDialog(passLoading);
                    finish();
                }else {
                    showToast("请重试");
                    WeiboDialogUtils.closeDialog(passLoading);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                showToast("请重试");
                WeiboDialogUtils.closeDialog(passLoading);
            }
        });
    }

    private void save() {
        upLoading=WeiboDialogUtils.createLoadingDialog(this,"上传中...");
        DCOModifyOrder dcoMO = new DCOModifyOrder();
        dcoMO.setOrderId(orderId);
        dcoMO.setHeadRemark(etHeadRemark.getText()+"");
        List<DCOModifyOrder.DCOModifyOrderproductList> dcoList=new ArrayList<DCOModifyOrder.DCOModifyOrderproductList>();
        for (int i=0;i<pros.size();i++){
            DCOModifyOrder.DCOModifyOrderproductList dco = new DCOModifyOrder.DCOModifyOrderproductList(pros.get(i).getProductId()
                    ,pros.get(i).getProductAmount(),pros.get(i).getRemark(),pros.get(i).getGiveAmount());
            dcoList.add(dco);
        }
        dcoMO.setProductList(dcoList);
        Gson gson=new Gson();
        final String orderJson = gson.toJson(dcoMO);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    request= UploadUserInformationByPostService.dealerChildOrderModifyOrder(token,
                            orderJson);
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
        loading= WeiboDialogUtils.createLoadingDialog(SCFHD_newActivity.this,"加载中...");
        Call<TbGetOrderInfo> call= RetrofitService.getInstance().tbGetOrderInfo(token,orderId+"");
        call.enqueue(new Callback<TbGetOrderInfo>() {
            @Override
            public void onResponse(Response<TbGetOrderInfo> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    res=response.body();
                    pros=res.getProductList();
                    setData();
                    setAdapter();
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
        tvNumber.setText(res.getOrderCode()+"");
        tvPerson.setText(res.getName()+"");
        tvPhone.setText(res.getPhone()+"");
        tvAddr.setText(res.getAreas()+res.getAddress());
        etRemark.setText(res.getDealerRemark()+"");
        etHeadRemark.setText(res.getHeadRemark()+"");
        if (isShow==2){
            llBottom.setVisibility(View.GONE);
            tvClose.setVisibility(View.GONE);
            tvPerson.setClickable(false);
        }else{
            llBottom.setVisibility(View.VISIBLE);
            tvClose.setVisibility(View.VISIBLE);
            tvPerson.setClickable(false);
        }
    }

    private void setAdapter() {
        adapter=new NewAdapter();
        lvProduct.setAdapter(adapter);
    }

    class NewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pros.size();
        }

        @Override
        public TbGetOrderInfo.TbGetOrderInfoProductList getItem(int i) {
            return pros.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            NewHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_scfhd_new,null);
                holder=new NewHolder();
                holder.img=(ImageView)view.findViewById(R.id.img);
                holder.tvDele=(TextView) view.findViewById(R.id.tvDele);
                holder.tvProName=(TextView) view.findViewById(R.id.tvProName);
                holder.tvProSpec=(TextView) view.findViewById(R.id.tvProSpec);
                holder.tvProUnit=(TextView) view.findViewById(R.id.tvProUnit);
                holder.tvProNeedNumber=(TextView) view.findViewById(R.id.tvProNeedNumber);
                holder.tvProActualNumber=(TextView) view.findViewById(R.id.tvProActualNumber);
                holder.tvProRemark=(TextView) view.findViewById(R.id.tvProRemark);
                view.setTag(holder);
            }
            holder= (NewHolder) view.getTag();
            final TbGetOrderInfo.TbGetOrderInfoProductList item = getItem(i);
            Glide.with(getApplicationContext()).load(item.getLogo()).error(R.mipmap.xiuzhneg).into(holder.img);
            holder.tvDele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pros.remove(i);
                    adapter.notifyDataSetChanged();
                }
            });
            holder.tvProName.setText(item.getName()+"");
            holder.tvProSpec.setText("规格："+item.getProSpecifications());
            holder.tvProNeedNumber.setText(""+item.getProductAmount());
            holder.tvProActualNumber.setText(""+item.getGiveAmount());
            holder.tvProRemark.setText(""+item.getRemark());
            //订货数编辑
            holder.tvProNeedNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showNeedDialog(i);
                }
            });
            //配赠数编辑
            holder.tvProActualNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showActualDialog(i);
                }
            });
            //商品备注编辑
            holder.tvProRemark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showRemarkDialog(i);
                }
            });
            return view;
        }

        class NewHolder{
            ImageView img;
            TextView tvDele,tvProName,tvProSpec,tvProUnit,tvProNeedNumber,tvProActualNumber,tvProRemark;
        }
    }
    //订货数Dialog
    private void showNeedDialog(final int i) {
        final EditText et = new EditText(this);
        et.setBackgroundResource(R.drawable.bg_batch);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setHint("输入订货数");
        et.setMaxLines(1);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setSingleLine(true);
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("请输入数字？")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = (et.getText() + "").trim();
                        if (input==null || input.equals("")){
                            dialog.dismiss();
                        }else{
                            pros.get(i).setProductAmount(Integer.parseInt((et.getText()+"").trim()));
                            adapter.notifyDataSetChanged();
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
        setDialogWindowAttr(serialDialog);
    }
    //配赠数Dialog
    private void showActualDialog(final int i) {
        final EditText et = new EditText(this);
        et.setBackgroundResource(R.drawable.bg_batch);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setHint("输入配赠数");
        et.setMaxLines(1);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setSingleLine(true);
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("请输入数字？")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = (et.getText() + "").trim();
                        if (input==null || input.equals("")){
                            dialog.dismiss();
                        }else{
                            pros.get(i).setGiveAmount(Integer.parseInt((et.getText()+"").trim()));
                            adapter.notifyDataSetChanged();
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
        setDialogWindowAttr(serialDialog);
    }
    //产品备注Dialog
    private void showRemarkDialog(final int i) {
        final EditText et = new EditText(this);
        et.setBackgroundResource(R.drawable.bg_batch);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setHint("输入该商品备注");
        et.setMaxLines(1);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setSingleLine(true);
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("输入该商品备注")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = (et.getText() + "").trim();
                        if (input==null || input.equals("")){
                            dialog.dismiss();
                        }else{
                            pros.get(i).setRemark(input);
                            adapter.notifyDataSetChanged();
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
        setDialogWindowAttr(serialDialog);
    }
    //在dialog.show()之后调用
    public void setDialogWindowAttr(Dialog dlg){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = 4*(width/5);//宽高可设置具体大小
        lp.height = 2*(height/7);
        dlg.getWindow().setAttributes(lp);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
//        orderId=extras.getString("orderId");
        orderId=150;
        isShow=extras.getInt("isShow",0);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_scfhd_new;
    }
}
