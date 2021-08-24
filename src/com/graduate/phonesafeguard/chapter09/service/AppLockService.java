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
		//注册接收者，接受来自数据库的变化
		getContentResolver().registerContentObserver(uri, true, observer);
		//获取数据控中的所有包名,优化
		packagenames = dao.findAll();
		receiver = new AppLockReceiver();
		IntentFilter filter=new IntentFilter("com.graduate.phonesafeguard.applock");
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, filter);
		//获取ActivityManager对象
		am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
		startApplockService();
		super.onCreate();
	}
	/**
	 * 开启监控程序服务
	 * @author Administrator
	 * 看门狗
	 *uses-permission android:name="android.permission.GET_TASKS"需要权限
	 */
	private void startApplockService(){
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//控制线程运行的标识符
				flag=true;
				while(flag){
					//获取当前运行的任务栈，使用的打开的任务栈在集合最前面
					taskInfos = am.getRunningTasks(1);
					//拿到最新的任务栈
					taskInfo = taskInfos.get(0);
					//获取当前最新栈顶所在的包名
					packagename = taskInfo.topActivity.getPackageName();
					//判断这个包是否需要被保护
					if(packagenames.contains(packagename)){
						//判断当前应用程序是否需要临时停止保护（输入正确密码）
						if(!packagename.equals(tempStopProtectPackname)){
							//需要保护，弹出一个输入密码界面,建立Intent实例，用来打开输入密码页面
							intent=new Intent(AppLockService.this,EnterPswActivity.class);
							System.out.println("掉尼玛比");
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra("packagename", packagename);
							startActivity(intent);
						}
					}
					//系统睡眠
					SystemClock.sleep(30);
				}		
			};
		}.start();
	}
	/**
	 * 广播接收者
	 * @author Administrator
	 *
	 */
	class AppLockReceiver extends BroadcastReceiver{
		//跳过验证过程广播，每次传一个包名
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if("com.graduate.phonesafeguard.applock".equals(intent.getAction())){
				//跳过包名
				tempStopProtectPackname = intent.getStringExtra("packagename");
			}else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
				tempStopProtectPackname=null;
				//停止监控程序
				flag=false;
			}else if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
				//开启监控程序
				if(flag==false){
					startApplockService();
				}
			}
		}
		
	}
	//内容观察
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
		//注销数据库的监听
		getContentResolver().unregisterContentObserver(observer);
		observer=null;
		super.onDestroy();
	}

}
