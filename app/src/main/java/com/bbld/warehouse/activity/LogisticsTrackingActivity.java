package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.R.id.list;

/**
 * Created by zzy on 2017/5/24.
 * 物流追踪
 */

public class LogisticsTrackingActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_yincang)
    TextView tvYincang;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_logistics)
    TextView tvLogistics;
    @BindView(R.id.lv_logistics_trackinfo)
    ListView lvLogistics;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private List<GetLogisticsTrackInfo.GetLogisticsTrackInfoList> logisticsTrackInfoList;
    private List<GetOrderLogisticsInfo.GetOrderLogisticsInfoList> logisticsInfoList;
    private GetLogisticsTrackInfoListAdapter getLogisticsTrackInfoAdapter;
    private int logisticsId;
    private  String number;
    private  int invoiceid;
    private String[] items;
    @Override
    protected void initViewsAndEvents() {
        loadData();
        setListeners();
    }
    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(LogisticsTrackingActivity.this);
            }
        });
        tvLogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseTypeIdDialog();
            }
        });
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        invoiceid = Integer.parseInt(extras.getString("OrderID()"));
    }
    private void loadData1(){
        /**
         物流跟踪查询接口
         */
        Call<GetLogisticsTrackInfo> call = RetrofitService.getInstance().getLogisticsTrackInfo(new MyToken(LogisticsTrackingActivity.this).getToken() + "", logisticsId, number);
//        showToast( "物流id"+logisticsId+"物流编号"+number);
        call.enqueue(new Callback<GetLogisticsTrackInfo>() {
            @Override
            public void onResponse(Response<GetLogisticsTrackInfo> response, Retrofit retrofit) {
                if (response.body() == null) {
                    showToast("服务器错误");
                    return;
                }
                if (response.body().getStatus() == 0) {
                    logisticsTrackInfoList=response.body().getList();
                    setAdapter();

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
                    showToast("请选择物流信息");
                    return;
                }
                if (response.body().getStatus() == 0) {
                    logisticsInfoList = response.body().getList();
                    if(!logisticsInfoList.isEmpty()){
                        tvLogistics.setText("【"+logisticsInfoList.get(0).getLogisticsName()+"】"+logisticsInfoList.get(0).getLogisticsNumber());
                        tvName.setText(logisticsInfoList.get(0).getLogisticsName() + "");
                        tvNumber.setText(logisticsInfoList.get(0).getLogisticsNumber() + "");
                        tvYincang.setText(logisticsInfoList.get(0).getLogisticsID()+"");
                        number=tvNumber.getText().toString();
                        logisticsId=Integer.parseInt(tvYincang.getText().toString());
                        loadData1();
                    }
                    items = new String[ logisticsInfoList.size()];
                    for (int t=0;t< logisticsInfoList.size();t++){
                        items[t]= "【"+logisticsInfoList.get(t).getLogisticsName()+"】"+logisticsInfoList.get(t).getLogisticsNumber();
                    }
                }else {
                    showToast(""+response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    private void setAdapter() {
        getLogisticsTrackInfoAdapter=new GetLogisticsTrackInfoListAdapter();
        lvLogistics.setAdapter(getLogisticsTrackInfoAdapter);
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

    private void chooseTypeIdDialog() {
        AlertDialog alertDialog=new AlertDialog.Builder(LogisticsTrackingActivity.this)

                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logisticsId=Integer.parseInt(logisticsInfoList.get(i).getLogisticsID());
                        tvLogistics.setText("【"+logisticsInfoList.get(i).getLogisticsName()+"】"+logisticsInfoList.get(i).getLogisticsNumber());
                        tvName.setText(logisticsInfoList.get(i).getLogisticsName() + "");
                        tvNumber.setText(logisticsInfoList.get(i).getLogisticsNumber() + "");
                        tvYincang.setText(logisticsInfoList.get(i).getLogisticsID()+"");
                        number=tvNumber.getText().toString();
                        logisticsId=Integer.parseInt(tvYincang.getText().toString());
                        loadData1();
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }
}
