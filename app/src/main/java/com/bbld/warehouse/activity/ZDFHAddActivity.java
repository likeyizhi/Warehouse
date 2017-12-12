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
import com.bbld.warehouse.bean.CusInvoiceInitAddGetInfo;
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
 * 终端发货-添加
 * Created by likey on 2017/11/29.
 */

public class ZDFHAddActivity extends BaseActivity{
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

    private Dialog loading;
    private String token;
    private List<CusInvoiceInitAddGetInfo.CusInvoiceInitAddGetInfoDealerWarehouseList> dwList;
    private List<CusInvoiceInitAddGetInfo.CusInvoiceInitAddGetInfoCusInvoiceList> ciList;
    private String[] dwItems;
    private String[] ciItems;
    private CusInvoiceAdd cusInvoiceJsonBean;
    private int warehouseId=0;
    private int cusDealerId=0;
    private ArrayList<CusInvoiceAdd.CusInvoiceAddlist> cusInvoiceAddlist;
    private ZdfhAddAdapter adapter;
    private ArrayList<ZdfhOtherThree> others;
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
        loadData();
        setAdapter();
        setListeners();
    }

    private void setAdapter() {
        adapter=new ZdfhAddAdapter();
        lvZdfhAdd.setAdapter(adapter);
    }

    class ZdfhAddAdapter extends BaseAdapter{

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
            ZdfhAddHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_zdfh_add,null);
                holder=new ZdfhAddHolder();
                holder.img=(ImageView)view.findViewById(R.id.img);
                holder.tvProName=(TextView)view.findViewById(R.id.tvProName);
                holder.tvProSpec=(TextView)view.findViewById(R.id.tvProSpec);
                holder.tvProCount=(TextView)view.findViewById(R.id.tvProCount);
                holder.tvDelete=(TextView)view.findViewById(R.id.tvDelete);
                view.setTag(holder);
            }
            holder= (ZdfhAddHolder) view.getTag();
            ZdfhOtherThree item = getItem(i);
            Glide.with(getApplicationContext()).load(item.getProductLogo()).error(R.mipmap.xiuzhneg).into(holder.img);
            holder.tvProName.setText(item.getProductName()+"");
            holder.tvProSpec.setText("规格："+item.getProductSpec());
            holder.tvProCount.setText("发货数："+cusInvoiceAddlist.get(i).getProductTotal());
            holder.tvProCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showChangeCountDialog(i);
                }
            });
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    others.remove(i);
                    cusInvoiceAddlist.remove(i);
                    adapter.notifyDataSetChanged();
                }
            });
            return view;
        }

        class ZdfhAddHolder{
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
        cusInvoiceJsonBean.setId(0);
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

    private void loadData() {
        loading= WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        Call<CusInvoiceInitAddGetInfo> call= RetrofitService.getInstance().cusInvoiceInitAddGetInfo(token);
        call.enqueue(new Callback<CusInvoiceInitAddGetInfo>() {
            @Override
            public void onResponse(Response<CusInvoiceInitAddGetInfo> response, Retrofit retrofit) {
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
                    WeiboDialogUtils.closeDialog(loading);
                }else{
                    showToast(response.body().getMes());
                    WeiboDialogUtils.closeDialog(loading);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(loading);
            }
        });
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

    }

    @Override
    public int getContentView() {
        return R.layout.activity_zdfh_add;
    }
}
