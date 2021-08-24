package com.graduate.phonesafeguard.chapter09.db.dao;

import org.w3c.dom.Text;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * �����ز�ѯ���ݿ��װ
 * @author Administrator
 *
 */
public class NumBelongtoDao {
	public static String getLocation(String phonenumber){
		String location="δ֪����";
		SQLiteDatabase db=SQLiteDatabase.openDatabase("/data/data/com.graduate.phonesafeguard/files/address.db", null, 
				SQLiteDatabase.OPEN_READONLY);
		//�ж��Ƿ����ֻ�����
		//ͨ��������ʽƥ��ŶΣ�13X 14X 15X 17X 18X 
		//130 131 132 133 134 135 136 137 139
		if(phonenumber.matches("^1[34578]\\d{9}$")){
			//�ֻ�����Ĳ�ѯ
			Cursor cursor = db.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)", new String[]{phonenumber.substring(0, 7)});
			if(cursor.moveToFirst()){
				location=cursor.getString(0);
				System.out.println(location);
			}
			cursor.close();
		}else{//�����绰
			switch (phonenumber.length()) {
			case 3://110 120 119 121 999
				if("110".equals(phonenumber)){
					location="�˾�";
				}else if("120".equals(phonenumber)){
					location="����";
				}else{
					location="��������";
				}
				break;
			case 4:
				location="ģ����";
				break;
			case 5:
				location="�ͷ��绰";
				break;
			case 7:
				location="���ص绰";
				break;
			case 8:
				location="���ص绰";
				break;
			default:
				if(location.length()>=9 && location.startsWith("0")){
					String address=null;
					Cursor cursor = db.rawQuery("select location from data2 where area=?", new String[]{location.substring(1, 3)});
					if(cursor.moveToNext()){
						String str = cursor.getString(0);
						address=str.substring(0, str.length()-2);
					}
					cursor.close();
					cursor=db.rawQuery("select location from data2 where area=?", new String[]{location.substring(1, 4)});
					if(cursor.moveToNext()){
						String str = cursor.getString(0);
						address=str.substring(0, str.length()-2);
					}
					cursor.close();
					if(!TextUtils.isEmpty(address)){
						location=address;
					}
				}
				break;
			}
		}
		db.close();
		return location;
		
	}
}
