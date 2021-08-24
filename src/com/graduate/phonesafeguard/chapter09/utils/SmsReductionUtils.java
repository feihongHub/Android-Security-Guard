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
		 * �ڶ��Ż�ԭ֮ǰ���÷���
		 * @param size
		 * �ܵĶ��Ÿ���
		 */
		void beforeSmsReduction(int size);
		/**
		 * ��sms���Ż�ԭ�����е��õķ���
		 * @param process��ǰ�Ľ���
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
			int eventType = parser.getEventType();//��ȡʱ������
			Integer max=null;
			int progress=0;
			ContentResolver resolver = context.getContentResolver();
			Uri uri=Uri.parse("content://sms/");
			//�жϽڵ�����
			while (eventType!=XmlPullParser.END_DOCUMENT & flag) {
				switch (eventType) {
				//һ���ڵ㿪ʼ
				case XmlPullParser.START_TAG:
					if("smss".equals(parser.getName())){
						String maxStr = parser.getAttributeValue(0);
						max=new Integer(maxStr);
						callBack.beforeSmsReduction(max);
					}else if("sms".equals(parser.getName())){
						smsInfo=new SmsInfo();
					}else if("body".equals(parser.getName())){
						//���������д����
//						try {
//							smsInfo.body = Crypto.decrypt("123", parser.nextText());
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//							//������Ϣ��ԭʧ��
//							smsInfo.body="���Ż�ԭʧ��";
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
					//һ���ڵ�Ľ���
				case XmlPullParser.END_TAG:
					if("sms".equals(parser.getName())){
						//��������ݿ��в���һ������
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
				//�õ���һ���ڵ���¼����ͣ����д���һ���������������ѭ��
				eventType=parser.next();
			}
			//��ֹ�����ڱ���δ��ɵ�����£���ԭ����
			if(eventType==XmlPullParser.END_DOCUMENT & max!=null){
				if(progress<max){
					callBack.onSmsReduction(max);
				}
			}
		}else{
			//���backup.xml�����ڣ���˵��û�б��ݶ���
			UIUtils.showToast(context, "����û�б��ݶ���");
		}
		return flag;
	}
	
}
