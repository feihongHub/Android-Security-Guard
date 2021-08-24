package com.graduate.phonesafeguard.chapter03.reciever;

import com.graduate.phonesafeguard.chapter03.db.dao.BlackNumberDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;

public class InterceptSmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		SharedPreferences mSp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean BlackNumStatus = mSp.getBoolean("BlackNumStatus", true);
		if(!BlackNumStatus){
			//���������عر�
			return;
		}
		BlackNumberDao dao=new BlackNumberDao(context);
		Object[] object = (Object[]) intent.getExtras().get("pdus");
		for (Object objs : object) {
			SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) objs);
			String sender = smsMessage.getOriginatingAddress();
			String body = smsMessage.getMessageBody();
			//���ݶ�������ֻ������
			if(body.contains("fapiao")){
				abortBroadcast();
			}
			if(sender.startsWith("+86")){
				sender=sender.substring(3, sender.length());
			}
			/**
			 * ģ��������Զ������룬��ʵ�ֻ�ǰ+86
			 */
//			if(sender.startsWith("1555521")){
//				sender=sender.substring(7,sender.length());
//			}
			int mode = dao.getBlackContactMode(sender);
//			System.out.println("����:"+mode);
			if(mode==2 || mode==3){
				//��Ҫ���ض��ţ����ع㲥
				System.out.println("����:"+mode);
				abortBroadcast();
			}
		}
	}

}
