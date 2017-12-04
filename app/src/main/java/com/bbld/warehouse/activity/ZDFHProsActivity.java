package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
 * 终端发货-添加-添加商品
 * Created by likey on 2017/11/29.
 */

public class ZDFHProsActivity extends BaseActivity{
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
        loading= WeiboDialogUtils.createLoadingDialog(this,"加载中...");
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

    class ProsAdapter extends BaseAdapter {

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
                    showCountDialog(item);
                }
            });
            return view;
        }

        class ProsHolder {
            ImageView img;
            TextView tvProName,tvProSpec;
        }
    }

    private void showCountDialog(final TbGetProductList.TbGetProductListlist item) {
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
                            Intent intent=new Intent(ZDFHProsActivity.this,ZDFHAddActivity.class);
                            intent.putExtra("ProductId",item.getId());
                            intent.putExtra("ProductName",item.getName()+"");
                            intent.putExtra("ProductLogo",item.getLogo()+"");
                            intent.putExtra("ProductSpec",item.getProSpecifications());
                            intent.putExtra("ProductCount",(et.getText()+"").trim());
                            setResult(1010,intent);
                            finish();
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
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_zdfh_pros;
    }
}
