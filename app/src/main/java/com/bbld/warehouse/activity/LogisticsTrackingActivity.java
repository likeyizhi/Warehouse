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
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.sp_logistics)
    Spinner spLogistics;
    @BindView(R.id.lv_logistics_trackinfo)
    ListView lvLogistics;
    private List<GetLogisticsTrackInfo.GetLogisticsTrackInfoList> logisticsTrackInfoList;
    private List<GetOrderLogisticsInfo.GetOrderLogisticsInfoList> logisticsInfoList;
    private List<GetLogisticsList.GetLogisticsListList> getLogisticsList;

    private GetLogisticsListAdapter getLogisticsListAdapter;
    private GetLogisticsTrackInfoListAdapter getLogisticsTrackInfoAdapter;
    int logisticsId;
    String number;
    int invoiceid;

    @Override
    protected void initViewsAndEvents() {
        loadData1();
        spLogistics.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvName.setText("物流公司：" + getLogisticsList.get(i).getLogisticsName() + "");
                tvNumber.setText("物流单号：" + getLogisticsList.get(i).getLogisticsID() + "");
//                logisticsId=logisticsInfoList.get(i).getLogisticsID();
//                number=getLogisticsList.get(i).getLogisticsID()+"";
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                tvName.setText("");
                tvNumber.setText("");
                loadData2();
            }
        });

    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        invoiceid = Integer.parseInt(extras.getString("OrderID()"));

    }
private void loadData2(){
    /*
  物流跟踪查询接口
 */
    Call<GetLogisticsTrackInfo> call = RetrofitService.getInstance().getLogisticsTrackInfo(new MyToken(LogisticsTrackingActivity.this).getToken() + "", logisticsId, number);

    call.enqueue(new Callback<GetLogisticsTrackInfo>() {
        @Override
        public void onResponse(Response<GetLogisticsTrackInfo> response, Retrofit retrofit) {
            if (response.body() == null) {
                showToast("服务器错误");
                return;
            }
            if (response.body().getStatus() == 0) {
                logisticsTrackInfoList=response.body().getList();
                loadData3();
            }
        }

        @Override
        public void onFailure(Throwable throwable) {

        }
    });
}
private void loadData3(){
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
    private void loadData1() {



  /*
    获取物流公司字典接口
   */
        Call<GetLogisticsList> call2 = RetrofitService.getInstance().getLogisticsList();
        call2.enqueue(new Callback<GetLogisticsList>() {
            @Override
            public void onResponse(Response<GetLogisticsList> response, Retrofit retrofit) {
                if (response.body() == null) {
                    showToast("服务器出错");
                    return;
                }
                if (response.body().getStatus() == 0) {
                    getLogisticsList = response.body().getList();

                    setAdapter1();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    private void setAdapter1() {
        getLogisticsListAdapter = new GetLogisticsListAdapter();
        spLogistics.setAdapter(getLogisticsListAdapter);

    }
    private void setAdapter2() {

        getLogisticsTrackInfoAdapter=new GetLogisticsTrackInfoListAdapter();
        lvLogistics.setAdapter(getLogisticsTrackInfoAdapter);
    }

    class GetLogisticsListAdapter extends BaseAdapter {
        GetLogisticsListHolder holder = null;

        @Override
        public int getCount() {
            return getLogisticsList.size();
        }

        @Override
        public GetLogisticsList.GetLogisticsListList getItem(int i) {
            return getLogisticsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(getLogisticsList.get(i).getLogisticsID() + "");
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_sp_logistics_tracking, null);
                holder = new GetLogisticsListHolder();
                holder.tvInfo = (TextView) view.findViewById(R.id.tv_info);
                view.setTag(holder);
            }
            holder = (GetLogisticsListHolder) view.getTag();
            final GetLogisticsList.GetLogisticsListList list = getItem(i);




            holder.tvInfo.setText(list.getLogisticsName() + list.getLogisticsID() + "");

            return view;
        }

        class GetLogisticsListHolder {
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
