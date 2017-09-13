package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.CartSQLBean;
import com.bbld.warehouse.bean.RemoveScanCode;
import com.bbld.warehouse.bean.ScanCode;
import com.bbld.warehouse.db.UserDataBaseOperate;
import com.bbld.warehouse.db.UserSQLiteOpenHelper;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;

import java.util.Collections;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * iData终端扫码
 * Created by likey on 2017/8/10.
 */

public class IDataScanStockActivity extends BaseActivity{
    private UserSQLiteOpenHelper mUserSQLiteOpenHelper;
    private UserDataBaseOperate mUserDataBaseOperate;
    private TextView tv_needCount;
    private TextView tvBatchNumber;
    private int isNeedBatch;
    public static String batchNumber="";
    private String productId;
    private String productName;
    private String storage;
    private String invoiceid;
    private String needCount;
    private String type;
    private ListView lvScan;
    private Button btnComplete;
    private int scanCount;
    private TextView tv_scanCount;
    private Call<ScanCode> call;
    private TextView tvInput;
    private EditText etScan;
    private ImageButton ibBack;
    private String uuid;

    @Override
    protected void initViewsAndEvents() {
        doMyThings();
    }

    private void doMyThings() {
        //数据库
        mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(IDataScanStockActivity.this);
        mUserDataBaseOperate = new UserDataBaseOperate(mUserSQLiteOpenHelper.getWritableDatabase());

        etScan=(EditText)findViewById(R.id.etScan);
        etScan.addTextChangedListener(watcher);
        tv_needCount=(TextView)findViewById(R.id.tv_needCount);

        tvBatchNumber=(TextView)findViewById(R.id.tvBatchNumber);
        Intent intent=getIntent();
        uuid=intent.getExtras().getString("uuid");
        isNeedBatch=intent.getExtras().getInt("NeedBatch", 0);
        if (isNeedBatch==1){
            showBatchDialog();
        }else{
            batchNumber="";
        }
        productId=intent.getExtras().getString("productId");
        productName=intent.getExtras().getString("productName");
        storage=intent.getExtras().getString("storage");
        if (storage.equals("no")) {
            invoiceid=intent.getExtras().getString("orderId");
            needCount=intent.getExtras().getString("needCount");
            type=intent.getExtras().getString("type");
        }else{
            if (intent.getExtras().getString("other").equals("yes")){
                type=intent.getExtras().getString("type");
                needCount=intent.getExtras().getString("needCount");
            }else{
                type=intent.getExtras().getString("type");
                needCount=10000+"";
                tv_needCount.setVisibility(View.INVISIBLE);
            }
        }
        TextView tvProductName = (TextView) findViewById(R.id.tv_productName);
        tvProductName.setText(productName+"");
        lvScan=(ListView)findViewById(R.id.lv_scan);
        List<CartSQLBean> products=mUserDataBaseOperate.findUserById(productId+"");
        Collections.reverse(products);
        lvScan.setAdapter(new ScanAdapter(products));
        btnComplete=(Button)findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<CartSQLBean> products=mUserDataBaseOperate.findUserById(productId+"");
                scanCount=0;
                for (int i=0;i<products.size();i++){
                    scanCount=scanCount+products.get(i).getProCount();
                }
                if (Integer.parseInt(needCount+"")<Integer.parseInt(scanCount+"")){
                    showBiggerDialog("扫码数量不能大于发货数量");
                }else {
                    finish();
                }
            }
        });

        tv_needCount.setText(needCount+"(盒)");
        tv_scanCount=(TextView)findViewById(R.id.tv_scanCount);
        scanCount=0;
        for (int i=0;i<products.size();i++){
            scanCount=scanCount+products.get(i).getProCount();
        }
        tv_scanCount.setText(scanCount+"(盒)");

        tvInput=(TextView)findViewById(R.id.tvInput);
        tvInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //手动输入条形码
                showInputDialog();
            }
        });
        ibBack=(ImageButton)findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length()!=0){
                scanCount=0;
                List<CartSQLBean> products=mUserDataBaseOperate.findUserById(productId+"");
                for (int a=0;a<products.size();a++){
                    scanCount=scanCount+products.get(a).getProCount();
                }
                if (Integer.parseInt(needCount+"")<=Integer.parseInt(scanCount+"")){
//                Toast.makeText(CaptureActivity.this,"商品已扫完",Toast.LENGTH_SHORT).show();
                    showOKDialog();
                    etScan.setText("");
                }else{
                    getScanCode(editable+"");
                    etScan.setText("");
                }
            }else{

            }
        }
    };

    /**
     * 扫码下部分的listview的adapter
     */
    class ScanAdapter extends BaseAdapter {
        private List<CartSQLBean> products;
        public ScanAdapter(List<CartSQLBean> products){
            super();
            this.products=products;
        }
        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public CartSQLBean getItem(int i) {
            return products.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(products.get(i).getProductId());
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ScanHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_idata,null);
                holder=new ScanHolder();
                holder.tv_code=(TextView)view.findViewById(R.id.tv_code);
                holder.tv_type=(TextView)view.findViewById(R.id.tv_type);
                holder.tv_count=(TextView)view.findViewById(R.id.tv_count);
                holder.tv_serialNumber=(TextView)view.findViewById(R.id.tv_serialNumber);
                holder.iv_del=(ImageView) view.findViewById(R.id.iv_del);
                holder.rl_serialNumber=(RelativeLayout) view.findViewById(R.id.rl_serialNumber);
                view.setTag(holder);
            }
            holder= (ScanHolder) view.getTag();
            final CartSQLBean product = getItem(i);
            holder.tv_code.setText(product.getProductCode()+"  ");
            holder.tv_type.setText(getType(product.getProductType())+"");
            holder.tv_count.setText("【"+product.getProCount()+"盒】");
            holder.tv_serialNumber.setVisibility(View.VISIBLE);
            if (product.getSerialNumber().trim().equals("")){
                holder.tv_serialNumber.setText("请输入序列号");
            }else{
                holder.tv_serialNumber.setText(product.getSerialNumber()+"");
            }
            holder.tv_serialNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSerialNumberDialog(product.getProductCode(),product.getSerialNumber());
                }
            });
            holder.iv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDelDialog(product.getProductCode()+"");
                }
            });
            return view;
        }

        private String getType(String productType) {
            //type=1=箱码;type=2=盒码
            if (productType.equals("1")){
                return "【箱码】";
            }else{
                return "【盒码】";
            }
        }

        class ScanHolder{
            TextView tv_code;
            TextView tv_type;
            TextView tv_count;
            TextView tv_serialNumber;
            ImageView iv_del;
            RelativeLayout rl_serialNumber;
        }
    }

    private void showDelDialog(final String code) {

        AlertDialog.Builder builder=new AlertDialog.Builder(IDataScanStockActivity.this);
        builder.setMessage("删除("+code+")?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (type.equals("1") || type.equals("2")){
                    delToNet(code);
                }else{
                    mUserDataBaseOperate.deleteUserByCode(code);
                    List<CartSQLBean> products=mUserDataBaseOperate.findUserById(productId+"");
                    Collections.reverse(products);
                    lvScan.setAdapter(new ScanAdapter(products));
                    scanCount=0;
                    for (int a=0;a<products.size();a++){
                        scanCount=scanCount+products.get(a).getProCount();
                    }
                    tv_scanCount.setText(scanCount+"(盒)");
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //连续扫码
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void delToNet(final String code) {
        Call<RemoveScanCode> removeCall=RetrofitService.getInstance().removeScanCodeRefund(new MyToken(IDataScanStockActivity.this).getToken(),code,uuid);
        removeCall.enqueue(new Callback<RemoveScanCode>() {
            @Override
            public void onResponse(Response<RemoveScanCode> response, Retrofit retrofit) {
                if (response==null){
                    return;
                }
                if (response.body().getStatus()==0){
                    mUserDataBaseOperate.deleteUserByCode(code);
                    List<CartSQLBean> products=mUserDataBaseOperate.findUserById(productId+"");
                    Collections.reverse(products);
                    lvScan.setAdapter(new ScanAdapter(products));
                    scanCount=0;
                    for (int a=0;a<products.size();a++){
                        scanCount=scanCount+products.get(a).getProCount();
                    }
                    tv_scanCount.setText(scanCount+"(盒)");
                }else{
                    Toast.makeText(IDataScanStockActivity.this,"操作失败,请重试",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void showSerialNumberDialog(final String productCode, final String serialNumber) {
        final EditText et = new EditText(this);
        et.setBackgroundResource(R.drawable.bg_batch);
        et.setText(serialNumber);
        et.setMaxLines(1);
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("请设置序列号")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getApplicationContext(), "还未设置序列号！" + input, Toast.LENGTH_LONG).show();
                        } else {
                            List<CartSQLBean> bean = mUserDataBaseOperate.findUserByName(productCode);
                            CartSQLBean one = bean.get(0);
                            one.setSerialNumber(input);
                            mUserDataBaseOperate.updateUser(one);
                            List<CartSQLBean> products = mUserDataBaseOperate.findAll();
                            Collections.reverse(products);
                            lvScan.setAdapter(new ScanAdapter(products));
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
        setDialogWindowAttr(serialDialog);
    }
    private void showInputDialog() {
        final EditText et = new EditText(this);
        et.setBackgroundResource(R.drawable.bg_batch);
        et.setMaxLines(1);
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("请输入条形码")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getApplicationContext(), "还未设置序列号！" + input, Toast.LENGTH_LONG).show();
                        } else {
                            getScanCode(input);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
        setDialogWindowAttr(serialDialog);
    }
    private void showBiggerDialog(String biggerMessage) {
        AlertDialog.Builder builder=new AlertDialog.Builder(IDataScanStockActivity.this);
        builder.setMessage(biggerMessage+"");
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                finish();
//            }
//        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //连续扫码
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
    private void showBatchDialog() {
        final EditText et = new EditText(this);
        et.setBackgroundResource(R.drawable.bg_batch);
        et.setText(batchNumber);
        et.setMaxLines(1);
        AlertDialog batchDialog = new AlertDialog.Builder(this).setTitle("请设置批号")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getApplicationContext(), "还未设置批号！" + input, Toast.LENGTH_LONG).show();
                            showBatchDialog();
                        } else {
                            batchNumber = input;
                            tvBatchNumber.setText("批号:" + batchNumber);
                            tvBatchNumber.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
        setDialogWindowAttr(batchDialog);
    }
    private void showOKDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(IDataScanStockActivity.this);
        builder.setMessage("商品扫描完成");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
        builder.create().show();
    }
    private void showFailDialog(String failMessage) {
        AlertDialog.Builder builder=new AlertDialog.Builder(IDataScanStockActivity.this);
        builder.setMessage(failMessage+"");
//        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                finish();
//            }
//        });
        builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //连续扫码
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
    private void getScanCode(final String code) {
        List<CartSQLBean> sqlCodeFirst =mUserDataBaseOperate.findUserByName(code+"");
        if (sqlCodeFirst.isEmpty()||sqlCodeFirst.size()==0){
            call= RetrofitService.getInstance().storageInventoryScanCode(new MyToken(IDataScanStockActivity.this).getToken()+"",
                    productId,code,uuid);
            call.enqueue(new Callback<ScanCode>() {
                @Override
                public void onResponse(Response<ScanCode> response, Retrofit retrofit) {
                    if (response.body()==null){
                        Toast.makeText(IDataScanStockActivity.this,"服务器错误",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.body().getStatus()==0){
//                    Toast.makeText(CaptureActivity.this,invoiceid+","+productId+","+code+","+type,Toast.LENGTH_SHORT).show();
                        if (response.body().getInfo().getIsRight()==1){
                            //IsRight--条码是否正确，优先进行判断，如果是 0，则代表条码不存在，
                            //如果是 1，则代表条码存在
                            //成功，添加到数据库（productId,code,type,count）type=1=箱码;type=2=盒码
                            List<CartSQLBean> sqlCode =mUserDataBaseOperate.findUserByName(code+"");
                            if (sqlCode.isEmpty()){
                                CartSQLBean sqlBean=new CartSQLBean();
                                sqlBean.setProductId(productId+"");
                                sqlBean.setProductCode(code+"");
                                sqlBean.setProductType(response.body().getInfo().getType()+"");
                                sqlBean.setSerialNumber(response.body().getInfo().getSerialNumber()+"");
                                if (batchNumber.equals("") || batchNumber==null){
                                    sqlBean.setBatchNumber(response.body().getInfo().getBatchNumber()+"");
                                }else{
                                    sqlBean.setBatchNumber(batchNumber+"");
                                }
                                sqlBean.setProCount(response.body().getInfo().getCount());
                                mUserDataBaseOperate.insertToUser(sqlBean);
                                List<CartSQLBean> products=mUserDataBaseOperate.findUserById(productId+"");
                                Collections.reverse(products);
                                lvScan.setAdapter(new ScanAdapter(products));
                                scanCount=0;
                                for (int i=0;i<products.size();i++){
                                    scanCount=scanCount+products.get(i).getProCount();
                                }
                                tv_scanCount.setText(scanCount+"(盒)");
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }else{
//                            Toast.makeText(CaptureActivity.this,"该商品码已经扫过",Toast.LENGTH_SHORT).show();
                                showFailDialog("该商品码已经扫过");
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            //失败
//                        Toast.makeText(CaptureActivity.this,"不存在该商品,请重试",Toast.LENGTH_SHORT).show();
                            showFailDialog("不存在该商品,请重试");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
//                    Toast.makeText(CaptureActivity.this,""+response.body().getMes(),Toast.LENGTH_SHORT).show();
                        showFailDialog(""+response.body().getMes());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Toast.makeText(IDataScanStockActivity.this,""+throwable,Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            showFailDialog("该商品码已经扫过");
        }
    }
    //在dialog.show()之后调用
    public void setDialogWindowAttr(Dialog dlg){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = 4*(width/5);//宽高可设置具体大小
        lp.height = 3*(height/7);
        dlg.getWindow().setAttributes(lp);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_idata_scan;
    }
}
