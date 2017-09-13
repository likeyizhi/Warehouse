package com.bbld.warehouse.utils;

import android.util.Log;
import com.bbld.warehouse.base.Constants;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class UploadUserInformationByPostService {
    public static String save(String token, String invoiceid, String codejson) throws Exception{
//        String path = Constants.BASE_URL + "Order/OrderSend?token="+token+"&invoiceid="+orderId+"&codejson="+codejson;
        String path = Constants.BASE_URL + "Order/OrderSend";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("invoiceid", invoiceid);
        params.put("codejson", codejson);
        Log.i("save", "save="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    public static String saveNew(String token, String invoiceid, String codejson, String unique) throws Exception{
        String path = Constants.BASE_URL + "Order/OrderSendNew";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("invoiceid", invoiceid);
        params.put("codejson", codejson);
        params.put("unique", unique);
        Log.i("saveNew", "saveNew="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    public static String saveStorage(String token, String type, String typeId, String number, String linkName, String linkPhone, String remark, String codejson) throws Exception{
//        String path = Constants.BASE_URL + "Storage/CommitStorage?token="+token+
//                "&type="+type+
//                "&typeId="+typeId+
//                "&number="+number+
//                "&linkName="+linkName+
//                "&linkPhone="+linkPhone+
//                "&remark="+remark+
//                "&codejson="+codejson;
        String path = Constants.BASE_URL + "Storage/CommitStorage";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("type", type);
        params.put("typeId", typeId);
        params.put("number", number);
        params.put("linkName", linkName);
        params.put("linkPhone", linkPhone);
        params.put("remark", remark);
        params.put("codejson", codejson);
        Log.i("saveStorage", "saveStorage="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    public static String saveStocking(String token,String number, String remark, String codejson) throws Exception{
//        String path = Constants.BASE_URL + "Storage/CommitInventory?token="+token+
//                "&number="+number+
//                "&remark="+remark+
//                "&codejson="+codejson;
        String path = Constants.BASE_URL + "Storage/CommitInventory";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("number", number);
        params.put("remark", remark);
        params.put("codejson", codejson);
        Log.i("saveStocking", "saveStocking="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    public static String commitHandOver(String token,String handoverId, String codejson) throws Exception{
//        String path = Constants.BASE_URL + "Storage/CommitHandOver?token="+token+
//                "&handoverId="+handoverId+
//                "&codejson="+codejson;
        String path = Constants.BASE_URL + "Storage/CommitHandOver";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("handoverId", handoverId);
        params.put("codejson", codejson);
        Log.i("commitHandOver", "commitHandOver="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    public static String orderReceipt(String token,String invoiceid, String codejson) throws Exception{
//        String path = Constants.BASE_URL + "Order/OrderReceipt?token="+token+
//                "&invoiceid="+invoiceid+
//                "&codejson="+codejson;
        String path = Constants.BASE_URL + "Order/OrderReceipt";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("invoiceid", invoiceid);
        params.put("codejson", codejson);
        Log.i("orderReceipt", "orderReceipt="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    /**订单扫码收货*/
    public static String orderScanCodeDelivery(String token,String invoiceid, String codejson, String unique) throws Exception{
        String path = Constants.BASE_URL + "Order/OrderScanCodeDelivery";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("invoiceid", invoiceid);
        params.put("codejson", codejson);
        params.put("unique", unique);
        Log.i("OrderScanCodeDelivery", "OrderScanCodeDelivery="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    /**其他出库*/
    public static String commitOutStorage(String token,String storageId, String codejson) throws Exception{
//        String path = Constants.BASE_URL + "Storage/CommitOutStorage?token="+token+
//                "&storageId="+storageId+
//                "&codejson="+codejson;
        String path = Constants.BASE_URL + "Storage/CommitOutStorage";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("storageId", storageId);
        params.put("codejson", codejson);
        Log.i("commitOutStorage", "commitOutStorage="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    /**其他入库*/
    public static String commitInStorage(String token,String storageId, String codejson) throws Exception{
//        String path = Constants.BASE_URL + "Storage/CommitInStorage?token="+token+
//                "&storageId="+storageId+
//                "&codejson="+codejson;
        String path = Constants.BASE_URL + "Storage/CommitInStorage";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("storageId", storageId);
        params.put("codejson", codejson);
        Log.i("commitInStorage", "commitInStorage="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    //http://mapi.xiuzheng.cc:83/Api/Storage/CusInvoiceSend?token=a81d296a00c7465f8f391d23c4a38bf2&codejson=
    public static String cusInvoiceSend(String token, String codejson, String customerInvoiceId) throws Exception{
//        String path = Constants.BASE_URL + "Storage/CusInvoiceSend?token="+token+
//                "&CustomerInvoiceId="+customerInvoiceId+
//                "&codejson="+codejson;
        String path = Constants.BASE_URL + "Storage/CusInvoiceSend";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("CustomerInvoiceId", customerInvoiceId);
        params.put("codejson", codejson);
        Log.i("cusInvoiceSend", "cusInvoiceSend="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    //http://mapi.xiuzheng.cc:83/Api/Storage/CusInvoiceConfirm?token=a81d296a00c7465f8f391d23c4a38bf2&CustomerInvoiceId=1
    public static String cusInvoiceConfirm(String token, String customerInvoiceId) throws Exception{
//        String path = Constants.BASE_URL + "Storage/CusInvoiceConfirm?token="+token+
//                "&CustomerInvoiceId="+customerInvoiceId;
        String path = Constants.BASE_URL + "Storage/CusInvoiceConfirm";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("CustomerInvoiceId", customerInvoiceId);
        Log.i("cusInvoiceConfirm", "cusInvoiceConfirm="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    /**退货单*/
    public static String commitRefund(String token, String RefundId, String codejson) throws Exception{
        String path = Constants.BASE_URL + "Storage/CommitRefund";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("RefundId", RefundId);
        params.put("codejson", codejson);
        Log.i("commitRefund", "commitRefund="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    /**人工收货*/
    public static String orderManualDelivery(String token, String invoiceid) throws Exception{
        String path = Constants.BASE_URL + "Order/OrderManualDelivery";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("invoiceid", invoiceid);
        Log.i("orderManualDelivery", "orderManualDelivery="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    /**退货单*/
    public static String refundCommitRefund(String token, String refundcode, String unique, String remark, String codejson) throws Exception{
        String path = Constants.BASE_URL + "Refund/CommitRefund";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("refundcode", refundcode);
        params.put("unique", unique);
        params.put("remark", remark);
        params.put("codejson", codejson);
        Log.i("refundCommitRefund", "refundCommitRefund="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    /**退货入库*/
    public static String refundReceiveRefund(String token, String refundId) throws Exception{
        String path = Constants.BASE_URL + "Refund/ReceiveRefund";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("refundId", refundId);
        Log.i("refundReceiveRefund", "refundReceiveRefund="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    /**还货出库*/
    public static String givebackCommitGiveback(String token, String id,String unique,String codejson) throws Exception{
        String path = Constants.BASE_URL + "Giveback/CommitGiveback";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("unique", unique);
        params.put("codejson", codejson);
        Log.i("givebackCommitGiveback", "givebackCommitGiveback="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    /**还货入库提交*/
    public static String givebackReceiveGiveback(String token, String id) throws Exception{
        String path = Constants.BASE_URL + "Giveback/ReceiveGiveback";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        Log.i("givebackReceiveGiveback", "givebackReceiveGiveback="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }
    /**库存盘点详情*/
    public static String storageFinishInventory(String token, String inventoryId) throws Exception{
        String path = Constants.BASE_URL + "Storage/FinishInventory";
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("inventoryId", inventoryId);
        Log.i("storageFinishInventory", "storageFinishInventory="+params);
        return sendPOSTRequest(path, params, "UTF-8");
    }

    /**
     * 发送POST请求
     * @param path 请求路径
     * @param params 请求参数
     * @return
     */
    private static String sendPOSTRequest(String path, Map<String, String> params, String encoding) throws Exception{
        //  title=liming&length=30
        StringBuilder sb = new StringBuilder();
        if(params!=null && !params.isEmpty()){
            for(Map.Entry<String, String> entry : params.entrySet()){
                sb.append(entry.getKey()).append("=");
                sb.append(URLEncoder.encode(entry.getValue(), encoding));
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        byte[] data = sb.toString().getBytes();

        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(90*1000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);//允许对外传输数据
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", data.length+"");
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();
        if(conn.getResponseCode() == 200){
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sbb = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null){
                sbb.append(line);
            }
            String jsonText = sbb.toString();
            //jsonText :  {result:ok}  {result:error, msg:xxx}
            JSONObject obj = new JSONObject(jsonText);
            Log.i("obj", "obj="+obj);
            String ress=obj.getString("mes");
            int status=obj.getInt("status");
            Log.i("ress", "ress="+ress);
            Log.i("status", "status="+status);
            if (status==0){
                return "操作成功";
            }else{
                return ress;
            }
        }
        Log.i("ResponseCode","ResponseCode="+conn.getResponseCode());
        return "发生错误,操作失败";
    }
}
