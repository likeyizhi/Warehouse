package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.CloseOrder;
import com.bbld.warehouse.bean.TbCreateOrder;
import com.bbld.warehouse.bean.TbGetDealerDeliveryList;
import com.bbld.warehouse.bean.TbGetOrderInfo;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.utils.UploadUserInformationByPostService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 订单提报--编辑、添加
 * Created by likey on 2017/11/2.
 */

public class DDTBAddActivity extends BaseActivity{
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
    @BindView(R.id.btnAddPro)
    Button btnAddPro;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    @BindView(R.id.ib_back)
    ImageButton ibBack;


    private String token;
    private String orderId;
    private Dialog loading;
    private TbGetOrderInfo res;
    private List<TbGetOrderInfo.TbGetOrderInfoProductList> pros=new ArrayList<TbGetOrderInfo.TbGetOrderInfoProductList>();
    private DDTBAddAdapter adapter;
    private List<TbGetDealerDeliveryList.TbGetDealerDeliveryListlist> personList;
    private String[] items;
    private int deliveryId;
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
        loadPersonData();
        if (!orderId.equals("0")) {
            loadData();
        }else{
            tvClose.setVisibility(View.GONE);
        }
        setListeners();
    }

    private void loadPersonData() {
        final Dialog loadPerson = WeiboDialogUtils.createLoadingDialog(DDTBAddActivity.this, "");
        Call<TbGetDealerDeliveryList> call=RetrofitService.getInstance().tbGetDealerDeliveryList(token);
        call.enqueue(new Callback<TbGetDealerDeliveryList>() {
            @Override
            public void onResponse(Response<TbGetDealerDeliveryList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loadPerson);
                    return;
                }
                if (response.body().getStatus()==0){
                    personList=response.body().getList();
                    items = new String[personList.size()];
                    for (int i=0;i<personList.size();i++){
                        items[i] = personList.get(i).getName()+personList.get(i).getPhone()+"\n"+personList.get(i).getAddress();
                    }
                    WeiboDialogUtils.closeDialog(loadPerson);
                }else {
                    WeiboDialogUtils.closeDialog(loadPerson);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(loadPerson);
            }
        });
    }

    private void setListeners() {
        btnAddPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoForResult(DDTBProsActivity.class,25);
            }
        });
        tvPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonDialog();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((tvPerson.getText()+"").equals("") || (tvPerson.getText()+"").equals("选择")){
                    showToast("请选择收货人");
                }else if ((tvPhone.getText()+"").equals("")){
                    showToast("请选择收货人");
                }else if ((tvAddr.getText()+"").equals("")){
                    showToast("请选择收货人");
                }else{
                    upLoading=WeiboDialogUtils.createLoadingDialog(DDTBAddActivity.this,"上传中...");
                    TbCreateOrder addData = new TbCreateOrder();
                    addData.setOrderId(Integer.parseInt(orderId));
                    addData.setDeliveryId(deliveryId);
                    addData.setDealerRemark(etRemark.getText()+"");
                    ArrayList<TbCreateOrder.TbCreateOrderproductList> createList = new ArrayList<TbCreateOrder.TbCreateOrderproductList>();
                    for (int i=0;i<pros.size();i++){
                        TbCreateOrder.TbCreateOrderproductList create=new TbCreateOrder.TbCreateOrderproductList();
                        create.setProductId(pros.get(i).getProductId());
                        create.setProductAmount(pros.get(i).getProductAmount());
                        create.setProductRemark(pros.get(i).getRemark());
                        createList.add(create);
                    }
                    addData.setProductList(createList);
                    Gson gson=new Gson();
                    final String orderJson=gson.toJson(addData);
//                    showToast(orderJson);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                request= UploadUserInformationByPostService.tbCreateOrder(token,
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
            }
        });
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCloseDialog();
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==25){
            int proid = data.getIntExtra("_id",0);
            String cpbh = data.getStringExtra("_cpbh");
            String cpmc = data.getStringExtra("_cpmc");
            String cpgg = data.getStringExtra("_cpgg");
            String cpsl = data.getStringExtra("_cpsl");
            String chjs = data.getStringExtra("_chjs");
            String bz = data.getStringExtra("_bz");
            String logo = data.getStringExtra("_logo");

            TbGetOrderInfo.TbGetOrderInfoProductList addPro = new TbGetOrderInfo.TbGetOrderInfoProductList(0, proid, cpmc, cpgg, Integer.parseInt(cpsl), 0, 0, 0, bz, logo);
            pros.add(addPro);
            if (orderId.equals("0")){
                setAdapter();
            }else{
                adapter.notifyDataSetChanged();
            }
        }

    }

    private void loadData() {
        loading= WeiboDialogUtils.createLoadingDialog(DDTBAddActivity.this,"加载中...");
        Call<TbGetOrderInfo> call= RetrofitService.getInstance().tbGetOrderInfo(token,orderId);
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
                    deliveryId=Integer.parseInt(res.getDeliveryId());
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

    private void setAdapter() {
        adapter=new DDTBAddAdapter();
        lvProduct.setAdapter(adapter);
    }

    private void setData() {
        tvNumber.setText(res.getOrderCode()+"");
        tvPerson.setText(res.getName()+"");
        tvPhone.setText(res.getPhone()+"");
        tvAddr.setText(res.getAreas()+res.getAddress());
        etRemark.setText(res.getDealerRemark()+"");
        if (res.getOrderStatus()==1){
            llBottom.setVisibility(View.VISIBLE);
            tvClose.setVisibility(View.VISIBLE);
            tvPerson.setClickable(true);
        }else{
            llBottom.setVisibility(View.GONE);
            tvClose.setVisibility(View.GONE);
            tvPerson.setClickable(false);
        }
    }

    class DDTBAddAdapter extends BaseAdapter{

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
            DDTBAddHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_ddtb_add_product,null);
                holder=new DDTBAddHolder();
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
            holder= (DDTBAddHolder) view.getTag();
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
//            holder.tvProUnit.setText("单位：");
            holder.tvProNeedNumber.setText("订货数："+item.getProductAmount());
            holder.tvProActualNumber.setText("配赠数："+item.getGiveAmount());
            holder.tvProRemark.setText("备注："+item.getRemark());
            return view;
        }

        class DDTBAddHolder{
            ImageView img;
            TextView tvDele,tvProName,tvProSpec,tvProUnit,tvProNeedNumber,tvProActualNumber,tvProRemark;
        }
    }

    /**
     * 收货人
     */
    private void PersonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvPerson.setText(personList.get(which).getName());
                tvPhone.setText(personList.get(which).getPhone());
                tvAddr.setText(personList.get(which).getAreas()+personList.get(which).getAddress());
                deliveryId=personList.get(which).getId();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showCloseDialog() {
        final EditText et = new EditText(this);
        et.setBackgroundResource(R.drawable.bg_batch);
        et.setHint("输入备注");
        et.setMaxLines(1);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setSingleLine(true);
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("关闭此单？")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        closeOrder(et.getText()+"",orderId);
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

    private void closeOrder(String s, String orderId) {
        if (s.equals("")||s==null){
            showToast("请输入备注信息");
        }else{
            Call<CloseOrder> call=RetrofitService.getInstance().closeOrder(token, orderId, s);
            call.enqueue(new Callback<CloseOrder>() {
                @Override
                public void onResponse(Response<CloseOrder> response, Retrofit retrofit) {
                    if (response==null){
                        return;
                    }
                    if (response.body().getStatus()==0){
                        finish();
                    }else{
                        showToast("操作失败，请重试");
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    showToast("操作失败，请重试");
                }
            });
        }
    }


    @Override
    protected void getBundleExtras(Bundle extras) {
        orderId=extras.getString("orderId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_ddtb_add;
    }
}
