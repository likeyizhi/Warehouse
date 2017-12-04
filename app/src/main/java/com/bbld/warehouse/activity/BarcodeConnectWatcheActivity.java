package com.bbld.warehouse.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.BarcodeConnectWatchXM;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 查看关联
 * Created by likey on 2017/11/16.
 */

public class BarcodeConnectWatcheActivity extends BaseActivity{
    @BindView(R.id.lvWatch)
    ListView lvWatch;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.tvStart)
    TextView tvStart;
    @BindView(R.id.tvEnd)
    TextView tvEnd;
    @BindView(R.id.tvAllCount)
    TextView tvAllCount;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tvTimeClear)
    TextView tvTimeClear;

    private String token;
    private String code="";
    private String start="";
    private String end="";
    private int page=1;
    private int pagesize=10;
    private Dialog loading;
    private List<BarcodeConnectWatchXM.WatchXMlist> list;
    private CodeAdapter codeAdapter;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        calendar = Calendar.getInstance();
        loadData(false);
        setListeners();
    }

    private void setListeners() {
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code=(etCode.getText()+"").trim();
                page=1;
                if (!(tvStart.getText()+"").equals("开始时间")){
                    start=tvStart.getText()+"";
                }
                if (!(tvEnd.getText()+"").equals("结束时间")){
                    end=tvEnd.getText()+"";
                }
                loadData(false);
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDailog(tvStart);
            }
        });
        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDailog(tvEnd);
            }
        });
        lvWatch.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isBottom;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_FLING:
                        //Log.i("info", "SCROLL_STATE_FLING");
                        break;
                    case SCROLL_STATE_IDLE:
                        if (isBottom) {
                            page++;
                            loadData(true);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount){
                    //Log.i("info", "到底了....");
                    isBottom = true;
                }else{
                    isBottom = false;
                }
            }
        });
        tvTimeClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvStart.setText("开始时间");
                tvEnd.setText("结束时间");
                start="";
                end="";
            }
        });
    }

    private void loadData(final boolean isLoadMore) {
        loading = WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        Call<BarcodeConnectWatchXM> call= RetrofitService.getInstance().barcodeConnectWatchXM(token,code,start,end,page,pagesize);
        call.enqueue(new Callback<BarcodeConnectWatchXM>() {
            @Override
            public void onResponse(Response<BarcodeConnectWatchXM> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    if (isLoadMore){
                        List<BarcodeConnectWatchXM.WatchXMlist> listAdd = response.body().getList();
                        list.addAll(listAdd);
                        tvAllCount.setText(Html.fromHtml("总数："+"<font color=\"#C62F2F\">"+list.size()+"</font>"));
                        codeAdapter.notifyDataSetChanged();
                    }else{
                        list=response.body().getList();
                        tvAllCount.setText(Html.fromHtml("总数："+"<font color=\"#C62F2F\">"+list.size()+"</font>"));
                        setAdapter();
                    }
                    WeiboDialogUtils.closeDialog(loading);
                }else{
                    showToast(response.body().getMes()+"");
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
        codeAdapter=new CodeAdapter();
        lvWatch.setAdapter(codeAdapter);
    }

    class CodeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public BarcodeConnectWatchXM.WatchXMlist getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            CodeHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_barcode_connect_watch,null);
                holder=new CodeHolder();
                holder.tvNumber=(TextView)view.findViewById(R.id.tvNumber);
                holder.tvXM=(TextView)view.findViewById(R.id.tvXM);
                holder.tvTime=(TextView)view.findViewById(R.id.tvTime);
                view.setTag(holder);
            }
            holder= (CodeHolder) view.getTag();
            final BarcodeConnectWatchXM.WatchXMlist item = getItem(i);
            holder.tvNumber.setText(i+"");
            holder.tvXM.setText(item.getBoxCode()+"");
            if (item.getAddTime().contains("T")){
                holder.tvTime.setText(item.getAddTime().substring(0,item.getAddTime().indexOf("T"))+"");
            }else{
                holder.tvTime.setText(item.getAddTime()+"");
            }
            if (view!=null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle=new Bundle();
                        bundle.putInt("boxId",item.getNOIDTBConnBoxCode());
                        bundle.putString("boxCode",item.getBoxCode()+"");
                        readyGo(BarcodeConnectInfoActivity.class,bundle);
                    }
                });
            }
            return view;
        }

        class CodeHolder{
            TextView tvNumber,tvXM,tvTime;
        }
    }

    private void showDateDailog(final TextView tv) {
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //monthOfYear 得到的月份会减1所以我们要加1
                String time = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + Integer.toString(dayOfMonth);
                tv.setText(time);
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        //自动弹出键盘问题解决
        datePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_barcode_connect_watch;
    }
}
