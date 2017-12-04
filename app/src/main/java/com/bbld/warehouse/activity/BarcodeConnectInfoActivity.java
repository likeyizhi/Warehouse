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
import com.bbld.warehouse.bean.BarcodeConnectWatchHM;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 查看关联-详情
 * Created by likey on 2017/11/16.
 */

public class BarcodeConnectInfoActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lvTMInfo)
    ListView lvTMInfo;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private int boxId;
    private String boxCode;
    private String token;
    private Dialog loading;
    private List<BarcodeConnectWatchHM.WatchHMlist> list;
    private CodeAdapter adapter;

    @Override
    protected void initViewsAndEvents() {
        tvTitle.setText("箱码："+boxCode);
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
        Call<BarcodeConnectWatchHM> call= RetrofitService.getInstance().barcodeConnectWatchHM(token,boxId);
        call.enqueue(new Callback<BarcodeConnectWatchHM>() {
            @Override
            public void onResponse(Response<BarcodeConnectWatchHM> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    list=response.body().getList();
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
        adapter=new CodeAdapter();
        lvTMInfo.setAdapter(adapter);
    }

    class CodeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public BarcodeConnectWatchHM.WatchHMlist getItem(int i) {
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
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_barcode_connect_info,null);
                holder=new CodeHolder();
                holder.tvNumber=(TextView)view.findViewById(R.id.tvNumber);
                holder.tvTM=(TextView)view.findViewById(R.id.tvTM);
                view.setTag(holder);
            }
            holder= (CodeHolder) view.getTag();
            BarcodeConnectWatchHM.WatchHMlist item = getItem(i);
            holder.tvNumber.setText(i+"");
            holder.tvTM.setText(item.getBarCode()+"");
            return view;
        }

        class CodeHolder{
            TextView tvNumber,tvTM;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        boxId=extras.getInt("boxId",0);
        boxCode=extras.getString("boxCode","");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_barcode_connect_info;
    }
}
