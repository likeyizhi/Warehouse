package com.bbld.warehouse.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;

import butterknife.BindView;

/**
 * 产品出库，产品入库
 * Created by likey on 2017/5/27.
 */

public class OutBoundOrderActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private String title;

    @Override
    protected void initViewsAndEvents() {
        tvTitle.setText(title);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        title=extras.getString("title");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_outbound_order;
    }
}
