package com.graduate.phonesafeguard.chapter06.adapter;

import java.util.List;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter06.entity.CacheInfo;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CacheCleanAdapter extends BaseAdapter {
	private Context context;
	private List<CacheInfo> cacheInfos;
	public CacheCleanAdapter(android.content.Context context, List<CacheInfo> cacheInfos) {
		super();
		this.context = context;
		this.cacheInfos = cacheInfos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cacheInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cacheInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=View.inflate(context, R.layout.item_cacheclean_list, null);
			holder.mAppIconImgv=(ImageView) convertView.findViewById(R.id.imgv_appicon_cacheclean);
			holder.mAppNameTV=(TextView) convertView.findViewById(R.id.tv_appname_cacheclean);
			holder.mCacheSizeTV=(TextView) convertView.findViewById(R.id.tv_appsize_cacheclean);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		CacheInfo cacheInfo=cacheInfos.get(position);
		holder.mAppIconImgv.setImageDrawable(cacheInfo.appIcon);
		holder.mAppNameTV.setText(cacheInfo.appName);
		holder.mCacheSizeTV.setText(Formatter.formatFileSize(context, cacheInfo.cacheSize));
		return convertView;
	}
	static class ViewHolder{
		ImageView mAppIconImgv;
		TextView mAppNameTV;
		TextView mCacheSizeTV;
	}

}
