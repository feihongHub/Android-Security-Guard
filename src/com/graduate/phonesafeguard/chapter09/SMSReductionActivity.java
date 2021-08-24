package com.graduate.phonesafeguard.chapter09;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter09.utils.SmsReductionUtils;
import com.graduate.phonesafeguard.chapter09.utils.SmsReductionUtils.SmsReductionCallBack;
import com.graduate.phonesafeguard.chapter09.utils.UIUtils;
import com.graduate.phonesafeguard.chapter09.widget.MyCircleProgress;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Window;

public class SMSReductionActivity extends Activity implements OnClickListener {
	private static final int FINISH_RESTOR=300;
	private MyCircleProgress mProgressButton;
	private boolean flag=false;
	private SmsReductionUtils smsReductionUtils;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FINISH_RESTOR:
				mProgressButton.setText("完成还原");
				break;

			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reduction);
		initView();
		smsReductionUtils = new SmsReductionUtils();
	}
	private void initView(){
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.deep_red));
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView)findViewById(R.id.tv_title)).setText("短信还原");
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.title_back);
		mProgressButton = (MyCircleProgress) findViewById(R.id.mcp_reduction);
		mProgressButton.setOnClickListener(this);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		flag=false;
		smsReductionUtils.setFlag(flag);
		super.onDestroy();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.mcp_reduction:
			if(flag){
				flag=false;
				mProgressButton.setText("一键还原");
			}else{
				flag=true;
				mProgressButton.setText("取消还原");
			}
			smsReductionUtils.setFlag(flag);
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					try {
						boolean smsReductionSms=smsReductionUtils.reductionSms(SMSReductionActivity.this, new SmsReductionCallBack() {
							
							@Override
							public void onSmsReduction(int process) {
								// TODO Auto-generated method stub
								mProgressButton.setProcess(process);
							}
							
							@Override
							public void beforeSmsReduction(int size) {
								// TODO Auto-generated method stub
								mProgressButton.setMax(size);
							}
						});
						if(smsReductionSms){
							UIUtils.showToast(SMSReductionActivity.this, "还原成功");
							handler.sendEmptyMessage(FINISH_RESTOR);
							
						}else{
							UIUtils.showToast(SMSReductionActivity.this, "还原失败");
//							mProgressButton.setText("完成备份");
						}
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						UIUtils.showToast(SMSReductionActivity.this, "文件格式错误");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						UIUtils.showToast(SMSReductionActivity.this, "读写错误");
					}
				}
			}.start();
			break;
		}
	}

}
