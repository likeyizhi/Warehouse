package com.bbld.warehouse.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;

import com.bbld.warehouse.bean.GetLogisticsList;
import com.bbld.warehouse.bean.GetLogisticsTrackInfo;
import com.bbld.warehouse.bean.GetOrderLogisticsInfo;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.R.id.list;

/**
 * Created by dell on 2017/5/24.
 */

public class LogisticsTrackingActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_yincang)
    TextView tvYincang;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.sp_logistics)
    Spinner spLogistics;
    @BindView(R.id.lv_logistics_trackinfo)
    ListView lvLogistics;
    private List<GetLogisticsTrackInfo.GetLogisticsTrackInfoList> logisticsTrackInfoList;
    private List<GetOrderLogisticsInfo.GetOrderLogisticsInfoList> logisticsInfoList;
    private GetOrderLogisticsInfoAdapter getOrderLogisticsInfoAdapter;
    private GetLogisticsTrackInfoListAdapter getLogisticsTrackInfoAdapter;
    int logisticsId;
    String number;
    int invoiceid;

    @Override
    protected void initViewsAndEvents() {
        loadData();

        spLogistics.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvName.setText(logisticsInfoList.get(i).getLogisticsName() + "");
                tvNumber.setText(logisticsInfoList.get(i).getLogisticsNumber() + "");
                tvYincang.setText(logisticsInfoList.get(i).getLogisticsID()+"");
        number=tvNumber.getText().toString();
        logisticsId=Integer.parseInt(tvYincang.getText().toString());
                loadData1();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        invoiceid = Integer.parseInt(extras.getString("OrderID()"));

    }
    private void loadData1(){
    /*
  物流跟踪查询接口
     */

//        number=tvNumber.getText().toString();
//        logisticsId=Integer.parseInt(tvYincang.getText().toString());

        Call<GetLogisticsTrackInfo> call = RetrofitService.getInstance().getLogisticsTrackInfo(new MyToken(LogisticsTrackingActivity.this).getToken() + "", logisticsId, number);
        showToast( "物流id"+logisticsId+"物流编号"+number);
        call.enqueue(new Callback<GetLogisticsTrackInfo>() {
            @Override
            public void onResponse(Response<GetLogisticsTrackInfo> response, Retrofit retrofit) {
                if (response.body() == null) {
                    showToast("服务器错误");
                    return;
                }
                if (response.body().getStatus() == 0) {
                    logisticsTrackInfoList=response.body().getList();
                    setAdapter1();

                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
    private void loadData(){
            /*
  物流信息查询接口
 */
        Call<GetOrderLogisticsInfo> call1 = RetrofitService.getInstance().getOrderLogisticsInfo(new MyToken(LogisticsTrackingActivity.this).getToken() + "", invoiceid);
        call1.enqueue(new Callback<GetOrderLogisticsInfo>() {
            @Override
            public void onResponse(Response<GetOrderLogisticsInfo> response, Retrofit retrofit) {
                if (response.body() == null) {
                    showToast("服务器出错");
                    return;
                }
                if (response.body().getStatus() == 0) {
                    logisticsInfoList = response.body().getList();
                    setAdapter2();

                }

            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }


    private void setAdapter2() {
        getOrderLogisticsInfoAdapter = new GetOrderLogisticsInfoAdapter();
        spLogistics.setAdapter(getOrderLogisticsInfoAdapter);

    }
    private void setAdapter1() {

        getLogisticsTrackInfoAdapter=new GetLogisticsTrackInfoListAdapter();
        lvLogistics.setAdapter(getLogisticsTrackInfoAdapter);
    }

    class GetOrderLogisticsInfoAdapter extends BaseAdapter {
        GetOrderLogisticsInfoHolder holder = null;

        @Override
        public int getCount() {
            return logisticsInfoList.size();
        }

        @Override
        public GetOrderLogisticsInfo.GetOrderLogisticsInfoList getItem(int i) {
            return logisticsInfoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_sp_logistics_tracking, null);
                holder = new GetOrderLogisticsInfoHolder();
                holder.tvInfo = (TextView) view.findViewById(R.id.tv_info);
                view.setTag(holder);
            }
            holder = (GetOrderLogisticsInfoHolder) view.getTag();
            final GetOrderLogisticsInfo.GetOrderLogisticsInfoList list = getItem(i);
            holder.tvInfo.setText(list.getLogisticsName() + list.getLogisticsNumber() + "");
            return view;
        }
        class GetOrderLogisticsInfoHolder {
            TextView tvInfo;
        }
    }

    class GetLogisticsTrackInfoListAdapter extends BaseAdapter {
        GetLogisticsTrackInfoListHolder holder = null;

        @Override
        public int getCount() {
            return logisticsTrackInfoList.size();
        }

        @Override
        public GetLogisticsTrackInfo.GetLogisticsTrackInfoList getItem(int i) {
            return logisticsTrackInfoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_logistics_tracking, null);
                holder = new GetLogisticsTrackInfoListHolder();
                holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
                holder.tvContent = (TextView) view.findViewById(R.id.tv_content);
                view.setTag(holder);
            }
            holder = (GetLogisticsTrackInfoListHolder) view.getTag();
            final GetLogisticsTrackInfo.GetLogisticsTrackInfoList list = getItem(i);
            holder.tvTime.setText(list.getTime()+"");

            holder.tvContent.setText(list.getContent()+"");
            return view;
        }
        class GetLogisticsTrackInfoListHolder {

            TextView tvTime;
            TextView tvContent;
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_logistics_tracking;
    }
}
