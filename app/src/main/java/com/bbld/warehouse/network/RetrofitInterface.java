package com.bbld.warehouse.network;

import com.bbld.warehouse.bean.VersionAndroid;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by young on 2016/11/6.
 */

public interface RetrofitInterface {
    /**
     * 测试
     */
    @GET("GetVersionAndroid.aspx")
    Call<VersionAndroid> getVersionAndroid();
}
