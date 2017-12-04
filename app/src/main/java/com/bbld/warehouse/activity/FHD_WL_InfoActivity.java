package com.bbld.warehouse.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.FHDGetLogisticsInfo;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 发货单-物流-物流信息
 * Created by likey on 2017/11/22.
 */

public class FHD_WL_InfoActivity extends BaseActivity{
    @BindView(R.id.lvWlInfo)
    ListView lvWlInfo;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private int id;
    private Dialog loading;
    private String token;
    private List<FHDGetLogisticsInfo.FHDGetLogisticsInfoinfo> info;
    private InfoAdapter adapter;

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
        loading = WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        Call<FHDGetLogisticsInfo> call= RetrofitService.getInstance().fhdGetLogisticsInfo(token,id);
        call.enqueue(new Callback<FHDGetLogisticsInfo>() {
            @Override
            public void onResponse(Response<FHDGetLogisticsInfo> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    info=response.body().getInfo();
                    setAdapter();
                    WeiboDialogUtils.closeDialog(loading);
                }else{
                    showToast(response.body().getMes());
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
        adapter=new InfoAdapter();
        lvWlInfo.setAdapter(adapter);
    }

    class InfoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return info.size();
        }

        @Override
        public FHDGetLogisticsInfo.FHDGetLogisticsInfoinfo getItem(int i) {
            return info.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            InfoHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_fhd_wl_info,null);
                holder=new InfoHolder();
                holder.tvTime=(TextView)view.findViewById(R.id.tvTime);
                holder.tvInfo=(TextView)view.findViewById(R.id.tvInfo);
                view.setTag(holder);
            }
            holder= (InfoHolder) view.getTag();
            FHDGetLogisticsInfo.FHDGetLogisticsInfoinfo item = getItem(i);
            holder.tvTime.setText(item.getTime()+"");
            holder.tvInfo.setText(item.getStatus()+"");
            return view;
        }

        class InfoHolder{
            TextView tvTime,tvInfo;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        id=extras.getInt("id",0);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_fhd_wl_info;
    }
}
