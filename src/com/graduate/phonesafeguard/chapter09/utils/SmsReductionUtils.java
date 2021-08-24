package com.graduate.phonesafeguard.chapter09.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.graduate.phonesafeguard.chapter09.entity.SmsInfo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

public class SmsReductionUtils {
	public interface SmsReductionCallBack{
		/**
		 * 在短信还原之前调用方法
		 * @param size
		 * 总的短信个数
		 */
		void beforeSmsReduction(int size);
		/**
		 * 当sms短信还原过程中调用的方法
		 * @param process当前的进度
		 */
		void onSmsReduction(int process);
	}
	private boolean flag=true;
	public void setFlag(boolean flag){
		this.flag=flag;
	}
	public boolean reductionSms(Activity context,SmsReductionCallBack callBack) throws XmlPullParserException, IOException{
		File file=new File(Environment.getExternalStorageDirectory(),"backup.xml");
		if(file.exists()){
			FileInputStream is=new FileInputStream(file);
			XmlPullParser parser=Xml.newPullParser();
			parser.setInput(is,"utf-8");
			SmsInfo smsInfo=null;
			int eventType = parser.getEventType();//获取时间类型
			Integer max=null;
			int progress=0;
			ContentResolver resolver = context.getContentResolver();
			Uri uri=Uri.parse("content://sms/");
			//判断节点类型
			while (eventType!=XmlPullParser.END_DOCUMENT & flag) {
				switch (eventType) {
				//一个节点开始
				case XmlPullParser.START_TAG:
					if("smss".equals(parser.getName())){
						String maxStr = parser.getAttributeValue(0);
						max=new Integer(maxStr);
						callBack.beforeSmsReduction(max);
					}else if("sms".equals(parser.getName())){
						smsInfo=new SmsInfo();
					}else if("body".equals(parser.getName())){
						//乱码问题有待解决
//						try {
//							smsInfo.body = Crypto.decrypt("123", parser.nextText());
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//							//此条信息还原失败
//							smsInfo.body="短信还原失败";
//						} 
						smsInfo.body=parser.nextText();
					}else if("address".equals(parser.getName())){
						smsInfo.address=parser.nextText();
					}else if("type".equals(parser.getName())){
						smsInfo.type=parser.nextText();
					}else if("date".equals(parser.getName())){
						smsInfo.date=parser.nextText();
					}
					break;
					//一个节点的结束
				case XmlPullParser.END_TAG:
					if("sms".equals(parser.getName())){
						//向短信数据库中插入一条数据
						ContentValues values=new ContentValues();
						values.put("address", smsInfo.address);
						values.put("type", smsInfo.type);
						values.put("date", smsInfo.date);
						values.put("body", smsInfo.body);
						resolver.insert(uri, values);
						smsInfo=null;
						progress++;
						callBack.onSmsReduction(progress);
					}
					break;
				}
				//得到下一个节点的事件类型，此行代码一定不能忘否则会死循环
				eventType=parser.next();
			}
			//防止出现在备份未完成的情况下，还原短信
			if(eventType==XmlPullParser.END_DOCUMENT & max!=null){
				if(progress<max){
					callBack.onSmsReduction(max);
				}
			}
		}else{
			//如果backup.xml不存在，则说明没有备份短信
			UIUtils.showToast(context, "您还没有备份短信");
		}
		return flag;
	}
	
}
