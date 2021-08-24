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
		//��Ϊ��������Ŀ��Ҫ������ʾ�û��Ľ��̣����ϵͳ������Ҫ��2

		return UserAppInfos.size()+SystemAppInfos.size()+2;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position==0){
			//��0��λ����ʾ��Ӧ�����û���������ı�ǩ
			return null;
		}else if(position==(UserAppInfos.size()+1)){
			return null;
		}
		AppInfo appInfo;
		if(position<(UserAppInfos.size()+1)){
			//�û�����
			appInfo=UserAppInfos.get(position-1);
			//����һ��TextView��ǩ��λ��-1
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
		//���positionΪ0����ΪTextview
		if(position==0){
			TextView tv=getTextView();
			tv.setText("�û�����:"+UserAppInfos.size()+"��");
			return tv;
			//ϵͳӦ��
		}else if(position==(UserAppInfos.size()+1)){
			TextView tv=getTextView();
			tv.setText("ϵͳ����:"+SystemAppInfos.size()+"��");
			return tv;
		}
		//��ȡ��ǰApp����
		AppInfo appInfo;
		if(position<(UserAppInfos.size()+1)){
			//position 0ΪTextView
			appInfo=UserAppInfos.get(position-1);
		}else{
			//ϵͳӦ��
			appInfo=SystemAppInfos.get(position-UserAppInfos.size()-2);
		}
		//����ViewHolder
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
	//����һ��TextView
	private TextView getTextView(){
		TextView tv=new TextView(context);
		tv.setBackgroundColor(context.getResources().getColor(color.grey));
		tv.setPadding(DensityUtils.dip2px(context, 5), DensityUtils.dip2px(context, 5)
				, DensityUtils.dip2px(context, 5), DensityUtils.dip2px(context, 5));
		tv.setTextColor(context.getResources().getColor(color.black));
		return tv;
	}
	static class ViewHolder{
		//����App
		TextView mLuanchAppTV;
		//ж��App
		TextView mUninstallTV;
		//����App
		TextView mShareAppTV;
		//����App
		TextView mSettingAppTV;
		//appͼ��
		ImageView mAppIconImgv;
		//appλ��
		TextView mAppLocationTV;
		//app��С
		TextView mAppSizeTV;
		//app����
		TextView mAppNameTV;
		//����App�����Բ���
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
				//����Ӧ��
				EngineUtils.startApplication(context, appInfo);
				break;
			case R.id.tv_share_app:
				//����Ӧ��
				EngineUtils.shareApplication(context, appInfo);
				break;
			case R.id.tv_setting_app:
				//����Ӧ��
				EngineUtils.SettingAppDetail(context, appInfo);
				break;
			case R.id.tv_uninstall_app:
				//ж��Ӧ�ã���Ҫע��㲥������
				if(appInfo.packageName.equals(context.getPackageName())){
					Toast.makeText(context, "��û��Ȩ��ж�ش�Ӧ��", 0).show();
					return;
				}
				EngineUtils.uninstallApplication(context, appInfo);
				break;
			}
		}
		
	}

}
