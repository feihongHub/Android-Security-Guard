package com.graduate.phonesafeguard.chapter09.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.graduate.phonesafeguard.chapter09.db.AppLockOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * 程序锁数据库操作逻辑类
 * @author Administrator
 *
 */
public class AppLockDao {
	private AppLockOpenHelper openHelper;
	private Context context;
	private Uri uri=Uri.parse("content://com.graduate.phonesafeguard.applock");

	/**
	 * 构造方法
	 */

	public AppLockDao(Context context) {
		super();
		this.context = context;
		openHelper=new AppLockOpenHelper(context);
	}
	/**
	 * 添加数据
	 */
	public boolean insert(String packagename){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("packagename", packagename);
		long rowId = db.insert("applock", null, values);
		if(rowId==-1){//插入不成功
			return false;
		}else{
			//插入成功,数据发生变化时调用,通知数据库发生变化
			context.getContentResolver().notifyChange(uri, null);
			return true;
		}
	}
	/**
	 * 删除一条数据
	 */
	public boolean delete(String packagename){
		SQLiteDatabase db=openHelper.getWritableDatabase();
		int rownum = db.delete("applock", "packagename=?", new String[]{packagename});
		if(rownum==0){
			return false;
		}else{
			//通知数据库已发生变化
			context.getContentResolver().notifyChange(uri, null);
			return true;
		}
	}
	/**
	 * 查询某个包名是否存在
	 */
	public boolean find(String packagename){
		SQLiteDatabase db=openHelper.getWritableDatabase();
		Cursor cursor = db.query("applock", null, "packagename=?", new String[]{packagename}, null, null, null);
		if(cursor.moveToNext()){
			cursor.close();
			db.close();
			return true;
		}
		cursor.close();
		return false;
		
	}
	/**
	 * 查询表中所有的包名
	 */
	public List<String> findAll(){
		SQLiteDatabase db=openHelper.getWritableDatabase();
		Cursor cursor = db.query("applock", null, null, null, null, null, null);
		List<String> packages=new ArrayList<String>();
		while (cursor.moveToNext()) {
			String string = cursor.getString(cursor.getColumnIndex("packagename"));
			packages.add(string);
		}
		return packages;
		
	}

}
