package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.bbld.warehouse.utils.MyToken;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by likey on 2017/11/1.
 */

public class MenuDealerActivity extends BaseActivity{
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.ll_toDDTB)
    LinearLayout llToDDTB;
    @BindView(R.id.ll_toSCFHD)
    LinearLayout llToSCFHD;
    @BindView(R.id.ll_toHXSB)
    LinearLayout llToHXSB;

    private PopupWindow popupWindow;
    private View view;
    private ListView lv_group;
    private ArrayList<String> groups;

    @Override
    protected void initViewsAndEvents() {
        setListeners();
    }

    private void setListeners() {
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(tvName);
            }
        });
        //跳转--订单提报
        llToDDTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(DDTBActivity.class);
            }
        });
        //跳转--生成发货单
        llToSCFHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //跳转--货需上报
        llToHXSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        AlertDialog.Builder builder=new AlertDialog.Builder(MenuDealerActivity.this);
        builder.setMessage("确认退出登录？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //                new MyToken(MenuActivity.this).delSPFile();
                new MyToken(MenuDealerActivity.this).delToken();
                SharedPreferences sharedAP=getSharedPreferences("WearhouseAP",MODE_PRIVATE);
                SharedPreferences.Editor editorAP = sharedAP.edit();
                editorAP.putString("WHACC","");
                editorAP.putString("WHPWD","");
                editorAP.commit();
                ActivityManagerUtil.getInstance().finishActivity(MenuDealerActivity.this);
                readyGo(LoginActivity.class);
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

    }

    @Override
    public int getContentView() {
        return R.layout.activity_menu_dealer;
    }
}
