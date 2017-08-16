package com.bbld.warehouse.update;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import retrofit.Call;


/**
 * 更新管理器
 */
@SuppressWarnings("unused")
public class UpdateAppUtils {

    @SuppressWarnings("unused")
    private static final String TAG = "DEBUG-WCL: " + UpdateAppUtils.class.getSimpleName();

    /**
     * 检查更新
     */
    @SuppressWarnings("unused")
    public static void checkUpdate(String appCode, int curVersion, final UpdateCallback updateCallback) {
//        UpdateService updateService =
//                ServiceFactory.createServiceFrom(UpdateService.class, UpdateService.ENDPOINT);
//
//        updateService.getUpdateInfo(appCode, curVersion)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(updateInfo -> onNext(updateInfo, updateCallback),
//                        throwable -> onError(throwable, updateCallback));
//        Call<UpdateInfo> call = RetrofitService.getInstance().getUpdateInfo(curVersion);
//        call.enqueue(new Callback<UpdateInfo>() {
//            @Override
//            public void onResponse(Response<UpdateInfo> response, Retrofit retrofit) {
//                UpdateInfo updateInfo = response.body();
//                if (updateInfo != null && updateInfo.getStatus() == 1) {
//                    onNext(updateInfo, updateCallback);
//                } else {
//                    onError(updateCallback);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                onError(t, updateCallback);
//            }
//        });
//        updateCallback.cancle(call);
    }

    // 显示信息
    private static void onNext(UpdateInfo updateInfo, UpdateCallback updateCallback) {
        Log.e(TAG, "返回数据: " + updateInfo.toString());
        if (updateInfo.getStatus() != 1 || updateInfo.getUrl() == null) {
            updateCallback.onError(); // 失败
        } else {
            updateCallback.onSuccess(updateInfo);
        }
    }

    // 错误信息
    private static void onError(Throwable throwable, UpdateCallback updateCallback) {
        updateCallback.onError();
    }

    // 错误信息
    private static void onError(UpdateCallback updateCallback) {
        updateCallback.onError();
    }


    /**
     * 下载Apk, 并设置Apk地址,
     * 默认位置: /storage/sdcard0/Download
     *
     * @param context    上下文
     * @param updateInfo 更新信息
     * @param infoName   通知名称
     * @param storeApk   存储的Apk
     */
    @SuppressWarnings("unused")
    public static void downloadApk(
            Context context, UpdateInfo updateInfo,
            String infoName, String storeApk
    ) {
        if (!isDownloadManagerAvailable()) {
            return;
        }

        String description = updateInfo.getDesc();
        String appUrl = updateInfo.getUrl();

        if (appUrl == null || appUrl.isEmpty()) {
            Log.e(TAG, "请填写\"App下载地址\"");
            return;
        }

        appUrl = appUrl.trim(); // 去掉首尾空格

        if (!appUrl.startsWith("http")) {
            appUrl = "http://" + appUrl; // 添加Http信息
        }

        Log.e(TAG, "appUrl: " + appUrl);

        DownloadManager.Request request;
        try {
            request = new DownloadManager.Request(Uri.parse(appUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        request.setTitle(infoName);
        request.setDescription(description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        /*File folder = new File("/mmjh");
        if((folder.exists() && folder.isDirectory()) || folder.mkdirs()) {
            request.setDestinationInExternalPublicDir(folder.getAbsolutePath(), storeApk);
        }else{
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, storeApk);
        }*/
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, storeApk);

        Context appContext = context.getApplicationContext();
        DownloadManager manager = (DownloadManager)
                appContext.getSystemService(Context.DOWNLOAD_SERVICE);

//        Toast.makeText(context, DownloadManager.getRecommendedMaxBytesOverMobile(context)+"", Toast.LENGTH_SHORT).show();

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        // 存储下载Key
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(appContext);
        long id = 0;
        try {
            id = sp.getLong(PrefsConsts.DOWNLOAD_APK_ID_PREFS, -1L);
            manager.remove(id);//删除旧的
        } catch (Exception e) {
            e.printStackTrace();
        }
        sp.edit().putLong(PrefsConsts.DOWNLOAD_APK_ID_PREFS, manager.enqueue(request)).apply();
    }

    // 最小版本号大于9
    private static boolean isDownloadManagerAvailable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    // 错误回调
    public interface UpdateCallback {
        void onSuccess(UpdateInfo updateInfo);

        void onError();

        void cancle(Call<UpdateInfo> call);
    }
}
