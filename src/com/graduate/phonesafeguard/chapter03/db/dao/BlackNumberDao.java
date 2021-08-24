package com.graduate.phonesafeguard.chapter03.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.graduate.phonesafeguard.chapter03.db.BlackNumberOpenHelper;
import com.graduate.phonesafeguard.chapter03.entity.BlackContactInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

public class BlackNumberDao {
	private BlackNumberOpenHelper blackNumberOpenHelper;
	private static BlackNumberDao isInstance=null;
	public BlackNumberDao(Context context) {
		super();
		// TODO Auto-generated constructor stub
		blackNumberOpenHelper=new BlackNumberOpenHelper(context);
	}
	/**
	 * 公开获取对象,单例模式
	 */
	public static BlackNumberDao getInstance(Context context){
		if(isInstance==null){
			synchronized(BlackNumberDao.class){
				if(isInstance==null){
					isInstance=new BlackNumberDao(context);
				}
			}
		}
		return isInstance;
		
	}
	/**
	 * 添加数据
	 */
	public boolean add(BlackContactInfo blackContactInfo){
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		ContentValues values =new ContentValues();
		if(blackContactInfo.phoneNumber.startsWith("+86")){
			blackContactInfo.phoneNumber=blackContactInfo.phoneNumber.
					substring(3,blackContactInfo.phoneNumber.length());
		}
		values.put("number", blackContactInfo.phoneNumber);
		values.put("name", blackContactInfo.contactName);
		values.put("mode", blackContactInfo.mode);
		long rowid = db.insert("blacknumber", null, values);
		if(rowid==-1){
			return false;
		}else{
			return true;
		}
		
	}
	/**
	 * 删除数据
	 */
	public boolean delete(BlackContactInfo blackContactInfo){
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		int rownumber = db.delete("blacknumber", "number=?", new String[]{blackContactInfo.phoneNumber});
		if(rownumber==0){
			//删除数据不成功
			return false;
		}else{
			return true;
		}
	}
	//判断可读数据库中是否存在于黑名单中
	public boolean IsNumberExist(String number){
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("blacknumber", null, "number=?", new String[]{number}, null, null, null);
		if(cursor.moveToNext()){
			cursor.close();
			db.close();
			return true;
		}else{
			cursor.close();
			db.close();
			return false;
		}

	}
	/**
	 * 分页查找数据库记录
	 */
	public List<BlackContactInfo> getPageBlackNumber(int pagenumber,int pagesize){
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select number,mode,name from blacknumber limit ? offset ?", new String[]{String.valueOf(pagesize),String.valueOf(pagesize*pagenumber)});
		List<BlackContactInfo> mBlackContactInfos=new ArrayList<BlackContactInfo>();
		while (cursor.moveToNext()) {
			BlackContactInfo info=new BlackContactInfo();
			info.phoneNumber=cursor.getString(0);
			info.mode=cursor.getInt(1);
			info.contactName=cursor.getString(2);
			mBlackContactInfos.add(info);
		}
		cursor.close();
		db.close();
		SystemClock.sleep(30);
		return mBlackContactInfos;
		
	}
	/**
	 * 根据号码查询黑名单信息
	 */
	public int getBlackContactMode(String number){
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
		System.out.println("执行");
		int mode=0;
		if(cursor.moveToNext()){
			System.out.println("数据库中表："+mode);
			mode=cursor.getInt(cursor.getColumnIndex("mode"));
		}
		cursor.close();
		db.close();
		return mode;
		
	}
	/**
	 * 获取数据库总条目
	 */
	public int getTotalNumber(){
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
		cursor.moveToNext();
		int count=cursor.getInt(0);
		cursor.close();
		db.close();
		return count;
		
	}
	
}
