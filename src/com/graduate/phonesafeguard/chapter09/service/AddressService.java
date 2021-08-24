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
		//��������
		//��̬��㲥
		mTM.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
		mReceiver = new InnerReceiver();
		IntentFilter filter=new IntentFilter();
		registerReceiver(mReceiver, filter);
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(mReceiver, filter);
	}
	//ȥ��㲥
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
		//��״̬�����ı�ʱ����
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			//ժ��
			case TelephonyManager.CALL_STATE_OFFHOOK:

				break;
				//����
			case TelephonyManager.CALL_STATE_IDLE:
				if(mWM!=null && view!=null){
					mWM.removeView(view);
				}

				break;
				//����
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
	//android.permission.SYSTEM_ALERT_WINDOWҪ��Ȩ��
	private void showToast(String text){
		//���ڹ�������Android��߼������ļ���չʾactivity֪ͨ��
		mWM=(WindowManager) getSystemService(WINDOW_SERVICE);
		mWMwidth = mWM.getDefaultDisplay().getWidth();
		mWMheight = mWM.getDefaultDisplay().getHeight();	
		mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
		//��ʼ������
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;//�޸Ĵ�������ΪTYPE_PHONE���ſ��Ա��ϴ���
		params.setTitle("Toast");
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		params.gravity=Gravity.LEFT+Gravity.TOP;//�������趨�����Ϸ���λ��
		
		view = new TextView(this);
		view=View.inflate(this, R.layout.custom_toast, null);
		TextView addressTV=(TextView) view.findViewById(R.id.custom_toast);
		//��ȡ��ʽ����
		mSp = getSharedPreferences("config", MODE_PRIVATE);
		int styleNum = mSp.getInt("address_style", 0);
		//����ɫ����
		int [] idItem=new int[]{R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue,
				R.drawable.call_locate_gray,R.drawable.call_locate_green};
		addressTV.setBackgroundResource(idItem[styleNum]);
		addressTV.setText(text);
		//�޸Ĳ���λ��
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
					System.out.println("����");
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					System.out.println("�ƶ�");
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					int dx=endX-startX;
					int dy=endY-startY;
					//����ƫ��������λ��
					params.x=params.x+dx;
					params.y=params.y+dy;
					//��ֹ��Ļ�����߽�
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
					//���´��ڲ���
					mWM.updateViewLayout(view, params);
					//���ݵ�ǰλ����ʾ�ı���
					//���³�ʼ����ʼ����
					startX=(int) event.getRawX();
					startY=(int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					System.out.println("̧��");
					//����
					mSp.edit().putInt("lastX", params.x).commit();
					mSp.edit().putInt("lastY", params.y).commit();
					break;
				}
				//�������¼�
				return true;
			}
		});
		mWM.addView(view, params);
	}

}
