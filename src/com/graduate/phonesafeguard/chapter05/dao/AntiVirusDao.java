package com.graduate.phonesafeguard.chapter05.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntiVirusDao {
	public static String path="/data/data/com.graduate.phonesafeguard/files/antivirus.db";
	/**
	 * ���ĳ��md5�Ƿ��ǲ���
	 * return null ��ʾɨ�谲ȫ
	 */
	public static String checkVirus(String md5){
		String desc=null;
		//�򿪲������ݿ�
		SQLiteDatabase db=SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor=db.rawQuery("select desc from datable where md5=?", new String[]{md5});
		if(cursor.moveToNext()){
			desc=cursor.getString(0);
		}
		cursor.close();
		db.close();
		return desc;
		
	}
}
