package com.graduate.phonesafeguard.chapter02;

import com.graduate.phonesafeguard.R;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SetUp4Activity extends BaseSetUpActivity{
	private TextView mStatusTV;
	private ToggleButton mToggleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		initView();
	}
	private void initView(){
		((RadioButton)findViewById(R.id.rb_four)).setChecked(true);
		mStatusTV = (TextView) findViewById(R.id.tv_setup4_status);
		mToggleButton = (ToggleButton) findViewById(R.id.togglebtn_securityfunction);
		mToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					mStatusTV.setText("防盗保护已经开启");
				}else{
					mStatusTV.setText("防盗保护没有开启");
				}
				Editor editor = sp.edit();
				editor.putBoolean("protecting", isChecked);
				editor.commit();
			}
		});
		boolean protecting = sp.getBoolean("protecting", true);
		if(protecting){
			mStatusTV.setText("防盗保护已经开启");
			mToggleButton.setChecked(true);
		}else{
			mStatusTV.setText("防盗保护没有开启");
			mToggleButton.setChecked(false);
		}
	}
	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		Editor editor = sp.edit();
		Editor isSetUp = editor.putBoolean("isSetUp", true);
		editor.commit();
		startActivityAndFinishSelf(LostFindActivity.class);
	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		startActivityAndFinishSelf(SetUp3Activity.class);
	}
	
}
