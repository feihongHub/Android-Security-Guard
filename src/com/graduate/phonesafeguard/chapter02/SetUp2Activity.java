package com.graduate.phonesafeguard.chapter02;

import com.graduate.phonesafeguard.R;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class SetUp2Activity extends BaseSetUpActivity implements OnClickListener{
	private TelephonyManager mTelephonyManager;
	private Button mbindSIMBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		mTelephonyManager=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		initView();
	}
	private void initView(){
		((RadioButton)findViewById(R.id.rb_second)).setChecked(true);
		mbindSIMBtn=(Button) findViewById(R.id.btn_bindsim);
		mbindSIMBtn.setOnClickListener(this);
		if(isBind()){
			mbindSIMBtn.setText("SIM卡已绑定");
			mbindSIMBtn.setEnabled(false);
		}else{
			mbindSIMBtn.setEnabled(true);
		}
	}
	private boolean isBind(){
		String simString = sp.getString("sim", null);
		if(TextUtils.isEmpty(simString)){
			return false;
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_bindsim:
			bindSIM();
			break;
		}
	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		if(!isBind()){
			Toast.makeText(this, "您还未绑定SIM卡", 0).show();
			return;
		}
		startActivityAndFinishSelf(SetUp3Activity.class);
	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		startActivityAndFinishSelf(SetUp1Activity.class);
	}
	//绑定SIM卡
	private void bindSIM(){
		if(!isBind()){
			String simSerialNumber = mTelephonyManager.getSimSerialNumber();
			Editor edit = sp.edit();
			//这个小bug花费了我10元短信费
			edit.putString("sim", simSerialNumber);
			edit.commit();
			Toast.makeText(this, "SIM卡绑定成功", 0).show();
			mbindSIMBtn.setEnabled(false);
		}else{
			//已经绑定
			Toast.makeText(this, "SIM卡已经绑定", 0).show();
			mbindSIMBtn.setEnabled(false);
		}
	}

}
