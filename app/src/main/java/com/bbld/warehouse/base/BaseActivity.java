package com.bbld.warehouse.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bbld.warehouse.activity.CaptureFinishActivity;
import com.bbld.warehouse.activity.MenuActivity;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;
import com.wuxiaolong.androidutils.library.NetConnectUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by likey on 2017/5/11.
 */

public abstract class BaseActivity extends AppCompatActivity{
    public ActivityManagerUtil activityManagerUtil;
    public Activity mActivity;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ActivityManagerUtil.getInstance().pushOneActivity(this);
        unbinder = ButterKnife.bind(this);
        mActivity = this;
        activityManagerUtil = ActivityManagerUtil.getInstance();
        activityManagerUtil.pushOneActivity(this);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }
        initViewsAndEvents();
    }
    /**
     * init all views and add events
     */
    protected abstract void initViewsAndEvents();

    /**
     * get bundle data
     *
     * @param extras
     */
    protected abstract void getBundleExtras(Bundle extras);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        //结束Activity&从栈中移除该Activity
        activityManagerUtil.popOneActivity(this);
    }
    /**
     * 绑定布局资源文件.xml
     *
     * @return id of layout resource
     */
    public abstract int getContentView();
    /**
     * show toast
     *
     * @param msg
     */
    public void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 隐藏输入法
     */
    public void hideInputMethod() {
        // 隐藏输入法
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) { //是否打开输入法
            //隐藏输入法
            try {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            finish();
        }
        return false;
    }
    private void showNetDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("请确认网络是否正常连接");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
        builder.create().show();
    }
}
