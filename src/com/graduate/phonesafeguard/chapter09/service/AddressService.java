package com.graduate.phonesafeguard.chapter09.service;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter09.db.dao.NumBelongtoDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AddressService extends Service {

	private TelephonyManager mTM;
	private MyListener mListener;
	private InnerReceiver mReceiver;
	private WindowManager mWM;
	private View view;
	private SharedPreferences mSp;
	private int startX;
	private int startY;
	private int mWMwidth;
	private int mWMheight;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mListener = new MyListener();
		//监听来电
		//动态搞广播
		mTM.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
		mReceiver = new InnerReceiver();
		IntentFilter filter=new IntentFilter();
		registerReceiver(mReceiver, filter);
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(mReceiver, filter);
	}
	//去电广播
	class InnerReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String number = getResultData();
			String address = NumBelongtoDao.getLocation(number);
			//			Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
			showToast(address);
		}

	}
	class MyListener extends PhoneStateListener{
		//当状态发生改变时调用
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			//摘机
			case TelephonyManager.CALL_STATE_OFFHOOK:

				break;
				//空闲
			case TelephonyManager.CALL_STATE_IDLE:
				if(mWM!=null && view!=null){
					mWM.removeView(view);
				}

				break;
				//响铃
			case TelephonyManager.CALL_STATE_RINGING:
				String address = NumBelongtoDao.getLocation(incomingNumber);
				//				Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
				showToast(address);
				break;

			}
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mTM.listen(mListener, PhoneStateListener.LISTEN_NONE);
		mListener=null;
		unregisterReceiver(mReceiver);
		mReceiver=null;
	}
	//android.permission.SYSTEM_ALERT_WINDOW要加权限
	private void showToast(String text){
		//窗口管理器，Android最高级布局文件，展示activity通知栏
		mWM=(WindowManager) getSystemService(WINDOW_SERVICE);
		mWMwidth = mWM.getDefaultDisplay().getWidth();
		mWMheight = mWM.getDefaultDisplay().getHeight();	
		mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
		//初始化布局
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;//修改窗口类型为TYPE_PHONE，才可以保障触摸
		params.setTitle("Toast");
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		params.gravity=Gravity.LEFT+Gravity.TOP;//将重心设定到左上方的位置
		
		view = new TextView(this);
		view=View.inflate(this, R.layout.custom_toast, null);
		TextView addressTV=(TextView) view.findViewById(R.id.custom_toast);
		//获取样式数据
		mSp = getSharedPreferences("config", MODE_PRIVATE);
		int styleNum = mSp.getInt("address_style", 0);
		//背景色数组
		int [] idItem=new int[]{R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue,
				R.drawable.call_locate_gray,R.drawable.call_locate_green};
		addressTV.setBackgroundResource(idItem[styleNum]);
		addressTV.setText(text);
		//修改布局位置
		int lastX = mSp.getInt("lastX", 0);
		int lastY = mSp.getInt("lastY", 0);
		params.x=lastX;
		params.y=lastY;
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					System.out.println("按下");
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					System.out.println("移动");
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					int dx=endX-startX;
					int dy=endY-startY;
					//根据偏移量更新位置
					params.x=params.x+dx;
					params.y=params.y+dy;
					//防止屏幕超出边界
					if(params.x<0){
						params.x=0;
					}
					if(params.x>mWMwidth-view.getWidth()){
						params.x=mWMwidth-view.getWidth();
					}
					if(params.y<0){
						params.y=0;
					}
					if(params.y>mWMheight-view.getHeight()-25){
						params.y=mWMheight-view.getHeight()-25;
					}
					//更新窗口布局
					mWM.updateViewLayout(view, params);
					//根据当前位置显示文本框
					//重新初始化起始坐标
					startX=(int) event.getRawX();
					startY=(int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					System.out.println("抬起");
					//保存
					mSp.edit().putInt("lastX", params.x).commit();
					mSp.edit().putInt("lastY", params.y).commit();
					break;
				}
				//消耗子事件
				return true;
			}
		});
		mWM.addView(view, params);
	}

}
