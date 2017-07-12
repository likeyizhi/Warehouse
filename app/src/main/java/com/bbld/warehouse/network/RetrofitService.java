package com.bbld.warehouse.network;

import com.bbld.warehouse.base.Constants;
import com.bbld.warehouse.bean.AddOrderLogisticsInfo;
import com.bbld.warehouse.bean.CancelInventory;
import com.bbld.warehouse.bean.FinishInventory;
import com.bbld.warehouse.bean.GetLogisticsList;
import com.bbld.warehouse.bean.GetLogisticsTrackInfo;
import com.bbld.warehouse.bean.GetNewNumber;
import com.bbld.warehouse.bean.GetOrderLogisticsInfo;
import com.bbld.warehouse.bean.GetSearchTypeList;
import com.bbld.warehouse.bean.GetTypeList;
import com.bbld.warehouse.bean.HandOverSacnFinish;
import com.bbld.warehouse.bean.HandoverEdit;
import com.bbld.warehouse.bean.HandoverInfo;
import com.bbld.warehouse.bean.HandoverList;
import com.bbld.warehouse.bean.IndexInfo;
import com.bbld.warehouse.bean.InventoryEdit;
import com.bbld.warehouse.bean.InventoryInfo;
import com.bbld.warehouse.bean.InventoryList;
import com.bbld.warehouse.bean.Login;
import com.bbld.warehouse.bean.OrderDetails;
import com.bbld.warehouse.bean.OrderList;
import com.bbld.warehouse.bean.OrderSend;
import com.bbld.warehouse.bean.PendingOutStorageList;
import com.bbld.warehouse.bean.ProductCountList;
import com.bbld.warehouse.bean.ProductList;
import com.bbld.warehouse.bean.ScanCode;
import com.bbld.warehouse.bean.StorageDetails;
import com.bbld.warehouse.bean.StorageList;
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
    public Call<ScanCode> scanCode(String token, int invoiceid, int productId, String  code, int type){
        return retrofitInterface.scanCode(token, invoiceid, productId, code, type);
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
    /**
     * 获取出入库类型列表
     */
    public Call<GetTypeList> getTypeList(int type){
        return retrofitInterface.getTypeList(type);
    }
    /**
     * 获取出/入库单列表
     * type:1：出库，2：入库
     */
    public Call<StorageList> storageList(int type, int typeid, String token, int page, int pagesize){
        return retrofitInterface.storageList(type, typeid, token, page, pagesize);
    }
    /**
     * 获取出/入库单详情
     * type:1：出库，2：入库
     */
    public Call<StorageDetails> storageDetails(int type, String token, String storageId){
        return retrofitInterface.storageDetails(type, token, storageId);
    }
    /**
     * 商品列表
     */
    public Call<ProductList> productList(String token){
        return retrofitInterface.productList(token);
    }
    /**
     * 出入库单扫码查询接口
     */
    public Call<ScanCode> storageScanCode(String token, int type, int productId, String  code){
        return retrofitInterface.storageScanCode(token, type, productId, code);
    }
    /**
     * 库存查询
     */
    public Call<ProductCountList> productCountList(String token, int page, int pagesize){
        return retrofitInterface.productCountList(token,page,pagesize);
    }
    /**
     * 库存盘点
     */
    public Call<InventoryList> inventoryList(String token){
        return retrofitInterface.inventoryList(token);
    }
    /**
     * 库存盘点详情
     */
    public Call<InventoryInfo> inventoryInfo(String token, String inventoryId){
        return retrofitInterface.inventoryInfo(token, inventoryId);
    }
    /**
     * 库存盘点作废
     */
    public Call<CancelInventory> cancelInventory(String token, String inventoryId){
        return retrofitInterface.cancelInventory(token, inventoryId);
    }
    /**
     * 库存盘点决策
     */
    public Call<FinishInventory> finishInventory(String token, String inventoryId){
        return retrofitInterface.finishInventory(token, inventoryId);
    }
    /**
     * 库存盘点决策
     */
    public Call<InventoryEdit> inventoryEdit(String token, String inventoryId){
        return retrofitInterface.inventoryEdit(token, inventoryId);
    }
    /**
     * 市场交接
     */
    public Call<HandoverList> handoverList(String token){
        return retrofitInterface.handoverList(token);
    }
    /**
     * 市场交接--详情
     */
    public Call<HandoverInfo> handoverInfo(String token, String handoverId){
        return retrofitInterface.handoverInfo(token, handoverId);
    }
    /**
     * 市场交接--编辑
     */
    public Call<HandoverEdit> handoverEdit(String token, String handoverId){
        return retrofitInterface.handoverEdit(token, handoverId);
    }
    /**
     * 市场交接--提交
     */
    public Call<HandOverSacnFinish> handOverSacnFinish(String token, String handoverId){
        return retrofitInterface.handOverSacnFinish(token, handoverId);
    }
    /**
     * 创建新单号
     */
    public Call<GetNewNumber> getNewNumber(String token, String type){
        return retrofitInterface.getNewNumber(token, type);
    }
    /**
     * 出入库筛选
     */
    public Call<GetSearchTypeList> getSearchTypeList(String token, String type){
        return retrofitInterface.getSearchTypeList(token, type);
    }
    /**
     * 其他出库
     */
    public Call<PendingOutStorageList> getPendingOutStorageList(String token, int page, int pagesize){
        return retrofitInterface.getPendingOutStorageList(token, page, pagesize);
    }
}
