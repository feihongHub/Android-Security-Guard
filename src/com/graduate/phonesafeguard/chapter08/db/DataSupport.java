package com.graduate.phonesafeguard.chapter08.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <pre>
 * 业务名:
 * 功能说明: 为整个程序提供数据
 * 编写日期:	2016-7-30

 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class DataSupport extends SQLiteOpenHelper {

	public DataSupport(Context context) {
		super(context, "trafficdata.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//流量数据表
		db.execSQL("create table traffictable (_id integer primary key autoincrement,date datetime not null ,traffic integer ,type text,typename text,history text)");
		//标志数据表
		db.execSQL("create table signtable (_id integer primary key autoincrement,date datetime not null ,flagtype text,flagtypename text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//如果之前有的话就把它干掉
		db.execSQL("drop table if exists traffictable");
		onCreate(db);
	}
	/**
	 * 
	 * 方法说明：查询并曾经有多少次关机保存
	 * 
	 * @param type
	 * @param history
	 * @return
	 */
	public Cursor selectday(String type, String history) {
		SQLiteDatabase db = getReadableDatabase();
		//查询哪天到哪天的流量总数，类型
		Cursor cursor = db.rawQuery("select date,typename,traffic from traffictable where type = ? and history=?", new String[] { type, history });
		return cursor;
	}
	public Cursor selectbetweenday(String type, String history,String datestart, String dateover) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select date,typename,traffic from traffictable where type = ? and history=? and date between datetime(?) and datetime(?)",
				new String[] { type, history,datestart, dateover });
		
		return cursor;
	}
	/**
	 * 
	 * 方法说明：查询最新的数据
	 * 
	 * @param type
	 * @return
	 */
	public Cursor selectNow(String type) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from traffictable where _id in (select max(_id) from traffictable where type = ?)",
				new String[] { type });
		//当ID值为最大的时候是最近的
		return cursor;
	}
	/**
	 * 
	 * 方法说明：看是否是第一次安装
	 * 
	 * @param flagtype
	 * @return
	 */
	public Cursor selectsign(String flagtype) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from signtable where flagtype = ?", new String[] { flagtype });
		return cursor;
	}
	/**
	 * 
	 * 方法说明：查询两个时间段之间的最小数据
	 * 
	 * @param datestart
	 * @param datestop
	 * @param type
	 * @return
	 */
	public Cursor selectBettweenstart(String datestart, String datestop,String type) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from traffictable where _id in (select min(_id) from traffictable where type=? and date between datetime(?) and datetime(?))",
				new String[] { type, datestart,datestop });
		return cursor;
	}
	/**
	 * 
	 * 方法说明：两个时间段之间最大的数据
	 * 
	 * @param datestart
	 * @param datestop
	 * @param type
	 * @return
	 */
	public Cursor selectBettweenstop(String datestart, String datestop,String type) {

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from traffictable where _id in (select max(_id)from traffictable where type=? and date between datetime(?) and datetime(?))",
				 new String[] { type, datestart,datestop });
		
		return cursor;
	}
	/**
	 * 
	 * 方法说明：插入数据
	 * 
	 * @param liuliang
	 * @param type
	 * @param typename
	 * @param history
	 */
	public void insertNow(long traffic, String type, String typename,
			String history) {
		SQLiteDatabase db = getWritableDatabase();
		String insertstr = "insert into traffictable(date,traffic,type,typename,history) values(datetime('now'),?,?,?,?) ";
		db.execSQL(insertstr,
				new Object[] { traffic, type, typename, history });
		db.close();
	}
	/**
	 * 
	 * 方法说明：插入带有关机标志的数据
	 * 
	 * @param flagtype
	 * @param flagtypename
	 */
	public void insertsign(String flagtype, String flagtypename) {
		SQLiteDatabase db = getWritableDatabase();
		String insertbiaozhi = "insert into signtable(date,flagtype,flagtypename) values(datetime('now'),?,?) ";
		db.execSQL(insertbiaozhi, new Object[] { flagtype, flagtypename });
		db.close();
	}


}
