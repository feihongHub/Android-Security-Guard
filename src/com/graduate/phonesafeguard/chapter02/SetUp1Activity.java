package com.graduate.phonesafeguard.chapter02;

import com.graduate.phonesafeguard.R;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

public class SetUp1Activity extends BaseSetUpActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
		initView();
	}
	//��ʼ��
	private void initView(){
		((RadioButton)findViewById(R.id.rb_first)).setChecked(true);
	}
	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		startActivityAndFinishSelf(SetUp2Activity.class);
	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "��ǰҳ���Ѿ��ǵ�һҳŶ��~", 0).show();
	}

}
