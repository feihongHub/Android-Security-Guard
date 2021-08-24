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
	 * 先定义一个接口，用于回调
	 */
	public interface BackupStatusCallback{
		/**
		 * 短信备份之前调用
		 */
		void beforeSmsBackup(int size);
		/**
		 * sms短信备份过程中调用方法
		 * @param process
		 */
		void onSmsBackup(int process);
	}
	private boolean flag=true;
	public void setFlag(boolean flag){
		this.flag=flag;
	}
	/**
	 * 备份短信,将其保存为xml文件
	 * 序列化xml文件
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 
	 */
	public boolean backUpSms(Context context,BackupStatusCallback callback) throws IllegalArgumentException, IllegalStateException, IOException {
		XmlSerializer serializer=Xml.newSerializer();
		//建立文件，指向sd卡目录下
		File sdDir=Environment.getExternalStorageDirectory();
		//得到剩余空间
		long freesize=sdDir.getFreeSpace();
		//建立文件名字为backup.xml
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && freesize>1024*10241){
			File file=new File(Environment.getExternalStorageDirectory(),"backup.xml");
				FileOutputStream os=new FileOutputStream(file);
				//初始化xml文件序列化器
				serializer.setOutput(os,"utf-8");
				//写xml文件的头
				serializer.startDocument("utf-8", true);
				//写根节点
				ContentResolver resolver = context.getContentResolver();
				Uri uri=Uri.parse("content://sms/");
				Cursor cursor = resolver.query(uri, new String[]{"address","body","type","date"}, null, null,null);
				//得到总的条目个数
				int size = cursor.getCount();
				//设置进度的总大小
				callback.beforeSmsBackup(size);
				serializer.startTag(null, "smss");
				serializer.attribute(null, "size", String.valueOf(size));
				int process=0;
				while (cursor.moveToNext() & flag) {
					serializer.startTag(null, "sms");
					serializer.startTag(null, "body");
//					//可能会有乱码问题，如果出现乱码则备份失败
//					try {
//						String bodyencpyt = Crypto.encrypt("123", cursor.getString(1));
//						serializer.text(bodyencpyt);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						serializer.text("短信读取失败");
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
					//设置进度条对话框
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
			throw new IllegalStateException("sd卡不存在或者空间不足");
		}		
	}
}
