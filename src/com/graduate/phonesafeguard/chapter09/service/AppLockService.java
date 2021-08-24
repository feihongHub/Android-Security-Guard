package com.graduate.phonesafeguard.chapter09.service;

import java.util.List;

import com.graduate.phonesafeguard.chapter09.EnterPswActivity;
import com.graduate.phonesafeguard.chapter09.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

public class AppLockService extends Service {

	private AppLockDao dao;
	private boolean flag=false;
	private MyObserver observer;
	private Uri uri=Uri.parse("content://com.graduate.phonesafeguard.applock");
	private List<String> packagenames;
	private ActivityManager am;
	private List<RunningTaskInfo> taskInfos;
	private RunningTaskInfo taskInfo;
	private String packagename;
	private String tempStopProtectPackname;
	private Intent intent;
	private AppLockReceiver receiver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		dao = new AppLockDao(this);
		observer = new MyObserver(new Handler());
		//ע������ߣ������������ݿ�ı仯
		getContentResolver().registerContentObserver(uri, true, observer);
		//��ȡ���ݿ��е����а���,�Ż�
		packagenames = dao.findAll();
		receiver = new AppLockReceiver();
		IntentFilter filter=new IntentFilter("com.graduate.phonesafeguard.applock");
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, filter);
		//��ȡActivityManager����
		am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
		startApplockService();
		super.onCreate();
	}
	/**
	 * ������س������
	 * @author Administrator
	 * ���Ź�
	 *uses-permission android:name="android.permission.GET_TASKS"��ҪȨ��
	 */
	private void startApplockService(){
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//�����߳����еı�ʶ��
				flag=true;
				while(flag){
					//��ȡ��ǰ���е�����ջ��ʹ�õĴ򿪵�����ջ�ڼ�����ǰ��
					taskInfos = am.getRunningTasks(1);
					//�õ����µ�����ջ
					taskInfo = taskInfos.get(0);
					//��ȡ��ǰ����ջ�����ڵİ���
					packagename = taskInfo.topActivity.getPackageName();
					//�ж�������Ƿ���Ҫ������
					if(packagenames.contains(packagename)){
						//�жϵ�ǰӦ�ó����Ƿ���Ҫ��ʱֹͣ������������ȷ���룩
						if(!packagename.equals(tempStopProtectPackname)){
							//��Ҫ����������һ�������������,����Intentʵ������������������ҳ��
							intent=new Intent(AppLockService.this,EnterPswActivity.class);
							System.out.println("�������");
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra("packagename", packagename);
							startActivity(intent);
						}
					}
					//ϵͳ˯��
					SystemClock.sleep(30);
				}		
			};
		}.start();
	}
	/**
	 * �㲥������
	 * @author Administrator
	 *
	 */
	class AppLockReceiver extends BroadcastReceiver{
		//������֤���̹㲥��ÿ�δ�һ������
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if("com.graduate.phonesafeguard.applock".equals(intent.getAction())){
				//��������
				tempStopProtectPackname = intent.getStringExtra("packagename");
			}else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
				tempStopProtectPackname=null;
				//ֹͣ��س���
				flag=false;
			}else if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
				//������س���
				if(flag==false){
					startApplockService();
				}
			}
		}
		
	}
	//���ݹ۲�
	class MyObserver extends ContentObserver{
		public MyObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void onChange(boolean selfChange) {
			packagenames = dao.findAll();
			super.onChange(selfChange);
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		flag=false;
		unregisterReceiver(receiver);
		receiver=null;
		//ע�����ݿ�ļ���
		getContentResolver().unregisterContentObserver(observer);
		observer=null;
		super.onDestroy();
	}

}
