package com.graduate.phonesafeguard.chapter09;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter09.db.dao.NumBelongtoDao;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NumBelongtoActivity extends Activity implements OnClickListener{
	private EditText mNumTV;
	private String dbName="address.db";
	private TextView mResultTV;
	private Button mbtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_numbelongto);
		initView();
//		copyDB(dbName);
	}
	private void initView(){
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.deep_red));
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView)findViewById(R.id.tv_title)).setText("号码归属地查询");
		mLeftImgv.setImageResource(R.drawable.title_back);
		mLeftImgv.setOnClickListener(this);
		mNumTV = (EditText) findViewById(R.id.et_num_numbelongto);
		mbtn = (Button) findViewById(R.id.btn_searchnumbelongto);
		mbtn.setOnClickListener(this);
		mResultTV = (TextView) findViewById(R.id.tv_searchresult);
		mNumTV.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String string = s.toString().trim();
				if(string.length()==0){
					mResultTV.setText("");
				}
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
		case R.id.btn_searchnumbelongto:
			//判断edittext中的号码是否为空
			//判断数据库是否存在
			String phonenumber=mNumTV.getText().toString().trim();
			if(!TextUtils.isEmpty(phonenumber)){
				File file=new File(getFilesDir(),dbName);
				if(!file.exists() &&file.length()>0){
					//数据库不存在
					copyDB(dbName);
				}
				//查询
				String location = NumBelongtoDao.getLocation(phonenumber);
				mResultTV.setText("归属地:"+location);
			}else{
				Toast.makeText(this, "请输入需要查询的号码", 0).show();
			}
			break;
		}
	}
	/**
	 * 复制资源目录下的数据库文件
	 */
	public void copyDB(final String dbname){
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				File file=new File(getFilesDir(),dbname);
				if(file.exists()&& file.length()>0){
					System.out.println("数据库已经存在");
					return;
				}
				try {
					InputStream is=getAssets().open(dbname);
					FileOutputStream fos=openFileOutput(dbname, MODE_PRIVATE);
					byte[] buffer=new byte[1024];
					int len=0;
					while ((len=is.read(buffer))!=-1) {
						fos.write(buffer,0,len);
					}
					is.close();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}
}
