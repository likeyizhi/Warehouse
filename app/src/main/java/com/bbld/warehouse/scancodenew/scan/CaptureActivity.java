package com.bbld.warehouse.scancodenew.scan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbld.warehouse.R;
import com.bbld.warehouse.bean.CartSQLBean;
import com.bbld.warehouse.bean.ScanCode;
import com.bbld.warehouse.db.UserDataBaseOperate;
import com.bbld.warehouse.db.UserSQLiteOpenHelper;
import com.bbld.warehouse.network.RetrofitService;
import com.bbld.warehouse.scancodenew.camera.CameraManager;
import com.bbld.warehouse.scancodenew.common.BitmapUtils;
import com.bbld.warehouse.scancodenew.decode.BitmapDecoder;
import com.bbld.warehouse.scancodenew.decode.CaptureActivityHandler;
import com.bbld.warehouse.scancodenew.view.ViewfinderView;
import com.bbld.warehouse.utils.MyToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 此Activity所做的事：
 *  1.开启camera，在后台独立线程中完成扫描任务；
 *  2.绘制了一个扫描区（viewfinder）来帮助用户将条码置于其中以准确扫描；
 *  3.扫描成功后会将扫描结果展示在界面上。
 */
public final class CaptureActivity extends Activity implements
		SurfaceHolder.Callback, View.OnClickListener {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private static final int REQUEST_CODE = 100;
	private static final int PARSE_BARCODE_FAIL = 300;
	private static final int PARSE_BARCODE_SUC = 200;

	/**
	 * 是否有预览
	 */
	private boolean hasSurface;

	/**
	 * 活动监控器。如果手机没有连接电源线，那么当相机开启后如果一直处于不被使用状态则该服务会将当前activity关闭。
	 * 活动监控器全程监控扫描活跃状态，与CaptureActivity生命周期相同.每一次扫描过后都会重置该监控，即重新倒计时。
	 */
	private InactivityTimer inactivityTimer;

	/**
	 * 声音震动管理器。如果扫描成功后可以播放一段音频，也可以震动提醒，可以通过配置来决定扫描成功后的行为。
	 */
	private BeepManager beepManager;

	/**
	 * 闪光灯调节器。自动检测环境光线强弱并决定是否开启闪光灯
	 */
	private AmbientLightManager ambientLightManager;

	private CameraManager cameraManager;
	/**
	 * 扫描区域
	 */
	private ViewfinderView viewfinderView;

	private CaptureActivityHandler handler;

	private Result lastResult;

	private boolean isFlashlightOpen;

	/**
	 * 【辅助解码的参数(用作MultiFormatReader的参数)】 编码类型，该参数告诉扫描器采用何种编码方式解码，即EAN-13，QR
	 * Code等等 对应于DecodeHintType.POSSIBLE_FORMATS类型
	 * 参考DecodeThread构造函数中如下代码：hints.put(DecodeHintType.POSSIBLE_FORMATS,
	 * decodeFormats);
	 */
	private Collection<BarcodeFormat> decodeFormats;

	/**
	 * 【辅助解码的参数(用作MultiFormatReader的参数)】 该参数最终会传入MultiFormatReader，
	 * 上面的decodeFormats和characterSet最终会先加入到decodeHints中 最终被设置到MultiFormatReader中
	 * 参考DecodeHandler构造器中如下代码：multiFormatReader.setHints(hints);
	 */
	private Map<DecodeHintType, ?> decodeHints;

	/**
	 * 【辅助解码的参数(用作MultiFormatReader的参数)】 字符集，告诉扫描器该以何种字符集进行解码
	 * 对应于DecodeHintType.CHARACTER_SET类型
	 * 参考DecodeThread构造器如下代码：hints.put(DecodeHintType.CHARACTER_SET,
	 * characterSet);
	 */
	private String characterSet;

	private Result savedResultToShow;

	private IntentSource source;

	/**
	 * 图片的路径
	 */
	private String photoPath;
	
	private LinearLayout linearLayout_bottom;//底部提示部分
	
	private Handler mHandler = new MyHandler(this);
	private TextView tv_needCount;
	private UserSQLiteOpenHelper mUserSQLiteOpenHelper;
	private UserDataBaseOperate mUserDataBaseOperate;
	private TextView tvBatchNumber;
	private int isNeedBatch;
	public static String batchNumber="";
	private String productId;
	private String productName;
	private String storage;
	private String invoiceid;
	private String needCount;
	private String type;
	private ListView lvScan;
	private Button btnComplete;
	private int scanCount;
	private TextView tv_scanCount;
	private Call<ScanCode> call;
	private TextView tvInput;

	static class MyHandler extends Handler {

		private WeakReference<Activity> activityReference;

		public MyHandler(Activity activity) {
			activityReference = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
				case PARSE_BARCODE_SUC: // 解析图片成功
					String message=msg.obj.toString();
					Toast.makeText(activityReference.get(),"图片解析成功！", Toast.LENGTH_SHORT).show();
					Intent intent1 = new Intent();
					Bundle bunble=new Bundle();
					bunble.putString("data", message);
					intent1.putExtras(bunble);
					activityReference.get().setResult(100, intent1);
			        new Handler().postDelayed(new Runnable() {
			            public void run() {
			            	activityReference.get().finish();
			            }
			        }, 400);
					break;

				case PARSE_BARCODE_FAIL:// 解析图片失败
					Toast.makeText(activityReference.get(), "图片格式错误！请重新尝试！",Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
			}

			super.handleMessage(msg);
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_capture_new);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);
		ambientLightManager = new AmbientLightManager(this);

		// 监听图片识别按钮
		findViewById(R.id.capture_scan_photo).setOnClickListener(this);

		findViewById(R.id.capture_flashlight).setOnClickListener(this);
		
		findViewById(R.id.capture_button_cancel).setOnClickListener(this);
		
		linearLayout_bottom=(LinearLayout) findViewById(R.id.bottom);
		//自己写的东西
		doMyThings();

	}

	private void doMyThings() {
		//数据库
		mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(CaptureActivity.this);
		mUserDataBaseOperate = new UserDataBaseOperate(mUserSQLiteOpenHelper.getWritableDatabase());

		tv_needCount=(TextView)findViewById(R.id.tv_needCount);

		tvBatchNumber=(TextView)findViewById(R.id.tvBatchNumber);
		Intent intent=getIntent();
		isNeedBatch=intent.getExtras().getInt("NeedBatch", 0);
		if (isNeedBatch==1){
			showBatchDialog();
		}else{
			batchNumber="";
		}
		productId=intent.getExtras().getString("productId");
		productName=intent.getExtras().getString("productName");
		storage=intent.getExtras().getString("storage");
		if (storage.equals("no")) {
			invoiceid=intent.getExtras().getString("orderId");
			needCount=intent.getExtras().getString("needCount");
			type=intent.getExtras().getString("type");
		}else{
			if (intent.getExtras().getString("other").equals("yes")){
				type=intent.getExtras().getString("type");
				needCount=intent.getExtras().getString("needCount");
			}else{
				type=intent.getExtras().getString("type");
				needCount=10000+"";
				tv_needCount.setVisibility(View.INVISIBLE);
			}
		}
		TextView tvProductName = (TextView) findViewById(R.id.tv_productName);
		tvProductName.setText(productName+"");
		lvScan=(ListView)findViewById(R.id.lv_scan);
		List<CartSQLBean> products=mUserDataBaseOperate.findUserById(productId+"");
		Collections.reverse(products);
		lvScan.setAdapter(new ScanAdapter(products));
		btnComplete=(Button)findViewById(R.id.btn_complete);
		btnComplete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				List<CartSQLBean> products=mUserDataBaseOperate.findUserById(productId+"");
				scanCount=0;
				for (int i=0;i<products.size();i++){
					scanCount=scanCount+products.get(i).getProCount();
				}
				if (Integer.parseInt(needCount+"")<Integer.parseInt(scanCount+"")){
					showBiggerDialog("扫码数量不能大于发货数量");
				}else {
					finish();
				}
			}
		});

		tv_needCount.setText(needCount+"(盒)");
		tv_scanCount=(TextView)findViewById(R.id.tv_scanCount);
		scanCount=0;
		for (int i=0;i<products.size();i++){
			scanCount=scanCount+products.get(i).getProCount();
		}
		tv_scanCount.setText(scanCount+"(盒)");

		tvInput=(TextView)findViewById(R.id.tvInput);
		tvInput.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//手动输入条形码
				showInputDialog();
			}
		});
	}
	/**
	 * 扫码下部分的listview的adapter
	 */
	class ScanAdapter extends BaseAdapter {
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
				view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_capture,null);
				holder=new ScanAdapter.ScanHolder();
				holder.tv_code=(TextView)view.findViewById(R.id.tv_code);
				holder.tv_type=(TextView)view.findViewById(R.id.tv_type);
				holder.tv_count=(TextView)view.findViewById(R.id.tv_count);
				holder.tv_serialNumber=(TextView)view.findViewById(R.id.tv_serialNumber);
				holder.iv_del=(ImageView) view.findViewById(R.id.iv_del);
				holder.rl_serialNumber=(RelativeLayout) view.findViewById(R.id.rl_serialNumber);
				view.setTag(holder);
			}
			holder= (ScanHolder) view.getTag();
			final CartSQLBean product = getItem(i);
			holder.tv_code.setText(product.getProductCode()+"  ");
			holder.tv_type.setText(getType(product.getProductType())+"");
			holder.tv_count.setText("【"+product.getProCount()+"盒】");
			if (storage.equals("no")){
				holder.tv_serialNumber.setVisibility(View.VISIBLE);
				if (product.getSerialNumber().trim().equals("")){
					holder.tv_serialNumber.setText("请输入序列号");
				}else{
					holder.tv_serialNumber.setText(product.getSerialNumber()+"");
				}
				holder.tv_serialNumber.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						showSerialNumberDialog(product.getProductCode(),product.getSerialNumber());
					}
				});
			}else{
				holder.tv_serialNumber.setVisibility(View.GONE);
				holder.rl_serialNumber.setVisibility(View.GONE);
			}
			holder.iv_del.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					showDelDialog(product.getProductCode()+"");
				}
			});
			return view;
		}

		private String getType(String productType) {
			//type=1=箱码;type=2=盒码
			if (productType.equals("1")){
				return "【箱码】";
			}else{
				return "【盒码】";
			}
		}

		class ScanHolder{
			TextView tv_code;
			TextView tv_type;
			TextView tv_count;
			TextView tv_serialNumber;
			ImageView iv_del;
			RelativeLayout rl_serialNumber;
		}
	}
	private void showDelDialog(final String code) {
		AlertDialog.Builder builder=new AlertDialog.Builder(CaptureActivity.this);
		builder.setMessage("删除("+code+")?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				//连续扫码
				continuePreview();
				mUserDataBaseOperate.deleteUserByCode(code);
				List<CartSQLBean> products=mUserDataBaseOperate.findUserById(productId+"");
				Collections.reverse(products);
				lvScan.setAdapter(new ScanAdapter(products));
				scanCount=0;
				for (int a=0;a<products.size();a++){
					scanCount=scanCount+products.get(a).getProCount();
				}
				tv_scanCount.setText(scanCount+"(盒)");
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				//连续扫码
				continuePreview();
				dialogInterface.dismiss();
			}
		});
		builder.create().show();
	}
	private void showSerialNumberDialog(final String productCode, final String serialNumber) {
		final EditText et = new EditText(this);
		et.setBackgroundResource(R.drawable.bg_batch);
		et.setText(serialNumber);
		et.setMaxLines(1);
		et.setSingleLine(true);
		AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("请设置序列号")
				.setView(et)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String input = et.getText().toString();
						if (input.equals("")) {
							Toast.makeText(getApplicationContext(), "还未设置序列号！" + input, Toast.LENGTH_LONG).show();
						} else {
							List<CartSQLBean> bean = mUserDataBaseOperate.findUserByName(productCode);
							CartSQLBean one = bean.get(0);
							one.setSerialNumber(input);
							mUserDataBaseOperate.updateUser(one);
							List<CartSQLBean> products = mUserDataBaseOperate.findAll();
							Collections.reverse(products);
							lvScan.setAdapter(new ScanAdapter(products));
							dialog.dismiss();
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
		setDialogWindowAttr(serialDialog);
	}
	private void showInputDialog() {
		final EditText et = new EditText(this);
		et.setBackgroundResource(R.drawable.bg_batch);
		et.setMaxLines(1);
		AlertDialog serialDialog = new AlertDialog.Builder(this).setTitle("请输入条形码")
				.setView(et)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String input = et.getText().toString();
						if (input.equals("")) {
							Toast.makeText(getApplicationContext(), "还未设置序列号！" + input, Toast.LENGTH_LONG).show();
						} else {
							getScanCode(input);
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
		setDialogWindowAttr(serialDialog);
	}
	private void showBiggerDialog(String biggerMessage) {
		AlertDialog.Builder builder=new AlertDialog.Builder(CaptureActivity.this);
		builder.setMessage(biggerMessage+"");
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                finish();
//            }
//        });
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				//连续扫码
				continuePreview();
				dialogInterface.dismiss();
			}
		});
		builder.create().show();
	}
	private void showBatchDialog() {
		final EditText et = new EditText(this);
		et.setBackgroundResource(R.drawable.bg_batch);
		et.setText(batchNumber);
		et.setMaxLines(1);
		et.setSingleLine(true);
		AlertDialog batchDialog = new AlertDialog.Builder(this).setTitle("请设置批号")
				.setView(et)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String input = et.getText().toString();
						if (input.equals("")) {
							Toast.makeText(getApplicationContext(), "还未设置批号！" + input, Toast.LENGTH_LONG).show();
							showBatchDialog();
						} else {
							batchNumber = input;
							tvBatchNumber.setText("批号:" + batchNumber);
							tvBatchNumber.setVisibility(View.VISIBLE);
							dialog.dismiss();
						}
					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						finish();
					}
				})
				.setCancelable(false)
				.show();
		setDialogWindowAttr(batchDialog);
	}
	private void showOKDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(CaptureActivity.this);
		builder.setMessage("商品扫描完成");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		});
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
		builder.create().show();
	}
	private void showFailDialog(String failMessage) {
		AlertDialog.Builder builder=new AlertDialog.Builder(CaptureActivity.this);
		builder.setMessage(failMessage+"");
//        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                finish();
//            }
//        });
		builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				//连续扫码
				continuePreview();
				dialogInterface.dismiss();
			}
		});
		builder.create().show();
	}
	private void getScanCode(final String code) {
//        Toast.makeText(CaptureActivity.this,""+code,Toast.LENGTH_SHORT).show();
		if (storage.equals("no")){
			call= RetrofitService.getInstance().scanCode(new MyToken(CaptureActivity.this).getToken()+"",
					Integer.parseInt(invoiceid),Integer.parseInt(productId),code,Integer.parseInt(type));
		}else{
			call=RetrofitService.getInstance().storageScanCode(new MyToken(CaptureActivity.this).getToken()+"",
					Integer.parseInt(type),Integer.parseInt(productId),code);
		}
		call.enqueue(new Callback<ScanCode>() {
			@Override
			public void onResponse(Response<ScanCode> response, Retrofit retrofit) {
				if (response.body()==null){
					Toast.makeText(CaptureActivity.this,"服务器错误",Toast.LENGTH_SHORT).show();
					continuePreview();
					return;
				}
				if (response.body().getStatus()==0){
//                    Toast.makeText(CaptureActivity.this,invoiceid+","+productId+","+code+","+type,Toast.LENGTH_SHORT).show();
					if (response.body().getInfo().getIsRight()==1){
						//IsRight--条码是否正确，优先进行判断，如果是 0，则代表条码不存在，
						//如果是 1，则代表条码存在
						//成功，添加到数据库（productId,code,type,count）type=1=箱码;type=2=盒码
						List<CartSQLBean> sqlCode =mUserDataBaseOperate.findUserByName(code+"");
						if (sqlCode.isEmpty()){
							CartSQLBean sqlBean=new CartSQLBean();
							sqlBean.setProductId(productId+"");
							sqlBean.setProductCode(code+"");
							sqlBean.setProductType(response.body().getInfo().getType()+"");
							sqlBean.setSerialNumber(response.body().getInfo().getSerialNumber()+"");
							sqlBean.setBatchNumber(batchNumber+"");
							sqlBean.setProCount(response.body().getInfo().getCount());
							mUserDataBaseOperate.insertToUser(sqlBean);
							List<CartSQLBean> products=mUserDataBaseOperate.findUserById(productId+"");
							Collections.reverse(products);
							lvScan.setAdapter(new ScanAdapter(products));
							scanCount=0;
							for (int i=0;i<products.size();i++){
								scanCount=scanCount+products.get(i).getProCount();
							}
							tv_scanCount.setText(scanCount+"(盒)");
							try {
								Thread.sleep(1000);
								continuePreview();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}else{
//                            Toast.makeText(CaptureActivity.this,"该商品码已经扫过",Toast.LENGTH_SHORT).show();
							showFailDialog("该商品码已经扫过");
							try {
								Thread.sleep(1000);
								continuePreview();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}else{
						//失败
//                        Toast.makeText(CaptureActivity.this,"不存在该商品,请重试",Toast.LENGTH_SHORT).show();
						showFailDialog("不存在该商品,请重试");
						try {
							Thread.sleep(1000);
							continuePreview();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}else{
//                    Toast.makeText(CaptureActivity.this,""+response.body().getMes(),Toast.LENGTH_SHORT).show();
					showFailDialog(""+response.body().getMes());
					try {
						Thread.sleep(1000);
						continuePreview();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(Throwable throwable) {
				Toast.makeText(CaptureActivity.this,""+throwable,Toast.LENGTH_SHORT).show();
				continuePreview();
			}
		});
	}
	//在dialog.show()之后调用
	public void setDialogWindowAttr(Dialog dlg){
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;
		int width = dm.widthPixels;
		Window window = dlg.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.width = 4*(width/5);//宽高可设置具体大小
		lp.height = 2*(height/7);
		dlg.getWindow().setAttributes(lp);
	}
	/**
	 * 使Zxing能够继续扫描
	 */
	public void continuePreview() {
		if (handler != null) {
			handler.restartPreviewAndDecode();
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();

		// 相机初始化的动作需要开启相机并测量屏幕大小，这些操作
		// 不建议放到onCreate中，因为如果在onCreate中加上首次启动展示帮助信息的代码的 话，
		// 会导致扫描窗口的尺寸计算有误的bug
		cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.capture_viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);

		handler = null;
		lastResult = null;

		// 摄像头预览功能必须借助SurfaceView，因此也需要在一开始对其进行初始化
		// 如果需要了解SurfaceView的原理
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview_view); // 预览
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		}
		else {
			// 防止sdk8的设备初始化预览异常
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

			// Install the callback and wait for surfaceCreated() to init the camera
			surfaceHolder.addCallback(this);
		}

		// 加载声音配置，其实在BeemManager的构造器中也会调用该方法，即在onCreate的时候会调用一次
		beepManager.updatePrefs();

		// 启动闪光灯调节器
		ambientLightManager.start(cameraManager);

		// 恢复活动监控器
		inactivityTimer.onResume();

		source = IntentSource.NONE;
		decodeFormats = null;
		characterSet = null;
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		ambientLightManager.stop();
		beepManager.close();

		// 关闭摄像头
		cameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if ((source == IntentSource.NONE) && lastResult != null) { // 重新进行扫描
					restartPreviewAfterDelay(0L);
					return true;
				}
				break;
			case KeyEvent.KEYCODE_FOCUS:
			case KeyEvent.KEYCODE_CAMERA:
				// Handle these events so they don't launch the Camera app
				return true;

			case KeyEvent.KEYCODE_VOLUME_UP:
				cameraManager.zoomIn();
				return true;

			case KeyEvent.KEYCODE_VOLUME_DOWN:
				cameraManager.zoomOut();
				return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (resultCode == RESULT_OK) {
			final ProgressDialog progressDialog;
			switch (requestCode) {
				case REQUEST_CODE:

					// 获取选中图片的路径
					Cursor cursor = getContentResolver().query(
							intent.getData(), null, null, null, null);
					if (cursor.moveToFirst()) {
						photoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					}
					cursor.close();

					progressDialog = new ProgressDialog(this);
					progressDialog.setMessage("正在扫描...");
					progressDialog.setCancelable(false);
					progressDialog.show();

					new Thread(new Runnable() {

						@Override
						public void run() {

							Bitmap img = BitmapUtils.getCompressedBitmap(photoPath);

							BitmapDecoder decoder = new BitmapDecoder(CaptureActivity.this);
							Result result = decoder.getRawResult(img);

							if (result != null) {
								Message m = mHandler.obtainMessage();
								m.what = PARSE_BARCODE_SUC;
								m.obj = ResultParser.parseResult(result).toString();
								mHandler.sendMessage(m);
							}
							else {
								Message m = mHandler.obtainMessage();
								m.what = PARSE_BARCODE_FAIL;
								mHandler.sendMessage(m);
							}

							progressDialog.dismiss();

						}
					}).start();

					break;

			}
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG,"*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		/*hasSurface = false;*/
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 * 
	 * @param rawResult
	 *            The contents of the barcode.
	 * @param scaleFactor
	 *            amount by which thumbnail was scaled
	 * @param barcode
	 *            A greyscale bitmap of the camera data which was decoded.
	 */
	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {

		// 重新计时
		inactivityTimer.onActivity();
//
//		lastResult = rawResult;
//
//		beepManager.playBeepSoundAndVibrate();
//		String message= ResultParser.parseResult(rawResult).toString();
//		Intent intent = new Intent();
//		Bundle bunble=new Bundle();
//		bunble.putString("data", message);
//		intent.putExtras(bunble);
//        setResult(100, intent);
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                finish();
//            }
//        }, 400);
		boolean fromLiveScan = barcode != null;
		//这里处理解码完成后的结果，此处将参数回传到Activity处理
		if (fromLiveScan) {
			beepManager.playBeepSoundAndVibrate();

//            Toast.makeText(this, "扫描成功", Toast.LENGTH_SHORT).show();
//            text.setText(rawResult.getText()+""+barcode);
//            Intent intent = getIntent();
//            intent.putExtra("codedContent", rawResult.getText());
//            intent.putExtra("codedBitmap", barcode);
//            setResult(RESULT_OK, intent);
//            finish();
			//连续扫码
//            continuePreview();
			scanCount=0;
			List<CartSQLBean> products=mUserDataBaseOperate.findUserById(productId+"");
			for (int a=0;a<products.size();a++){
				scanCount=scanCount+products.get(a).getProCount();
			}
			if (Integer.parseInt(needCount+"")<=Integer.parseInt(scanCount+"")){
//                Toast.makeText(CaptureActivity.this,"商品已扫完",Toast.LENGTH_SHORT).show();
				showOKDialog();
			}else{
				getScanCode(rawResult.getText());
			}
//            Toast.makeText(CaptureActivity.this,""+rawResult.getText(),Toast.LENGTH_SHORT).show();
		}

	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
		resetStatusView();
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	private void resetStatusView() {
		viewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}

		if (cameraManager.isOpen()) {
			Log.w(TAG,
					"initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats,
						decodeHints, characterSet, cameraManager);
			}
			decodeOrStoreSavedBitmap(null, null);
		}
		catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		}
		catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	/**
	 * 向CaptureActivityHandler中发送消息，并展示扫描到的图像
	 * 
	 * @param bitmap
	 * @param result
	 */
	private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		// Bitmap isn't used yet -- will be used soon
		if (handler == null) {
			savedResultToShow = result;
		}
		else {
			if (result != null) {
				savedResultToShow = result;
			}
			if (savedResultToShow != null) {
				Message message = Message.obtain(handler,R.id.decode_succeeded, savedResultToShow);
				handler.sendMessage(message);
			}
			savedResultToShow = null;
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.capture_scan_photo: // 图片识别
				// 打开手机中的相册
				Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
				innerIntent.setType("image/*");
				Intent wrapperIntent = Intent.createChooser(innerIntent,"选择二维码图片");
				this.startActivityForResult(wrapperIntent, REQUEST_CODE);
				break;

			case R.id.capture_flashlight://是否开启闪光灯
				if (isFlashlightOpen) {
					cameraManager.setTorch(false); // 关闭闪光灯
					isFlashlightOpen = false;
				}
				else {
					cameraManager.setTorch(true); // 打开闪光灯
					isFlashlightOpen = true;
				}
				break;
			case R.id.capture_button_cancel:
				linearLayout_bottom.setVisibility(View.GONE);
				
			default:
				break;
		}

	}

}
