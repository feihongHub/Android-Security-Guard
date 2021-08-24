package com.graduate.phonesafeguard.chapter03.reciever;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.graduate.phonesafeguard.chapter03.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.TelephonyManager;

public class InterceptCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		SharedPreferences mSp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean BlackNumStatus = mSp.getBoolean("BlackNumStatus", true);
		if(!BlackNumStatus){
			//���������ر�
			return;
		}
		BlackNumberDao dao=new BlackNumberDao(context);
		if(!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
			String mIncomingNumber="";
			//���������
			TelephonyManager tManager=(TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
			switch (tManager.getCallState()) {
			case TelephonyManager.CALL_STATE_RINGING:
				mIncomingNumber=intent.getStringExtra("incoming_number");
				
				int blackContactMode = dao.getBlackContactMode(mIncomingNumber);
//				System.out.println("blackContactMode"+blackContactMode);//���������
				if(blackContactMode==1||blackContactMode==3){
					//�۲���м�¼�ı仯
					//������м�¼���ɣ��ͰѺ��м�¼ɾ��
					Uri uri=Uri.parse("content://call_log/calls");
					context.getContentResolver().registerContentObserver(uri, true, new CallLogObserver(new Handler(), mIncomingNumber, context));
					endCall(context);
				}
				break;
			}
		}
	}
	//ͨ�������ṩ�߹۲����ݿ�仯
	private class CallLogObserver extends ContentObserver{
		private String incomingNumber;
		private Context context;
		public CallLogObserver(Handler handler,String incomingNumber,Context context) {
			super(handler);
			// TODO Auto-generated constructor stub
			this.incomingNumber=incomingNumber;
			this.context=context;
		}
		//�۲����ݿ����ݱ仯���÷���
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			System.out.println("CallLogObserver���м�¼���ݿ����ݱ仯��");
			context.getContentResolver().unregisterContentObserver(this);
			//ɾ��
			deleteCallLog(incomingNumber, context);
			super.onChange(selfChange);
		}
		
	}
	//���ͨ����¼
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
	 * �Ҷϵ绰,��Ҫ��������AIDL
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
