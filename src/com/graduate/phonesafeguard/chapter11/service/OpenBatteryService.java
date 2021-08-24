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
	//摧毁
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	//播放音乐
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
			int current = intent.getExtras().getInt("level");//当前电量
			int total = intent.getExtras().getInt("scale");//总电量
			int percent=current*100/total;
			SmsManager sms=SmsManager.getDefault();
			phoneNumber="520520520";
			if((percent==95 || percent==57 || percent==15 || percent==5) && flag==0){
				switch (percent) {
				case 95:
					smsContent="电量很足，请尽情挥霍吧！";
					break;
				case 57:
					smsContent="啊哦，您的电量剩余过半，咱节约点儿！";
//					System.out.println("啊哦，您的电量剩余过半，咱节约点儿！");
//					System.out.println("啊哦，您的电量剩余过半，咱节约点儿！");
					break;
				case 15:
					smsContent="电量不多了，注意充电哦~";
					break;
				case 5:
					smsContent="赶紧存储重要文件，要关机啦~";
					break;
				default:
					break;
				}		
				sendSms(phoneNumber, smsContent);  
				player.start();
				writeToDataBase(phoneNumber, smsContent);
				//标识符表示电量短信发送数量级
				flag=1;
			}

		}

	}

	//发短信修改1
	private void sendSms(String phoneNumber, String smsContent) {   
		//当文本超过限定字符长度的时候（中文70，英文160）,在2.2中会nullpoint，4.1.1中发送无效  
		SmsManager smsManager = SmsManager.getDefault();  
		smsManager.sendTextMessage(phoneNumber, null, smsContent, null, null);  
		//        改为sendMultipartTextMessage()  

		ArrayList<String> messageArray = smsManager.divideMessage(smsContent);  
		smsManager.sendMultipartTextMessage(phoneNumber, null, messageArray, null, null);  

		Toast.makeText(this, "Send Success", Toast.LENGTH_LONG).show();  
	}
	//写入短信库文件
	private void writeToDataBase(String phoneNumber, String smsContent)  {
		ContentValues values = new ContentValues();  
		values.put("address", phoneNumber);  
		values.put("body", smsContent);  
		values.put("type", "1");  
		values.put("read", "0");//"1"means has read ,1表示已读  /inbox
		getContentResolver().insert(Uri.parse("content://sms"), values);  
	}  

}
