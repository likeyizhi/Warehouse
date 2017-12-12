package com.bbld.warehouse.network;

import com.bbld.warehouse.bean.AddOrderLogisticsInfo;
import com.bbld.warehouse.bean.BarcodeConnectScan;
import com.bbld.warehouse.bean.BarcodeConnectWatchHM;
import com.bbld.warehouse.bean.BarcodeConnectWatchXM;
import com.bbld.warehouse.bean.CancelInventory;
import com.bbld.warehouse.bean.ClearScanCode;
import com.bbld.warehouse.bean.CloseOrder;
import com.bbld.warehouse.bean.CusInvoiceConfirm;
import com.bbld.warehouse.bean.CusInvoiceGetCusInvoiceList;
import com.bbld.warehouse.bean.CusInvoiceGetInfo;
import com.bbld.warehouse.bean.CusInvoiceInfo;
import com.bbld.warehouse.bean.CusInvoiceInitAddGetInfo;
import com.bbld.warehouse.bean.CusInvoiceReceiptList;
import com.bbld.warehouse.bean.CusInvoiceSendList;
import com.bbld.warehouse.bean.DCGetChildOrderList;
import com.bbld.warehouse.bean.DCOGetEndCustomerList;
import com.bbld.warehouse.bean.DCOGetProductCirculationStatistics;
import com.bbld.warehouse.bean.DealerChildOrderInitOrderClose;
import com.bbld.warehouse.bean.DealerChildOrderInitOrderPass;
import com.bbld.warehouse.bean.FHDDeleteLogistics;
import com.bbld.warehouse.bean.FHDGetAddCurrentProviceFHDInfo;
import com.bbld.warehouse.bean.FHDGetAddOtherProviceFHDInfo;
import com.bbld.warehouse.bean.FHDGetCboLogistics;
import com.bbld.warehouse.bean.FHDGetCurrentProviceFHDInfo;
import com.bbld.warehouse.bean.FHDGetDealerDeliveryList;
import com.bbld.warehouse.bean.FHDGetFHDList;
import com.bbld.warehouse.bean.FHDGetInvoiceLogisticsList;
import com.bbld.warehouse.bean.FHDGetLogisticsInfo;
import com.bbld.warehouse.bean.FHDGetLogisticsList;
import com.bbld.warehouse.bean.FHDGetOrderProductList;
import com.bbld.warehouse.bean.FHDGetOtherProviceFHDInfo;
import com.bbld.warehouse.bean.FinishInventory;
import com.bbld.warehouse.bean.GetEndCustomerInfo;
import com.bbld.warehouse.bean.GetLogisticsList;
import com.bbld.warehouse.bean.GetLogisticsTrackInfo;
import com.bbld.warehouse.bean.GetNewNumber;
import com.bbld.warehouse.bean.GetOrderLogisticsInfo;
import com.bbld.warehouse.bean.GetOrderTbList;
import com.bbld.warehouse.bean.GetParentDealerForEndCustomer;
import com.bbld.warehouse.bean.GetProductNeedList;
import com.bbld.warehouse.bean.GetSearchTypeList;
import com.bbld.warehouse.bean.GetTypeList;
import com.bbld.warehouse.bean.GivebackGetGivebackDetail;
import com.bbld.warehouse.bean.GivebackGetGivebackForInList;
import com.bbld.warehouse.bean.GivebackGetGivebackForOutList;
import com.bbld.warehouse.bean.GivebackReciveScanCode;
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
import com.bbld.warehouse.bean.OrderManualDelivery;
import com.bbld.warehouse.bean.OrderSend;
import com.bbld.warehouse.bean.PendingOutStorageList;
import com.bbld.warehouse.bean.ProductCountDetails;
import com.bbld.warehouse.bean.ProductCountList;
import com.bbld.warehouse.bean.ProductList;
import com.bbld.warehouse.bean.QTInStorageList;
import com.bbld.warehouse.bean.ReciveScanCode;
import com.bbld.warehouse.bean.RefundDetail;
import com.bbld.warehouse.bean.RefundGetHQRefundList;
import com.bbld.warehouse.bean.RefundGetRefundDetail;
import com.bbld.warehouse.bean.RefundGetRefundList;
import com.bbld.warehouse.bean.RefundGetRefundProductScanCode;
import com.bbld.warehouse.bean.RefundList;
import com.bbld.warehouse.bean.RemoveCommitScanCode;
import com.bbld.warehouse.bean.RemoveScanCode;
import com.bbld.warehouse.bean.SaleScanCode;
import com.bbld.warehouse.bean.SaleStatistics;
import com.bbld.warehouse.bean.ScanCode;
import com.bbld.warehouse.bean.ScanCodeRefund;
import com.bbld.warehouse.bean.StorageCodeList;
import com.bbld.warehouse.bean.StorageDetails;
import com.bbld.warehouse.bean.StorageGetSaleList;
import com.bbld.warehouse.bean.StorageList;
import com.bbld.warehouse.bean.TbGetDealerDeliveryList;
import com.bbld.warehouse.bean.TbGetOrderInfo;
import com.bbld.warehouse.bean.TbGetProductInfo;
import com.bbld.warehouse.bean.TbGetProductList;
import com.bbld.warehouse.bean.VersionAndroid;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by young on 2016/11/6.
 */

