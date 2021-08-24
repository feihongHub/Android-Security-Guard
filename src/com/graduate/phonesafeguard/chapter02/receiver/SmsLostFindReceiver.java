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
			//��ȡ��������ԱȨ��
			DevicePolicyManager dpm=(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			//���ܶ��Ž�������
			Object [] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objs) {
				//����ȡ������Ϣת���ɶ��Ŷ���
				SmsMessage smsMessage=SmsMessage.createFromPdu((byte[])object);
				String body = smsMessage.getMessageBody();
				String sender = smsMessage.getOriginatingAddress();
				String safephone = sp.getString("safephone", null);
//				 sender.equals(safephone)������
				
				//����ö����ǰ�ȫ���뷢����
				if(!TextUtils.isEmpty(safephone)){
					if("#*location*#".equals(body)){
						Log.i(TAG, "����λ����Ϣ");
						//��ȡλ�÷��ڷ�������ȥʵ��
						Intent service=new Intent(context,GPSLocationService.class);
						context.startService(service);
						abortBroadcast();
					}else if("#*alarm*#".equals(body)){
						Log.i(TAG, "���ű�������");
						MediaPlayer player=MediaPlayer.create(context, R.raw.teenager);
						player.setVolume(1.0f, 1.0f);
						player.setLooping(true);//����ѭ��
						player.start();
						abortBroadcast();
					}else if("#*wipedata*#".equals(body)){
						Log.i(TAG, "Զ��ɾ��");
						dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
						abortBroadcast();
					}else if("#*lockScreen*#".equals(body)){
						Log.i(TAG, "Զ������");
						dpm.resetPassword("123", 0);
						abortBroadcast();
					}
				}
			}
		}
	}

}
