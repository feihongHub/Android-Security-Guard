package com.graduate.phonesafeguard.chapter04.adapter;

import java.util.List;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.R.color;
import com.graduate.phonesafeguard.chapter04.entity.AppInfo;
import com.graduate.phonesafeguard.chapter04.utils.DensityUtils;
import com.graduate.phonesafeguard.chapter04.utils.EngineUtils;

import android.content.Context;
import android.service.wallpaper.WallpaperService.Engine;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AppManagerAdapter extends BaseAdapter {
	private List<AppInfo> UserAppInfos;
	private List<AppInfo> SystemAppInfos;
	private Context context;

	public AppManagerAdapter(List<AppInfo> userAppInfos,List<AppInfo> systemAppInfos,Context context) {
		super();
		// TODO Auto-generated constructor stub
		UserAppInfos=userAppInfos;
		SystemAppInfos=systemAppInfos;
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//因为与两个条目需要用于显示用户的进程，因此系统进程需要加2

		return UserAppInfos.size()+SystemAppInfos.size()+2;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position==0){
			//第0个位置显示的应该是用户程序个数的标签
			return null;
		}else if(position==(UserAppInfos.size()+1)){
			return null;
		}
		AppInfo appInfo;
		if(position<(UserAppInfos.size()+1)){
			//用户程序
			appInfo=UserAppInfos.get(position-1);
			//多了一个TextView标签，位置-1
		}else{
			int location=position-UserAppInfos.size()-2;
			appInfo=SystemAppInfos.get(location);
		}
		return appInfo;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//如果position为0，则为Textview
		if(position==0){
			TextView tv=getTextView();
			tv.setText("用户程序:"+UserAppInfos.size()+"个");
			return tv;
			//系统应用
		}else if(position==(UserAppInfos.size()+1)){
			TextView tv=getTextView();
			tv.setText("系统程序:"+SystemAppInfos.size()+"个");
			return tv;
		}
		//获取当前App对象
		AppInfo appInfo;
		if(position<(UserAppInfos.size()+1)){
			//position 0为TextView
			appInfo=UserAppInfos.get(position-1);
		}else{
			//系统应用
			appInfo=SystemAppInfos.get(position-UserAppInfos.size()-2);
		}
		//利用ViewHolder
		ViewHolder viewHolder=null;
		if(convertView!=null & convertView instanceof LinearLayout){
			viewHolder=(ViewHolder) convertView.getTag();
			
		}else{
			viewHolder=new ViewHolder();
			convertView=View.inflate(context, R.layout.item_appmanager_list, null);
			viewHolder.mAppIconImgv=(ImageView) convertView.findViewById(R.id.imgv_appicon);
			viewHolder.mAppLocationTV=(TextView) convertView.findViewById(R.id.tv_appisroom);
			viewHolder.mAppSizeTV=(TextView) convertView.findViewById(R.id.tv_appsize);
			viewHolder.mAppNameTV=(TextView) convertView.findViewById(R.id.tv_appname);
			viewHolder.mLuanchAppTV=(TextView) convertView.findViewById(R.id.tv_launch_app);
			viewHolder.mSettingAppTV=(TextView) convertView.findViewById(R.id.tv_setting_app);
			viewHolder.mShareAppTV=(TextView) convertView.findViewById(R.id.tv_share_app);
			viewHolder.mUninstallTV=(TextView) convertView.findViewById(R.id.tv_uninstall_app);
			viewHolder.mAppOptionLL=(LinearLayout) convertView.findViewById(R.id.ll_option_app);
			convertView.setTag(viewHolder);
		}
		if(appInfo!=null){
			viewHolder.mAppLocationTV.setText(appInfo.getAppLocation(appInfo.isInRoom));
			viewHolder.mAppIconImgv.setImageDrawable(appInfo.icon);
			viewHolder.mAppSizeTV.setText(Formatter.formatFileSize(context, appInfo.appSize));
			viewHolder.mAppNameTV.setText(appInfo.appName);
			if(appInfo.isSelected){
				viewHolder.mAppOptionLL.setVisibility(View.VISIBLE);
			}else{
				viewHolder.mAppOptionLL.setVisibility(View.GONE);
			}
		}
		MyClickListener listener=new MyClickListener(appInfo);
		viewHolder.mLuanchAppTV.setOnClickListener(listener);
		viewHolder.mSettingAppTV.setOnClickListener(listener);
		viewHolder.mShareAppTV.setOnClickListener(listener);
		viewHolder.mUninstallTV.setOnClickListener(listener);
		
		return convertView;
	}
	//创建一个TextView
	private TextView getTextView(){
		TextView tv=new TextView(context);
		tv.setBackgroundColor(context.getResources().getColor(color.grey));
		tv.setPadding(DensityUtils.dip2px(context, 5), DensityUtils.dip2px(context, 5)
				, DensityUtils.dip2px(context, 5), DensityUtils.dip2px(context, 5));
		tv.setTextColor(context.getResources().getColor(color.black));
		return tv;
	}
	static class ViewHolder{
		//启动App
		TextView mLuanchAppTV;
		//卸载App
		TextView mUninstallTV;
		//分享App
		TextView mShareAppTV;
		//设置App
		TextView mSettingAppTV;
		//app图标
		ImageView mAppIconImgv;
		//app位置
		TextView mAppLocationTV;
		//app大小
		TextView mAppSizeTV;
		//app名称
		TextView mAppNameTV;
		//操作App的线性布局
		LinearLayout mAppOptionLL;

	}
	class MyClickListener implements OnClickListener{
		private AppInfo appInfo;
		
		public MyClickListener(AppInfo appInfo) {
			super();
			this.appInfo = appInfo;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tv_launch_app:
				//启动应用
				EngineUtils.startApplication(context, appInfo);
				break;
			case R.id.tv_share_app:
				//分享应用
				EngineUtils.shareApplication(context, appInfo);
				break;
			case R.id.tv_setting_app:
				//设置应用
				EngineUtils.SettingAppDetail(context, appInfo);
				break;
			case R.id.tv_uninstall_app:
				//卸载应用，需要注册广播接收者
				if(appInfo.packageName.equals(context.getPackageName())){
					Toast.makeText(context, "您没有权限卸载此应用", 0).show();
					return;
				}
				EngineUtils.uninstallApplication(context, appInfo);
				break;
			}
		}
		
	}

}
