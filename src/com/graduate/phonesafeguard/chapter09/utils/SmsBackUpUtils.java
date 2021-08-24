package com.graduate.phonesafeguard.chapter09.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.lidroid.xutils.http.client.entity.BodyParamsEntity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

public class SmsBackUpUtils {
	/**
	 * �ȶ���һ���ӿڣ����ڻص�
	 */
	public interface BackupStatusCallback{
		/**
		 * ���ű���֮ǰ����
		 */
		void beforeSmsBackup(int size);
		/**
		 * sms���ű��ݹ����е��÷���
		 * @param process
		 */
		void onSmsBackup(int process);
	}
	private boolean flag=true;
	public void setFlag(boolean flag){
		this.flag=flag;
	}
	/**
	 * ���ݶ���,���䱣��Ϊxml�ļ�
	 * ���л�xml�ļ�
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 
	 */
	public boolean backUpSms(Context context,BackupStatusCallback callback) throws IllegalArgumentException, IllegalStateException, IOException {
		XmlSerializer serializer=Xml.newSerializer();
		//�����ļ���ָ��sd��Ŀ¼��
		File sdDir=Environment.getExternalStorageDirectory();
		//�õ�ʣ��ռ�
		long freesize=sdDir.getFreeSpace();
		//�����ļ�����Ϊbackup.xml
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && freesize>1024*10241){
			File file=new File(Environment.getExternalStorageDirectory(),"backup.xml");
				FileOutputStream os=new FileOutputStream(file);
				//��ʼ��xml�ļ����л���
				serializer.setOutput(os,"utf-8");
				//дxml�ļ���ͷ
				serializer.startDocument("utf-8", true);
				//д���ڵ�
				ContentResolver resolver = context.getContentResolver();
				Uri uri=Uri.parse("content://sms/");
				Cursor cursor = resolver.query(uri, new String[]{"address","body","type","date"}, null, null,null);
				//�õ��ܵ���Ŀ����
				int size = cursor.getCount();
				//���ý��ȵ��ܴ�С
				callback.beforeSmsBackup(size);
				serializer.startTag(null, "smss");
				serializer.attribute(null, "size", String.valueOf(size));
				int process=0;
				while (cursor.moveToNext() & flag) {
					serializer.startTag(null, "sms");
					serializer.startTag(null, "body");
//					//���ܻ����������⣬������������򱸷�ʧ��
//					try {
//						String bodyencpyt = Crypto.encrypt("123", cursor.getString(1));
//						serializer.text(bodyencpyt);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						serializer.text("���Ŷ�ȡʧ��");
//					}
					serializer.text(cursor.getString(1));
					serializer.endTag(null, "body");
					serializer.startTag(null, "address");
					serializer.text(cursor.getString(0));
					serializer.endTag(null, "address");
					serializer.startTag(null, "type");
					serializer.text(cursor.getString(2));
					serializer.endTag(null, "type");
					serializer.startTag(null, "date");
					serializer.text(cursor.getString(3));
					serializer.endTag(null, "date");
					serializer.endTag(null, "sms");
					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//���ý������Ի���
					process++;
					callback.onSmsBackup(process);
				}
				cursor.close();
				serializer.endTag(null, "smss");
				serializer.endDocument();
				os.flush();
				os.close();
				return flag;
		}else{
			throw new IllegalStateException("sd�������ڻ��߿ռ䲻��");
		}		
	}
}
