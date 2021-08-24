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
 * ���������ݿ�����߼���
 * @author Administrator
 *
 */
public class AppLockDao {
	private AppLockOpenHelper openHelper;
	private Context context;
	private Uri uri=Uri.parse("content://com.graduate.phonesafeguard.applock");

	/**
	 * ���췽��
	 */

	public AppLockDao(Context context) {
		super();
		this.context = context;
		openHelper=new AppLockOpenHelper(context);
	}
	/**
	 * �������
	 */
	public boolean insert(String packagename){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("packagename", packagename);
		long rowId = db.insert("applock", null, values);
		if(rowId==-1){//���벻�ɹ�
			return false;
		}else{
			//����ɹ�,���ݷ����仯ʱ����,֪ͨ���ݿⷢ���仯
			context.getContentResolver().notifyChange(uri, null);
			return true;
		}
	}
	/**
	 * ɾ��һ������
	 */
	public boolean delete(String packagename){
		SQLiteDatabase db=openHelper.getWritableDatabase();
		int rownum = db.delete("applock", "packagename=?", new String[]{packagename});
		if(rownum==0){
			return false;
		}else{
			//֪ͨ���ݿ��ѷ����仯
			context.getContentResolver().notifyChange(uri, null);
			return true;
		}
	}
	/**
	 * ��ѯĳ�������Ƿ����
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
	 * ��ѯ�������еİ���
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
