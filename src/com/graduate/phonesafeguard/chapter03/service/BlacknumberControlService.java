package com.graduate.phonesafeguard.chapter03.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.graduate.phonesafeguard.chapter03.db.dao.BlackNumberDao;
//import com.graduate.phonesafeguard.chapter03.reciever.InterceptCallReceiver.CallLogObserver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class BlacknumberControlService extends Service {
	
	private InterceptSmsReceiver mReceiver;
	private InterceptCallReceiver mPhoneReceiver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mReceiver = new InterceptSmsReceiver();
		IntentFilter smsfilter = new IntentFilter();
		smsfilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		smsfilter.setPriority(Integer.MAX_VALUE);
		registerReceiver(mReceiver, smsfilter);
		mPhoneReceiver = new InterceptCallReceiver();
		IntentFilter telfilter=new IntentFilter();
		telfilter.addAction("android.intent.action.PHONE_STATE");
		telfilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		telfilter.setPriority(Integer.MAX_VALUE);
		registerReceiver(mPhoneReceiver, telfilter);
		System.out.println("开启");
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
		unregisterReceiver(mPhoneReceiver);
		System.out.println("关闭");
	}
	class InterceptSmsReceiver extends BroadcastReceiver {

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
//				if(sender.startsWith("1555521")){
//					sender=sender.substring(7,sender.length());
//				}
				int mode = dao.getBlackContactMode(sender);
//				System.out.println("类型:"+mode);
				if(mode==2 || mode==3){
					//需要拦截短信，拦截广播
					System.out.println("类型:"+mode);
					abortBroadcast();
				}
			}
		}
	}
	//电话拦截
	class InterceptCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			SharedPreferences mSp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
			boolean BlackNumStatus = mSp.getBoolean("BlackNumStatus", true);
			if(!BlackNumStatus){
				//黑名单被关闭
				return;
			}
			BlackNumberDao dao=new BlackNumberDao(context);
			if(!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
				String mIncomingNumber="";
				//如果是来电
				TelephonyManager tManager=(TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
				switch (tManager.getCallState()) {
				case TelephonyManager.CALL_STATE_RINGING:
					mIncomingNumber=intent.getStringExtra("incoming_number");
					
					int blackContactMode = dao.getBlackContactMode(mIncomingNumber);
//					System.out.println("blackContactMode"+blackContactMode);//方法体测试
					if(blackContactMode==1||blackContactMode==3){
						//观察呼叫记录的变化
						//如果呼叫记录生成，就把呼叫记录删掉
						Uri uri=Uri.parse("content://call_log/calls");
						context.getContentResolver().registerContentObserver(uri, true, new CallLogObserver(new Handler(), mIncomingNumber, context));
						endCall(context);
					}
					break;
				}
			}
		}
		//通过内容提供者观察数据库变化
		private class CallLogObserver extends ContentObserver{
			private String incomingNumber;
			private Context context;
			public CallLogObserver(Handler handler,String incomingNumber,Context context) {
				super(handler);
				// TODO Auto-generated constructor stub
				this.incomingNumber=incomingNumber;
				this.context=context;
			}
			//观察数据库内容变化调用方法
			@Override
			public void onChange(boolean selfChange) {
				// TODO Auto-generated method stub
				System.out.println("CallLogObserver呼叫记录数据库内容变化了");
				context.getContentResolver().unregisterContentObserver(this);
				//删除
				deleteCallLog(incomingNumber, context);
				super.onChange(selfChange);
			}
			
		}
		//清除通话记录
		public void deleteCallLog(String incomingNumber,Context context){
			ContentResolver resolver=context.getContentResolver();
			Uri uri=Uri.parse("content://call_log/calls");
			Cursor cursor=resolver.query(uri, new String[]{"_id"}, "number=?", new String[]{incomingNumber}, "_id desc limit 1");
			if(cursor.moveToNext()){
				String id = cursor.getString(0);
				resolver.delete(uri, "_id=?", new String[]{id});
			}
		}
		/**
		 * 挂断电话,需要复制两个AIDL
		 */
		public void endCall(Context context){
			try {
				Class clazz = context.getClassLoader().loadClass("android.os.ServiceManager");
				Method method = clazz.getDeclaredMethod("getService", String.class);
				IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
				ITelephony itelephony=ITelephony.Stub.asInterface(iBinder);
				itelephony.endCall();
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
