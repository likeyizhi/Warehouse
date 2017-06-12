package com.bbld.warehouse.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.Login;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 登录
 * Created by likey on 2017/5/23.
 */

public class LoginActivity extends BaseActivity{
    @BindView(R.id.et_acc)
    EditText etAcc;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private static final String TOKEN=null;
    private String acc="";
    private String pwd="";
    private SharedPreferences.Editor editorAP;

    @Override
    protected void initViewsAndEvents() {
        //保存帐号密码
        SharedPreferences sharedAP=getSharedPreferences("WearhouseAP",MODE_PRIVATE);
        editorAP=sharedAP.edit();

        SharedPreferences sharedGetAP=getSharedPreferences("WearhouseAP",MODE_PRIVATE);
        acc = sharedGetAP.getString("WHACC", "");
        pwd = sharedGetAP.getString("WHPWD", "");
//        showToast(acc+""+pwd);
        if (acc.equals("")&&pwd.equals("")){

        }else{
            //保存帐号密码
            editorAP.putString("WHACC",acc);
            editorAP.putString("WHPWD",pwd);
            editorAP.commit();
            login();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acc=etAcc.getText().toString();
                pwd=etPwd.getText().toString();
                login();
            }
        });
    }

    private void login() {
        Call<Login> call= RetrofitService.getInstance().login(/*"xz","123456"*/acc,pwd);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Response<Login> response, Retrofit retrofit) {
                if (response==null){
                    showToast("服务器错误");
                    return;
                }
                if (response.body().getStatus()==0){
                    //保存Token
                    SharedPreferences shared=getSharedPreferences("MyToken",MODE_PRIVATE);
                    SharedPreferences.Editor editor=shared.edit();
                    editor.putString(TOKEN,response.body().getToken());
                    editor.commit();
                    //保存帐号密码
                    editorAP.putString("WHACC",acc);
                    editorAP.putString("WHPWD",pwd);
                    editorAP.commit();
                    //提示登录成功
                    showToast(response.body().getMes()+"");
                    //跳转到主页，并释放LoginActivity.class
                    Bundle bundle=new Bundle();
                    bundle.putString("name", response.body().getName());
                    bundle.putString("dealerName", response.body().getDealerName());
                    bundle.putString("warehouseName", response.body().getWarehouseName());
                    bundle.putInt("type", response.body().getType());
                    bundle.putInt("ishandover", response.body().getIshandover());
                    readyGo(MenuActivity.class, bundle);
                    ActivityManagerUtil.getInstance().finishActivity(LoginActivity.this);
                }else{
                    showToast(response.body().getMes()+"");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }
}
