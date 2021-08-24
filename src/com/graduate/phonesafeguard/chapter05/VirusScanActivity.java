package com.graduate.phonesafeguard.chapter05;

import java.io.File; 
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.graduate.phonesafeguard.HomeActivity;
import com.graduate.phonesafeguard.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VirusScanActivity extends Activity implements OnClickListener{
	private SharedPreferences mSp;
	private TextView mLastTimeTV;
	private PopupWindow popupWindow;
	private ListView lv;
	private MyAdapter madapter;
	private ViewHolder holder;
	private RelativeLayout rl;
	private String[] name={"病毒发展史","病毒库","关于我们"};
	private int [] icon={R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
	private List<Map<String, Object>> listItem=new ArrayList<Map<String,Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature语句请求主屏幕头菜单隐藏，在关联布局文件之前
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_virusscan);

		mSp = getSharedPreferences("config", MODE_PRIVATE);
		//拷贝数据库
		copyDB("antivirus.db");
		initView();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		String string = mSp.getString("lastVirusScan", "您还没有查杀病毒!");
		mLastTimeTV.setText(string);
		super.onResume();
	}

	/**
	 * 复制病毒数据库
	 * @param dbname
	 */
	private void copyDB(final String dbname){
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					File file=new File(getFilesDir(),dbname);
					if(file.exists() && file.length()>0){
						Log.i("VirusScanActivity", "数据库已存在");
						return;
					}
					InputStream is=getAssets().open(dbname);
					FileOutputStream fos=openFileOutput(dbname, MODE_PRIVATE);
					byte[] buffer=new byte[1024];
					int len=0;
					while ((len=is.read(buffer))!=-1) {
						fos.write(buffer,0,len);
					}
					is.close();
					fos.close();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			};
		}.start();
	}
	/**
	 * 初始化控件
	 */
	private void initView(){
		rl=(RelativeLayout) findViewById(R.id.rl_aboutVirus);
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.deep_blue));
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView)findViewById(R.id.tv_title)).setText("病毒查杀");
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.title_back);
		initListView();
		mLastTimeTV = (TextView) findViewById(R.id.tv_lastscantime);
		findViewById(R.id.rl_allscanvirus).setOnClickListener(this);
		findViewById(R.id.rl_aboutVirus).setOnClickListener(this);
	}
	/**
	 * 初始化ListView
	 */
	private void initListView(){
		for (int i=0;i<name.length;i++) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("icon", icon[i]);
			map.put("name", name[i]);
			listItem.add(map);
		}
		lv=new ListView(this);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					AlertDialog.Builder builder=new Builder(VirusScanActivity.this);
					builder.setTitle("病毒发展史");
					builder.setIcon(R.drawable.ic_launcher);
					builder.setMessage("病毒科");
					builder.show();
					
					break;
				case 1:
					AlertDialog.Builder builder1=new Builder(VirusScanActivity.this);
					builder1.setTitle("病毒库");
					builder1.setIcon(R.drawable.ic_launcher);
					builder1.setMessage("病毒科");
					builder1.show();
					break;
				case 2:
					AlertDialog.Builder builder2=new Builder(VirusScanActivity.this);
					builder2.setTitle("关于我们");
					builder2.setIcon(R.drawable.ic_launcher);
					builder2.setMessage("Hello");
					builder2.show();
					break;
				}
				
			}
		});
		if(madapter==null){
			madapter = new MyAdapter();
			lv.setAdapter(madapter);
		}else{
			madapter.notifyDataSetChanged();
		}
		lv.setDividerHeight(2);
		lv.setVerticalScrollBarEnabled(false);
	}
	/**
	 * 初始化适配器
	 * @author Administrator
	 *
	 */
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItem.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			if(convertView==null){
				view=View.inflate(VirusScanActivity.this, R.layout.item_listpopwindows, null);
				holder=new ViewHolder();
				holder.tv=(TextView) view.findViewById(R.id.tv_listpopwindows_content);
				holder.iv=(ImageView) view.findViewById(R.id.imgv_listpopwindows_icon);
				view.setTag(holder);
			}else{
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			holder.iv.setImageResource((Integer) listItem.get(position).get("icon"));
			holder.tv.setText((String)listItem.get(position).get("name"));
			return view;
		}

	}
	//ViewHolder
	private class ViewHolder{
		private TextView tv;
		private ImageView iv;
	} 
	/**
	 *  设置popupwindows点击事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.rl_allscanvirus:
			startActivity(new Intent(this,VirusScanSpeedActivity.class));
			break;
		case R.id.rl_aboutVirus:
			System.out.println("点击了");
			//			popupWindow=new PopupWindow(VirusScanActivity.this);

			//			popupWindow.setOutsideTouchable(true);//点击其他位置popupWindow消失
			if(popupWindow==null){
				popupWindow=new PopupWindow(rl, rl.getWidth(), 400, true);
				popupWindow.setContentView(lv);
				popupWindow.setBackgroundDrawable(new ColorDrawable());//设置背景，点击空白处才可以消失

				popupWindow.showAsDropDown(rl, 0, 0);
			}else if(popupWindow!=null && popupWindow.isShowing()){		
				//				popupWindow.update();
				popupWindow.dismiss();
			}else {
				popupWindow.showAsDropDown(rl);
			}
			break;
		}
	}
}
