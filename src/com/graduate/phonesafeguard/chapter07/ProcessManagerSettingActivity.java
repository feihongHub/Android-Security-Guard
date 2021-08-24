package com.graduate.phonesafeguard.chapter07;

import com.graduate.phonesafeguard.HomeActivity;
import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter07.service.AutoKillProcessService;
import com.graduate.phonesafeguard.chapter07.utils.SystemInfoUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ProcessManagerSettingActivity extends Activity implements OnClickListener,OnCheckedChangeListener {
private SharedPreferences mSp;
private ToggleButton mShowSysAppsTgb;
private ToggleButton mKillProcessTgb;
private boolean running;
private Button saveSetting;
//	private ImageView
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_processmanagersetting);
		mSp = getSharedPreferences("config", MODE_PRIVATE);
		initView();
	}
	/**
	 * 初始化控件
	 */
	private void initView(){
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_green));
		saveSetting = (Button) findViewById(R.id.save_setting);
		saveSetting.setOnClickListener(this);
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.title_back);
		((TextView)findViewById(R.id.tv_title)).setText("进程管理设置");
		mShowSysAppsTgb = (ToggleButton) findViewById(R.id.tgb_showsys_process);
		mKillProcessTgb = (ToggleButton) findViewById(R.id.tgb_killprocess_lockscreen);
		mShowSysAppsTgb.setChecked(mSp.getBoolean("ShowSystemProcess", true));
		running = SystemInfoUtils.isServiceRunning(this, "com.graduate.phonesafeguard.chapter07.service.AutoKillProcessService");
		mKillProcessTgb.setChecked(running);
		initListener();
	}
	/**
	 * 初始化监听
	 * @param v
	 */
	private void initListener(){
		mKillProcessTgb.setOnCheckedChangeListener(this);
		mShowSysAppsTgb.setOnCheckedChangeListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.save_setting:
			finish();
			break;
		}

	}

	private void saveStatus(String string,boolean isChecked){
		Editor edit=mSp.edit();
		edit.putBoolean(string, isChecked);
		edit.commit();
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.tgb_showsys_process:
			saveStatus("ShowSystemProcess", isChecked);
			break;
		case R.id.tgb_killprocess_lockscreen:
			Intent service=new Intent(this,AutoKillProcessService.class);
			if(isChecked){
				//开启服务
				startService(service);
			}else{
				//关闭服务
				stopService(service);
			}
		}
	}

}
