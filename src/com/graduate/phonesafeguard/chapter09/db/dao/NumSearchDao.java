package com.graduate.phonesafeguard.chapter09.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.graduate.phonesafeguard.chapter09.entity.ChildInfo;
import com.graduate.phonesafeguard.chapter09.entity.GroupInfo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class NumSearchDao {
	public static final String path="/data/data/com.graduate.phonesafeguard/files/commonnum.db";
	/**
	 * 获取组内信息
	 */
	public static List<GroupInfo> getNumberGroup(){
		
		SQLiteDatabase db=SQLiteDatabase.openDatabase(path, null,SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.query("classlist", new String[]{"name","idx"}, null, null, null, null, null);
		List<GroupInfo> list=new ArrayList<GroupInfo>();
		while (cursor.moveToNext()) {
			GroupInfo info=new GroupInfo();
			String name=cursor.getString(0);
			String idx=cursor.getString(1);
			info.name=name;
			info.idx=idx;
			info.childInfo=getChildInfo(idx, db);
			list.add(info);
		}
		cursor.close();
		db.close();
		return list;
	}
	/**
	 * 获取组内孩子的信息
	 */
	public static List<ChildInfo> getChildInfo(String idx,SQLiteDatabase db){
		Cursor cursor = db.query("table"+idx, new String[]{"number","name"}, null, null, null, null, null);
		List<ChildInfo> list=new ArrayList<ChildInfo>();
		while (cursor.moveToNext()) {
			ChildInfo info=new ChildInfo();
			info.number=cursor.getString(0);
			info.name=cursor.getString(1);
			list.add(info);
		}
		return list;
	}

}
