package com.graduate.phonesafeguard.chapter02.receiver;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter02.service.GPSLocationService;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode.Mode;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class SmsLostFindReceiver extends BroadcastReceiver {
	private static final String TAG=SmsLostFindReceiver.class.getSimpleName();
	private SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		sp = context.getSharedPreferences("config", Activity.MODE_PRIVATE);
		boolean protecting = sp.getBoolean("protecting", true);
		if(protecting){
			//获取超级管理员权限
			DevicePolicyManager dpm=(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			//接受短信解析短信
			Object [] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objs) {
				//将获取到的信息转换成短信对象
				SmsMessage smsMessage=SmsMessage.createFromPdu((byte[])object);
				String body = smsMessage.getMessageBody();
				String sender = smsMessage.getOriginatingAddress();
				String safephone = sp.getString("safephone", null);
//				 sender.equals(safephone)看需求
				
				//如果该短信是安全号码发出的
				if(!TextUtils.isEmpty(safephone)){
					if("#*location*#".equals(body)){
						Log.i(TAG, "返回位置信息");
						//获取位置放在服务里面去实现
						Intent service=new Intent(context,GPSLocationService.class);
						context.startService(service);
						abortBroadcast();
					}else if("#*alarm*#".equals(body)){
						Log.i(TAG, "播放报警音乐");
						MediaPlayer player=MediaPlayer.create(context, R.raw.teenager);
						player.setVolume(1.0f, 1.0f);
						player.setLooping(true);//单曲循环
						player.start();
						abortBroadcast();
					}else if("#*wipedata*#".equals(body)){
						Log.i(TAG, "远程删除");
						dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
						abortBroadcast();
					}else if("#*lockScreen*#".equals(body)){
						Log.i(TAG, "远程锁屏");
						dpm.resetPassword("123", 0);
						abortBroadcast();
					}
				}
			}
		}
	}

}
