package com.graduate.phonesafeguard.chapter10;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter03.service.BlacknumberControlService;
import com.graduate.phonesafeguard.chapter07.utils.SystemInfoUtils;
import com.graduate.phonesafeguard.chapter09.DragViewActivity;
import com.graduate.phonesafeguard.chapter09.service.AddressService;
import com.graduate.phonesafeguard.chapter09.service.AppLockService;
import com.graduate.phonesafeguard.chapter10.utils.ServiceStatusUtils;
import com.graduate.phonesafeguard.chapter10.widget.SettingClickView;
import com.graduate.phonesafeguard.chapter10.widget.SettingView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener {
	private SharedPreferences mSp;
	private SettingView update;
	private SettingView address;
	private SettingView blackNumber;
	private SettingView mAppLockSV;
	private SettingClickView mStyle;
	private String [] mItems = new String[] {"��͸��","������","��ʿ��","������","ƻ����"};
	private SettingClickView mLocation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settings);
		mSp = getSharedPreferences("config", MODE_PRIVATE);
		initView();
	}
	private void initView(){
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.deep_blue));
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(this);
		((TextView)findViewById(R.id.tv_title)).setText("��������");
		mLeftImgv.setImageResource(R.drawable.title_back);
		initAutoUpdate();
		initAddress();
		initStyle();
		initSetLocation();
		initBlackNumber();
		initAppLock();
	}
	//����������
	private void initAppLock() {
		// TODO Auto-generated method stub
		mAppLockSV=(SettingView) findViewById(R.id.setting_applock_set);
		boolean running = ServiceStatusUtils.isServiceRunning("com.graduate.phonesafeguard.chapter09.service.AppLockService", this);
		if(running){
			mAppLockSV.setChecked(true);
		}else{
			mAppLockSV.setChecked(false);
		}
		mAppLockSV.setOnClickListener(this);
	}
	//���������ط���
	private void initBlackNumber() {
		// TODO Auto-generated method stub
		blackNumber=(SettingView) findViewById(R.id.setting_blacknamenumber);
		boolean running = ServiceStatusUtils.isServiceRunning("com.graduate.phonesafeguard.chapter03.service.BlacknumberControl", this);
		if(running){
			blackNumber.setChecked(true);
		}else{
			blackNumber.setChecked(false);
		}
		blackNumber.setOnClickListener(this);
	
	}
	private void initSetLocation() {
		mLocation = (SettingClickView) findViewById(R.id.setting_location);
		mLocation.setOnClickListener(this);
		mLocation.setTitle("���ù�������ʾ����ʾλ��");
		mLocation.setStatus("������������ʾ��λ��");
	}
	private void initStyle() {
		mStyle = (SettingClickView) findViewById(R.id.setting_style);
		//��������
		mStyle.setTitle("��������ʾ����");
		int styleNum = mSp.getInt("address_style", 0);
		mStyle.setStatus(mItems[styleNum]);
		mStyle.setOnClickListener(this);
	}
	private void showChooseDialog(){
		//�������ʽ
		int styleNum = mSp.getInt("address_style", 0);
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("��������ʾ���");
		builder.setIcon(R.drawable.ic_launcher);
		
		builder.setSingleChoiceItems(mItems, styleNum, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mSp.edit().putInt("address_style", which).commit();
				//������ʧ
				dialog.dismiss();
				mStyle.setStatus(mItems[which]);
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}
	/**
	 * ��ʼ����ַ
	 */
	private void initAddress(){
		address = (SettingView) findViewById(R.id.setting_address);
		boolean running = ServiceStatusUtils.isServiceRunning("com.graduate.phonesafeguard.chapter09.service.AddressService", this);
		if(running){
			address.setChecked(true);
		}else{
			address.setChecked(false);
		}
		address.setOnClickListener(this);
	}
	private void initAutoUpdate() {
		update = (SettingView) findViewById(R.id.setting_update);
		boolean autoUpdate=mSp.getBoolean("auto_update", true);
//		update.setTitle("����������");
		if(autoUpdate){
//			update.setStatus("�������ѿ���");
			update.setChecked(true);
		}else{
//			update.setStatus("�������ѹر�");
			update.setChecked(false);
		}
		update.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.setting_update:
			if(update.isChecked()){
				update.setChecked(false);
//				update.setStatus("�������ѹر�");
				mSp.edit().putBoolean("auto_update", false).commit();
			}else{
				update.setChecked(true);
//				update.setStatus("�������ѿ���");
				mSp.edit().putBoolean("auto_update", true).commit();
			}
			break;
		case R.id.setting_address:
			Intent service=new Intent(getApplicationContext(),AddressService.class);
			if(address.isChecked()){
				address.setChecked(false);
				stopService(service);
			}else{
				address.setChecked(true);
				startService(service);//���������ط���
			}
			break;
		case R.id.setting_style:
			showChooseDialog();
			break;
		case R.id.setting_location:
			startActivity(new Intent(getApplicationContext(),DragViewActivity.class));
			break;
		case R.id.setting_blacknamenumber:
			Intent blackService=new Intent(getApplicationContext(),BlacknumberControlService.class);
			if(blackNumber.isChecked()){
				blackNumber.setChecked(false);
				mSp.edit().putBoolean("blackNameNumber", false).commit();
				stopService(blackService);
			}else{
				blackNumber.setChecked(true);
				mSp.edit().putBoolean("blackNameNumber", true).commit();
				startService(blackService);//��������������
			}
			break;
		case R.id.setting_applock_set:
			Intent appLock=new Intent(getApplicationContext(),AppLockService.class);
			if(mAppLockSV.isChecked()){
				mAppLockSV.setChecked(false);
				mSp.edit().putBoolean("AppLockStatus", false).commit();
				stopService(appLock);
			}else{
				mAppLockSV.setChecked(true);
				mSp.edit().putBoolean("AppLockStatus", true).commit();
				startService(appLock);
			}
			break;
		}
	}
}
