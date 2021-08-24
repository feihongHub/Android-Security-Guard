package com.graduate.phonesafeguard.chapter05.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntiVirusDao {
	public static String path="/data/data/com.graduate.phonesafeguard/files/antivirus.db";
	/**
	 * 检查某个md5是否是病毒
	 * return null 表示扫描安全
	 */
	public static String checkVirus(String md5){
		String desc=null;
		//打开病毒数据库
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
