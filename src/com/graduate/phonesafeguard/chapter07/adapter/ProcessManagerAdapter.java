package com.graduate.phonesafeguard.chapter07.adapter;

import java.util.List;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter04.utils.DensityUtils;
import com.graduate.phonesafeguard.chapter07.entity.TaskInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProcessManagerAdapter extends BaseAdapter {
	private Context context;
	private List<TaskInfo> mUserTaskInfos;
	private List<TaskInfo> mSystemInfos;
	private SharedPreferences mSp;
	
	public ProcessManagerAdapter(Context context, List<TaskInfo> mUserTaskInfos, List<TaskInfo> mSystemInfos) {
		super();
		this.context = context;
		this.mUserTaskInfos = mUserTaskInfos;
		this.mSystemInfos = mSystemInfos;
		mSp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mSystemInfos.size()>0 & mSp.getBoolean("ShowSystemProcess", true)){
			return mUserTaskInfos.size()+mSystemInfos.size()+2;
		}else{
			return mUserTaskInfos.size()+1;
		}
		
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position==0 || position==mUserTaskInfos.size()+1){
			return null;
		}else if(position<=mUserTaskInfos.size()){
			//用户进程
			return mUserTaskInfos.get(position-1);
		}else{
			//系统进程
			return mSystemInfos.get(position-mUserTaskInfos.size()-2);
		}
		
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(position==0){
			TextView tv=getTextView();
			tv.setText("用户进程:"+mUserTaskInfos.size()+"个");
			return tv;
		}else if(position==mUserTaskInfos.size()+1){
			TextView tv=getTextView();
			if(mSystemInfos.size()>0){
				tv.setText("系统进程:"+mSystemInfos.size()+"个");
				return tv;
			}
		}
		//获取TaskInfo对象
		TaskInfo taskInfo=null;
		if(position<=mUserTaskInfos.size()){
			taskInfo=mUserTaskInfos.get(position-1);
		}else if(mSystemInfos.size()>0){
			taskInfo=mSystemInfos.get(position-mUserTaskInfos.size()-2);
		}
		ViewHolder holder=null;
		if(convertView!=null && convertView instanceof RelativeLayout){
			holder=(ViewHolder)convertView.getTag();
		}else{
			convertView=View.inflate(context, R.layout.item_processmanager, null);
			holder=new ViewHolder();
			holder.mAppIconImgv=(ImageView) convertView.findViewById(R.id.imgv_appicon_processmana);
			holder.mAppMemoryTV=(TextView) convertView.findViewById(R.id.tv_appmemory_processmana);
			holder.mAppNameTV=(TextView) convertView.findViewById(R.id.tv_appname_processmana);
			holder.mCheckBox=(CheckBox) convertView.findViewById(R.id.checkbox);
			convertView.setTag(holder);
		}
		if(taskInfo!=null){
			holder.mAppNameTV.setText(taskInfo.appName);
			holder.mAppMemoryTV.setText("占用内存:"+Formatter.formatFileSize(context, taskInfo.appMemory));
			holder.mAppIconImgv.setImageDrawable(taskInfo.appIcon);
			if(taskInfo.packageName.equals(context.getPackageName())){
				holder.mCheckBox.setVisibility(View.GONE);
			}else{
				holder.mCheckBox.setVisibility(View.VISIBLE);
			}
			holder.mCheckBox.setChecked(taskInfo.isChecked);
		}
		return convertView;
	}
	private TextView getTextView(){
		TextView tv=new TextView(context);
		tv.setBackgroundColor(context.getResources().getColor(R.color.grey));
		tv.setPadding(DensityUtils.dip2px(context, 5), DensityUtils.dip2px(context, 5),
				DensityUtils.dip2px(context, 5), DensityUtils.dip2px(context, 5));
		tv.setTextColor(context.getResources().getColor(R.color.black));
		return tv;
		
	}
	static class ViewHolder{
		ImageView mAppIconImgv;
		TextView mAppNameTV;
		TextView mAppMemoryTV;
		CheckBox mCheckBox;
	}

}
