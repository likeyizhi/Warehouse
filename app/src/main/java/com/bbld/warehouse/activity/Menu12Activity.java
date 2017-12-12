package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.adapter.GroupAdapter;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.IndexInfo;
import com.bbld.warehouse.bean.VersionAndroid;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.update.UpdateService;
import com.bbld.warehouse.utils.MyToken;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;
import com.wuxiaolong.androidutils.library.VersionUtil;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 总部 1，经销商 2
 * Created by likey on 2017/12/4.
 */

public class Menu12Activity extends BaseActivity{
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.warehouseName)
    TextView tvWarehouseName;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.llToDDTB)
    LinearLayout llToDDTB;
    @BindView(R.id.llToSCFHD)
    LinearLayout llToSCFHD;
    @BindView(R.id.llToHXSB)
    LinearLayout llToHXSB;
    @BindView(R.id.llToTHD)
    LinearLayout llToTHD;
    @BindView(R.id.llToDDFH)
    LinearLayout llToDDFH;
    @BindView(R.id.llToZDFH)
    LinearLayout llToZDFH;
    @BindView(R.id.llToOthers)
    LinearLayout llToOthers;
    @BindView(R.id.llToHHCK)
    LinearLayout llToHHCK;
    @BindView(R.id.llToGetOrder)
    LinearLayout llToGetOrder;
    @BindView(R.id.llToQTRK)
    LinearLayout llToQTRK;
    @BindView(R.id.llToTHRK)
    LinearLayout llToTHRK;
    @BindView(R.id.llToHHRK)
    LinearLayout llToHHRK;
    @BindView(R.id.llToQuery)
    LinearLayout llToQuery;
    @BindView(R.id.llToStocking)
    LinearLayout llToStocking;
    @BindView(R.id.llToWLGS)
    LinearLayout llToWLGS;
    @BindView(R.id.llToZDLR)
    LinearLayout llToZDLR;
    @BindView(R.id.llToTMLZ)
    LinearLayout llToTMLZ;
    @BindView(R.id.llZb)
    LinearLayout llZb;
    @BindView(R.id.llFzb)
    LinearLayout llFzb;
    @BindView(R.id.llToDDFH_zb)
    LinearLayout llToDDFH_zb;
    @BindView(R.id.tvDDFH)
    TextView tvDDFH;
    @BindView(R.id.tvDSH)
    TextView tvDSH;
    @BindView(R.id.tvXJDD)
    TextView tvXJDD;

    private String name;
    private String dealerName;
    private String warehouseName;
    private int type;
    private int ishandover;
    private View view;
    private ListView lv_group;
    private ArrayList<String> groups;
    private PopupWindow popupWindow;
    private Dialog loadingNumber;

    @Override
    protected void initViewsAndEvents() {
        if (type==1){//总部 1，经销商 2，终端 3，工厂 4
            llZb.setVisibility(View.VISIBLE);
            llFzb.setVisibility(View.GONE);
        }else{
            llZb.setVisibility(View.GONE);
            llFzb.setVisibility(View.VISIBLE);
        }
        setData();
        setListeners();
        getUpdateMsg();
        loadNumber();
    }

    private void loadNumber() {
        loadingNumber= WeiboDialogUtils.createLoadingDialog(Menu12Activity.this,getString(R.string.caozuo_ing));
        Call<IndexInfo> call= RetrofitService.getInstance().indexinfo(new MyToken(Menu12Activity.this).getToken()+"");
        call.enqueue(new Callback<IndexInfo>() {
            @Override
            public void onResponse(Response<IndexInfo> response, Retrofit retrofit) {
                if (response.body()==null){
                    showToast("服务器错误");
                    WeiboDialogUtils.closeDialog(loadingNumber);
                    return;
                }
                if (response.body().getStatus()==0){
                    if (response.body().getDck()!=0){
                        tvDDFH.setText(Html.fromHtml("订单发货 "+"<font color=\"#00A3D9\">"+response.body().getDck()+"</font>"));//#00A3D9
                    }else{
                        tvDDFH.setText("订单发货");
                    }
                    if (response.body().getDsh()!=0){
                        tvDSH.setText(Html.fromHtml("待收货 "+"<font color=\"#00A3D9\">"+response.body().getDsh()+"</font>"));//#00A3D9
                    }else{
                        tvDSH.setText("待收货");
                    }
                    if (response.body().getNewocount()!=0){
                        tvXJDD.setText(Html.fromHtml("下级订单 "+"<font color=\"#00A3D9\">"+response.body().getNewocount()+"</font>"));//#00A3D9
                    }else{
                        tvXJDD.setText("下级订单");
                    }
                    WeiboDialogUtils.closeDialog(loadingNumber);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(loadingNumber);
            }
        });
    }

    private void getUpdateMsg() {
        Call<VersionAndroid> call= RetrofitService.getInstance().getVersionAndroid();
        call.enqueue(new Callback<VersionAndroid>() {
            @Override
            public void onResponse(Response<VersionAndroid> response, Retrofit retrofit) {
                if (response==null){
                    return;
                }
                if (response.body().getStatus()==0){
                    String newVersionName = response.body().getVersion();
                    final String updateUrl = response.body().getUrl();
                    final String versionName= VersionUtil.getVersionName(getApplicationContext());
                    // TODO 判断网络VersionName和当前app的VersionName
                    if (!newVersionName.equals(versionName)){
                        AlertDialog.Builder builder=new AlertDialog.Builder(Menu12Activity.this);
                        builder.setTitle("更新").setMessage("是否下载新版本").setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Menu12Activity.this, UpdateService.class);
                                intent.putExtra("Key_App_Name",getResources().getString(R.string.app_name));//app名
                                intent.putExtra("Key_Down_Url",updateUrl);//网络获取的下载链接
                                startService(intent);
                                showToast("正在后台下载，不会影响您的正常使用");
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setCancelable(false).show();
                    }
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setListeners() {
        /**右上角-点击显示退出登录*/
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(tvName);
            }
        });
        /**跳转-订单提报*/
        llToDDTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(DDTBActivity.class);
            }
        });
        /**跳转--下级订单（生成发货单）*/
        llToSCFHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(SCFHDActivity.class);
            }
        });
        /**跳转--货需上报*/
        llToHXSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(HXSBActivity.class);
            }
        });
        /**跳转-退货申请(退货单)*/
        llToTHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(THDActivity.class);
            }
        });
        /**跳转-订单发货(发货单)*/
        llToDDFH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(FHDActivity.class);
            }
        });
        /**跳转--终端发货*/
        llToZDFH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(ZDFHActivity.class);
            }
        });
        /**跳转--其他出库*/
        llToOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(PendingOutActivity.class);
            }
        });
        /**跳转--还货出库 */
        llToHHCK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(HHCKActivity.class);
            }
        });
        /**跳转--待收货*/
        llToGetOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("status", 3);
                readyGo(BackOrderActivity.class, bundle);
            }
        });
        /**跳转--其他入库 */
        llToQTRK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(PendingInActivity.class);
            }
        });
        /**跳转--退货入库 */
        llToTHRK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(THRKActivity.class);
            }
        });
        /**跳转--还货入库 */
        llToHHRK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(HHRKActivity.class);
            }
        });
        /**跳转--库存查询*/
        llToQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(QueryActivity.class);
            }
        });
        /**跳转--库存盘点*/
        llToStocking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(StockingActivity.class);
            }
        });
        /**跳转--物流公司*/
        llToWLGS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(WLGSActivity.class);
            }
        });
        /**跳转--终端录入(终端客户)*/
        llToZDLR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(ZDKHActivity.class);
            }
        });
        /**跳转--条码流转*/
        llToTMLZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(TMLZActivity.class);
            }
        });
        /**跳转-订单发货(发货单)--总部*/
        llToDDFH_zb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(FHDActivity.class);
            }
        });
    }
    /**退出登录*/
    private void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.group_list, null);

            lv_group = (ListView) view.findViewById(R.id.lvGroup);
            // 加载数据
            groups = new ArrayList<String>();
            groups.add("退出登录");

            GroupAdapter groupAdapter = new GroupAdapter(this, groups);
            lv_group.setAdapter(groupAdapter);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, 300, 350);
        }

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2
                - popupWindow.getWidth() / 2;
        Log.i("coder", "xPos:" + xPos);

        popupWindow.showAsDropDown(parent, xPos, 0);

        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                showToast("退出登录");
                showSignOutDialog();
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
    }
    /**退出登录*/
    private void showSignOutDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(Menu12Activity.this);
        builder.setMessage("确认退出登录？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //                new MyToken(MenuActivity.this).delSPFile();
                new MyToken(Menu12Activity.this).delToken();
                SharedPreferences sharedAP=getSharedPreferences("WearhouseAP",MODE_PRIVATE);
                SharedPreferences.Editor editorAP = sharedAP.edit();
                editorAP.putString("WHACC","");
                editorAP.putString("WHPWD","");
                editorAP.commit();
                SharedPreferences shared = getSharedPreferences("MyToken", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putInt("loginType", 0);
                editor.commit();
                ActivityManagerUtil.getInstance().finishActivity(Menu12Activity.this);
                readyGo(LoginActivity.class);
//                ActivityManagerUtil.getInstance().appExit();
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

    private void setData() {
        tvType.setText(dealerName);
        tvWarehouseName.setText(dealerName);
        tvName.setText(dealerName);
//        tvWarehouseName.setText(warehouseName);
//        tvName.setText(name);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadNumber();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        name=extras.getString("name");
        dealerName=extras.getString("dealerName");
        warehouseName=extras.getString("warehouseName");
        type=extras.getInt("type", 0);//总部 1，经销商 2，终端 3，工厂 4
        ishandover=extras.getInt("ishandover", 0);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_menu_12;
    }
}
