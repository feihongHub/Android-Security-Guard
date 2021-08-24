package com.graduate.phonesafeguard.chapter11;

import java.util.ArrayList; 

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter10.utils.ServiceStatusUtils;
import com.graduate.phonesafeguard.chapter11.service.OpenBatteryService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BatteryObserveActivity extends Activity {
	private MyBatteryReceiver myBatteryReceiver;
	private Button button;

	private String phoneNumber;
	private String smsContent;
	public int percent;
//	public Intent intent;
	//	private SharedPreferences mSp;
	private boolean running;
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battery);
		init();
		openServer();
	}
	//初始化
	private void init() { 
		//mSp = getSharedPreferences("config", MODE_PRIVATE);

		button=(Button) findViewById(R.id.batter_server);
		running = ServiceStatusUtils.isServiceRunning("com.graduate.phonesafeguard.chapter11.service.OpenBatteryService", this);
		myBatteryReceiver = new MyBatteryReceiver();
		IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(myBatteryReceiver, filter);

	}
	private class MyBatteryReceiver extends BroadcastReceiver{
		public TextView battery_tv;
		public ImageView imageView;
		public TextView tv_show1,tv_show2,tv_show3;
		//		public int percent;
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int current = intent.getExtras().getInt("level");//当前电量
			int total = intent.getExtras().getInt("scale");//总电量
			percent = current*100/total;
			imageView=(ImageView) findViewById(R.id.battery_srcshow);
			battery_tv = (TextView) findViewById(R.id.Battery_show);
			tv_show1=(TextView) findViewById(R.id.battery_adv1);
			tv_show2=(TextView) findViewById(R.id.battery_adv2);
			tv_show3=(TextView) findViewById(R.id.battery_adv3);
			battery_tv.setText("您的电量剩余: "+percent+"%");
			if(percent<=100 && percent>=80 ){
				imageView.setImageResource(R.drawable.battery_green);
				tv_show1.setText("*  可放心观看视频");
				tv_show2.setText("*  可放心用wifi、4G上网");
				tv_show3.setText("*  可调整手机亮度为最佳");
				//绿色
			}else if(percent<80 && percent>=50 ){
				imageView.setImageResource(R.drawable.battery_orange);
				tv_show1.setText("*  电量仍很足请放心使用");
				tv_show2.setText("*  视频等高耗能软件谨慎使用");
				tv_show3.setText("*  可调整手机亮度为最佳");
				//橘红色
			}else if(percent<50 && percent>=10 ){
				imageView.setImageResource(R.drawable.battery_red);
				tv_show1.setText("*  电量不足，不宜看视频");
				tv_show2.setText("*  wifi网络建议断开");
				tv_show3.setText("*  请调整手机亮度以防电量快速耗损");
				//红色
			}else if(percent<10){
				imageView.setImageResource(R.drawable.battery_over);
				tv_show1.setText("*  请链接您的充电器");
				tv_show2.setText("*  不久后将自动关机");
				tv_show3.setText("*  请保存您的必要数据");
				//没了
			}
			
			//发短信
		}

	}
	//开启短信服务
	public void openServer() {

		final Intent intent=new Intent(getApplicationContext(),OpenBatteryService.class);
		// TODO Auto-generated method stub
		//		myBatteryReceiver=new MyBatteryReceiver();
		if(running){
			button.setText("服务正在运行");
			//mSp.edit().putBoolean("auto_update", true).commit();

		}else{
			button.setText("一键开启服务");
			//mSp.edit().putBoolean("auto_update", false).commit();
		}
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(running){
					//mSp.edit().putBoolean("auto_update", false).commit();
					button.setText("一键开启服务");
					running=false;
					stopService(intent);
				}else{
					//mSp.edit().putBoolean("auto_update", true).commit();				
					button.setText("服务正在运行");
					running=true;
					startService(intent);//混合启动
					bindService(intent, new ServiceConnection() {
						@Override
						public void onServiceDisconnected(ComponentName name) {
							// TODO Auto-generated method stub
							//未连接什么都不做
							System.out.println("失败");
						}
						@Override
						public void onServiceConnected(ComponentName name, IBinder service) {
							// TODO Auto-generated method stub
							
						}
					}, BIND_AUTO_CREATE);
				}
			}

		});

	}

}