public interface RetrofitInterface {
    /**
     * 自动升级
     */
    @GET("Admin/GetVersionAndroid")
    Call<VersionAndroid> getVersionAndroid();
    /**
     * 库管登录
     * 登录
     */
//    @FormUrlEncoded
//    @POST("Admin/Login")
//    Call<Login> login(@Field("acc") String acc, @Field("pwd") String pwd);
    @GET("Admin/Login")
    Call<Login> login(@Query("acc") String acc, @Query("pwd") String pwd);
    /**
     * 经销商登录
     * 登录
     */
    @GET("DealerAdmin/Login")
    Call<Login> dealerLogin(@Query("acc") String acc, @Query("pwd") String pwd);
    /**
     * 工厂登录
     * 登录
     */
    @GET("Plant/login")
    Call<Login> plantLogin(@Query("acc") String acc, @Query("pwd") String pwd);
    /**
     * 首页接口
     */
//    @FormUrlEncoded
//    @POST("Admin/indexinfo")
//    Call<IndexInfo> indexinfo(@Field("token") String token);
    @GET("Admin/indexinfo")
    Call<IndexInfo> indexinfo(@Query("token") String token);
    /**
     * 订单列表接口
     */
//    @FormUrlEncoded
//    @POST("Order/OrderList")
//    Call<OrderList> orderList(@Field("token") String token, @Field("status") int status);
    @GET("Order/OrderList")
    Call<OrderList> orderList(@Query("token") String token, @Query("status") int status, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 订单详情接口
     */
//    @FormUrlEncoded
//    @POST("Order/OrderList")
//    Call<OrderDetails> orderDetails(@Field("token") String token, @Field("invoiceid") int invoiceid);
    @GET("Order/OrderDetails")
    Call<OrderDetails> orderDetails(@Query("token") String token, @Query("invoiceid") int invoiceid);
    /**
     * 扫码查询接口
     */
//    @FormUrlEncoded
//    @POST("Order/ScanCode")
//    Call<ScanCode> scanCode(@Field("token") String token, @Field("invoiceid") int invoiceid, @Field("productId") int productId, @Field("code") String code);
    @GET("Order/ScanCode")
    Call<ScanCode> scanCode(@Query("token") String token, @Query("invoiceid") int invoiceid, @Query("productId") int productId, @Query("code") String code, @Query("type") int type);
    /**
     * 扫码查询接口(new)
     */
//    @FormUrlEncoded
//    @POST("Order/ScanCode")
//    Call<ScanCode> scanCode(@Field("token") String token, @Field("invoiceid") int invoiceid, @Field("productId") int productId, @Field("code") String code);
    @GET("Order/ScanCodeNew")
    Call<ScanCode> scanCodeNew(@Query("token") String token, @Query("invoiceid") int invoiceid, @Query("productId") int productId, @Query("code") String code, @Query("type") int type, @Query("unique") String unique);
    /**
     * 订单出库接口
     */
    @FormUrlEncoded
    @POST("Order/OrderSend")
    Call<OrderSend> orderSend(@Field("token") String token, @Field("invoiceid") int invoiceid, @Field("codejson") String codejson);
//    @GET("Order/OrderSend")
//    Call<OrderSend> orderSend(@Query("token") String token, @Query("invoiceid") int invoiceid, @Query("codejson") String codejson);
    /**
     * 获取物流公司字典接口
     */
//    @FormUrlEncoded
//    @POST("Order/GetLogisticsList")
//    Call<GetLogisticsList> getLogisticsList();
    @GET("Order/GetLogisticsList")
    Call<GetLogisticsList> getLogisticsList(@Query("token") String token);
    /**
     * 增加物流信息接口
     */
//    @FormUrlEncoded
//    @POST("Order/AddOrderLogisticsInfo")
//    Call<AddOrderLogisticsInfo> addOrderLogisticsInfo(@Field("token") String token, @Field("invoiceid") int invoiceid, @Field("logisticsId") int logisticsId, @Field("number") String number);
    @GET("Order/AddOrderLogisticsInfo")
    Call<AddOrderLogisticsInfo> addOrderLogisticsInfo(@Query("token") String token, @Query("invoiceid") int invoiceid, @Query("logisticsId") int logisticsId, @Query("number") String number);
    /**
     * 物流信息查询接口
     */
//    @FormUrlEncoded
//    @POST("Order/GetOrderLogisticsInfo")
//    Call<GetOrderLogisticsInfo> getOrderLogisticsInfo(@Field("token") String token, @Field("invoiceid") int invoiceid);
    @GET("Order/GetOrderLogisticsInfo")
    Call<GetOrderLogisticsInfo> getOrderLogisticsInfo(@Query("token") String token, @Query("invoiceid") int invoiceid);
    /**
     * 物流跟踪查询接口
     */
//    @FormUrlEncoded
//    @POST("Order/GetLogisticsTrackInfo")
//    Call<GetLogisticsTrackInfo> getLogisticsTrackInfo(@Field("token") String token, @Field("logisticsId") int logisticsId, @Field("number") String number);
    @GET("Order/GetLogisticsTrackInfo")
    Call<GetLogisticsTrackInfo> getLogisticsTrackInfo(@Query("token") String token, @Query("logisticsId") int logisticsId, @Query("number") String number);
    /**
     * 获取出入库类型列表
     */
    @GET("Storage/GetTypeList")
    Call<GetTypeList> getTypeList(@Query("type") int type);
    /**
     * 获取出/入库单列表
     * type:1：出库，2：入库
     */
    @GET("Storage/StorageList")
    Call<StorageList> storageList(@Query("type") int type, @Query("typeid") int typeid, @Query("token") String token, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 获取出/入库单详情
     * type:1：出库，2：入库
     */
    @GET("Storage/StorageDetails")
    Call<StorageDetails> storageDetails(@Query("type") int type, @Query("token") String token, @Query("storageId") String storageId);
    /**
     * 商品列表
     */
    @GET("Storage/ProductList")
    Call<ProductList> productList(@Query("token") String token);
    /**
     * 出入库单扫码查询接口
     */
    @GET("Storage/ScanCode")
    Call<ScanCode> storageScanCode(@Query("token") String token, @Query("type") int type, @Query("productId") int productId, @Query("code") String code);
    /**
     * 库存查询
     */
    @GET("Storage/ProductCountList")
    Call<ProductCountList> productCountList(@Query("token") String token, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 库存盘点
     */
    @GET("Storage/InventoryList")
    Call<InventoryList> inventoryList(@Query("token") String token);
    /**
     * 库存盘点详情
     */
    @GET("Storage/InventoryInfo")
    Call<InventoryInfo> inventoryInfo(@Query("token") String token, @Query("inventoryId") String inventoryId);
    /**
     * 库存盘点作废
     */
    @GET("Storage/CancelInventory")
    Call<CancelInventory> cancelInventory(@Query("token") String token, @Query("inventoryId") String inventoryId);
    /**
     * 库存盘点决策
     */
    @GET("Storage/FinishInventory")
    Call<FinishInventory> finishInventory(@Query("token") String token, @Query("inventoryId") String inventoryId);
    /**
     * 库存盘点编辑
     */
    @GET("Storage/InventoryEdit")
    Call<InventoryEdit> inventoryEdit(@Query("token") String token, @Query("inventoryId") String inventoryId);
    /**
     * 市场交接
     */
    @GET("Storage/HandoverList")
    Call<HandoverList> handoverList(@Query("token") String token);
    /**
     * 市场交接--详情
     */
    @GET("Storage/HandoverInfo")
    Call<HandoverInfo> handoverInfo(@Query("token") String token, @Query("handoverId") String handoverId);
    /**
     * 市场交接--编辑
     */
    @GET("Storage/HandoverEdit")
    Call<HandoverEdit> handoverEdit(@Query("token") String token, @Query("handoverId") String handoverId);
    /**
     * 市场交接--提交
     */
    @GET("Storage/HandOverSacnFinish")
    Call<HandOverSacnFinish> handOverSacnFinish(@Query("token") String token, @Query("handoverId") String handoverId);
    /**
     * 创建新单号
     */
    @GET("Storage/GetNewNumber")
    Call<GetNewNumber> getNewNumber(@Query("token") String token, @Query("type") String type);
    /**
     * 出入库筛选
     */
    @GET("Storage/GetSearchTypeList")
    Call<GetSearchTypeList> getSearchTypeList(@Query("token") String token, @Query("type") String type);
    /**
     * 其他出库
     */
    @GET("Storage/GetPendingOutStorageList")
    Call<PendingOutStorageList> getPendingOutStorageList(@Query("token") String token, @Query("page") int type, @Query("pagesize") int pagesize);
    /**
     * 其他入库
     */
    @GET("Storage/GetQTInStorageList")
    Call<QTInStorageList> getQTInStorageList(@Query("token") String token, @Query("page") int type, @Query("pagesize") int pagesize);
    /**
     * 库存盘点详情
     */
    @GET("Storage/ProductCountDetails")
    Call<ProductCountDetails> getProductCountDetails(@Query("token") String token, @Query("productId") String productId);
    /**
     * 终端配送
     */
    @GET("Storage/GetCusInvoiceSendList")
    Call<CusInvoiceSendList> getCusInvoiceSendList(@Query("token") String token);
    /**
     * 终端配送详情/到货确认详情
     */
    @GET("Storage/GetCusInvoiceInfo")
    Call<CusInvoiceInfo> getCusInvoiceInfo(@Query("token") String token, @Query("CustomerInvoiceId") String CustomerInvoiceId);
    /**
     * 到货确认
     */
    @GET("Storage/GetCusInvoiceReceiptList")
    Call<CusInvoiceReceiptList> getCusInvoiceReceiptList(@Query("token") String token);
    /**
     * 到货确认详情-到货确认
     */
    @GET("Storage/CusInvoiceConfirm")
    Call<CusInvoiceConfirm> cusInvoiceConfirm(@Query("token") String token, @Query("CustomerInvoiceId") String CustomerInvoiceId);
    /**
     * 销售出库-扫码
     */
    @GET("Storage/SaleScanCode")
    Call<SaleScanCode> saleScanCode(@Query("token") String token, @Query("code") String code);
    /**
     * 退货单-扫码
     */
    @GET("Storage/ScanCodeRefund")
    Call<ScanCodeRefund> scanCodeRefund(@Query("token") String token, @Query("code") String code, @Query("refundId") String refundId, @Query("productId") String productId);
    /**
     *
     */
    @GET("Storage/StorageCodeList")
    Call<StorageCodeList> storageCodeList(@Query("token") String token, @Query("type") String type, @Query("storageId") String storageId);
    /**
     * 销售统计
     */
    @GET("Storage/SaleStatistics")
    Call<SaleStatistics> saleStatistics(@Query("token") String token, @Query("start") String start, @Query("end") String end);
    /**
     * 退货单
     */
    @GET("Storage/GetRefundList")
    Call<RefundList> getRefundList(@Query("token") String token, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 退货单-详情
     */
    @GET("Storage/GetRefundDetail")
    Call<RefundDetail> getRefundDetail(@Query("token") String token, @Query("id") String id);
    /**
     * 删除条码
     */
    @GET("Order/RemoveScanCode")
    Call<RemoveScanCode> removeScanCode(@Query("token") String token, @Query("type") String type, @Query("invoiceId") String invoiceId, @Query("productId") String productId, @Query("code") String code, @Query("unique") String unique);
    /**
     * 删除条码(退货)
     */
    @GET("Refund/RemoveScanCode")
    Call<RemoveScanCode> removeScanCodeRefund(@Query("token") String token, @Query("code") String code, @Query("unique") String unique);
    /**
     * 清空已扫条码
     */
    @GET("Order/ClearScanCode")
    Call<ClearScanCode> clearScanCode(@Query("token") String token, @Query("type") String type, @Query("invoiceId") String invoiceId, @Query("unique") String unique);
    /**
     * 清空已扫条码-退货
     */
    @GET("Refund/ClearScanCode")
    Call<ClearScanCode> clearScanCodeRefund(@Query("token") String token, @Query("unique") String unique);
    /**
     * 人工收货
     */
    @GET("Order/OrderManualDelivery")
    Call<OrderManualDelivery> orderManualDelivery(@Query("token") String token, @Query("invoiceId") String invoiceId);
    /**
     * 重置网络数据
     */
    @GET("Order/RemoveCommitScanCode")
    Call<RemoveCommitScanCode> removeCommitScanCode(@Query("token") String token, @Query("unique") String unique);
    /**
     * 退货扫码
     */
    @GET("Refund/ScanCode")
    Call<ScanCode> refundScanCode(@Query("token") String token, @Query("unique") String unique, @Query("productId") String productId, @Query("code") String code);
    /**
     * 退货申请列表
     */
    @GET("Refund/GetRefundList")
    Call<RefundGetRefundList> refundGetRefundList(@Query("token") String token, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 退货申请-详情
     */
    @GET("Refund/GetRefundDetail")
    Call<RefundGetRefundDetail> refundGetRefundDetail(@Query("token") String token, @Query("id") String id);
    /**
     * 退货申请-详情-明细
     */
    @GET("Refund/GetRefundProductScanCode")
    Call<RefundGetRefundProductScanCode> refundGetRefundProductScanCode(@Query("token") String token, @Query("id") String id, @Query("productId") String productId);
    /**
     * 还货出库-详情-明细
     */
    @GET("Giveback/GetGivebackScanCode")
    Call<RefundGetRefundProductScanCode> givebackGetGivebackScanCode(@Query("token") String token, @Query("id") String id, @Query("productId") String productId);
    /**
     * 退货入库列表
     */
    @GET("Refund/GetHQRefundList")
    Call<RefundGetHQRefundList> refundGetHQRefundList(@Query("token") String token, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 退货入库列表
     */
    @GET("Giveback/GetGivebackForInList")
    Call<GivebackGetGivebackForInList> givebackGetGivebackForInList(@Query("token") String token, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 退货入库-扫码
     */
    @GET("Refund/ReciveScanCode")
    Call<ReciveScanCode> reciveScanCode(@Query("token") String token, @Query("refundId") String refundId, @Query("productId") String productId, @Query("code") String code);
    /**
     * 还货出库
     */
    @GET("Giveback/GetGivebackForOutList")
    Call<GivebackGetGivebackForOutList> givebackGetGivebackForOutList(@Query("token") String token, @Query("page") int page, @Query("size") int size);
    /**
     * 还货出库-详情
     */
    @GET("Giveback/GetGivebackDetail")
    Call<GivebackGetGivebackDetail> givebackGetGivebackDetail(@Query("token") String token, @Query("id") String id);
    /**
     * 还货出库-扫码
     */
    @GET("Giveback/ReciveScanCode")
    Call<GivebackReciveScanCode> givebackReciveScanCode(@Query("token") String token, @Query("id") String id, @Query("productId") String productId, @Query("code") String code);
    /**
     * 盘点单-扫码
     */
    @GET("Storage/InventoryScanCode")
    Call<ScanCode> storageInventoryScanCode(@Query("token") String token, @Query("productId") String productId, @Query("code") String code, @Query("unique") String unique);
    /**
     * 订单提报列表
     */
    @GET("DealerOrderTb/GetOrderTbList")
    Call<GetOrderTbList> getOrderTbList(@Query("token") String token,@Query("status") int status, @Query("page") int page, @Query("size") int size);
    /**
     * 关闭订单
     */
    @GET("DealerOrderTb/CloseOrder")
    Call<CloseOrder> closeOrder(@Query("token") String token, @Query("id") String id, @Query("remark") String remark);
    /**
     * 获取订单详情
     */
    @GET("DealerOrderTb/GetOrderInfo")
    Call<TbGetOrderInfo> tbGetOrderInfo(@Query("token") String token, @Query("id") String id);
    /**
     * 订单提报-商品列表
     */
    @GET("DealerOrderTb/GetProductList")
    Call<TbGetProductList> tbGetProductList(@Query("token") String token);
    /**
     * 订单提报-商品详情
     */
    @GET("DealerOrderTb/GetProductInfo")
    Call<TbGetProductInfo> tbGetProductInfo(@Query("token") String token, @Query("id") String id);
    /**
     * 获取经销商配送地址列表
     */
    @GET("DealerOrderTb/GetDealerDeliveryList")
    Call<TbGetDealerDeliveryList> tbGetDealerDeliveryList(@Query("token") String token);
    /**
     * 根据月份获取货需数据
     */
    @GET("ProductNeed/GetProductNeedList")
    Call<GetProductNeedList> getProductNeedList(@Query("token") String token, @Query("month") String month);
    /**
     * 下级提报订单
     */
    @GET("DealerChildOrder/GetChildOrderList")
    Call<DCGetChildOrderList> dcGetChildOrderList(@Query("token") String token,@Query("status") int status, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 点击添加本省发货单
     */
    @GET("FHD/GetAddCurrentProviceFHDInfo")
    Call<FHDGetAddCurrentProviceFHDInfo> fhdGetAddCurrentProviceFHDInfo(@Query("token") String token, @Query("order") int order);
    /**
     * 获取订单商品
     */
    @GET("FHD/GetOrderProductList")
    Call<FHDGetOrderProductList> fhdGetOrderProductList(@Query("token") String token, @Query("order") int order);
    /**
     * 点击添加外省发货单
     */
    @GET("FHD/GetAddOtherProviceFHDInfo")
    Call<FHDGetAddOtherProviceFHDInfo> fhdGetAddOtherProviceFHDInfo(@Query("token") String token, @Query("order") int order);
    /**
     * 获取经销商配送地址
     */
    @GET("FHD/GetDealerDeliveryList")
    Call<FHDGetDealerDeliveryList> fhdGetDealerDeliveryList(@Query("token") String token, @Query("id") int id);
    /**
     * 订单审核通过
     */
    @GET("DealerChildOrder/InitOrderPass")
    Call<DealerChildOrderInitOrderPass> dealerChildOrderInitOrderPass(@Query("token") String token, @Query("id") int id, @Query("remark") String remark);
    /**
     * 订单关闭
     */
    @GET("DealerChildOrder/InitOrderClose")
    Call<DealerChildOrderInitOrderClose> dealerChildOrderInitOrderClose(@Query("token") String token, @Query("id") int id, @Query("remark") String remark);
    /**
     * 关联扫码
     */
    @GET("BarCodeConnect/ScanCode")
    Call<BarcodeConnectScan> barcodeConnectScan(@Query("token") String token, @Query("code") String code);
    /**
     * 箱码关联列表
     */
    @GET("BarCodeConnect/GetConnectBoxCodeList")
    Call<BarcodeConnectWatchXM> barcodeConnectWatchXM(@Query("token") String token, @Query("code") String code, @Query("start") String start, @Query("end") String end, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 关联盒码明细
     */
    @GET("BarCodeConnect/GetConnectBarCodeList")
    Call<BarcodeConnectWatchHM> barcodeConnectWatchHM(@Query("token") String token, @Query("boxId") int boxId);
    /**
     * 获取发货单列表
     */
    @GET("FHD/GetFHDList")
    Call<FHDGetFHDList> fhdGetFHDList(@Query("token") String token,@Query("status") int status, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 获取本省发货单详情
     */
    @GET("FHD/GetCurrentProviceFHDInfo")
    Call<FHDGetCurrentProviceFHDInfo> fhdGetCurrentProviceFHDInfo(@Query("token") String token, @Query("id") int id);
    /**
     * 获取外省发货单详情
     */
    @GET("FHD/GetOtherProviceFHDInfo")
    Call<FHDGetOtherProviceFHDInfo> fhdGetOtherProviceFHDInfo(@Query("token") String token, @Query("id") int id);
    /**
     * 终端客户
     */
    @GET("dealerChildOrder/GetEndCustomerList")
    Call<DCOGetEndCustomerList> dcoGetEndCustomerList(@Query("token") String token, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 销售查询
     */
    @GET("Storage/GetSaleList")
    Call<StorageGetSaleList> storageGetSaleList(@Query("token") String token, @Query("key") String key, @Query("barcode") String barcode, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 条码流转
     */
    @GET("DealerChildOrder/GetProductCirculationStatistics")
    Call<DCOGetProductCirculationStatistics> dcoGetProductCirculationStatistics(@Query("token") String token, @Query("key") String key, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 获取经销商物流公司
     */
    @GET("FHD/GetLogisticsList")
    Call<FHDGetLogisticsList> fhdGetLogisticsList(@Query("token") String token);
    /**
     * 删除物流公司
     */
    @GET("FHD/DeleteLogistics")
    Call<FHDDeleteLogistics> fhdDeleteLogistics(@Query("token") String token, @Query("id") int id);
    /**
     * 添加物流公司
     */
    @GET("FHD/AddLogistics")
    Call<FHDDeleteLogistics> fhdAddLogistics(@Query("token") String token, @Query("name") String name, @Query("remark") String remark);
    /**
     * 发货单物流信息列表
     */
    @GET("FHD/GetInvoiceLogisticsList")
    Call<FHDGetInvoiceLogisticsList> fhdGetInvoiceLogisticsList(@Query("token") String token, @Query("invoiceId") int invoiceId);
    /**
     * 查询快递接口
     */
    @GET("FHD/GetLogisticsInfo")
    Call<FHDGetLogisticsInfo> fhdGetLogisticsInfo(@Query("token") String token, @Query("id") int id);
    /**
     * 获取经销商物流公司(下拉使用)
     */
    @GET("FHD/GetCboLogistics")
    Call<FHDGetCboLogistics> fhdGetCboLogistics(@Query("token") String token);
    /**
     * 发货单添加物流单号
     */
    @GET("FHD/AddInvoiceLogisticsInfo")
    Call<FHDDeleteLogistics> fhdAddInvoiceLogisticsInfo(@Query("token") String token, @Query("invoiceId") int invoiceId, @Query("logisticsId") int logisticsId, @Query("number") String number, @Query("remark") String remark);
    /**
     * 获取终端发货单
     */
    @GET("CusInvoice/GetCusInvoiceList")
    Call<CusInvoiceGetCusInvoiceList> cusInvoiceGetCusInvoiceList(@Query("token") String token, @Query("page") int page, @Query("pagesize") int pagesize);
    /**
     * 点击添加终端发货单
     */
    @GET("CusInvoice/InitAddGetInfo")
    Call<CusInvoiceInitAddGetInfo> cusInvoiceInitAddGetInfo(@Query("token") String token);
    /**
     * 点击编辑终端发货单
     */
    @GET("CusInvoice/GetInfo")
    Call<CusInvoiceGetInfo> cusInvoiceGetInfo(@Query("token") String token, @Query("id") int id);
    /**
     * 终端录入（终端客户）--添加/编辑
     */
    @GET("DealerChildOrder/GetEndCustomerInfo")
    Call<GetEndCustomerInfo> getEndCustomerInfo(@Query("token") String token, @Query("id") int id);
    /**
     * 终端录入（终端客户）--添加/编辑
     */
    @GET("DealerChildOrder/GetParentDealerForEndCustomer")
    Call<GetParentDealerForEndCustomer> getParentDealerForEndCustomer(@Query("token") String token);
    /**
     * 终端录入（终端客户）--添加/编辑-确定
     */
    @GET("DealerChildOrder/AddEndCustomer")
    Call<CloseOrder> addEndCustomer(@Query("token") String token,
                                    @Query("Id") int Id,
                                    @Query("Name") String Name,
                                    @Query("contacts") String contacts,
                                    @Query("contactphone") String contactphone,
                                    @Query("address") String address,
                                    @Query("dealerId") int dealerId,
                                    @Query("x") String x,
                                    @Query("y") String y);

}
