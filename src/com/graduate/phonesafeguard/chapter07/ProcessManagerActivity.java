package com.graduate.phonesafeguard.chapter07;

import java.util.ArrayList;
import java.util.List;

import com.graduate.phonesafeguard.HomeActivity;
import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter07.adapter.ProcessManagerAdapter;
import com.graduate.phonesafeguard.chapter07.entity.TaskInfo;
import com.graduate.phonesafeguard.chapter07.utils.SystemInfoUtils;
import com.graduate.phonesafeguard.chapter07.utils.TaskInfoParser;

import android.R.bool;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProcessManagerActivity extends Activity implements OnClickListener{
	private TextView mRunProcessNum;
	private TextView mMemoryTV;
	private TextView mProcessNumTV;
	private int runningPocessCount;
	private long totalMem;
	private ListView mListView;
	private ProcessManagerAdapter adapter;
	private List<TaskInfo> userTaskInfos=new ArrayList<TaskInfo>();
	private List<TaskInfo> sysTaskInfos=new ArrayList<TaskInfo>();
	private ActivityManager manager;
	private List<TaskInfo> runningTaskInfos;
	SharedPreferences mSp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_processmanager);
		mSp=getSharedPreferences("config",MODE_PRIVATE);
		initView();
//		fillData();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		sysTaskInfos.clear();
		userTaskInfos.clear();
		runningPocessCount = SystemInfoUtils.getRunningPocessCount(ProcessManagerActivity.this);
		mRunProcessNum.setText("�����еĽ���:"+runningPocessCount+"��");
		fillData();
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
		super.onResume();
	}
	private void initView(){
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_green));
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.title_back);
		ImageView mRightImgv=(ImageView) findViewById(R.id.imgv_Rightbtn);
		mRightImgv.setOnClickListener(this);
		mRightImgv.setImageResource(R.drawable.processmanager_setting_icon);
		((TextView)findViewById(R.id.tv_title)).setText("���̹���");
		mRunProcessNum = (TextView) findViewById(R.id.tv_runningprocess_num);
		mMemoryTV = (TextView) findViewById(R.id.tv_memory_processmanager);
		mProcessNumTV = (TextView) findViewById(R.id.tv_user_runningprocess);
		runningPocessCount = SystemInfoUtils.getRunningPocessCount(ProcessManagerActivity.this);
		mRunProcessNum.setText("�����еĽ���:"+runningPocessCount+"��");
		long totalAvailMem = SystemInfoUtils.getAvailMem(ProcessManagerActivity.this);
		totalMem = SystemInfoUtils.getTotalMem();
		mMemoryTV.setText("����/���ڴ�:"+Formatter.formatFileSize(this, totalAvailMem)+"/"+Formatter.formatFileSize(this, totalMem));
		mListView=(ListView) findViewById(R.id.lv_runningapps);
		initListener();
		
	}
	private void initListener(){
		findViewById(R.id.btn_selectall).setOnClickListener(this);
		findViewById(R.id.btn_select_inverse).setOnClickListener(this);
		findViewById(R.id.btn_cleanprocess).setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Object object = mListView.getItemAtPosition(position);
				if(object!=null & object instanceof TaskInfo){
					TaskInfo info=(TaskInfo) object;
					if(info.packageName.equals(getPackageName())){
						//�����Ŀ��Ӧ�ó���
						return;
					}
					info.isChecked=!info.isChecked;
					adapter.notifyDataSetChanged();
				}
			}
		});
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem>=userTaskInfos.size()+1){
					mProcessNumTV.setText("ϵͳ����:"+sysTaskInfos.size()+"��");
				}else{
					mProcessNumTV.setText("�û�����:"+userTaskInfos.size()+"��");
				}
			}
		});
	}
	private void fillData(){
		userTaskInfos.clear();
		sysTaskInfos.clear();
		final boolean hit=mSp.getBoolean("ShowSystemProcess", true);
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				
				runningTaskInfos = TaskInfoParser.getRunningTaskInfos(getApplicationContext());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						for (TaskInfo taskInfo : runningTaskInfos) {
							if(taskInfo.isUserApp){
								userTaskInfos.add(taskInfo);
							}else{
//								sysTaskInfos.add(taskInfo);
								if(hit){
									sysTaskInfos.add(taskInfo);
								}
							}
						}
						if(adapter==null){
							adapter=new ProcessManagerAdapter(getApplicationContext(), userTaskInfos, sysTaskInfos);
							mListView.setAdapter(adapter);
						}else{
							adapter.notifyDataSetChanged();
						}
						if(userTaskInfos.size()>0){
							mProcessNumTV.setText("�û�����:"+userTaskInfos.size()+"��");
						}else{
							mProcessNumTV.setText("ϵͳ����:"+sysTaskInfos.size()+"��");
						}
					}
				});
			};
		}.start();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.imgv_Rightbtn:
			//��ת�����̹�������ҳ��
			Intent intent=new Intent(this,ProcessManagerSettingActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_selectall:
			selectAll();
			break;
		case R.id.btn_select_inverse:
			inverse();
			break;
		case R.id.btn_cleanprocess:
			cleanProcess();
			break;
		}
	}
	/**
	 * �������
	 */
	private void cleanProcess(){
		manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		int count=0;
		long saveMemory=0;
		List<TaskInfo> killedtaskInfos=new ArrayList<TaskInfo>();
		//��������
		for (TaskInfo info : userTaskInfos) {
			if(info.isChecked){
				count++;
				saveMemory+=info.appMemory;
				manager.killBackgroundProcesses(info.packageName);
				killedtaskInfos.add(info);
			}
		}
		for (TaskInfo info : sysTaskInfos) {
			if(info.isChecked){
				count++;
				saveMemory+=info.appMemory;
				manager.killBackgroundProcesses(info.packageName);
				killedtaskInfos.add(info);
			}
		}
		for (TaskInfo info : killedtaskInfos) {
			if(info.isUserApp){
				userTaskInfos.remove(info);
			}else{
				sysTaskInfos.remove(info);
			}
		}
		runningPocessCount-=count;
	
		mRunProcessNum.setText("�����еĽ���:"+runningPocessCount+"��");
		mMemoryTV.setText("����/���ڴ�:"+Formatter.formatFileSize(this, SystemInfoUtils.getAvailMem(this))+"/"+Formatter.formatFileSize(this, totalMem));
		Toast.makeText(this, "������"+count+"������,�ͷ���"+Formatter.formatFileSize(this, saveMemory)+"�ڴ�", 1).show();
		mProcessNumTV.setText("�û�����:"+userTaskInfos.size()+"��");
		adapter.notifyDataSetChanged();
	}
	/**
	 * ��ѡ
	 */
	private void inverse(){
		for (TaskInfo taskInfo : userTaskInfos) {
			//���Ǳ�Ӧ�ó���
			if(taskInfo.packageName.equals(getPackageName())){
				continue;
			}
			boolean checked=taskInfo.isChecked;
			taskInfo.isChecked=!checked;
		}
		for (TaskInfo taskInfo : sysTaskInfos) {
			boolean checked=taskInfo.isChecked;
			taskInfo.isChecked=!checked;
		}
		adapter.notifyDataSetChanged();
	}
	/**
	 * ȫѡ
	 */
	private void selectAll(){
		for (TaskInfo taskInfo : userTaskInfos) {
			//���Ǳ�Ӧ�ý���
			if(taskInfo.packageName.equals(getPackageName())){
				continue;
			}
			taskInfo.isChecked=true;
		}
		for (TaskInfo taskInfo : sysTaskInfos) {
			taskInfo.isChecked=true;
		}
		adapter.notifyDataSetChanged();
	}
	
}
