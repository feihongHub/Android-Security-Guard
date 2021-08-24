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
	//ʵ��һ�����췽��
	public EditText mFirstPWDET;
	public EditText mAffirmET;
	
	public SetUpPasswordDialog(Context context) {
		super(context,R.style.dialog_custom);//�����Զ���Ի�����ʽ
		
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
	//��ʼ���Ի���
	private void initView(){
		mTitleTV = (TextView) findViewById(R.id.tv_setuppwd_title);
		mFirstPWDET = (EditText) findViewById(R.id.et_firstpwd);
		mAffirmET = (EditText) findViewById(R.id.et_affirm_password);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_cancle).setOnClickListener(this);
		
	} 
	//���öԻ������
	private void setTitle(String title){
		if(!TextUtils.isEmpty(title)){
			mTitleTV.setText(title);
		}
	}
	//���õ���¼�
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
