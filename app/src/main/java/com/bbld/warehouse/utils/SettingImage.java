package com.bbld.warehouse.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SettingImage {
	private Bitmap bitmap;
	private String picName;
	public SettingImage(Bitmap bitmap, String picName) {
		super();
		this.bitmap = bitmap;
		this.picName = picName;
	}
	public SettingImage(Bitmap bitmap) {
		super();
		this.bitmap = bitmap;
	}
	
	public String imagePath(){
		AsyncTask<Bitmap, Bitmap, String> task=new settingImageAsyncTask().execute();
		String imagePath=null;
		try {
			imagePath=task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imagePath;
	}
//
//	public Bitmap smallBitmap(){
//		AsyncTask<Bitmap, Bitmap, Bitmap> task=new smallBitmapAsyncTask().execute();
//		Bitmap rebitmap=null;
//		try {
//			rebitmap = task.get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return rebitmap;
//
//	}
//	private class smallBitmapAsyncTask extends AsyncTask<Bitmap, Bitmap, Bitmap>{
//		@Override
//		protected Bitmap doInBackground(Bitmap... params) {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
////			int options = 100;
////			while ( baos.toByteArray().length / 1024>1024 ) {
////				baos.reset();
////				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
////				options -= 10;
////			}
//			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//			Bitmap smallBitmap = BitmapFactory.decodeStream(isBm, null, null);
//			return smallBitmap;
//		}
//
//	}
	
	private class settingImageAsyncTask extends AsyncTask<Bitmap, Bitmap, String> {
		/**
		 * ѹ��ͼƬ
		 */
		@Override
		protected String doInBackground(Bitmap... params) {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
//			int options = 0;
//			while ( baos.toByteArray().length / 1024>1024 ) {  //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
//				baos.reset();//����baos�����baos
//				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ��������ݴ�ŵ�baos��
//				options -= 10;//ÿ�ζ�����10
//			}
//			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ���������baos��ŵ�ByteArrayInputStream��
//			Bitmap smallBitmap = BitmapFactory.decodeStream(isBm, null, null);//��ByteArrayInputStream��������ͼƬ
			
			/**
			 * 保存图片
			 */
			File f = new File("/sdcard/", picName);
			if (f.exists()) {
				f.delete();
			}
			try {
				FileOutputStream out = new FileOutputStream(f);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "/sdcard/"+picName;
		}
	}
}
