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
	//��ʼ��
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
			int current = intent.getExtras().getInt("level");//��ǰ����
			int total = intent.getExtras().getInt("scale");//�ܵ���
			percent = current*100/total;
			imageView=(ImageView) findViewById(R.id.battery_srcshow);
			battery_tv = (TextView) findViewById(R.id.Battery_show);
			tv_show1=(TextView) findViewById(R.id.battery_adv1);
			tv_show2=(TextView) findViewById(R.id.battery_adv2);
			tv_show3=(TextView) findViewById(R.id.battery_adv3);
			battery_tv.setText("���ĵ���ʣ��: "+percent+"%");
			if(percent<=100 && percent>=80 ){
				imageView.setImageResource(R.drawable.battery_green);
				tv_show1.setText("*  �ɷ��Ĺۿ���Ƶ");
				tv_show2.setText("*  �ɷ�����wifi��4G����");
				tv_show3.setText("*  �ɵ����ֻ�����Ϊ���");
				//��ɫ
			}else if(percent<80 && percent>=50 ){
				imageView.setImageResource(R.drawable.battery_orange);
				tv_show1.setText("*  �����Ժ��������ʹ��");
				tv_show2.setText("*  ��Ƶ�ȸߺ����������ʹ��");
				tv_show3.setText("*  �ɵ����ֻ�����Ϊ���");
				//�ٺ�ɫ
			}else if(percent<50 && percent>=10 ){
				imageView.setImageResource(R.drawable.battery_red);
				tv_show1.setText("*  �������㣬���˿���Ƶ");
				tv_show2.setText("*  wifi���罨��Ͽ�");
				tv_show3.setText("*  ������ֻ������Է��������ٺ���");
				//��ɫ
			}else if(percent<10){
				imageView.setImageResource(R.drawable.battery_over);
				tv_show1.setText("*  ���������ĳ����");
				tv_show2.setText("*  ���ú��Զ��ػ�");
				tv_show3.setText("*  �뱣�����ı�Ҫ����");
				//û��
			}
			
			//������
		}

	}
	//�������ŷ���
	public void openServer() {

		final Intent intent=new Intent(getApplicationContext(),OpenBatteryService.class);
		// TODO Auto-generated method stub
		//		myBatteryReceiver=new MyBatteryReceiver();
		if(running){
			button.setText("������������");
			//mSp.edit().putBoolean("auto_update", true).commit();

		}else{
			button.setText("һ����������");
			//mSp.edit().putBoolean("auto_update", false).commit();
		}
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(running){
					//mSp.edit().putBoolean("auto_update", false).commit();
					button.setText("һ����������");
					running=false;
					stopService(intent);
				}else{
					//mSp.edit().putBoolean("auto_update", true).commit();				
					button.setText("������������");
					running=true;
					startService(intent);//�������
					bindService(intent, new ServiceConnection() {
						@Override
						public void onServiceDisconnected(ComponentName name) {
							// TODO Auto-generated method stub
							//δ����ʲô������
							System.out.println("ʧ��");
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
