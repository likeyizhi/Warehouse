package com.bbld.warehouse.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.TbGetProductList;
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
 * 订单提报--商品列表
 * Created by likey on 2017/11/2.
 */

public class DDTBProsActivity extends BaseActivity{
    @BindView(R.id.lvPros)
    ListView lvPros;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private String token;
    private Dialog loading;
    private List<TbGetProductList.TbGetProductListlist> pros;
    private ProsAdapter adapter;

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
    }

    private void loadData() {
        loading= WeiboDialogUtils.createLoadingDialog(DDTBProsActivity.this,"加载中...");
        Call<TbGetProductList> call= RetrofitService.getInstance().tbGetProductList(token);
        call.enqueue(new Callback<TbGetProductList>() {
            @Override
            public void onResponse(Response<TbGetProductList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    pros=response.body().getList();
                    setAdapter();
                    WeiboDialogUtils.closeDialog(loading);
                }else{
                    showToast("数据获取失败，请重试");
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
        adapter=new ProsAdapter();
        lvPros.setAdapter(adapter);
    }

    class ProsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return pros.size();
        }

        @Override
        public TbGetProductList.TbGetProductListlist getItem(int i) {
            return pros.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ProsHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_ddtb_product,null);
                holder=new ProsHolder();
                holder.img=(ImageView)view.findViewById(R.id.img);
                holder.tvProName=(TextView)view.findViewById(R.id.tvProName);
                holder.tvProSpec=(TextView)view.findViewById(R.id.tvProSpec);
                view.setTag(holder);
            }
            holder= (ProsHolder) view.getTag();
            final TbGetProductList.TbGetProductListlist item = getItem(i);
            Glide.with(getApplicationContext()).load(item.getLogo()+"").error(R.mipmap.xiuzhneg).into(holder.img);
            holder.tvProName.setText(item.getName()+"");
            holder.tvProSpec.setText("规格："+item.getProSpecifications()+"");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("proId", item.getId()+"");
                    readyGoForResult(DDTBProInfoActivity.class,26,bundle);
                }
            });
            return view;
        }

        class ProsHolder {
            ImageView img;
            TextView tvProName,tvProSpec;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==26){
            int proid = data.getIntExtra("_id",0);
            String cpbh = data.getStringExtra("_cpbh");
            String cpmc = data.getStringExtra("_cpmc");
            String cpgg = data.getStringExtra("_cpgg");
            String cpsl = data.getStringExtra("_cpsl");
            String chjs = data.getStringExtra("_chjs");
            String bz = data.getStringExtra("_bz");
            Intent intent=new Intent(DDTBProsActivity.this,DDTBAddActivity.class);
            intent.putExtra("_id",proid);
            intent.putExtra("_cpbh",cpbh);
            intent.putExtra("_cpmc",cpmc);
            intent.putExtra("_cpgg",cpgg);
            intent.putExtra("_cpsl",cpsl);
            intent.putExtra("_chjs",chjs);
            intent.putExtra("_bz",bz);
            for (int i=0;i<pros.size();i++){
                if (proid==pros.get(i).getId()){
                    intent.putExtra("_logo",pros.get(i).getLogo());
                }
            }
            setResult(25,intent);
            finish();
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_ddtb_pros;
    }
}
