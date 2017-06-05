package com.bbld.warehouse.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
    public static String save(String token, String orderId, String codejson) throws Exception{
        String path = Constants.BASE_URL + "Order/OrderSend?token="+token+"&invoiceid="+orderId+"&codejson="+codejson;
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("orderId", orderId);
        params.put("codejson", codejson);
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
        conn.setConnectTimeout(5000);
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
                return "出库成功";
            }else{
                return ress;
            }
        }

        return "出库失败";
    }
}
