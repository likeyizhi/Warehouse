package com.bbld.warehouse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.SouSuoDiZhi;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 搜索地址
 * Created by likey on 2017/12/5.
 */

public class SetXYSearchActivity extends BaseActivity{
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.lvAddr)
    ListView lvAddr;

    private PoiCitySearchOption poiCitySearchOption;
    private PoiSearch poiSearch;
    private SouSuoDiZhiAdapter adapter;

    @Override
    protected void initViewsAndEvents() {
        poiSearch= PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(onGetPoiSearchResultListener);
        setListeners();
    }

    private void setListeners() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(etSearch.getText()+"").trim().equals("")){
                    poiCitySearchOption= new PoiCitySearchOption().city("").keyword(etSearch.getText()+"");
                    poiSearch.searchInCity(poiCitySearchOption);
                }
            }
        });
        etSearch.addTextChangedListener(watcher);
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
            if (!(editable+"").trim().equals("") || editable!=null){
                poiCitySearchOption= new PoiCitySearchOption().city("").keyword(etSearch.getText()+"");
                poiSearch.searchInCity(poiCitySearchOption);
            }
        }
    };

    OnGetPoiSearchResultListener onGetPoiSearchResultListener=new OnGetPoiSearchResultListener(){

        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            ArrayList<SouSuoDiZhi> diZhiArrayList=new ArrayList<SouSuoDiZhi>();
            if (poiResult.getAllPoi()!=null){
//                showToast(poiResult.getAllPoi().get(0).location.latitude+","+poiResult.getAllPoi().get(0).location.longitude
//                +","+poiResult.getAllPoi().get(0).name+","+poiResult.getAllPoi().get(0).address);
                for (int d=0;d<poiResult.getAllPoi().size();d++){
                    if (poiResult.getAllPoi().get(d).location!=null){
                        SouSuoDiZhi diZhi = new SouSuoDiZhi(poiResult.getAllPoi().get(d).location.latitude+"",
                                poiResult.getAllPoi().get(d).location.longitude+"",
                                poiResult.getAllPoi().get(d).name+"",
                                poiResult.getAllPoi().get(d).address+"");
                        diZhiArrayList.add(diZhi);
                    }
                }
            }
            setSouSuoDiZhiAdapter(diZhiArrayList);
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };

    private void setSouSuoDiZhiAdapter(ArrayList<SouSuoDiZhi> diZhiArrayList) {
        adapter=new SouSuoDiZhiAdapter(diZhiArrayList);
        lvAddr.setAdapter(adapter);
    }

    class SouSuoDiZhiAdapter extends BaseAdapter{
        private ArrayList<SouSuoDiZhi> diZhiArrayList;

        public SouSuoDiZhiAdapter(ArrayList<SouSuoDiZhi> diZhiArrayList) {
            super();
            this.diZhiArrayList=diZhiArrayList;
        }

        @Override
        public int getCount() {
            return diZhiArrayList.size();
        }

        @Override
        public SouSuoDiZhi getItem(int i) {
            return diZhiArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            SouSuoDiZhiHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_set_x_y_search,null);
                holder=new SouSuoDiZhiHolder();
                holder.tvName=(TextView)view.findViewById(R.id.tvName);
                holder.tvAddress=(TextView)view.findViewById(R.id.tvAddress);
                holder.tvXY=(TextView)view.findViewById(R.id.tvXY);
                view.setTag(holder);
            }
            holder= (SouSuoDiZhiHolder) view.getTag();
            final SouSuoDiZhi item = getItem(i);
            holder.tvName.setText(item.getDizhiName()+"");
            holder.tvAddress.setText(item.getDizhiAddress()+"");
            holder.tvXY.setText("纬度:"+item.getX()+",经度:"+item.getY());
            if (view!=null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent();
                        intent.putExtra("_X",item.getX());
                        intent.putExtra("_Y",item.getY());
                        setResult(2011,intent);
                        finish();
                    }
                });
            }
            return view;
        }

        class SouSuoDiZhiHolder{
            TextView tvName,tvAddress,tvXY;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            finish();
            overridePendingTransition(0, 0);
        }
        return false;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_set_x_y_search;
    }
}
