package com.graduate.phonesafeguard.chapter02;

import com.graduate.phonesafeguard.R;
import com.lidroid.xutils.db.annotation.Finder;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


public class SetUp3Activity extends BaseSetUpActivity implements OnClickListener{
	private EditText mInputPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		initView();
	}
	//初始化
	private void initView(){
		((RadioButton)findViewById(R.id.rb_third)).setChecked(true);
		findViewById(R.id.btn_addcontact).setOnClickListener(this);
		mInputPhone = (EditText) findViewById(R.id.ed_inputphone);
		String safephone = sp.getString("safephone", "");
		if(!TextUtils.isEmpty(safephone)){
			mInputPhone.setText(safephone);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_addcontact:
//进入联系人页面			
			startActivityForResult(new Intent(this,ContactSelectActivity.class), 0);
			break;

		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			String phone = data.getStringExtra("phone");
//			Toast.makeText(this, "phone"+phone, 0).show();
			mInputPhone.setText(phone);
		}
	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		String safephone = mInputPhone.getText().toString().trim();
		if(TextUtils.isEmpty(safephone)){
			Toast.makeText(this, "请输入安全号码", 0).show();
			return ;
		}else{
			Editor editor = sp.edit();
			editor.putString("safephone", safephone);
			editor.commit();
			startActivityAndFinishSelf(SetUp4Activity.class);
		}
	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		startActivityAndFinishSelf(SetUp2Activity.class);
	}

}
