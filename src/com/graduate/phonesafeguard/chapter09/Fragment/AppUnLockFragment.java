package com.graduate.phonesafeguard.chapter09.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter09.adapter.AppLockAdapter;
import com.graduate.phonesafeguard.chapter09.db.dao.AppLockDao;
import com.graduate.phonesafeguard.chapter09.entity.AppInfo;
import com.graduate.phonesafeguard.chapter09.entity.AppInfoParser;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class AppUnLockFragment extends Fragment {
	private Uri uri=Uri.parse("content://com.graduate.phonesafeguard.applock");
	private TextView mUnLockTV;
	private ListView mUnLockLV;
	private AppLockDao dao;
	private List<AppInfo> appInfos;
	private List<AppInfo> unlockApps=new ArrayList<AppInfo>();
	private AppLockAdapter adapter;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				unlockApps.clear();
				unlockApps.addAll((List<AppInfo>)msg.obj);
				if(adapter==null){
					adapter=new AppLockAdapter(unlockApps, getActivity());
					mUnLockLV.setAdapter(adapter);
				}else{
					adapter.notifyDataSetChanged();
				}
				mUnLockTV.setText("δ����Ӧ��"+unlockApps.size()+"��");
				break;
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_appunlock, null);
		mUnLockTV = (TextView) view.findViewById(R.id.tv_unlock);
		mUnLockLV = (ListView) view.findViewById(R.id.lv_unlock);
		return view;
	}
	@Override
	public void onResume() {
		dao = new AppLockDao(getActivity());
		appInfos = AppInfoParser.getAppInfos(getActivity());
		fillData();
		initListener();
		super.onResume();
		getActivity().getContentResolver().registerContentObserver(uri, true, 
				new ContentObserver(new Handler()) {
			@Override
			public void onChange(boolean selfChange) {
				// TODO Auto-generated method stub
				
				fillData();
			}
		});
	}
	public void fillData(){
		final List<AppInfo> apInfos=new ArrayList<AppInfo>();
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (AppInfo info : appInfos) {
					if(!dao.find(info.packageName)){
						//δ����
						info.isLock=false;
						apInfos.add(info);
					}
				}
				Message msg=new Message();
				msg.obj=apInfos;
				msg.what=100;
				handler.sendMessage(msg);
			
			};
		}.start();
	}
	private void initListener(){
		mUnLockLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub
				if(unlockApps.get(position).packageName.equals("com.graduate.phonesafeguard")){
					return;
				}
				//��Ӧ�ý�����������һ������
				//�����ƶ�
				//���������
				TranslateAnimation ta=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
						Animation.RELATIVE_TO_SELF,1,
						Animation.RELATIVE_TO_SELF,0,
						Animation.RELATIVE_TO_SELF, 0);
				ta.setDuration(300);
				view.startAnimation(ta);
				new Thread(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//���򱻼������ݿ�
								dao.insert(unlockApps.get(position).packageName);
								unlockApps.remove(position);
								adapter.notifyDataSetChanged();
							}
						});
					};
				}.start();
			}
		});
	}
}
