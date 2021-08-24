package com.graduate.phonesafeguard.chapter09;


import java.io.FileNotFoundException;
import java.io.IOException;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter09.utils.SmsBackUpUtils;
import com.graduate.phonesafeguard.chapter09.utils.SmsBackUpUtils.BackupStatusCallback;
import com.graduate.phonesafeguard.chapter09.utils.UIUtils;
import com.graduate.phonesafeguard.chapter09.widget.MyCircleProgress;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class SMSBackupActivity extends Activity implements OnClickListener {
	private static final int CHANGE_BUTTON_TEXT=100;
	private static final int FINISH_COPY=200;
	private MyCircleProgress mProgressButton;
	private boolean flag=false;
	private SmsBackUpUtils smsBackUpUtils;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_BUTTON_TEXT:
				mProgressButton.setText("һ������");
				finish();
				break;
			case FINISH_COPY:
				mProgressButton.setText("��ɱ���");
				finish();
				break;
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_smsbackup);
		smsBackUpUtils=new SmsBackUpUtils();
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.deep_red));
		ImageView mLeftbtn=(ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView)findViewById(R.id.tv_title)).setText("���ű���");
		mLeftbtn.setImageResource(R.drawable.title_back);
		mLeftbtn.setOnClickListener(this);
		mProgressButton = (MyCircleProgress) findViewById(R.id.mcp_smsbackup);
		mProgressButton.setOnClickListener(this);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		flag=false;
		smsBackUpUtils.setFlag(flag);
		super.onDestroy();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.mcp_smsbackup:
			if(flag){
				flag=false;
				mProgressButton.setText("һ������");
			}else{
				flag=true;
				mProgressButton.setText("ȡ������");
			}
			smsBackUpUtils.setFlag(flag);
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					try {
						boolean backUpSms = smsBackUpUtils.backUpSms(SMSBackupActivity.this, new BackupStatusCallback() {
							
							@Override
							public void onSmsBackup(int process) {
								// TODO Auto-generated method stub
								mProgressButton.setProcess(process);
							}
							
							@Override
							public void beforeSmsBackup(int size) {
								// TODO Auto-generated method stub
								if(size<=0){
									flag=false;
									smsBackUpUtils.setFlag(flag);
									UIUtils.showToast(SMSBackupActivity.this, "����û�ж���");
									handler.sendEmptyMessage(CHANGE_BUTTON_TEXT);
								}else{
									mProgressButton.setMax(size);
								}
							}
						});
						if(backUpSms){
							UIUtils.showToast(SMSBackupActivity.this, "���ݳɹ�");
							handler.sendEmptyMessage(FINISH_COPY);
							
						}else{
							UIUtils.showToast(SMSBackupActivity.this, "����ʧ��");
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						UIUtils.showToast(SMSBackupActivity.this, "�ļ�����ʧ��");
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						UIUtils.showToast(SMSBackupActivity.this, "SD�������û�SD���ڴ治��");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						UIUtils.showToast(SMSBackupActivity.this, "��д����");
					}
				};
			}.start();
		
			break;
		}
		
	}

}
