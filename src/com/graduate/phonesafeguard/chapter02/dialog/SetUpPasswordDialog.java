package com.graduate.phonesafeguard.chapter02.dialog;

import com.graduate.phonesafeguard.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SetUpPasswordDialog extends Dialog implements android.view.View.OnClickListener{
	public MyCallBack myCallBack;
	public TextView mTitleTV;
	//实现一个构造方法
	public EditText mFirstPWDET;
	public EditText mAffirmET;
	
	public SetUpPasswordDialog(Context context) {
		super(context,R.style.dialog_custom);//引入自定义对话框样式
		
	}
	public void setCallBack(MyCallBack myCallBack){
		this.myCallBack=myCallBack;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_password_dialog);
		initView();
	}
	//初始化对话框
	private void initView(){
		mTitleTV = (TextView) findViewById(R.id.tv_setuppwd_title);
		mFirstPWDET = (EditText) findViewById(R.id.et_firstpwd);
		mAffirmET = (EditText) findViewById(R.id.et_affirm_password);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_cancle).setOnClickListener(this);
		
	} 
	//设置对话框标题
	private void setTitle(String title){
		if(!TextUtils.isEmpty(title)){
			mTitleTV.setText(title);
		}
	}
	//设置点击事件
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok:
			myCallBack.ok();
			break;
		case R.id.btn_cancle:
			myCallBack.cancle();
			break;
		}
	}
	public interface MyCallBack{
		void ok();
		void cancle();
	}

}
