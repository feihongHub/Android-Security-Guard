package com.graduate.phonesafeguard.chapter09;

import com.graduate.phonesafeguard.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class AdvanceToolsActivity extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_advancetools);
		initView();
	}
	private void startActivity(Class<?> cls){
		Intent intent=new Intent(this,cls);
		startActivity(intent);
	}
	//初始化控件
	private void initView(){
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.deep_red));
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView)findViewById(R.id.tv_title)).setText("高级工具");
		mLeftImgv.setImageResource(R.drawable.title_back);
		mLeftImgv.setOnClickListener(this);
		findViewById(R.id.advanceview_numbelongs).setOnClickListener(this);
		findViewById(R.id.advanceview_numbesearch).setOnClickListener(this);
		findViewById(R.id.advanceview_smsbackup).setOnClickListener(this);
		findViewById(R.id.advanceview_smsreduction).setOnClickListener(this);
		findViewById(R.id.advanceview_applock).setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.advanceview_numbelongs:
			startActivity(NumBelongtoActivity.class);
			break;
		case R.id.advanceview_numbesearch:
			startActivity(NumbesearchActivity.class);
			break;
		case R.id.advanceview_smsbackup:
			startActivity(SMSBackupActivity.class);
			break;
		case R.id.advanceview_smsreduction:
			startActivity(SMSReductionActivity.class);
			break;
		case R.id.advanceview_applock:
			startActivity(AppLockActivity.class);
			break;
		}
	}



}
