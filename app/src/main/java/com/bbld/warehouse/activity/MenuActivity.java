package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.IndexInfo;
import com.bbld.warehouse.db.UserDataBaseOperate;
import com.bbld.warehouse.db.UserSQLiteOpenHelper;
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
    @BindView(R.id.ll_toInBound)
    LinearLayout llToInBound;
    @BindView(R.id.ll_toOutBound)
    LinearLayout llToOutBound;
    @BindView(R.id.ll_toStocking)
    LinearLayout llToStocking;
    @BindView(R.id.ll_toQuery)
    LinearLayout llToQuery;
    @BindView(R.id.ll_toTransfer)
    LinearLayout llToTransfer;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.srl_menu)
    SwipeRefreshLayout srlMenu;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_dck)
    TextView tvDck;
    @BindView(R.id.tv_yck)
    TextView tvYck;
    @BindView(R.id.tv_dsh)
    TextView tvDsh;
    @BindView(R.id.warehouseName)
    TextView tvWarehouseName;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;

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
    private String name;
    private String dealerName;
    private String warehouseName;
    private int type;
    private int ishandover;

    @Override
    protected void initViewsAndEvents() {
        loadData();
        setData();
        delDB();
        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignOutDialog();
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
        llToInBound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("title","产品入库");
                bundle.putInt("type",2);
                bundle.putInt("typeid",2);
                readyGo(OutBoundOrderActivity.class, bundle);
            }
        });
        llToOutBound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("title","产品出库");
                bundle.putInt("type",1);
                bundle.putInt("typeid",1);
                readyGo(OutBoundOrderActivity.class, bundle);
            }
        });
        llToStocking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(StockingActivity.class);
            }
        });
        llToQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(QueryActivity.class);
            }
        });
        llToTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(TransferActivity.class);
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
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llMenu.getVisibility()==View.VISIBLE){
                    llMenu.setVisibility(View.GONE);
                }else{
                    llMenu.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void delDB() {
        UserSQLiteOpenHelper mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(MenuActivity.this);
        UserDataBaseOperate mUserDataBaseOperate = new UserDataBaseOperate(mUserSQLiteOpenHelper.getWritableDatabase());
        mUserDataBaseOperate.deleteAll();
    }

    private void setData() {
//        if(type==1){
//            tvType.setText("总部");
//        }else{
//            tvType.setText("经销商");
//        }
        tvType.setText(dealerName);
        tvWarehouseName.setText(warehouseName);
        tvName.setText(name);
        if (ishandover!=1){
            llToTransfer.setVisibility(View.INVISIBLE);
            llToTransfer.setClickable(false);
        }
    }

    private void showSignOutDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MenuActivity.this);
        builder.setMessage("确认退出登录？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //                new MyToken(MenuActivity.this).delSPFile();
                new MyToken(MenuActivity.this).delToken();
                SharedPreferences sharedAP=getSharedPreferences("WearhouseAP",MODE_PRIVATE);
                SharedPreferences.Editor editorAP = sharedAP.edit();
                editorAP.putString("WHACC","");
                editorAP.putString("WHPWD","");
                editorAP.commit();
                ActivityManagerUtil.getInstance().finishActivity(MenuActivity.this);
                readyGo(LoginActivity.class);
//                ActivityManagerUtil.getInstance().appExit();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
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
                    if (response.body().getDck()!=0){
//                        tvDck.setText("待出库  "+response.body().getDck());
                        tvDck.setText(Html.fromHtml("待出库  "+"<font color=\"#00A3D9\">"+response.body().getDck()+"</font>"));//#00A3D9
                    }else{
                        tvDck.setText("待出库");
                    }
                    if (response.body().getYck()!=0){
//                        tvYck.setText("已出库  "+response.body().getYck());
                        tvYck.setText(Html.fromHtml("已出库  "+"<font color=\"#00A3D9\">"+response.body().getYck()+"</font>"));//#00A3D9
                    }else{
                        tvYck.setText("已出库");
                    }
                    if (response.body().getDsh()!=0){
//                        tvDsh.setText("待收货  "+response.body().getDsh());
                        tvDsh.setText(Html.fromHtml("待收货  "+"<font color=\"#00A3D9\">"+response.body().getDsh()+"</font>"));//#00A3D9
                    }else{
                        tvDsh.setText("待收货");
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        llMenu.setVisibility(View.GONE);
        name=extras.getString("name");
        dealerName=extras.getString("dealerName");
        warehouseName=extras.getString("warehouseName");
        type=extras.getInt("type", 0);
        ishandover=extras.getInt("ishandover", 0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            showAppExitDialog();
        }
        return false;
    }

    private void showAppExitDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MenuActivity.this);
        builder.setMessage("确定退出?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityManagerUtil.getInstance().appExit();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_menu;
    }
}
