package com.bbld.warehouse.base;

import android.os.Environment;

/**
 * Created by young on 2016/11/6.
 */

public class Constants {
//    public final static String BASE_URL = "http://182.92.183.143:8040";//测试地址
    public final static String BASE_URL = "http://182.92.183.143:8061/Api/";//测试

    public final static int STATUS_0 = 0;
    public final static int STATUS_1 = 1;
    public final static int STATUS_2 = 2;

    /** SharedPreference 配置文件,存储user实体类 */
    public static final String SHAREDPREFERENCE_CONFIG_USER = "mmjh_sp_config_user";
    //保存个人信息
    public static final String USER = "user";

    public static final int EVENT_BEGIN = 0X100;
    public static final int EVENT_REFRESH_DATA = EVENT_BEGIN + 10;
    public static final int EVENT_LOAD_MORE_DATA = EVENT_BEGIN + 20;
    public static final int EVENT_FINISH_ACTIVITY = EVENT_BEGIN + 30;
    public static final int EVENT_SHOW_CAMERA_ICON = EVENT_BEGIN + 40;
    public static final int EVENT_HIDE_CAMERA_ICON = EVENT_BEGIN + 50;
    public static final int EVENT_LOGOUT = EVENT_BEGIN + 60;
    public static final int EVENT_CHANGE = EVENT_BEGIN + 70;

    public static final int EVENT_PUSH = EVENT_BEGIN + 80;//推送相关

    public static final int EVENT_LIVE_PUSH = EVENT_BEGIN + 90;

    public static final int PAGE_LAZY_LOAD_DELAY_TIME_MS = 200;

    public static final int PAGE_SIZE = 20;

    public static final int FOLLOWORPRAISE = 1;
    public static final int UNFOLLOWORUNPRAISE = 0;

    //** 分享icon *//
    public static final String SHARE_ICON = "1";
    //** 分享 二维码 *//
    public static final String SHARE_QR_CODE = "2";

    public static final class Paths {
        public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        public static final String IMAGE_LOADER_CACHE_PATH = "/MaternalInfant/Images/";
    }
}
//	                            _ooOoo_
//	                           o8888888o
//	                           88" . "88
//	                           (| -_- |)
//	                            O\ = /O
//	                        ____/`---'\____
//	                      .   ' \\| |// `.
//	                       / \\||| : |||// \
//	                     / _||||| -:- |||||- \
//	                       | | \\\ - /// | |
//	                     | \_| ''\---/'' | |
//	                      \ .-\__ `-` ___/-. /
//	                   ___`. .' /--.--\ `. . __
//	                ."" '< `.___\_<|>_/___.' >'"".
//	               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//	                 \ \ `-. \_ __\ /__ _/ .-` / /
//	         ======`-.____`-.___\_____/___.-`____.-'======
//	                            `=---='
//
//	         .............................................
//	                  佛祖保佑             永无BUG
//	          佛曰:
//	                  写字楼里写字间，写字间里程序员；
//	                  程序人员写程序，又拿程序换酒钱。
//	                  酒醒只在网上坐，酒醉还来网下眠；
//	                  酒醉酒醒日复日，网上网下年复年。
//	                  但愿老死电脑间，不愿鞠躬老板前；
//	                  奔驰宝马贵者趣，公交自行程序员。
//	                  别人笑我忒疯癫，我笑自己命太贱；
//	                  不见满街漂亮妹，哪个归得程序员？
