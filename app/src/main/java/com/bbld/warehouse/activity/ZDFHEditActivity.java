package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.bbld.warehouse.bean.CusInvoiceAdd;
import com.bbld.warehouse.bean.CusInvoiceGetInfo;
import com.bbld.warehouse.bean.ZdfhOtherThree;
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
 * 终端发货-编辑/详情
 * Created by likey on 2017/11/30.
 */

public class ZDFHEditActivity extends BaseActivity{
    @BindView(R.id.tvDealerWarehouse)
    TextView tvDealerWarehouse;
    @BindView(R.id.tvCusInvoice)
    TextView tvCusInvoice;
    @BindView(R.id.tvCusContacts)
    TextView tvCusContacts;
    @BindView(R.id.tvContactPhone)
    TextView tvContactPhone;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.btnSure)
    Button btnSure;
    @BindView(R.id.lvZdfhAdd)
    ListView lvZdfhAdd;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private int id;
    private int isEdit;
    private String token;
    private Dialog loading;
    private List<CusInvoiceGetInfo.CusInvoiceGetInfoDealerWarehouseList> dwList;
    private String[] dwItems;
    private List<CusInvoiceGetInfo.CusInvoiceGetInfoCusInvoiceList> ciList;
    private String[] ciItems;
    private List<CusInvoiceGetInfo.CusInvoiceGetInfoCusInvoiceProductList> cipList;
    private CusInvoiceAdd cusInvoiceJsonBean;
    private ArrayList<CusInvoiceAdd.CusInvoiceAddlist> cusInvoiceAddlist;
    private int warehouseId;
    private int cusDealerId;
    private String remark;
    private ArrayList<ZdfhOtherThree> others;
    private ZdfhEditAdapter adapter;
    private Dialog upLoading;
    private String cusInvoiceJson;
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
        cusInvoiceJsonBean=new CusInvoiceAdd();
        cusInvoiceAddlist=new ArrayList<CusInvoiceAdd.CusInvoiceAddlist>();
        others=new ArrayList<ZdfhOtherThree>();
        if (isEdit==0){
            btnAdd.setVisibility(View.GONE);
            btnSure.setVisibility(View.GONE);
            tvDealerWarehouse.setClickable(false);
            tvCusInvoice.setClickable(false);
        }else{
            btnAdd.setVisibility(View.VISIBLE);
            btnSure.setVisibility(View.VISIBLE);
        }
        loadData();
        setListeners();
    }

    private void setListeners() {
        tvDealerWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DWDialog();
            }
        });
        tvCusInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CIDialog();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoForResult(ZDFHProsActivity.class,1010);
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (warehouseId==0 || cusDealerId==0){
                    showToast("请完整信息");
                }else{
                    createOrder();
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

    private void createOrder() {
        upLoading=WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        cusInvoiceJsonBean.setId(id);
        cusInvoiceJsonBean.setWarehouseId(warehouseId);
        cusInvoiceJsonBean.setCusDealerId(cusDealerId);
        cusInvoiceJsonBean.setRemark(etRemark.getText()+"");
        cusInvoiceJsonBean.setList(cusInvoiceAddlist);
        Gson gson=new Gson();
        cusInvoiceJson=gson.toJson(cusInvoiceJsonBean);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    request= UploadUserInformationByPostService.cusInvoiceAdd(token,
                            cusInvoiceJson);
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

    private void DWDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setItems(dwItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                warehouseId=dwList.get(which).getId();
                tvDealerWarehouse.setText(dwList.get(which).getName());
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void CIDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setItems(ciItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cusDealerId=ciList.get(which).getId();
                tvCusInvoice.setText(ciList.get(which).getName());
                tvCusContacts.setText(ciList.get(which).getContacts()+"");
                tvContactPhone.setText(ciList.get(which).getContactPhone()+"");
                tvAddress.setText(ciList.get(which).getAddress()+"");
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void loadData() {
        loading= WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        Call<CusInvoiceGetInfo> call= RetrofitService.getInstance().cusInvoiceGetInfo(token,id);
        call.enqueue(new Callback<CusInvoiceGetInfo>() {
            @Override
            public void onResponse(Response<CusInvoiceGetInfo> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    dwList=response.body().getDealerWarehouseList();
                    dwItems=new String[dwList.size()];
                    for (int i=0;i<dwList.size();i++){
                        dwItems[i]=dwList.get(i).getName();
                    }
                    ciList=response.body().getCusInvoiceList();
                    ciItems=new String[ciList.size()];
                    for (int i=0;i<ciList.size();i++){
                        ciItems[i]=ciList.get(i).getName();
                    }
                    cipList=response.body().getCusInvoiceProductList();
                    warehouseId=response.body().getWarehouseId();
                    cusDealerId=response.body().getCusDealerId();
                    remark=response.body().getRemark()+"";
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
        for (int i=0;i<dwList.size();i++){
            if (warehouseId==dwList.get(i).getId()){
                tvDealerWarehouse.setText(dwList.get(i).getName());
            }
        }
        for (int i=0;i<ciList.size();i++){
            if (cusDealerId==ciList.get(i).getId()){
                tvCusInvoice.setText(ciList.get(i).getName());
                tvCusContacts.setText(ciList.get(i).getContacts());
                tvContactPhone.setText(ciList.get(i).getContactPhone());
                tvAddress.setText(ciList.get(i).getAddress());
            }
        }
        etRemark.setText(remark);
    }

    private void setAdapter() {
        for (int i=0;i<cipList.size();i++){
            others.add(new ZdfhOtherThree(cipList.get(i).getName(),cipList.get(i).getLogo(),cipList.get(i).getProSpecifications()));
            cusInvoiceAddlist.add(new CusInvoiceAdd.CusInvoiceAddlist(cipList.get(i).getProductId(),cipList.get(i).getProductTotal()));
        }
        adapter=new ZdfhEditAdapter();
        lvZdfhAdd.setAdapter(adapter);
    }

    class ZdfhEditAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return others.size();
        }

        @Override
        public ZdfhOtherThree getItem(int i) {
            return others.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ZdfhEditHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_zdfh_add,null);
                holder=new ZdfhEditHolder();
                holder.img=(ImageView)view.findViewById(R.id.img);
                holder.tvProName=(TextView)view.findViewById(R.id.tvProName);
                holder.tvProSpec=(TextView)view.findViewById(R.id.tvProSpec);
                holder.tvProCount=(TextView)view.findViewById(R.id.tvProCount);
                holder.tvDelete=(TextView)view.findViewById(R.id.tvDelete);
                view.setTag(holder);
            }
            holder= (ZdfhEditHolder) view.getTag();
            ZdfhOtherThree item = getItem(i);
            Glide.with(getApplicationContext()).load(item.getProductLogo()).error(R.mipmap.xiuzhneg).into(holder.img);
            holder.tvProName.setText(item.getProductName()+"");
            holder.tvProSpec.setText("规格："+item.getProductSpec());
            if (isEdit==0){
                holder.tvProCount.setText("发货数："+cusInvoiceAddlist.get(i).getProductTotal());
                holder.tvDelete.setVisibility(View.GONE);
            }else{
                holder.tvProCount.setText("发货数："+cusInvoiceAddlist.get(i).getProductTotal());
                holder.tvProCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showChangeCountDialog(i);
                    }
                });
                holder.tvDelete.setVisibility(View.VISIBLE);
                holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        others.remove(i);
                        cusInvoiceAddlist.remove(i);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
            return view;
        }

        class ZdfhEditHolder{
            ImageView img;
            TextView tvProName,tvProSpec,tvProCount,tvDelete;
        }
    }

    private void showChangeCountDialog(final int i) {
        final EditText et = new EditText(this);
        et.setBackgroundResource(R.drawable.bg_batch);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setHint("输入发货商品数量");
        et.setMaxLines(1);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setSingleLine(true);
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("发货商品数量")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if ((et.getText()+"").trim().equals("") || (et.getText()+"")==null){
                            showToast("请输入发货商品数量");
                        }else{
                            cusInvoiceAddlist.get(i).setProductTotal(Integer.parseInt((et.getText()+"").trim()));
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1010){
            int ProductId = data.getIntExtra("ProductId", 0);
            String ProductName = data.getStringExtra("ProductName");
            String ProductLogo = data.getStringExtra("ProductLogo");
            String ProductSpec = data.getStringExtra("ProductSpec");
            String ProductCount = data.getStringExtra("ProductCount");
            boolean refresh = true;
            for (int i=0;i<cusInvoiceAddlist.size();i++){
                if (ProductId==cusInvoiceAddlist.get(i).getProductId()){
                    refresh=false;
                }
            }
            if (refresh){
                cusInvoiceAddlist.add(new CusInvoiceAdd.CusInvoiceAddlist(ProductId,Integer.parseInt(ProductCount)));
                others.add(new ZdfhOtherThree(ProductName,ProductLogo,ProductSpec));
                adapter.notifyDataSetChanged();
            }else{
                showToast("该商品已经存在");
            }
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        id=extras.getInt("id",0);
        isEdit=extras.getInt("isEdit",0);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_zdfh_edit;
    }
}
