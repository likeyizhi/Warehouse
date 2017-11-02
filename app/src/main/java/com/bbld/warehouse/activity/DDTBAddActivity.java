package com.bbld.warehouse.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.TbGetOrderInfo;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bumptech.glide.Glide;

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


    private String token;
    private String orderId;
    private Dialog loading;
    private TbGetOrderInfo res;
    private List<TbGetOrderInfo.TbGetOrderInfoProductList> pros;
    private DDTBAddAdapter adapter;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        setListeners();
    }

    private void setListeners() {
        btnAddPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoForResult(DDTBProsActivity.class,25);
            }
        });
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
        pros=res.getProductList();
        adapter=new DDTBAddAdapter();
        lvProduct.setAdapter(adapter);
    }

    private void setData() {
        tvNumber.setText(res.getOrderCode()+"");
        tvPerson.setText(res.getName()+"");
        tvPhone.setText(res.getPhone()+"");
        tvAddr.setText(res.getAreas()+res.getAddress());
        etRemark.setText(res.getDealerRemark()+"");
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
        public View getView(int i, View view, ViewGroup viewGroup) {
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
//            Glide.with(getApplicationContext()).load(item.get).error(R.mipmap.xiuzhneg).into(holder.img);
            holder.tvDele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast("删除"+item.getId());
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

    @Override
    protected void getBundleExtras(Bundle extras) {
        orderId=extras.getString("orderId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_ddtb_add;
    }
}
