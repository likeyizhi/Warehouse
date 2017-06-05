package com.bbld.warehouse.network;

import com.bbld.warehouse.base.Constants;
import com.bbld.warehouse.bean.AddOrderLogisticsInfo;
import com.bbld.warehouse.bean.GetLogisticsList;
import com.bbld.warehouse.bean.GetLogisticsTrackInfo;
import com.bbld.warehouse.bean.GetOrderLogisticsInfo;
import com.bbld.warehouse.bean.IndexInfo;
import com.bbld.warehouse.bean.Login;
import com.bbld.warehouse.bean.OrderDetails;
import com.bbld.warehouse.bean.OrderList;
import com.bbld.warehouse.bean.OrderSend;
import com.bbld.warehouse.bean.ScanCode;
import com.bbld.warehouse.bean.VersionAndroid;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by young on 2016/11/6.
 */

public class RetrofitService {
    private static RetrofitService retrofitService = new RetrofitService();
    private static RetrofitInterface retrofitInterface;

    private RetrofitService() {
        initRetrofit();
    }

    public static RetrofitService getInstance() {
        if (retrofitService == null) {
            retrofitService = new RetrofitService();
        }
        return retrofitService;
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    /**
     * 测试
     */
    public Call<VersionAndroid> getVersionAndroid(){
        return retrofitInterface.getVersionAndroid();
    }
    /**
     * 登录
     */
    public Call<Login> login(String acc, String pwd){
        return retrofitInterface.login(acc, pwd);
    }
    /**
     * 首页接口
     */
    public Call<IndexInfo> indexinfo(String token){
        return retrofitInterface.indexinfo(token);
    }
    /**
     * 订单列表接口
     */
    public Call<OrderList> orderList(String token, int status, int page, int pagesize){
        return retrofitInterface.orderList(token, status, page, pagesize);
    }
    /**
     * 订单列表接口
     */
    public Call<OrderDetails> orderDetails(String token, int invoiceid){
        return retrofitInterface.orderDetails(token, invoiceid);
    }
    /**
     * 扫码查询接口
     */
    public Call<ScanCode> scanCode(String token, int invoiceid, int productId, String  code){
        return retrofitInterface.scanCode(token, invoiceid, productId, code);
    }
    /**
     * 订单出库接口
     */
    public Call<OrderSend> orderSend(String token, int invoiceid, String codejson){
        return retrofitInterface.orderSend(token, invoiceid, codejson);
    }
    /**
     * 获取物流公司字典接口
     */
    public Call<GetLogisticsList> getLogisticsList(String token){
        return retrofitInterface.getLogisticsList(token);
    }
    /**
     * 增加物流信息接口
     */
    public Call<AddOrderLogisticsInfo> addOrderLogisticsInfo(String token, int invoiceid, int logisticsId, String number){
        return retrofitInterface.addOrderLogisticsInfo(token, invoiceid, logisticsId, number);
    }
    /**
     * 物流信息查询接口
     */
    public Call<GetOrderLogisticsInfo> getOrderLogisticsInfo(String token, int invoiceid){
        return retrofitInterface.getOrderLogisticsInfo(token, invoiceid);
    }
    /**
     * 物流跟踪查询接口
     */
    public Call<GetLogisticsTrackInfo> getLogisticsTrackInfo(String token, int logisticsId, String number){
        return retrofitInterface.getLogisticsTrackInfo(token, logisticsId, number);
    }
}
