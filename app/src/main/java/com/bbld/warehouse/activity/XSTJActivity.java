package com.bbld.warehouse.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.SaleStatistics;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 销售统计
 * Created by likey on 2017/8/21.
 */

public class XSTJActivity extends BaseActivity{
    @BindView(R.id.lv_xstj)
    ListView lvXSTJ;
    @BindView(R.id.tvStart)
    TextView tvStart;
    @BindView(R.id.tvEnd)
    TextView tvEnd;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private String token;
    private String startTime="";
    private String endTime="";
    private List<SaleStatistics.SaleStatisticsInfo> infos;
    private XSTJAdapter adapter;
    final int DATE_DIALOG = 1;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int timeType=1;
    private Dialog loading;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        setListeners();
    }

    private void setListeners() {
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeType=1;
                showDialog(DATE_DIALOG);
            }
        });
        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeType=2;
                showDialog(DATE_DIALOG);
            }
        });
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime=tvStart.getText()+"";
                endTime=tvEnd.getText()+"";
                loadData();
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(XSTJActivity.this);
            }
        });
    }

    private void loadData() {
        loading=WeiboDialogUtils.createLoadingDialog(XSTJActivity.this,"加载中...");
        Call<SaleStatistics> call= RetrofitService.getInstance().saleStatistics(token,startTime,endTime);
        call.enqueue(new Callback<SaleStatistics>() {
            @Override
            public void onResponse(Response<SaleStatistics> response, Retrofit retrofit) {
                if (response==null){
                    showToast("数据获取失败");
                    return;
                }
                if (response.body().getStatus()==0){
                    infos=response.body().getInfo();
                    setAdapter();
                }else{
                    showToast(response.body().getMes());
                }
                WeiboDialogUtils.closeDialog(loading);
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(loading);
            }
        });
    }

    private void setAdapter() {
        adapter=new XSTJAdapter();
        lvXSTJ.setAdapter(adapter);
    }

    class XSTJAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public SaleStatistics.SaleStatisticsInfo getItem(int i) {
            return infos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            XSTJHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_xstj,null);
                holder=new XSTJHolder();
                holder.ivImg=(ImageView)view.findViewById(R.id.ivImg);
                holder.tvName=(TextView)view.findViewById(R.id.tvName);
                holder.tvCount=(TextView)view.findViewById(R.id.tvCount);
                view.setTag(holder);
            }
            SaleStatistics.SaleStatisticsInfo item = getItem(i);
            holder= (XSTJHolder) view.getTag();
            Glide.with(getApplicationContext()).load(item.getLogo()).error(R.mipmap.xiuzhneg).into(holder.ivImg);
            holder.tvName.setText(item.getName()+"");
            holder.tvCount.setText("数量："+item.getSaleCount()+"");
            return view;
        }

        class XSTJHolder {
            ImageView ivImg;
            TextView tvName,tvCount;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }
    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        if (timeType==1){
            tvStart.setText(new StringBuffer().append(mYear).append("-").append(mMonth+1).append("-").append(mDay).append(" "));
        }else{
            tvEnd.setText(new StringBuffer().append(mYear).append("-").append(mMonth+1).append("-").append(mDay).append(" "));
        }
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };


    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_xstj;
    }
}
