package com.graduate.phonesafeguard.chapter03;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter03.adapter.BlackContactAdapter;
import com.graduate.phonesafeguard.chapter03.db.dao.BlackNumberDao;
import com.graduate.phonesafeguard.chapter03.entity.BlackContactInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddBlackNumberActivity extends Activity implements OnClickListener {
	private CheckBox mSmsCB;
	private CheckBox mTelCB;
	private EditText mNumET;
	private EditText mNameET;
	private BlackNumberDao dao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_blacknumber);
		dao=new BlackNumberDao(AddBlackNumberActivity.this);
		initView();
	}
	private void initView(){
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple_red));
		((TextView) findViewById(R.id.tv_title)).setText("添加黑名单");
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.title_back);
		mSmsCB=(CheckBox) findViewById(R.id.cb_blacknumber_sms);
		mTelCB=(CheckBox) findViewById(R.id.cb_blacknumber_tel);
		mNumET=(EditText) findViewById(R.id.et_blacknumber);
		mNameET=(EditText) findViewById(R.id.et_blackname);
		findViewById(R.id.add_blacknum_btn).setOnClickListener(this);
		findViewById(R.id.add_fromcontact_btn).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.add_blacknum_btn:
			String number = mNumET.getText().toString().trim();
			String name = mNameET.getText().toString().trim();
			if(TextUtils.isEmpty(number) || TextUtils.isEmpty(name)){
				Toast.makeText(this, "电话和手机号不得为空", 0).show();
				return;
			}else{
				BlackContactInfo blackContactInfo=new BlackContactInfo();
				blackContactInfo.phoneNumber=number;
				blackContactInfo.contactName=name;
				if(mSmsCB.isChecked() & mTelCB.isChecked()){
					//两种拦截模式都选
					blackContactInfo.mode=3;
				}else if(mSmsCB.isChecked() &!mTelCB.isChecked()){
					blackContactInfo.mode=2;
				}else if(mTelCB.isChecked() &!mSmsCB.isChecked()){
					blackContactInfo.mode=1;
				}else{
					Toast.makeText(this, "请选择拦截模式", 0).show();
					return;
				}
				if(!dao.IsNumberExist(blackContactInfo.phoneNumber)){
					dao.add(blackContactInfo);
				}else{
					Toast.makeText(this, "该号码已经被添加至黑名单", 0).show();
				}
				finish();
			}
			break;
		case R.id.add_fromcontact_btn:
			startActivityForResult(new Intent(this,ContactSelectActivity.class),0);
			break;
		
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			//选择获取的联系人
			String phone = data.getStringExtra("phone");
			String name = data.getStringExtra("name");
			mNameET.setText(name);
			mNumET.setText(phone);
			
		}
	}

}
