package com.bbld.warehouse.app;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
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

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        //程序崩溃处理
        LogUtil.setLog(false);
        CrashHandlerUtil crashHandlerUtil=CrashHandlerUtil.getInstance();
        crashHandlerUtil.init(this);
        crashHandlerUtil.setCrashTip("很抱歉，程序出现异常，即将退出！");
    }
}
