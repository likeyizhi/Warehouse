package com.bbld.warehouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.IndexInfo;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.zxing.android.CaptureActivity;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 菜单
 * Created by likey on 2017/5/23.
 */

public class MenuActivity extends BaseActivity{
    @BindView(R.id.tv_signOut)
    TextView tvSignOut;
    @BindView(R.id.ll_toBackOrder)
    LinearLayout llToBackOrder;
    @BindView(R.id.ll_toOutOrder)
    LinearLayout llToOutOrder;
    @BindView(R.id.ll_toGetOrder)
    LinearLayout llToGetOrder;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.srl_menu)
    SwipeRefreshLayout srlMenu;
    @BindView(R.id.iv_head)
    ImageView ivHead;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    srlMenu.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void initViewsAndEvents() {
        loadData();

        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new MyToken(MenuActivity.this).delToken();
                new MyToken(MenuActivity.this).delSPFile();
                ActivityManagerUtil.getInstance().appExit();
            }
        });
        llToBackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("status", 1);
                readyGo(BackOrderActivity.class, bundle);
            }
        });
        llToOutOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("status", 2);
                readyGo(BackOrderActivity.class, bundle);
            }
        });
        llToGetOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("status", 3);
                readyGo(BackOrderActivity.class, bundle);
            }
        });
        srlMenu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadData();
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(CaptureActivity.class);
            }
        });
    }

    private void loadData() {
        Call<IndexInfo> call= RetrofitService.getInstance().indexinfo(new MyToken(MenuActivity.this).getToken()+"");
        call.enqueue(new Callback<IndexInfo>() {
            @Override
            public void onResponse(Response<IndexInfo> response, Retrofit retrofit) {
                if (response.body()==null){
                    showToast("服务器错误");
                    return;
                }
                if (response.body().getStatus()==0){
                    tvType.setText(response.body().getTypeinfo());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_menu;
    }
}
