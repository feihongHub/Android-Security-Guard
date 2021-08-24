package com.graduate.phonesafeguard.chapter02;

import java.util.List;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter02.adapter.ContactAdapter;
import com.graduate.phonesafeguard.chapter02.entity.ContactInfo;
import com.graduate.phonesafeguard.chapter02.utils.ContactInfoParser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactSelectActivity extends Activity implements OnClickListener{
	private ListView mListView;
	private ContactAdapter adapter;
	private List<ContactInfo> systemContacts;
	private Handler mhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 10:
				if(systemContacts!=null){
					adapter=new ContactAdapter(ContactSelectActivity.this,systemContacts);
					mListView.setAdapter(adapter);
				}
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contact_select);
		initView();
	}
	private void initView(){
		((TextView)findViewById(R.id.tv_title)).setText("选择联系人");
		ImageView mLeftImav=(ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImav.setOnClickListener(this);
		mLeftImav.setImageResource(R.drawable.title_back);
		//设置导航栏颜色
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));
		mListView=(ListView) findViewById(R.id.lv_contact);
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				systemContacts=ContactInfoParser.getSystemContact(ContactSelectActivity.this);
				mhandler.sendEmptyMessage(10);
			}
		}.start();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				ContactInfo item=(ContactInfo) adapter.getItem(position);
				Intent intent=new Intent();
				intent.putExtra("phone", item.phone);
				setResult(0,intent);
				finish();
				
			}
		});
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		}
	}

}
