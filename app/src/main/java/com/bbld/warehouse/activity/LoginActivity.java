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
    @Override
    protected void initViewsAndEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Login> call= RetrofitService.getInstance().login("xz","123456"/*etAcc.getText().toString(),etPwd.getText().toString()*/);
                call.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Response<Login> response, Retrofit retrofit) {
                        if (response==null){
                            showToast("服务器错误");
                            return;
                        }
                        if (response.body().getStatus()==0){
                            SharedPreferences shared=getSharedPreferences("MyToken",MODE_PRIVATE);
                            SharedPreferences.Editor editor=shared.edit();
                            editor.putString(TOKEN,response.body().getToken());
                            editor.commit();
                            showToast(response.body().getMes()+"");
                            readyGo(MenuActivity.class);
                        }else{
                            showToast(response.body().getMes()+"");
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });

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
