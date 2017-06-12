package com.bbld.warehouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.ProductList;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 添加选择商品
 * Created by likey on 2017/6/5.
 */

public class SelectGoodsActivity extends BaseActivity{
    @BindView(R.id.lv_productList)
    ListView lvProductList;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private List<ProductList.ProductListList> productList;

    @Override
    protected void initViewsAndEvents() {
        loadData();
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("init","no");
                setResult(1020, intent);
                finish();
            }
        });
    }

    private void loadData() {
        Call<ProductList> call= RetrofitService.getInstance().productList(new MyToken(SelectGoodsActivity.this).getToken());
        call.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Response<ProductList> response, Retrofit retrofit) {
                if (response==null){
                    showToast("服务器错误");
                    return;
                }
                if (response.body().getStatus()==0){
                    productList = response.body().getList();
                    setAdapter();
                }else{
                    showToast(response.body().getMes()+"");
                }
            }
            @Override
            public void onFailure(Throwable throwable) {
                showToast(throwable+"");
            }
        });
    }

    private void setAdapter() {
        SelectGoodsAdapter adapter=new SelectGoodsAdapter();
        lvProductList.setAdapter(adapter);
    }

    class SelectGoodsAdapter extends BaseAdapter{
        private static final int TYPE_COUNT = 2;//item类型的总数
        private static final int TYPE_POSITION = 0;//第n~n+9个
        private static final int TYPE_PRODUCT = 1;//商品

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public ProductList.ProductListList getItem(int i) {
            return productList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(productList.get(i).getProductID());
        }

        @Override
        public int getItemViewType(int position) {
            if ("0".equals((position+"").substring((position+"").length()-1,(position+"").length()))){
                return TYPE_POSITION;
            }else{
                return TYPE_PRODUCT;
            }
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_COUNT;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            SelectGoodsHolder holder=null;
            SelectGoodsHolder02 holder02=null;
            int type=getItemViewType(i);

            if (view==null){
                switch (type){
                    case TYPE_POSITION:
                        view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_select_goods02,null);
                        holder02=new SelectGoodsHolder02();
                        holder02.tv_position=(TextView)view.findViewById(R.id.tv_position);
                        holder02.iv_productImg=(ImageView)view.findViewById(R.id.iv_productImg);
                        holder02.tv_productName=(TextView)view.findViewById(R.id.tv_productName);
                        holder02.tv_productSpec=(TextView)view.findViewById(R.id.tv_productSpec);
                        view.setTag(holder02);
                        break;
                    case TYPE_PRODUCT:
                        view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_select_goods,null);
                        holder=new SelectGoodsHolder();
                        holder.iv_productImg=(ImageView)view.findViewById(R.id.iv_productImg);
                        holder.tv_productName=(TextView)view.findViewById(R.id.tv_productName);
                        holder.tv_productSpec=(TextView)view.findViewById(R.id.tv_productSpec);
                        view.setTag(holder);
                        break;
                    default:
                        break;
                }
            }else{
                switch (type){
                    case TYPE_POSITION:
                        holder02= (SelectGoodsHolder02) view.getTag();
                        break;
                    case TYPE_PRODUCT:
                        holder= (SelectGoodsHolder) view.getTag();
                        break;
                }
            }
            final ProductList.ProductListList product = getItem(i);
            switch (type){
                case TYPE_POSITION:
                    Glide.with(getApplicationContext()).load(product.getProductImg()).error(R.mipmap.xiuzhneg).into(holder02.iv_productImg);
                    holder02.tv_position.setText("第"+(i+1)+"-"+(i+10)+"个");
                    holder02.tv_productName.setText(product.getProductName()+"");
                    holder02.tv_productSpec.setText(product.getProductSpec()+"");
                    if (view!=null){
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent();
                                intent.putExtra("productid",product.getProductID()+"");
                                intent.putExtra("productimg",product.getProductImg()+"");
                                intent.putExtra("productName",product.getProductName()+"");
                                intent.putExtra("productSpec",product.getProductSpec()+"");
                                intent.putExtra("init","yes");
                                setResult(1020, intent);
                                finish();
                            }
                        });
                    }
                    break;
                case TYPE_PRODUCT:
                    Glide.with(getApplicationContext()).load(product.getProductImg()).error(R.mipmap.xiuzhneg).into(holder.iv_productImg);
                    holder.tv_productName.setText(product.getProductName()+"");
                    holder.tv_productSpec.setText(product.getProductSpec()+"");
                    if (view!=null){
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent();
                                intent.putExtra("productid",product.getProductID()+"");
                                intent.putExtra("productimg",product.getProductImg()+"");
                                intent.putExtra("productName",product.getProductName()+"");
                                intent.putExtra("productSpec",product.getProductSpec()+"");
                                intent.putExtra("init","yes");
                                setResult(1020, intent);
                                finish();
                            }
                        });
                    }
                    break;
            }
            return view;
        }
        class SelectGoodsHolder{
            ImageView iv_productImg;
            TextView tv_productName;
            TextView tv_productSpec;
        }
        class SelectGoodsHolder02{
            TextView tv_position;
            ImageView iv_productImg;
            TextView tv_productName;
            TextView tv_productSpec;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            Intent intent=new Intent();
            intent.putExtra("init","no");
            setResult(1020, intent);
            finish();
        }
        return false;
    }
    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_select_goods;
    }
}
