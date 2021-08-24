package com.graduate.phonesafeguard.chapter02;

import com.graduate.phonesafeguard.R;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LostFindActivity extends Activity implements OnClickListener{
	public SharedPreferences sp;
	private TextView mSafePhoneTV;
	private ToggleButton mToggleButton;
	private RelativeLayout mInterSetUpRL;
	private TextView mProtectStatusTV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lostfind);
//		Toast.makeText(LostFindActivity.this, "enter success", 0).show();
		sp=getSharedPreferences("config", MODE_PRIVATE);
		if(!isSetUp()){
			//如果之前没进过
			startSetUp1Activity();
		}
		initView();
	}
	//初始化
	private void initView(){
		TextView mTitleTV=(TextView) findViewById(R.id.tv_title);
		mTitleTV.setText("手机防盗");
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.title_back);
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));
		mSafePhoneTV = (TextView) findViewById(R.id.tv_safephone);
		mSafePhoneTV.setText(sp.getString("safephone", ""));
		mToggleButton = (ToggleButton) findViewById(R.id.togglebtn_lostfind);
		mInterSetUpRL = (RelativeLayout) findViewById(R.id.rl_inter_setup_wizard);
		mInterSetUpRL.setOnClickListener(this);
		mProtectStatusTV = (TextView) findViewById(R.id.tv_lostfind_protectstatus);
		boolean protecting = sp.getBoolean("protecting", true);
		if(protecting){
			mProtectStatusTV.setText("防盗保护已经启动");
			mToggleButton.setChecked(true);
		}else{
			mProtectStatusTV.setText("防盗保护没有开启");
			mToggleButton.setChecked(false);
		}
		mToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					mProtectStatusTV.setText("防盗保护已经启动");
				}else{
					mProtectStatusTV.setText("防盗保护没有开启");
				}
				Editor editor = sp.edit();
				editor.putBoolean("protecting", isChecked);
				editor.commit();
			}
		});
	
		
	}
	//判断是否第一次进入
	private boolean isSetUp(){
		return sp.getBoolean("isSetUp", false); 
	}
	private void startSetUp1Activity(){
		Intent intent=new Intent(LostFindActivity.this,SetUp1Activity.class);
		startActivity(intent);
		finish();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_inter_setup_wizard:
			startSetUp1Activity();
			break;
		case R.id.imgv_leftbtn:
			finish();
			break;
		}
	}
}
