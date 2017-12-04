package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.GetProductNeedList;
import com.bbld.warehouse.bean.OrderList;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 货需上报
 * Created by likey on 2017/11/7.
 */

public class HXSBActivity extends BaseActivity{
    @BindView(R.id.rvNeedMonth)
    RecyclerView rvNeedMonth;
    @BindView(R.id.tvYear)
    TextView tvYear;
    @BindView(R.id.lvHXSB)
    ListView lvHXSB;
    @BindView(R.id.tvEdit)
    TextView tvEdit;
    @BindView(R.id.tvBz)
    TextView tvBz;
    @BindView(R.id.btnSB)
    Button btnSB;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private MonthAdapter monthAdapter;
    private int monthPosition=0;
    private String[] items;
    private int mYear;
    private int mMonth;
    private Dialog loading;
    private String token;
    private List<GetProductNeedList.GetProductNeedListlist> orders;
    private HXSBAdapter adapter;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        mYear = c.get(Calendar.YEAR); // 获取当前年份+1
        mMonth = c.get(Calendar.MONTH);// 获取当前月份
        monthPosition=mMonth;
        loadYear();
        loadMonth();
        setListeners();
        loadData();
    }

    private void loadData() {
        loading= WeiboDialogUtils.createLoadingDialog(HXSBActivity.this,"加载中...");
        Call<GetProductNeedList> call= RetrofitService.getInstance().getProductNeedList(token,(mYear)+"-"+(monthPosition+1));
        call.enqueue(new Callback<GetProductNeedList>() {
            @Override
            public void onResponse(Response<GetProductNeedList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    orders = response.body().getList();
                    tvBz.setText(response.body().getRemark()+"");
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
        adapter=new HXSBAdapter();
        lvHXSB.setAdapter(adapter);
    }

    class HXSBAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public GetProductNeedList.GetProductNeedListlist getItem(int i) {
            return orders.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            HXSBHolder holder=null;
            if (view==null){
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_hxsb,null);
                holder=new HXSBHolder();
                holder.img=(ImageView)view.findViewById(R.id.img);
                holder.tvName=(TextView)view.findViewById(R.id.tvName);
                holder.tvMonth=(TextView)view.findViewById(R.id.tvMonth);
                holder.tvSpec=(TextView)view.findViewById(R.id.tvSpec);
                holder.tvCount=(TextView)view.findViewById(R.id.tvCount);
                holder.tvUnitCount=(TextView)view.findViewById(R.id.tvUnitCount);
                holder.tvCreatTime=(TextView)view.findViewById(R.id.tvCreatTime);
                holder.tvDelete=(TextView)view.findViewById(R.id.tvDelete);
                holder.tvEdit=(TextView)view.findViewById(R.id.tvEdit);
                view.setTag(holder);
            }
            holder= (HXSBHolder) view.getTag();
            final GetProductNeedList.GetProductNeedListlist item = getItem(i);
            Glide.with(getApplicationContext()).load(item.getLogo()).error(R.mipmap.xiuzhneg).into(holder.img);
            holder.tvName.setText(item.getName()+"");
            holder.tvMonth.setText(Html.fromHtml("月份："+"<font color=\"#FF9982\">"+item.getNeedMonth()+"</font>"));
            holder.tvSpec.setText("规格："+item.getProSpecifications());
            holder.tvCount.setText("盒数："+item.getNeedTotal());
            holder.tvUnitCount.setText("件数："+item.getNeedJTotal());
//            holder.tvCreatTime.setText("创建时间："+item.getAddDate());
            if (item.getAddDate().contains("T")){
                holder.tvCreatTime.setText("创建时间："+item.getAddDate().substring(0,item.getAddDate().indexOf("T"))+"");
            }else{
                holder.tvCreatTime.setText("创建时间："+item.getAddDate()+"");
            }
            return view;
        }

        class HXSBHolder {
            ImageView img;
            TextView tvName,tvMonth,tvSpec,tvCount,tvUnitCount,tvCreatTime,tvDelete,tvEdit;
        }
    }

    private void loadYear() {
        tvYear.setText(mYear+"");
        int showSize = (mYear+1) - 1990;
        items=new String[showSize];
        for (int i=0;i<items.length;i++){
            items[i]=(mYear+1-i)+"";
        }
    }

    private void setListeners() {
        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YearDialog();
            }
        });
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("mYear",mYear+"");
                bundle.putString("mMonth",(monthPosition + 1)+"");
                readyGo(HXSBSBActivity.class,bundle);
            }
        });
        btnSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(HXSBSBAddActivity.class);
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void YearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                showToast(items[which]);
                mYear=Integer.parseInt(items[which]);
                tvYear.setText(items[which]);
                loadData();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void loadMonth() {
        List<String> months=new ArrayList<String>();
        for (int i=1;i<13;i++){
            months.add(i+"月");
        }
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.scrollToPositionWithOffset(monthPosition, 0);
        mLayoutManager.setStackFromEnd(true);
        rvNeedMonth.setLayoutManager(mLayoutManager);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvNeedMonth.setItemAnimator(new DefaultItemAnimator());
        rvNeedMonth.setHasFixedSize(true);
        monthAdapter=new MonthAdapter(months);
        rvNeedMonth.setAdapter(monthAdapter);
    }

    private class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthHolder> {
        private List<String> months;
        public MonthAdapter(List<String> months) {
            super();
            this.months=months;
        }

        @Override
        public MonthAdapter.MonthHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hxsb_month,parent,false);
            MonthAdapter.MonthHolder recHolder = new MonthAdapter.MonthHolder(view);
            return recHolder;
        }

        @Override
        public void onBindViewHolder(MonthAdapter.MonthHolder holder, final int position) {
            holder.tvNeedMonth.setText(months.get(position));
            if (position==monthPosition){
                holder.tvNeedMonth.setBackgroundColor(Color.rgb(0,163,217));
            }else{
                holder.tvNeedMonth.setBackgroundColor(Color.rgb(174,174,174));
            }

            holder.tvNeedMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    monthPosition=position;
//                    showToast(position+1+"月"+"=="+(monthPosition+1)+"月");
                    monthAdapter.notifyDataSetChanged();
                    loadData();
                }
            });
        }

        @Override
        public int getItemCount() {
            return months.size();
        }
        class MonthHolder extends RecyclerView.ViewHolder {
            public TextView tvNeedMonth;
            public MonthHolder(View view){
                super(view);
                tvNeedMonth = (TextView) view.findViewById(R.id.tvNeedMonth);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_hxsb;
    }
}
