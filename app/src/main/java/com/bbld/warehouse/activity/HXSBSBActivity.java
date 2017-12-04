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
import com.bbld.warehouse.bean.ProductNeedJson;
import com.bbld.warehouse.bean.TbGetProductList;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.utils.UploadUserInformationByPostService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 货需上报-上报页
 * Created by likey on 2017/11/8.
 */

public class HXSBSBActivity extends BaseActivity{
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

    private String token;
    private List<GetProductNeedList.GetProductNeedListlist> orders;
    private Dialog loadingEdit;
    private Dialog loadingAdd;
    private String mYear;
    private String mMonth;
    private EditAdapter editAdapter;
    private Dialog upLoading;
    private String request;
    private GetProductNeedList need;
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
        loadEditData();
        setListeners();
    }

    private void setListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEdit();
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void saveEdit() {
        if ((etYear.getText()+"").trim().equals("") || (etMonth.getText()+"").trim().equals("")){
            showToast("请输入年月");
        }else{
            upLoading=WeiboDialogUtils.createLoadingDialog(HXSBSBActivity.this,"提交中...");
            ProductNeedJson pnj=new ProductNeedJson();
            pnj.setId(need.getId());
            pnj.setMonth(mYear+"-"+mMonth);
            pnj.setRemark(etBz.getText()+"");
            List<ProductNeedJson.ProductNeedJsonlist> pnjList=new ArrayList<ProductNeedJson.ProductNeedJsonlist>();
            for (int i=0;i<orders.size();i++){
                ProductNeedJson.ProductNeedJsonlist pnjItem = new ProductNeedJson.ProductNeedJsonlist(orders.get(i).getProductId(),orders.get(i).getNeedTotal());
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

    private void loadEditData() {
        loadingEdit= WeiboDialogUtils.createLoadingDialog(HXSBSBActivity.this,"加载中...");
        Call<GetProductNeedList> call= RetrofitService.getInstance().getProductNeedList(token,mYear+"-"+mMonth);
        call.enqueue(new Callback<GetProductNeedList>() {
            @Override
            public void onResponse(Response<GetProductNeedList> response, Retrofit retrofit) {
                if (response==null){
                    WeiboDialogUtils.closeDialog(loadingEdit);
                    return;
                }
                if (response.body().getStatus()==0){
                    need = response.body();
                    orders = response.body().getList();
                    etBz.setText(response.body().getRemark()+"");
                    etYear.setText(mYear);
                    etMonth.setText(mMonth);
                    setEditAdapter();
                    WeiboDialogUtils.closeDialog(loadingEdit);
                }else{
                    WeiboDialogUtils.closeDialog(loadingEdit);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(loadingEdit);
            }
        });
    }

    private void setEditAdapter() {
        editAdapter=new EditAdapter();
        lvSB.setAdapter(editAdapter);
    }

    class EditAdapter extends BaseAdapter{
        EditHolder editHolder=null;
        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public GetProductNeedList.GetProductNeedListlist getItem(int i) {
            return orders.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_hxsb_sb,null);
                editHolder=new EditHolder();
                editHolder.img=(ImageView)view.findViewById(R.id.img);
                editHolder.tvName=(TextView) view.findViewById(R.id.tvName);
                editHolder.tvSpec=(TextView) view.findViewById(R.id.tvSpec);
                editHolder.tvTotal=(TextView) view.findViewById(R.id.tvTotal);
                editHolder.tvJTotal=(TextView) view.findViewById(R.id.tvJTotal);
                view.setTag(editHolder);
            }
            editHolder= (EditHolder) view.getTag();
            final GetProductNeedList.GetProductNeedListlist item = getItem(i);
            Glide.with(getApplicationContext()).load(item.getLogo()).error(R.mipmap.xiuzhneg).into(editHolder.img);
            editHolder.tvName.setText(item.getName()+"");
            editHolder.tvSpec.setText("规格："+item.getProSpecifications());
            editHolder.tvTotal.setText(item.getNeedTotal()+"");
            editHolder.tvJTotal.setText(item.getNeedJTotal()+"");
            editHolder.tvTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEditDialog(1,i);
                }
            });
            editHolder.tvJTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEditDialog(2,i);
                }
            });
            return view;
        }

        class EditHolder {
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
                                orders.get(i).setNeedTotal(Integer.parseInt(input));
                                int pack = orders.get(i).getPackSpecifications();
                                if (Integer.parseInt(input)%pack>0){
                                    orders.get(i).setNeedJTotal(Integer.parseInt(input)/pack+1);
                                }else{
                                    orders.get(i).setNeedJTotal(Integer.parseInt(input)/pack);
                                }
                                editAdapter.notifyDataSetChanged();
                            }else{
                                orders.get(i).setNeedJTotal(Integer.parseInt(input));
                                editAdapter.notifyDataSetChanged();
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
        mYear=extras.getString("mYear","");
        mMonth=extras.getString("mMonth","");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_hxsb_sb;
    }
}
