package com.graduate.phonesafeguard.chapter02.dialog;

import com.graduate.phonesafeguard.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class InputPasswordDialog extends Dialog implements android.view.View.OnClickListener{
	/**
	 * 输入密码框逻辑
	 * @param context
	 */
	public MyCallBack myCallBack;
	private TextView mTitleTV;
	private EditText mFirstPWDET;
	public InputPasswordDialog(Context context) {
		super(context,R.style.dialog_custom);
		// TODO Auto-generated constructor stub
	}
	public void setCallBack(MyCallBack myCallBack){
		this.myCallBack=myCallBack;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inter_password_dialog);
		initView();
	}
	public void initView(){
		mTitleTV = (TextView) findViewById(R.id.tv_interpwd_title);
		mFirstPWDET = (EditText) findViewById(R.id.et_inter_password);
		findViewById(R.id.btn_confirm).setOnClickListener(this);
		findViewById(R.id.btn_dismiss).setOnClickListener(this);
	}
	//设置对话框标题
	public void setTitle(String title){
		if(!TextUtils.isEmpty(title)){
			mTitleTV.setText(title);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_confirm:
			myCallBack.inputok();
			break;
		case R.id.btn_dismiss:
			myCallBack.disMiss();
			break;
		}
	}
	public interface MyCallBack{
		void inputok();
		void disMiss();
	}
	//提取密码
	public String getPassword(){
		String password = mFirstPWDET.getText().toString().trim();
		if(!TextUtils.isEmpty(password)){
			return password;
		}
		return "";
		
	}

}
