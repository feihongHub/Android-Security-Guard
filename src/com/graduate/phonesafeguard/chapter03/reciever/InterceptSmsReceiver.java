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
			//黑名单拦截关闭
			return;
		}
		BlackNumberDao dao=new BlackNumberDao(context);
		Object[] object = (Object[]) intent.getExtras().get("pdus");
		for (Object objs : object) {
			SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) objs);
			String sender = smsMessage.getOriginatingAddress();
			String body = smsMessage.getMessageBody();
			//根据短信内容只能拦截
			if(body.contains("fapiao")){
				abortBroadcast();
			}
			if(sender.startsWith("+86")){
				sender=sender.substring(3, sender.length());
			}
			/**
			 * 模拟机测试自动填充号码，真实手机前+86
			 */
//			if(sender.startsWith("1555521")){
//				sender=sender.substring(7,sender.length());
//			}
			int mode = dao.getBlackContactMode(sender);
//			System.out.println("类型:"+mode);
			if(mode==2 || mode==3){
				//需要拦截短信，拦截广播
				System.out.println("类型:"+mode);
				abortBroadcast();
			}
		}
	}

}
