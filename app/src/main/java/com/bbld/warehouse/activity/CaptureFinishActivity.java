package com.bbld.warehouse.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.CartSQLBean;
import com.bbld.warehouse.db.UserDataBaseOperate;
import com.bbld.warehouse.db.UserSQLiteOpenHelper;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 待出库--出库--明细
 * Created by likey on 2017/5/24.
 */

public class CaptureFinishActivity extends BaseActivity{
    @BindView(R.id.lv_scan)
    ListView lvScan;
    @BindView(R.id.tv_needCount)
    TextView tvNeedCount;
    @BindView(R.id.tv_scanCount)
    TextView tvScanCount;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tv_title;

    private UserSQLiteOpenHelper mUserSQLiteOpenHelper;
    private UserDataBaseOperate mUserDataBaseOperate;
    private String productId;
    private String productName;
    private String needCount;
    private int scanCount;
    private String showBS;

    @Override
    protected void initViewsAndEvents() {
        loadData();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(CaptureFinishActivity.this);
            }
        });
    }

    private void loadData() {
        mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(CaptureFinishActivity.this);
        mUserDataBaseOperate = new UserDataBaseOperate(mUserSQLiteOpenHelper.getWritableDatabase());
        List<CartSQLBean> products = mUserDataBaseOperate.findUserById(productId + "");
        lvScan.setAdapter(new ScanAdapter(products));

        tvNeedCount.setText(needCount+"(盒)");
        scanCount=0;
        for (int i=0;i<products.size();i++){
            scanCount=scanCount+products.get(i).getProCount();
        }
        tvScanCount.setText(scanCount+"(盒)");
        tv_title.setText(productName+"");
    }

    class ScanAdapter extends BaseAdapter{
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
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_capture_finish,null);
                holder=new ScanHolder();
                holder.tv_code=(TextView)view.findViewById(R.id.tv_code);
                holder.tv_type=(TextView)view.findViewById(R.id.tv_type);
                holder.tv_count=(TextView)view.findViewById(R.id.tv_count);
                holder.tvBatchNumber=(TextView)view.findViewById(R.id.tvBatchNumber);
                holder.tvSerialNumber=(TextView)view.findViewById(R.id.tvSerialNumber);
                holder.llBS=(LinearLayout) view.findViewById(R.id.llBS);
                view.setTag(holder);
            }
            holder= (ScanHolder) view.getTag();
            CartSQLBean product = getItem(i);
            holder.tv_code.setText(product.getProductCode()+"");
            holder.tv_type.setText(getType(product.getProductType()+""));
            holder.tv_count.setText(product.getProCount()+"(盒)");
            if (showBS.equals("yes")){
                holder.llBS.setVisibility(View.VISIBLE);
                holder.tvBatchNumber.setText("批次号："+product.getBatchNumber());
                holder.tvSerialNumber.setText("序列号："+product.getSerialNumber());
            }else{
                holder.llBS.setVisibility(View.GONE);
            }
            return view;
        }

        class ScanHolder{
            TextView tv_code;
            TextView tv_type;
            TextView tv_count;
            TextView tvBatchNumber;
            TextView tvSerialNumber;
            LinearLayout llBS;
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
    protected void getBundleExtras(Bundle extras) {
        productId=extras.getString("productId");
        productName=extras.getString("productName");
        needCount=extras.getString("needCount");
        showBS=extras.getString("showBS", "");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_capturefinish;
    }

}
