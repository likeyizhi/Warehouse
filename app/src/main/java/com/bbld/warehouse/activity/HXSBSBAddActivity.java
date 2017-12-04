package com.bbld.warehouse.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.bean.GetProductNeedList;
import com.bbld.warehouse.bean.HXSBSBAdd;
import com.bbld.warehouse.bean.ProductNeedJson;
import com.bbld.warehouse.bean.TbGetProductList;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.utils.UploadUserInformationByPostService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 货需上报-上报
 * Created by likey on 2017/11/9.
 */

public class HXSBSBAddActivity extends BaseActivity{
    @BindView(R.id.etBz)
    EditText etBz;
    @BindView(R.id.etYear)
    EditText etYear;
    @BindView(R.id.etMonth)
    EditText etMonth;
    @BindView(R.id.lvSB)
    ListView lvSB;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private Dialog loadingAdd;
    private String token;
    private List<TbGetProductList.TbGetProductListlist> pros;
    private AddAdapter addAdapter;
    private ArrayList<HXSBSBAdd> adds;
    private Dialog upLoading;
    private String request;
    private int mYear;
    private int mMonth;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    WeiboDialogUtils.closeDialog(upLoading);
                    showToast(""+request);
                    finish();
                    break;
                case 222:
                    WeiboDialogUtils.closeDialog(upLoading);
                    showToast(""+request);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        mYear = c.get(Calendar.YEAR); // 获取当前年份
        mMonth = c.get(Calendar.MONTH);// 获取当前月份
        etYear.setText(mYear+"");
        etMonth.setText((mMonth+1)+"");
        loadAddData();
        setListeners();
    }

    private void setListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAdd();
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void saveAdd() {
        if ((etYear.getText()+"").trim().equals("") || (etMonth.getText()+"").trim().equals("")){
            showToast("请输入年月");
        }else{
            upLoading=WeiboDialogUtils.createLoadingDialog(HXSBSBAddActivity.this,"提交中...");
            ProductNeedJson pnj=new ProductNeedJson();
            pnj.setId(0);
            pnj.setMonth((etYear.getText()+"").trim()+"-"+(etMonth.getText()+"").trim());
            pnj.setRemark(etBz.getText()+"");
            List<ProductNeedJson.ProductNeedJsonlist> pnjList=new ArrayList<ProductNeedJson.ProductNeedJsonlist>();
            for (int i=0;i<adds.size();i++){
                ProductNeedJson.ProductNeedJsonlist pnjItem = new ProductNeedJson.ProductNeedJsonlist(adds.get(i).getId(),adds.get(i).getNeedTotal());
                pnjList.add(pnjItem);
            }
            pnj.setList(pnjList);
            Gson gson=new Gson();
            final String needJson = gson.toJson(pnj);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        request= UploadUserInformationByPostService.pnCreateProductNeed(token,
                                needJson);
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
    }

    private void loadAddData() {
        loadingAdd= WeiboDialogUtils.createLoadingDialog(HXSBSBAddActivity.this,"加载中...");
        Call<TbGetProductList> call= RetrofitService.getInstance().tbGetProductList(token);
        call.enqueue(new Callback<TbGetProductList>() {
            @Override
            public void onResponse(Response<TbGetProductList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loadingAdd);
                    return;
                }
                if (response.body().getStatus()==0){
                    pros=response.body().getList();
                    adds = new ArrayList<HXSBSBAdd>();
                    for (int i=0;i<pros.size();i++){
                        HXSBSBAdd add=new HXSBSBAdd(pros.get(i).getId(),pros.get(i).getName(),pros.get(i).getLogo(),pros.get(i).getProSpecifications(),0,0);
                        adds.add(add);
                    }
                    setAdapter();
                    WeiboDialogUtils.closeDialog(loadingAdd);
                }else{
                    showToast("数据获取失败，请重试");
                    WeiboDialogUtils.closeDialog(loadingAdd);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(loadingAdd);
            }
        });
    }

    private void setAdapter() {
        addAdapter = new AddAdapter();
        lvSB.setAdapter(addAdapter);
    }

    class AddAdapter extends BaseAdapter{
        AddHolder addHolder=null;
        @Override
        public int getCount() {
            return adds.size();
        }

        @Override
        public HXSBSBAdd getItem(int i) {
            return adds.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_hxsb_sb,null);
                addHolder=new AddHolder();
                addHolder.img=(ImageView)view.findViewById(R.id.img);
                addHolder.tvName=(TextView) view.findViewById(R.id.tvName);
                addHolder.tvSpec=(TextView) view.findViewById(R.id.tvSpec);
                addHolder.tvTotal=(TextView) view.findViewById(R.id.tvTotal);
                addHolder.tvJTotal=(TextView) view.findViewById(R.id.tvJTotal);
                view.setTag(addHolder);
            }
            addHolder= (AddHolder) view.getTag();
            HXSBSBAdd item = getItem(i);
            Glide.with(getApplicationContext()).load(item.getLogo()).error(R.mipmap.xiuzhneg).into(addHolder.img);
            addHolder.tvName.setText(item.getName()+"");
            addHolder.tvSpec.setText("规格："+item.getProSpecifications());
            addHolder.tvTotal.setText(item.getNeedTotal()+"");
            addHolder.tvJTotal.setText(item.getNeedJTotal()+"");
            addHolder.tvTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEditDialog(1,i);
                }
            });
            addHolder.tvJTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEditDialog(2,i);
                }
            });
            return view;
        }

        class AddHolder{
            ImageView img;
            TextView tvName,tvSpec,tvTotal,tvJTotal;
        }
    }

    private void showEditDialog(final int whichC, final int i) {
        final EditText et = new EditText(this);
        et.setBackgroundResource(R.drawable.bg_batch);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setHint("输入数量");
        et.setMaxLines(1);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setSingleLine(true);
        AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("请输入数量？")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText() + "";
                        if (!input.trim().equals("")){
                            if (whichC==1){
                                adds.get(i).setNeedTotal(Integer.parseInt(input));
                                int pack = pros.get(i).getPackSpecifications();
                                if (Integer.parseInt(input)%pack>0){
                                    adds.get(i).setNeedJTotal(Integer.parseInt(input)/pack+1);
                                }else{
                                    adds.get(i).setNeedJTotal(Integer.parseInt(input)/pack);
                                }
                                addAdapter.notifyDataSetChanged();
                            }else{
                                adds.get(i).setNeedJTotal(Integer.parseInt(input));
                                addAdapter.notifyDataSetChanged();
                            }
                        }else{
                            showToast("请输入数量");
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_hxsb_sb;
    }
}
