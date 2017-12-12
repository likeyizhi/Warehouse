package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.adapter.GroupAdapter;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.VersionAndroid;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.update.UpdateService;
import com.bbld.warehouse.utils.MyToken;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;
import com.wuxiaolong.androidutils.library.VersionUtil;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 工厂登录菜单页
 * Created by likey on 2017/11/21.
 */

public class MenuPlantActivity extends BaseActivity{
    @BindView(R.id.ll_toGL)
    LinearLayout llToGL;
    @BindView(R.id.ll_toCKGL)
    LinearLayout llToCKGL;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.warehouseName)
    TextView tvWarehouseName;

    private PopupWindow popupWindow;
    private View view;
    private ListView lv_group;
    private ArrayList<String> groups;
    private String name;
    private String dealerName;
    private String warehouseName;
    private int type;
    private int ishandover;

    @Override
    protected void initViewsAndEvents() {
        tvType.setText(dealerName);
        tvWarehouseName.setText(dealerName);
        tvName.setText(dealerName);
        setListeners();
    }

    private void setListeners() {
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(tvName);
            }
        });
        //跳转--关联
        llToGL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(BarcodeConnectActivity.class);
            }
        });
        //跳转--查看关联
        llToCKGL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(BarcodeConnectWatcheActivity.class);
            }
        });
        getUpdateMsg();
    }

    private void getUpdateMsg() {
        Call<VersionAndroid> call= RetrofitService.getInstance().getVersionAndroid();
        call.enqueue(new Callback<VersionAndroid>() {
            @Override
            public void onResponse(Response<VersionAndroid> response, Retrofit retrofit) {
                if (response==null){
                    return;
                }
                if (response.body().getStatus()==0){
                    String newVersionName = response.body().getVersion();
                    final String updateUrl = response.body().getUrl();
                    final String versionName= VersionUtil.getVersionName(getApplicationContext());
                    // TODO 判断网络VersionName和当前app的VersionName
                    if (!newVersionName.equals(versionName)){
                        AlertDialog.Builder builder=new AlertDialog.Builder(MenuPlantActivity.this);
                        builder.setTitle("更新").setMessage("是否下载新版本").setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(MenuPlantActivity.this, UpdateService.class);
                                intent.putExtra("Key_App_Name",getResources().getString(R.string.app_name));//app名
                                intent.putExtra("Key_Down_Url",updateUrl);//网络获取的下载链接
                                startService(intent);
                                showToast("正在后台下载，不会影响您的正常使用");
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setCancelable(false).show();
                    }
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

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

    private void showSignOutDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MenuPlantActivity.this);
        builder.setMessage("确认退出登录？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //                new MyToken(MenuActivity.this).delSPFile();
                new MyToken(MenuPlantActivity.this).delToken();
                SharedPreferences sharedAP=getSharedPreferences("WearhouseAP",MODE_PRIVATE);
                SharedPreferences.Editor editorAP = sharedAP.edit();
                editorAP.putString("WHACC","");
                editorAP.putString("WHPWD","");
                editorAP.commit();
                SharedPreferences shared = getSharedPreferences("MyToken", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putInt("loginType", 0);
                editor.commit();
                ActivityManagerUtil.getInstance().finishActivity(MenuPlantActivity.this);
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

    @Override
    protected void getBundleExtras(Bundle extras) {
        name=extras.getString("name");
        dealerName=extras.getString("dealerName");
        warehouseName=extras.getString("warehouseName");
        type=extras.getInt("type", 0);
        ishandover=extras.getInt("ishandover", 0);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_menu_plant;
    }
}
