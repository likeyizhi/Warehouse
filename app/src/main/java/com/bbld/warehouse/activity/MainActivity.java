package com.bbld.warehouse.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.VersionAndroid;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.zxing.android.CaptureActivity;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_hello)
    TextView tvHello;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    @Override
    protected void initViewsAndEvents() {
        //测试网络请求，扫码功能的代码 ，正式写的时候删除↓
//        Call<VersionAndroid> call=RetrofitService.getInstance().getVersionAndroid();
//        call.enqueue(new Callback<VersionAndroid>() {
//            @Override
//            public void onResponse(Response<VersionAndroid> response, Retrofit retrofit) {
//                if (response.body().getStatus()==0){
//                    tvHello.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            tvHello.setText(10010+"");
//                            Intent intent = new Intent(MainActivity.this,
//                                    CaptureActivity.class);
//                            startActivityForResult(intent, REQUEST_CODE_SCAN);
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//
//            }
//        });
        tvHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHello.setText(10010+"");
                Intent intent = new Intent(MainActivity.this,
                        CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
        //测试网络请求，扫码功能的代码 ，正式写的时候删除↑
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

                tvHello.setText("解码结果： \n" + content);

            }
        }
    }
}
