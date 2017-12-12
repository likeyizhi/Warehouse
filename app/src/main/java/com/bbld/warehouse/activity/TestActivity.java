package com.bbld.warehouse.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;
import com.bbld.warehouse.base.Constants;
import com.bbld.warehouse.loading.WeiboDialogUtils;
import com.bbld.warehouse.utils.MyToken;
import com.bbld.warehouse.utils.SettingImage;
import com.bbld.warehouse.utils.UploadUtil;
import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;

/**
 * Created by likey on 2017/12/8.
 */

public class TestActivity extends BaseActivity{
    @BindView(R.id.iv01)
    ImageView iv01;
    @BindView(R.id.iv02)
    ImageView iv02;
    @BindView(R.id.iv03)
    ImageView iv03;
    @BindView(R.id.iv04)
    ImageView iv04;
    @BindView(R.id.iv05)
    ImageView iv05;
    @BindView(R.id.iv06)
    ImageView iv06;
    @BindView(R.id.iv07)
    ImageView iv07;
    @BindView(R.id.iv08)
    ImageView iv08;
    @BindView(R.id.iv09)
    ImageView iv09;
    @BindView(R.id.btnUp)
    Button btnUp;

    private String file_imgPath01="";
    private String file_imgPath02="";
    private String file_imgPath03="";
    private String file_imgPath04="";
    private String file_imgPath05="";
    private String file_imgPath06="";
    private String file_imgPath07="";
    private String file_imgPath08="";
    private String file_imgPath09="";
    private String checkedPosition;
    private static final int REQUEST_EXTERNAL_STORAGE = 321;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    private static final int SELECT_PIC_KITKAT = 49;
    private static final int IMAGE_REQUEST_CODE = 50;
    private static final int CAMERA_REQUEST_CODE = 51;
    private static final int RESULT_REQUEST_CODE = 52;
    private String[] items = new String[]{"选择本地图片", "拍照"};
    private String requestImg;
    private Dialog loadDialog;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    WeiboDialogUtils.closeDialog(loadDialog);
                    showToast(""+requestImg);
                    break;
                case 222:
                    WeiboDialogUtils.closeDialog(loadDialog);
                    showToast(""+requestImg);
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialog=WeiboDialogUtils.createLoadingDialog(TestActivity.this,"上传...");
                final Map<String, String> params = new HashMap<String, String>();
                final Map<String, File> files = new TreeMap<String, File>();
                if (!file_imgPath01.equals("")){
                    files.put("image01",new File(file_imgPath01));
                }
                if (!file_imgPath02.equals("")){
                    files.put("image02",new File(file_imgPath02));
                }
                if (!file_imgPath03.equals("")){
                    files.put("image03",new File(file_imgPath03));
                }
                if (!file_imgPath04.equals("")){
                    files.put("image04",new File(file_imgPath04));
                }
                if (!file_imgPath05.equals("")){
                    files.put("image05",new File(file_imgPath05));
                }
                if (!file_imgPath06.equals("")){
                    files.put("image06",new File(file_imgPath06));
                }
                if (!file_imgPath07.equals("")){
                    files.put("image07",new File(file_imgPath07));
                }
                if (!file_imgPath08.equals("")){
                    files.put("image08",new File(file_imgPath08));
                }
                if (!file_imgPath09.equals("")){
                    files.put("image09",new File(file_imgPath09));
                }
                Log.i("params", "params="+params);
                Log.i("files", "files="+files);
                final String requestURL = Constants.BASE_URL + "Values/OrderCommitError";
                Log.i("requestURL", "requestURL="+requestURL);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            requestImg = UploadUtil.post(requestURL, params, files);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if ((requestImg+"").contains("成功")) { // 请求成功
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
        });
    }

    public void addImage(View v){
        switch (v.getId()){
            case R.id.iv01:
//                showToast("01");
                checkedPosition="01";
                break;
            case R.id.iv02:
//                showToast("02");
                checkedPosition="02";
                break;
            case R.id.iv03:
//                showToast("03");
                checkedPosition="03";
                break;
            case R.id.iv04:
//                showToast("04");
                checkedPosition="04";
                break;
            case R.id.iv05:
//                showToast("05");
                checkedPosition="05";
                break;
            case R.id.iv06:
//                showToast("06");
                checkedPosition="06";
                break;
            case R.id.iv07:
//                showToast("07");
                checkedPosition="07";
                break;
            case R.id.iv08:
//                showToast("08");
                checkedPosition="08";
                break;
            case R.id.iv09:
//                showToast("09");
                checkedPosition="09";
                break;
        }
        showAddImgDialog();
        verifyStoragePermissions(this);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * 添加图片
     * 显示选择对话框
     */
    private void showAddImgDialog() {
        new AlertDialog.Builder(this)
                .setTitle("修改方式")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0 :
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                                intentFromGallery.addCategory(Intent.CATEGORY_OPENABLE);
                                intentFromGallery.setType("image/jpeg"); // 设置文件类型
                                if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){
                                    startActivityForResult(intentFromGallery, SELECT_PIC_KITKAT);
                                }else{
                                    startActivityForResult(intentFromGallery,IMAGE_REQUEST_CODE);
                                }
                                break;
                            case 1 :
                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                String state = Environment
                                        .getExternalStorageState();
                                if (state.equals(Environment.MEDIA_MOUNTED)) {
                                    File path = Environment
                                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                    File file = new File(path, "wearhouse"+checkedPosition+".jpg");
                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(file));
                                }
                                startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE :
