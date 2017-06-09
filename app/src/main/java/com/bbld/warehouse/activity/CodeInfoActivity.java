package com.bbld.warehouse.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.InventoryEdit;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 库存盘点--明细
 * Created by likey on 2017/6/8.
 */

public class CodeInfoActivity extends BaseActivity{
    @BindView(R.id.lv_scan)
    ListView lvScan;
    @BindView(R.id.tv_productName)
    TextView tvProductName;
    @BindView(R.id.tv_scanCount)
    TextView tvScanCount;

    private String InventoryId;
    private int clickPosition;
    private List<InventoryEdit.InventoryEditInfo.InventoryEditProductList.InventoryEditCodeList> codeList;
    private InventoryEdit.InventoryEditInfo.InventoryEditProductList product;
    private CodeInfoAdapter adapter;

    @Override
    protected void initViewsAndEvents() {
        loadData();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        InventoryId=extras.getString("InventoryId");
        clickPosition=extras.getInt("clickPosition");
    }

    private void loadData() {
        Call<InventoryEdit> call= RetrofitService.getInstance().inventoryEdit(new MyToken(CodeInfoActivity.this).getToken(), InventoryId);
        call.enqueue(new Callback<InventoryEdit>() {
            @Override
            public void onResponse(Response<InventoryEdit> response, Retrofit retrofit) {
                if (response==null){
                    showToast(getResources().getString(R.string.get_data_fail));
                    return;
                }
                if (response.body().getStatus()==0){
                    product = response.body().getInfo().getProductList().get(clickPosition);
                    codeList = response.body().getInfo().getProductList().get(clickPosition).getCodeList();
                    setData();
                    setAdapter();
                }else{
                    showToast(response.body().getMes()+"");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setData() {
        tvProductName.setText(product.getProductName()+"");
        tvScanCount.setText(product.getProductCount()+"");
    }

    private void setAdapter() {
        adapter=new CodeInfoAdapter();
        lvScan.setAdapter(adapter);
    }

    class CodeInfoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return codeList.size();
        }

        @Override
        public InventoryEdit.InventoryEditInfo.InventoryEditProductList.InventoryEditCodeList getItem(int i) {
            return codeList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            CodeInfoHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_capture_finish,null);
                holder=new CodeInfoHolder();
                holder.tv_code=(TextView)view.findViewById(R.id.tv_code);
                holder.tv_type=(TextView)view.findViewById(R.id.tv_type);
                holder.tv_count=(TextView)view.findViewById(R.id.tv_count);
                view.setTag(holder);
            }
            holder= (CodeInfoHolder) view.getTag();
            InventoryEdit.InventoryEditInfo.InventoryEditProductList.InventoryEditCodeList code = getItem(i);
            holder.tv_code.setText(code.getCode()+"");
            holder.tv_type.setText(getType(code.getCodeType()+""));
            holder.tv_count.setText(code.getCount()+"(盒)");
            return view;
        }

        class CodeInfoHolder{
            TextView tv_code, tv_type, tv_count;
        }

        private String getType(String productType) {
            //type=1=箱码;type=2=盒码
            if (productType.equals("1")){
                return "箱码";
            }else{
                return "盒码";
            }
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_code_info;
    }
}
