package com.graduate.phonesafeguard.chapter02.utils;

import java.util.ArrayList;
import java.util.List;

import com.graduate.phonesafeguard.chapter02.entity.ContactInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ContactInfoParser {
	//������ϵ��
	public static List<ContactInfo> getSystemContact(Context context){
		ContentResolver resolver=context.getContentResolver();
		Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
		Uri datauri=Uri.parse("content://com.android.contacts/data");
		List<ContactInfo> infos=new ArrayList<ContactInfo>();
		Cursor cursor=resolver.query(uri, new String []{"contact_id"}, null, null, null);
		while(cursor.moveToNext()){
			String id = cursor.getString(0);
			if(id!=null){
				System.out.println("��ϵ��id"+id);
				ContactInfo info=new ContactInfo();
				info.id=id;
				//������ϵ�˵�id����ѯdata�������id������ȡ����
				//ϵͳApi��ѯdata���ʱ���ǲ�ѯdata�����ͼ����������data��
				Cursor dataCursor=resolver.query(datauri, new String []{"data1","mimetype"} , "raw_contact_id=?", new String[]{id},null);
				while(dataCursor.moveToNext()){
					String data1 = dataCursor.getString(0);
					String mimetype = dataCursor.getString(1);
					if("vnd.android.cursor.item/name".equals(mimetype)){
						System.out.println("����="+data1);
						info.name=data1;
					}else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
						System.out.println("�绰="+data1);
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
