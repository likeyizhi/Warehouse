package com.bbld.warehouse.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.bbld.warehouse.bean.CartSQLBean;

import java.util.ArrayList;
import java.util.List;


public class UserDataBaseOperate {

	private static final String TAG = "DBRemoteLive";
	private static final boolean DEBUG = true;

	protected SQLiteDatabase mDB = null;

	public UserDataBaseOperate(SQLiteDatabase db) {
		if (null == db) {
			throw new NullPointerException("The db cannot be null.");
		}
		mDB = db;
	}

	public long insertToUser(CartSQLBean cart) {
		ContentValues values = new ContentValues();
		values.put(UserSQLiteOpenHelper.COL_PRODUCT_ID, cart.getProductId());
		values.put(UserSQLiteOpenHelper.COL_PRODUCT_CODE, cart.getProductCode());
		values.put(UserSQLiteOpenHelper.COL_PRODUCT_TYPE, cart.getProductType());
		values.put(UserSQLiteOpenHelper.COL_PRODUCT_COUNT, cart.getProCount());
		return mDB.insert(UserSQLiteOpenHelper.DATABASE_TABLE_USER, null,
				values);
	}

	public long updateUser(CartSQLBean cart) {
		ContentValues values = new ContentValues();
		values.put(UserSQLiteOpenHelper.COL_PRODUCT_ID, cart.getProductId());
		values.put(UserSQLiteOpenHelper.COL_PRODUCT_CODE, cart.getProductCode());
		values.put(UserSQLiteOpenHelper.COL_PRODUCT_TYPE, cart.getProductType());
		values.put(UserSQLiteOpenHelper.COL_PRODUCT_COUNT, cart.getProCount());
		return mDB.update(UserSQLiteOpenHelper.DATABASE_TABLE_USER, values,
				"productid=?", new String[] { ""+cart.getProductId() });
	}

	// clear databases
	public long deleteAll() {
		return mDB.delete(UserSQLiteOpenHelper.DATABASE_TABLE_USER, null, null);
	}

	public long deleteUserByCode(String productcode){
		
		return mDB.delete("warehouse_info", "productcode=?", new String[]{productcode});
	}

	public long getCount(String conditions, String[] args) {
		long count = 0;
		if (TextUtils.isEmpty(conditions)) {
			conditions = " 1 = 1 ";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(1) AS count FROM ");
		builder.append(UserSQLiteOpenHelper.DATABASE_TABLE_USER).append(" ");
		builder.append("WHERE ");
		builder.append(conditions);
		if (DEBUG)
			Log.d(TAG, "SQL: " + builder.toString());
		Cursor cursor = mDB.rawQuery(builder.toString(), args);
		if (null != cursor) {
			if (cursor.moveToNext()) {
				count = cursor.getLong(cursor.getColumnIndex("count"));
			}
			cursor.close();
		}
		return count;
	}
	public List<CartSQLBean> findAll() {
		List<CartSQLBean> cartList = new ArrayList<CartSQLBean>();
		//order by modifytime desc
		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
				null, null, null, null, null, UserSQLiteOpenHelper.COL_ID
						+ " asc");
		if (null != cursor) {
			while (cursor.moveToNext()) {
				CartSQLBean cart = new CartSQLBean();
				cart.setProductId(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_ID)));
				cart.setProductCode(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_CODE)));
				cart.setProductType(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_TYPE)));
				cart.setProCount(cursor.getInt(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_COUNT)));
				cartList.add(cart);
			}
			cursor.close();
		}
		return cartList;
	}
	public CartSQLBean findUserLatest() {
		CartSQLBean cart = null;
		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
				null, null, null, null, null, UserSQLiteOpenHelper.COL_ID
						+ " asc");
		if (null != cursor) {
			if (cursor.moveToFirst()) {
				cart = new CartSQLBean();
				cart.setProductId(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_ID)));
				cart.setProductCode(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_CODE)));
				cart.setProductType(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_TYPE)));
				cart.setProCount(cursor.getInt(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_COUNT)));
			}
			cursor.close();
		}
		return cart;
	}

	public List<CartSQLBean> findUserByName(String code) {

		List<CartSQLBean> cartList = new ArrayList<CartSQLBean>();
		//模糊查询			
//		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
//				null, UserSQLiteOpenHelper.COL_PRODUCT_ID + " like?",
//				new String[] {"%"+productId+"%"}, null, null, UserSQLiteOpenHelper.COL_ID
//				+ " asc");
		
		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
			null, UserSQLiteOpenHelper.COL_PRODUCT_CODE + " =?",
			new String[] {code}, null, null, UserSQLiteOpenHelper.COL_ID
			+ " asc");
		
		//多个条件查询
//		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
//				null, UserSQLiteOpenHelper.COL_NAME + " like?"+" and "+UserSQLiteOpenHelper.COL_ID+" >?",
//				new String[] {"%"+name+"%",2+""}, null, null, UserSQLiteOpenHelper.COL_ID
//				+ " desc");
		if (null != cursor) {
			while (cursor.moveToNext()) {
				CartSQLBean cart = new CartSQLBean();
				cart.setProductId(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_ID)));
				cart.setProductCode(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_CODE)));
				cart.setProductType(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_TYPE)));
				cart.setProCount(cursor.getInt(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_COUNT)));
				cartList.add(cart);
			}
			cursor.close();
		}
		return cartList;
	}
	public List<CartSQLBean> findUserById(String productId) {

		List<CartSQLBean> cartList = new ArrayList<CartSQLBean>();
		//模糊查询
//		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
//				null, UserSQLiteOpenHelper.COL_PRODUCT_ID + " like?",
//				new String[] {"%"+productId+"%"}, null, null, UserSQLiteOpenHelper.COL_ID
//				+ " asc");

		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
			null, UserSQLiteOpenHelper.COL_PRODUCT_ID + " =?",
			new String[] {productId}, null, null, UserSQLiteOpenHelper.COL_ID
			+ " asc");

		//多个条件查询
//		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
//				null, UserSQLiteOpenHelper.COL_NAME + " like?"+" and "+UserSQLiteOpenHelper.COL_ID+" >?",
//				new String[] {"%"+name+"%",2+""}, null, null, UserSQLiteOpenHelper.COL_ID
//				+ " desc");
		if (null != cursor) {
			while (cursor.moveToNext()) {
				CartSQLBean cart = new CartSQLBean();
				cart.setProductId(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_ID)));
				cart.setProductCode(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_CODE)));
				cart.setProductType(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_TYPE)));
				cart.setProCount(cursor.getInt(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_PRODUCT_COUNT)));
				cartList.add(cart);
			}
			cursor.close();
		}
		return cartList;
	}
}
