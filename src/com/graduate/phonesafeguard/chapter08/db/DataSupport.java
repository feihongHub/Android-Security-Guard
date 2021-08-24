package com.graduate.phonesafeguard.chapter08.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <pre>
 * ҵ����:
 * ����˵��: Ϊ���������ṩ����
 * ��д����:	2016-7-30

 * 
 * ��ʷ��¼
 * 1���޸����ڣ�
 *    �޸��ˣ�
 *    �޸����ݣ�
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
		//�������ݱ�
		db.execSQL("create table traffictable (_id integer primary key autoincrement,date datetime not null ,traffic integer ,type text,typename text,history text)");
		//��־���ݱ�
		db.execSQL("create table signtable (_id integer primary key autoincrement,date datetime not null ,flagtype text,flagtypename text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//���֮ǰ�еĻ��Ͱ����ɵ�
		db.execSQL("drop table if exists traffictable");
		onCreate(db);
	}
	/**
	 * 
	 * ����˵������ѯ�������ж��ٴιػ�����
	 * 
	 * @param type
	 * @param history
	 * @return
	 */
	public Cursor selectday(String type, String history) {
		SQLiteDatabase db = getReadableDatabase();
		//��ѯ���쵽�������������������
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
	 * ����˵������ѯ���µ�����
	 * 
	 * @param type
	 * @return
	 */
	public Cursor selectNow(String type) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from traffictable where _id in (select max(_id) from traffictable where type = ?)",
				new String[] { type });
		//��IDֵΪ����ʱ���������
		return cursor;
	}
	/**
	 * 
	 * ����˵�������Ƿ��ǵ�һ�ΰ�װ
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
	 * ����˵������ѯ����ʱ���֮�����С����
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
	 * ����˵��������ʱ���֮����������
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
	 * ����˵������������
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
	 * ����˵����������йػ���־������
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
