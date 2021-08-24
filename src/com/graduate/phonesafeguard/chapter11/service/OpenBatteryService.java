package com.graduate.phonesafeguard.chapter11.service;

import java.util.ArrayList;  

import com.graduate.phonesafeguard.R;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OpenBatteryService extends Service {
	public MediaPlayer player;
	private String phoneNumber;
	private String smsContent;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		MyBatteryReceiver myBatteryReceiver = new MyBatteryReceiver();
		IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(myBatteryReceiver, filter);
		player=MediaPlayer.create(getApplicationContext(), R.raw.whisper);
		//player.setLooping(true);
		super.onCreate();
	}
	//�ݻ�
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	//��������
	public void doPlay() {
		// TODO Auto-generated method stub
		player.start();
	}

	private class MyBatteryReceiver extends BroadcastReceiver{
		public int flag=0;

		//		public int percent;
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int current = intent.getExtras().getInt("level");//��ǰ����
			int total = intent.getExtras().getInt("scale");//�ܵ���
			int percent=current*100/total;
			SmsManager sms=SmsManager.getDefault();
			phoneNumber="520520520";
			if((percent==95 || percent==57 || percent==15 || percent==5) && flag==0){
				switch (percent) {
				case 95:
					smsContent="�������㣬�뾡��ӻ��ɣ�";
					break;
				case 57:
					smsContent="��Ŷ�����ĵ���ʣ����룬�۽�Լ�����";
//					System.out.println("��Ŷ�����ĵ���ʣ����룬�۽�Լ�����");
//					System.out.println("��Ŷ�����ĵ���ʣ����룬�۽�Լ�����");
					break;
				case 15:
					smsContent="���������ˣ�ע����Ŷ~";
					break;
				case 5:
					smsContent="�Ͻ��洢��Ҫ�ļ���Ҫ�ػ���~";
					break;
				default:
					break;
				}		
				sendSms(phoneNumber, smsContent);  
				player.start();
				writeToDataBase(phoneNumber, smsContent);
				//��ʶ����ʾ�������ŷ���������
				flag=1;
			}

		}

	}

	//�������޸�1
	private void sendSms(String phoneNumber, String smsContent) {   
		//���ı������޶��ַ����ȵ�ʱ������70��Ӣ��160��,��2.2�л�nullpoint��4.1.1�з�����Ч  
		SmsManager smsManager = SmsManager.getDefault();  
		smsManager.sendTextMessage(phoneNumber, null, smsContent, null, null);  
		//        ��ΪsendMultipartTextMessage()  

		ArrayList<String> messageArray = smsManager.divideMessage(smsContent);  
		smsManager.sendMultipartTextMessage(phoneNumber, null, messageArray, null, null);  

		Toast.makeText(this, "Send Success", Toast.LENGTH_LONG).show();  
	}
	//д����ſ��ļ�
	private void writeToDataBase(String phoneNumber, String smsContent)  {
		ContentValues values = new ContentValues();  
		values.put("address", phoneNumber);  
		values.put("body", smsContent);  
		values.put("type", "1");  
		values.put("read", "0");//"1"means has read ,1��ʾ�Ѷ�  /inbox
		getContentResolver().insert(Uri.parse("content://sms"), values);  
	}  

}
