package com.bbld.warehouse.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.RefundGetRefundProductScanCode;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 退货申请-详情-明细
 * Created by likey on 2017/9/7.
 */

public class THSQInfoMxActivity extends BaseActivity{
    @BindView(R.id.lv_thsq_mx)
    ListView lvMx;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private String id;
    private String productId;
    private String token;
    private List<RefundGetRefundProductScanCode.RefundGetRefundProductScanCodelist> list;
    private Dialog loading;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        setListeners();
    }

    private void setListeners() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(THSQInfoMxActivity.class);
            }
        });
    }

    private void loadData() {
        loading= WeiboDialogUtils.createLoadingDialog(THSQInfoMxActivity.this,"加载中...");
        Call<RefundGetRefundProductScanCode> call= RetrofitService.getInstance().refundGetRefundProductScanCode(token, id, productId);
        call.enqueue(new Callback<RefundGetRefundProductScanCode>() {
            @Override
            public void onResponse(Response<RefundGetRefundProductScanCode> response, Retrofit retrofit) {
                if (response==null){
                    showToast("数据获取失败,请重试");
                    WeiboDialogUtils.closeDialog(loading);
                    return;
                }
                if (response.body().getStatus()==0){
                    list=response.body().getList();
                    setAdapter();
                    WeiboDialogUtils.closeDialog(loading);
                }else {
                    showToast(response.body().getMes());
                    WeiboDialogUtils.closeDialog(loading);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                showToast("数据获取失败,请重试");
                WeiboDialogUtils.closeDialog(loading);
            }
        });
    }

    private void setAdapter() {
        lvMx.setAdapter(new MxAdapter());
    }

    class MxAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public RefundGetRefundProductScanCode.RefundGetRefundProductScanCodelist getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MxHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_thsq_info_mx,null);
                holder=new MxHolder();
                holder.tvCount=(TextView)view.findViewById(R.id.tvCount);
                holder.tvCode=(TextView)view.findViewById(R.id.tvCode);
                holder.tvSerialNumber=(TextView)view.findViewById(R.id.tvSerialNumber);
                holder.tvBatchNumber=(TextView)view.findViewById(R.id.tvBatchNumber);
                view.setTag(holder);
            }
            holder= (MxHolder) view.getTag();
            RefundGetRefundProductScanCode.RefundGetRefundProductScanCodelist item = getItem(i);
            holder.tvCount.setText(item.getCodeForCount()+CodeType(item.getCodeType())+"");
            holder.tvCode.setText(item.getScanCode()+"");
            holder.tvSerialNumber.setText(item.getSerialNumber()+"");
            holder.tvBatchNumber.setText(item.getBatchNumber()+"");
            return view;
        }

        private String CodeType(int codeType) {
            if (codeType==1){
                return "【箱】";
            }else{
                return "【盒】";
            }
        }

        class MxHolder{
            TextView tvCount,tvCode,tvSerialNumber,tvBatchNumber;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        id=extras.getString("id");
        productId=extras.getString("productId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_thsq_info_mx;
    }
}
