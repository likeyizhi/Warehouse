package com.bbld.warehouse.activity;

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
import com.bbld.warehouse.bean.ProductCountDetails;
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
 * 库存详情
 * Created by likey on 2017/7/31.
 */

public class QueryDetailsActivity extends BaseActivity{
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.iv_productImg)
    ImageView ivProductImg;
    @BindView(R.id.tv_productName)
    TextView tvProductName;
    @BindView(R.id.tv_productSpec)
    TextView tvProductSpec;
    @BindView(R.id.tv_productCount)
    TextView tvProductCount;
    @BindView(R.id.lvQueryDetails)
    ListView lvQueryDetails;

    private String img;
    private String name;
    private String spec;
    private String count;
    private String proid;
    private String token;
    private List<ProductCountDetails.ProductCountDetailsList> details;
    private DetailsAdapter adapter;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        setListeners();
        initData();
        loadData();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        Glide.with(getApplicationContext()).load(img).into(ivProductImg);
        tvProductName.setText(name);
        tvProductSpec.setText(spec);
        tvProductCount.setText(count);
    }

    private void loadData() {
        Call<ProductCountDetails> call= RetrofitService.getInstance().getProductCountDetails(token,proid);
        call.enqueue(new Callback<ProductCountDetails>() {
            @Override
            public void onResponse(Response<ProductCountDetails> response, Retrofit retrofit) {
                if (response==null){
                    showToast("数据获取失败");
                    return;
                }
                if (response.body().getStatus()==0){
                    details = response.body().getList();
                    setAdapter();
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setAdapter() {
        adapter=new DetailsAdapter();
        lvQueryDetails.setAdapter(adapter);
    }

    class DetailsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return details.size();
        }

        @Override
        public ProductCountDetails.ProductCountDetailsList getItem(int i) {
            return details.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            DetailsHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_details,null);
                holder=new DetailsHolder();
                holder.tvPH=(TextView)view.findViewById(R.id.tvPH);
                holder.tvSL=(TextView)view.findViewById(R.id.tvSL);
                view.setTag(holder);
            }
            ProductCountDetails.ProductCountDetailsList detail = getItem(i);
            holder= (DetailsHolder) view.getTag();
            holder.tvPH.setText(detail.getBatchNumber()+"");
            holder.tvSL.setText(detail.getCount()+"");
            return view;
        }

        class DetailsHolder{
            TextView tvPH,tvSL;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        img=extras.getString("img");
        name=extras.getString("name");
        spec=extras.getString("spec");
        count=extras.getString("count");
        proid=extras.getString("proid");
//        showToast(proid);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_query_details;
    }
}
