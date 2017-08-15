package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.CartSQLBean;
import com.bbld.warehouse.bean.CodeJson;
import com.bbld.warehouse.bean.SaleScanCode;
import com.bbld.warehouse.db.UserDataBaseOperate;
import com.bbld.warehouse.db.UserSQLiteOpenHelper;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.scancodenew_xsck.scan.CaptureActivity;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.utils.UploadUserInformationByPostService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

/**
 * 销售出库
 * Created by likey on 2017/8/15.
 */

public class XSCKActivity extends BaseActivity{
    @BindView(R.id.btnToScan)
    Button btnToScan;
    @BindView(R.id.lvXSCK)
    ListView lvXSCK;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.et_linkName)
    EditText etLinkName;
    @BindView(R.id.et_linkPhone)
    EditText etLinkPhone;
    @BindView(R.id.et_remark)
    EditText etRemark;

    private String token;
    private UserSQLiteOpenHelper mUserSQLiteOpenHelper;
    private UserDataBaseOperate mUserDataBaseOperate;
    private List<CartSQLBean> sqlBeanList;
    private XSCKScanAdapter adapter;
    private Dialog loadDialog;
    private String request;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    WeiboDialogUtils.closeDialog(loadDialog);
                    showToast(""+request);
                    //出库成功清空数据库，释放当前acticity
                    mUserDataBaseOperate.deleteAll();
                    ActivityManagerUtil.getInstance().finishActivity(XSCKActivity.this);
                    break;
                case 222:
                    WeiboDialogUtils.closeDialog(loadDialog);
                    showToast(""+request);
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        //数据库
        mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(XSCKActivity.this);
        mUserDataBaseOperate = new UserDataBaseOperate(mUserSQLiteOpenHelper.getWritableDatabase());
        setListeners();
    }

    private void setListeners() {
        btnToScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(CaptureActivity.class);
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBackDialog();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialog= WeiboDialogUtils.createLoadingDialog(XSCKActivity.this,getString(R.string.caozuo_ing));
                List<CartSQLBean> sqlProducts = mUserDataBaseOperate.findAll();
                List<CodeJson.CodeJsonList> A = new ArrayList<CodeJson.CodeJsonList>();
                CodeJson B=new CodeJson();
                for (int j=0;j<sqlProducts.size();j++){
                    Gson gson=new Gson();
                    String AString=gson.toJson(A);
                    if (!(AString.contains(sqlProducts.get(j).getProductId()))){
                        CodeJson.CodeJsonList a = new CodeJson.CodeJsonList();
                        a.setProductID(Integer.parseInt(sqlProducts.get(j).getProductId()));
                        a.setCodeList(new LinkedList<CodeJson.CodeJsonList.CodeJsonCodeList>());
                        A.add(a);
                    }
                }
                for(int q=0;q<sqlProducts.size();q++){
                    for (int k=0;k<A.size();k++){
                        if (sqlProducts.get(q).getProductId().toString().equals(A.get(k).getProductID()+"")){
                            CodeJson.CodeJsonList.CodeJsonCodeList x=new CodeJson.CodeJsonList.CodeJsonCodeList();
                            x.setCode(sqlProducts.get(q).getProductCode()+"");
                            A.get(k).getCodeList().add(x);
                            B.setList(A);
                        }
                    }
                }
                Gson gson=new Gson();
                String jsonString=gson.toJson(B);
//                showToast(jsonString);
                //需要参数：token,invoiceid(orderId),codejson
                final String codejson = jsonString;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            request= UploadUserInformationByPostService.saveStorage(token+""
                                    ,"1",
                                    ""+"8",
                                    "0",
                                    ""+etLinkName.getText(),
                                    ""+etLinkPhone.getText(),
                                    ""+etRemark.getText(),
                                    codejson);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (request.contains("成功")) { // 请求成功
                            Message message=new Message();
                            message.what=111;
                            handler.sendMessage(message);
                        } else { // 请求失败
                            Message message=new Message();
                            message.what=222;
                            handler.sendMessage(message);
                        }
                    }
                }).start();
            }
        });
    }

    private void setAdapter(){
        sqlBeanList = mUserDataBaseOperate.findAll();
        List<String> ids=new ArrayList<String>();
        List<CartSQLBean> afters=new ArrayList<CartSQLBean>();
        for (int s=0;s<sqlBeanList.size();s++){
            if (!ids.contains(sqlBeanList.get(s).getProductId())){
                ids.add(sqlBeanList.get(s).getProductId());
                CartSQLBean sqlBean=new CartSQLBean();
                sqlBean.setProductId(sqlBeanList.get(s).getProductId());
                sqlBean.setProductType(sqlBeanList.get(s).getProductType());
                sqlBean.setSerialNumber(sqlBeanList.get(s).getSerialNumber());
                sqlBean.setBatchNumber(sqlBeanList.get(s).getBatchNumber());
                sqlBean.setProductCode("");
                sqlBean.setProCount(0);
                afters.add(sqlBean);
            }
        }
        for (int s=0;s<sqlBeanList.size();s++){
            for (int a=0;a<afters.size();a++){
                if (sqlBeanList.get(s).getProductId().equals(afters.get(a).getProductId())){
                    afters.get(a).setProCount(afters.get(a).getProCount()+sqlBeanList.get(s).getProCount());
                }
            }
        }
        Collections.reverse(afters);
        adapter=new XSCKScanAdapter(afters);
        lvXSCK.setAdapter(adapter);
    }

    class XSCKScanAdapter extends BaseAdapter {
        private List<CartSQLBean> afters;

        public XSCKScanAdapter(List<CartSQLBean> afters) {
            super();
            this.afters=afters;
        }

        @Override
        public int getCount() {
            return afters.size();
        }

        @Override
        public CartSQLBean getItem(int i) {
            return afters.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            XSCKHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_xsck_scan,null);
                holder=new XSCKHolder();
                holder.ivProductImg=(ImageView)view.findViewById(R.id.ivProductImg);
                holder.ivDel=(ImageView)view.findViewById(R.id.ivDel);
                holder.tvProductName=(TextView) view.findViewById(R.id.tvProductName);
                holder.tvProductSpec=(TextView) view.findViewById(R.id.tvProductSpec);
                holder.btnInfo=(Button) view.findViewById(R.id.btnInfo);
                view.setTag(holder);
            }
            final CartSQLBean item = getItem(i);
            holder= (XSCKHolder) view.getTag();
            Glide.with(getApplicationContext()).load(item.getSerialNumber()).error(R.mipmap.xiuzhneg).into(holder.ivProductImg);
            holder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDelDialog(item.getProductId(),item.getBatchNumber());
                }
            });
            holder.tvProductName.setText(item.getBatchNumber()+"");
            holder.tvProductSpec.setText(item.getProCount()+"盒");
            holder.btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(XSCKActivity.this,"明细"+item.getProductId(),Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

        class XSCKHolder{
            ImageView ivProductImg,ivDel;
            TextView tvProductName,tvProductSpec;
            Button btnInfo;
        }
    }
    private void showDelDialog(final String id, String name) {
        AlertDialog.Builder builder=new AlertDialog.Builder(XSCKActivity.this);
        builder.setMessage("是否删除("+name+")");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //连续扫码
                mUserDataBaseOperate.deleteUserById(id);
                setAdapter();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //连续扫码
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
    private void showBackDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(XSCKActivity.this);
        builder.setMessage("退出将清空已扫的产品");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mUserDataBaseOperate.deleteAll();
                dialogInterface.dismiss();
                ActivityManagerUtil.getInstance().finishActivity(XSCKActivity.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            showBackDialog();
        }
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setAdapter();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_xsck;
    }
}
