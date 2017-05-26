package com.bbld.warehouse.app;

import android.app.Application;

import com.bbld.warehouse.utils.MyToken;
import com.wuxiaolong.androidutils.library.CrashHandlerUtil;
import com.wuxiaolong.androidutils.library.LogUtil;

/**
 * Created by likey on 2017/5/19.
 */

public class WHApplication extends Application{
    private static WHApplication instance;
    /**
     * @return Application 实例
     */
    public static WHApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //程序崩溃处理
        LogUtil.setLog(false);
        CrashHandlerUtil crashHandlerUtil=CrashHandlerUtil.getInstance();
        crashHandlerUtil.init(this);
        crashHandlerUtil.setCrashTip("很抱歉，程序出现异常，即将退出！");
    }
}
