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
				mProgressButton.setText("一键备份");
				finish();
				break;
			case FINISH_COPY:
				mProgressButton.setText("完成备份");
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
		((TextView)findViewById(R.id.tv_title)).setText("短信备份");
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
				mProgressButton.setText("一键备份");
			}else{
				flag=true;
				mProgressButton.setText("取消备份");
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
									UIUtils.showToast(SMSBackupActivity.this, "您还没有短信");
									handler.sendEmptyMessage(CHANGE_BUTTON_TEXT);
								}else{
									mProgressButton.setMax(size);
								}
							}
						});
						if(backUpSms){
							UIUtils.showToast(SMSBackupActivity.this, "备份成功");
							handler.sendEmptyMessage(FINISH_COPY);
							
						}else{
							UIUtils.showToast(SMSBackupActivity.this, "备份失败");
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						UIUtils.showToast(SMSBackupActivity.this, "文件生成失败");
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						UIUtils.showToast(SMSBackupActivity.this, "SD卡不可用或SD卡内存不足");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						UIUtils.showToast(SMSBackupActivity.this, "读写错误");
					}
				};
			}.start();
		
			break;
		}
		
	}

}