//                    startPhotoZoom(data.getData());
                    getImageToView(data.getData(),"图库");
                    break;
                case SELECT_PIC_KITKAT :
//                    startPhotoZoom(data.getData());
                    getImageToView(data.getData(),"图库");
                    break;
                case CAMERA_REQUEST_CODE :
                    // 判断存储卡是否可以用，可用进行存储
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File path = Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path, "wearhouse"+checkedPosition+".jpg");
//                        startPhotoZoom(Uri.fromFile(tempFile));
                        getImageToView(Uri.fromFile(tempFile),"相机");
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RESULT_REQUEST_CODE : // 图片缩放完成后
                    if (data != null) {
//                        getImageToView(data);
                    }
                    break;
            }
        }
    }

    public String getTime(){
        long time=System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳
        String str=String.valueOf(time);
        return str;
    }

    private void getImageToView(Uri data, String type){
        if (data != null) {
            Bitmap photo;
            if (type.equals("相机")){
                File file = null;
                if (null != data && data != null) {
                    file = getFileFromMediaUri(TestActivity.this, data);
                }
                Bitmap photoBmp = null;
                try {
                    photoBmp = getBitmapFormUri(TestActivity.this, Uri.fromFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int degree = getBitmapDegree(file.getAbsolutePath());
                /**
                 * 把图片旋转为正的方向
                 */
                photo = rotateBitmapByDegree(photoBmp, degree);
            }else{
                photo = null;
                try {
                    photo = getBitmapFormUri(TestActivity.this, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            switch (checkedPosition){
                case "01":
                    SettingImage settingImage01 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath01=settingImage01.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath01).error(R.mipmap.xiuzhneg).into(iv01);
                    iv02.setVisibility(View.VISIBLE);
                    break;
                case "02":
                    SettingImage settingImage02 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath02=settingImage02.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath02).error(R.mipmap.xiuzhneg).into(iv02);
                    iv03.setVisibility(View.VISIBLE);
                    break;
                case "03":
                    SettingImage settingImage03 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath03=settingImage03.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath03).error(R.mipmap.xiuzhneg).into(iv03);
                    iv04.setVisibility(View.VISIBLE);
                    break;
                case "04":
                    SettingImage settingImage04 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath04=settingImage04.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath04).error(R.mipmap.xiuzhneg).into(iv04);
                    iv05.setVisibility(View.VISIBLE);
                    break;
                case "05":
                    SettingImage settingImage05 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath05=settingImage05.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath05).error(R.mipmap.xiuzhneg).into(iv05);
                    iv06.setVisibility(View.VISIBLE);
                    break;
                case "06":
                    SettingImage settingImage06 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath06=settingImage06.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath06).error(R.mipmap.xiuzhneg).into(iv06);
                    iv07.setVisibility(View.VISIBLE);
                    break;
                case "07":
                    SettingImage settingImage07 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath07=settingImage07.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath07).error(R.mipmap.xiuzhneg).into(iv07);
                    iv08.setVisibility(View.VISIBLE);
                    break;
                case "08":
                    SettingImage settingImage08 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath08=settingImage08.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath08).error(R.mipmap.xiuzhneg).into(iv08);
                    iv09.setVisibility(View.VISIBLE);
                    break;
                case "09":
                    SettingImage settingImage09 = new SettingImage(photo, "wearhouse"+getTime());
                    file_imgPath09=settingImage09.imagePath();
                    Glide.with(getApplicationContext()).load(file_imgPath09).error(R.mipmap.xiuzhneg).into(iv09);
                    break;
            }
//            switch (checkedPosition) {
//                case "01":
//                    SettingImage settingImage1 = new SettingImage(photo, "file_img1");
//                    file_imgPath1 = settingImage1.imagePath();
//                    iv_img1.setImageBitmap(photo);
////                    Toast.makeText(OrderCommentActivity.this,""+file_imgPath1,Toast.LENGTH_SHORT).show();
//                    break;
//                case 2:
//                    SettingImage settingImage2 = new SettingImage(photo, "file_img2");
//                    file_imgPath2 = settingImage2.imagePath();
//                    iv_img2.setImageBitmap(photo);
////                    Toast.makeText(OrderCommentActivity.this,""+file_imgPath2,Toast.LENGTH_SHORT).show();
//                    break;
//                case 3:
//                    SettingImage settingImage3 = new SettingImage(photo, "file_img3");
//                    file_imgPath3 = settingImage3.imagePath();
//                    iv_img3.setImageBitmap(photo);
////                    Toast.makeText(OrderCommentActivity.this,""+file_imgPath3,Toast.LENGTH_SHORT).show();
//                    break;
//            }
        }
    }

    /**
     * 通过Uri获取文件
     * @param ac
     * @param uri
     * @return
     */
    public static File getFileFromMediaUri(Context ac, Uri uri) {
        if(uri.getScheme().toString().compareTo("content") == 0){
            ContentResolver cr = ac.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
            if (cursor != null) {
                cursor.moveToFirst();
                String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路径
                cursor.close();
                if (filePath != null) {
                    return new File(filePath);
                }
            }
        }else if(uri.getScheme().toString().compareTo("file") == 0){
            return new File(uri.toString().replace("file://",""));
        }
        return null;
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(TestActivity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以800x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 800f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_test;
    }
}
