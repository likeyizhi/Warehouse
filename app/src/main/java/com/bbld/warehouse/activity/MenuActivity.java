package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bbld.warehouse.R;
import com.bbld.warehouse.adapter.GroupAdapter;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.IndexInfo;
import com.bbld.warehouse.db.UserDataBaseOperate;
import com.bbld.warehouse.db.UserSQLiteOpenHelper;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.ArrayList;

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
    @BindView(R.id.ll_toOthers)
    LinearLayout llToOthers;
    @BindView(R.id.tv_type)
    TextView tvType;
//    @BindView(R.id.srl_menu)
//    SwipeRefreshLayout srlMenu;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_dck)
    TextView tvDck;
    @BindView(R.id.tv_yck)
    TextView tvYck;
    @BindView(R.id.tv_dsh)
    TextView tvDsh;
    @BindView(R.id.tv_qtckdck)
    TextView tvQtckdck;
    @BindView(R.id.warehouseName)
    TextView tvWarehouseName;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    @BindView(R.id.ll_toZDPH)
    LinearLayout llToZDPH;
    @BindView(R.id.ll_01)
    LinearLayout ll_01;
    @BindView(R.id.ll_02)
    LinearLayout ll_02;
    @BindView(R.id.ll_03)
    LinearLayout ll_03;
    @BindView(R.id.ll_04)
    LinearLayout ll_04;
    @BindView(R.id.ll_05)
    LinearLayout ll_05;
    @BindView(R.id.ll_06)
    LinearLayout ll_06;
    @BindView(R.id.ll_toDHQR)
    LinearLayout ll_toDHQR;
    @BindView(R.id.ll_toXSCK)
    LinearLayout ll_toXSCK;
    @BindView(R.id.ll_toXSTJ)
    LinearLayout ll_toXSTJ;
    @BindView(R.id.ll_toRKD)
    LinearLayout ll_toRKD;
    @BindView(R.id.ll_toCKD)
    LinearLayout ll_toCKD;
    @BindView(R.id.ll_toKCCX)
    LinearLayout ll_toKCCX;

//    private Handler mHandler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 1:
//                    srlMenu.setRefreshing(false);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
    private String name;
    private String dealerName;
    private String warehouseName;
    private int type;
    private int ishandover;
    private LayoutInflater mInflater;
    private Context mContext;
    private PopupWindow popupWindow;
    private View view;
    private ListView lv_group;
    private ArrayList<String> groups;
    private Dialog loadDialog;

    @Override
    protected void initViewsAndEvents() {
        loadData();
        setData();
        delDB();
        setShow();
        setListeners();
    }

    private void setShow() {
        if (type==1 || type==2){
            ll_01.setVisibility(View.VISIBLE);
            ll_02.setVisibility(View.VISIBLE);
            ll_03.setVisibility(View.VISIBLE);
            ll_04.setVisibility(View.VISIBLE);
            ll_05.setVisibility(View.GONE);
            ll_06.setVisibility(View.GONE);
        }else if (type==3){
            ll_01.setVisibility(View.GONE);
            ll_02.setVisibility(View.GONE);
            ll_03.setVisibility(View.GONE);
            ll_04.setVisibility(View.GONE);
            ll_05.setVisibility(View.VISIBLE);
            ll_06.setVisibility(View.VISIBLE);
        }else{
            ll_01.setVisibility(View.GONE);
            ll_02.setVisibility(View.GONE);
            ll_03.setVisibility(View.GONE);
            ll_04.setVisibility(View.GONE);
            ll_05.setVisibility(View.GONE);
            ll_06.setVisibility(View.GONE);
            showToast("数据获取失败，请重试");
        }
    }

    private void setListeners() {
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
                bundle.putInt("typeid",0);
                readyGo(OutBoundOrderActivity.class, bundle);
            }
        });
        llToOutBound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("title","产品出库");
                bundle.putInt("type",1);
                bundle.putInt("typeid",0);
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
        llToOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(PendingOutActivity.class);
            }
        });
        llToZDPH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(ZDPSActivity.class);
            }
        });
        //type==3
        /** 到货确认 */
        ll_toDHQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(DHQRActivity.class);
            }
        });
        /** 销售出库 */
        ll_toXSCK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(XSCKActivity.class);
            }
        });
        /** 销售统计 */
        ll_toXSTJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("销售统计");
            }
        });
        /** 入库单 */
        ll_toRKD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("title","产品入库");
                bundle.putInt("type",2);
                bundle.putInt("typeid",0);
                readyGo(OutBoundOrderActivity.class, bundle);
            }
        });
        /** 出库单 */
        ll_toCKD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("title","产品出库");
                bundle.putInt("type",1);
                bundle.putInt("typeid",0);
                readyGo(OutBoundOrderActivity.class, bundle);
            }
        });
        /** 库存查询 */
        ll_toKCCX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(QueryActivity.class);
            }
        });
//        srlMenu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            loadData();
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        mHandler.sendEmptyMessage(1);
//                    }
//                }).start();
//            }
//        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(tvName);
            }
        });
    }

    private void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.group_list, null);

            lv_group = (ListView) view.findViewById(R.id.lvGroup);
            // 加载数据
            groups = new ArrayList<String>();
            groups.add("退出登录");

            GroupAdapter groupAdapter = new GroupAdapter(this, groups);
            lv_group.setAdapter(groupAdapter);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, 300, 350);
        }

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2
                - popupWindow.getWidth() / 2;
        Log.i("coder", "xPos:" + xPos);

        popupWindow.showAsDropDown(parent, xPos, 0);

        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                showToast("退出登录");
                showSignOutDialog();
                if (popupWindow != null) {
                    popupWindow.dismiss();
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
        loadDialog=WeiboDialogUtils.createLoadingDialog(MenuActivity.this,getString(R.string.caozuo_ing));
        Call<IndexInfo> call= RetrofitService.getInstance().indexinfo(new MyToken(MenuActivity.this).getToken()+"");
        call.enqueue(new Callback<IndexInfo>() {
            @Override
            public void onResponse(Response<IndexInfo> response, Retrofit retrofit) {
                if (response.body()==null){
                    showToast("服务器错误");
                    WeiboDialogUtils.closeDialog(loadDialog);
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
                    if (response.body().getQtckdck()!=0){
                        tvQtckdck.setText(Html.fromHtml("其他出库"+"<font color=\"#00A3D9\">"+response.body().getQtckdck()+"</font>"));//#00A3D9
                    }else{
                        tvQtckdck.setText("其他出库");
                    }
                    WeiboDialogUtils.closeDialog(loadDialog);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(loadDialog);
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
