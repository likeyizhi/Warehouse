package com.bbld.warehouse.network;

import com.bbld.warehouse.base.Constants;
import com.bbld.warehouse.bean.AddOrderLogisticsInfo;
import com.bbld.warehouse.bean.CancelInventory;
import com.bbld.warehouse.bean.ClearScanCode;
import com.bbld.warehouse.bean.CloseOrder;
import com.bbld.warehouse.bean.CusInvoiceConfirm;
import com.bbld.warehouse.bean.CusInvoiceInfo;
import com.bbld.warehouse.bean.CusInvoiceReceiptList;
import com.bbld.warehouse.bean.CusInvoiceSendList;
import com.bbld.warehouse.bean.FinishInventory;
import com.bbld.warehouse.bean.GetLogisticsList;
import com.bbld.warehouse.bean.GetLogisticsTrackInfo;
import com.bbld.warehouse.bean.GetNewNumber;
import com.bbld.warehouse.bean.GetOrderLogisticsInfo;
import com.bbld.warehouse.bean.GetOrderTbList;
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
import com.bbld.warehouse.bean.StorageList;
import com.bbld.warehouse.bean.TbGetOrderInfo;
import com.bbld.warehouse.bean.VersionAndroid;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

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
     * 自动升级
     */
    public Call<VersionAndroid> getVersionAndroid(){
        return retrofitInterface.getVersionAndroid();
    }
    /**
     * 库管登录
     * 登录
     */
    public Call<Login> login(String acc, String pwd){
        return retrofitInterface.login(acc, pwd);
    }
    /**
     * 经销商登录
     * 登录
     */
    public Call<Login> dealerLogin(String acc, String pwd){
        return retrofitInterface.dealerLogin(acc, pwd);
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
     * 扫码查询接口(new)
     */
    public Call<ScanCode> scanCodeNew(String token, int invoiceid, int productId, String  code, int type, String unique){
        return retrofitInterface.scanCodeNew(token, invoiceid, productId, code, type, unique);
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
    /**
     * 其他入库
     */
    public Call<QTInStorageList> getQTInStorageList(String token, int page, int pagesize){
        return retrofitInterface.getQTInStorageList(token, page, pagesize);
    }
    /**
     * 库存盘点详情
     */
    public Call<ProductCountDetails> getProductCountDetails(String token, String productId){
        return retrofitInterface.getProductCountDetails(token, productId);
    }
    /**
     * 终端配送
     */
    public Call<CusInvoiceSendList> getCusInvoiceSendList(String token){
        return retrofitInterface.getCusInvoiceSendList(token);
    }
    /**
     * 终端配送详情
     */
    public Call<CusInvoiceInfo> getCusInvoiceInfo(String token, String CustomerInvoiceId){
        return retrofitInterface.getCusInvoiceInfo(token, CustomerInvoiceId);
    }
    /**
     * 到货确认
     */
    public Call<CusInvoiceReceiptList> getCusInvoiceReceiptList(String token){
        return retrofitInterface.getCusInvoiceReceiptList(token);
    }
    /**
     * 到货确认详情-到货确认
     */
    public Call<CusInvoiceConfirm> cusInvoiceConfirm(String token, String CustomerInvoiceId){
        return retrofitInterface.cusInvoiceConfirm(token,CustomerInvoiceId);
    }
    /**
     * 销售出库-扫码
     */
    public Call<SaleScanCode> saleScanCode(String token, String code){
        return retrofitInterface.saleScanCode(token,code);
    }
    /**
     * 退货单-扫码
     */
    public Call<ScanCodeRefund> scanCodeRefund(String token, String code, String refundId, String productId){
        return retrofitInterface.scanCodeRefund(token,code,refundId,productId);
    }
    /**
     *
     */
    public Call<StorageCodeList> storageCodeList(String token, String type, String storageId){
        return retrofitInterface.storageCodeList(token,type,storageId);
    }
    /**
     * 销售统计
     */
    public Call<SaleStatistics> saleStatistics(String token, String start, String end){
        return retrofitInterface.saleStatistics(token,start,end);
    }
    /**
     * 退货单
     */
    public Call<RefundList> getRefundList(String token, int page, int pagesize){
        return retrofitInterface.getRefundList(token,page,pagesize);
    }
    /**
     * 退货单-详情
     */
    public Call<RefundDetail> getRefundDetail(String token, String id){
        return retrofitInterface.getRefundDetail(token,id);
    }
    /**
     * 删除条码
     */
    public Call<RemoveScanCode> removeScanCode(String token, String type, String invoiceId, String productId, String code, String unique){
        return retrofitInterface.removeScanCode(token,type,invoiceId,productId,code,unique);
    }
    /**
     * 删除条码(退货)
     */
    public Call<RemoveScanCode> removeScanCodeRefund(String token, String code, String unique){
        return retrofitInterface.removeScanCodeRefund(token,code,unique);
    }
    /**
     * 清空已扫条码
     */
    public Call<ClearScanCode> clearScanCode(String token, String type, String invoiceId, String unique){
        return retrofitInterface.clearScanCode(token,type,invoiceId,unique);
    }
    /**
     * 清空已扫条码-退货
     */
    public Call<ClearScanCode> clearScanCodeRefund(String token, String unique){
        return retrofitInterface.clearScanCodeRefund(token,unique);
    }
    /**
     * 人工收货
     */
    public Call<OrderManualDelivery> orderManualDelivery(String token, String invoiceId){
        return retrofitInterface.orderManualDelivery(token,invoiceId);
    }
    /**
     * 重置网络数据
     */
    public Call<RemoveCommitScanCode> removeCommitScanCode(String token, String unique){
        return retrofitInterface.removeCommitScanCode(token,unique);
    }
    /**
     * 退货扫码
     */
    public Call<ScanCode> refundScanCode(String token, String unique, String productId, String code){
        return retrofitInterface.refundScanCode(token,unique,productId,code);
    }
    /**
     * 退货申请列表
     */
    public Call<RefundGetRefundList> refundGetRefundList(String token, int page, int pagesize){
        return retrofitInterface.refundGetRefundList(token,page,pagesize);
    }
    /**
     * 退货申请-详情
     */
    public Call<RefundGetRefundDetail> refundGetRefundDetail(String token, String id){
        return retrofitInterface.refundGetRefundDetail(token,id);
    }
    /**
     * 退货申请-详情-明细
     */
    public Call<RefundGetRefundProductScanCode> refundGetRefundProductScanCode(String token, String id, String productId){
        return retrofitInterface.refundGetRefundProductScanCode(token,id,productId);
    }
    /**
     * 退货申请-详情-明细
     */
    public Call<RefundGetRefundProductScanCode> givebackGetGivebackScanCode(String token, String id, String productId){
        return retrofitInterface.givebackGetGivebackScanCode(token,id,productId);
    }
    /**
     * 退货入库列表
     */
    public Call<RefundGetHQRefundList> refundGetHQRefundList(String token, int page, int pagesize){
        return retrofitInterface.refundGetHQRefundList(token,page,pagesize);
    }
    /**
     * 退货入库列表
     */
    public Call<GivebackGetGivebackForInList> givebackGetGivebackForInList(String token, int page, int pagesize){
        return retrofitInterface.givebackGetGivebackForInList(token,page,pagesize);
    }
    /**
     * 退货入库-扫码
     */
    public Call<ReciveScanCode> reciveScanCode(String token, String refundId, String productId, String code){
        return retrofitInterface.reciveScanCode(token, refundId, productId, code);
    }
    /**
     * 还货出库
     */
    public Call<GivebackGetGivebackForOutList> givebackGetGivebackForOutList(String token, int page, int size){
        return retrofitInterface.givebackGetGivebackForOutList(token, page, size);
    }
    /**
     * 还货出库-详情
     */
    public Call<GivebackGetGivebackDetail> givebackGetGivebackDetail(String token, String id){
        return retrofitInterface.givebackGetGivebackDetail(token,id);
    }
    /**
     * 还货出库-详情
     */
    public Call<GivebackReciveScanCode> givebackReciveScanCode(String token, String id, String productId,String code){
        return retrofitInterface.givebackReciveScanCode(token, id, productId, code);
    }
    /**
     * 盘点单-扫码
     */
    public Call<ScanCode> storageInventoryScanCode(String token, String productId,String code, String unique){
        return retrofitInterface.storageInventoryScanCode(token, productId, code, unique);
    }
    /**
     * 订单提报列表
     */
    public Call<GetOrderTbList> getOrderTbList(String token, int page, int size){
        return retrofitInterface.getOrderTbList(token, page, size);
    }
    /**
     * 关闭订单
     */
    public Call<CloseOrder> closeOrder(String token, String id, String remark){
        return retrofitInterface.closeOrder(token, id, remark);
    }
    /**
     * 获取订单详情
     */
    public Call<TbGetOrderInfo> tbGetOrderInfo(String token, String id){
        return retrofitInterface.tbGetOrderInfo(token, id);
    }

}
