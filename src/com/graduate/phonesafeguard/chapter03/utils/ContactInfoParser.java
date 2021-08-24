package com.graduate.phonesafeguard.chapter03.utils;
import java.util.ArrayList;
import java.util.List;

import com.graduate.phonesafeguard.chapter03.entity.ContactInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ContactInfoParser {

	//解析联系人
	public static List<ContactInfo> getSystemContact(Context context){
		ContentResolver resolver=context.getContentResolver();
		Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
		Uri datauri=Uri.parse("content://com.android.contacts/data");
		List<ContactInfo> infos=new ArrayList<ContactInfo>();
		Cursor cursor=resolver.query(uri, new String []{"contact_id"}, null, null, null);
		while(cursor.moveToNext()){
			String id = cursor.getString(0);
			if(id!=null){
				System.out.println("联系人id"+id);
				ContactInfo info=new ContactInfo();
				info.id=id;
				//根据联系人的id，查询data表，把这个id的数据取出来
				//系统Api查询data表的时候，是查询data表的视图，不是真正data表
				Cursor dataCursor=resolver.query(datauri, new String []{"data1","mimetype"} , "raw_contact_id=?", new String[]{id},null);
				while(dataCursor.moveToNext()){
					String data1 = dataCursor.getString(0);
					String mimetype = dataCursor.getString(1);
					if("vnd.android.cursor.item/name".equals(mimetype)){
						System.out.println("姓名="+data1);
						info.name=data1;
					}else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
						System.out.println("电话="+data1);
						info.phone=data1;
					}
				}
				infos.add(info);
				dataCursor.close();
			}
		}
		cursor.close();
		return infos;

	}

}
