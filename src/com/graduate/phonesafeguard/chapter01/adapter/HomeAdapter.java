package com.graduate.phonesafeguard.chapter01.adapter;

import com.graduate.phonesafeguard.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeAdapter extends BaseAdapter {
	private int [] image={R.drawable.safe_lock,R.drawable.safe_contact,R.drawable.software_manage,
			R.drawable.kill_virus,R.drawable.cache_clear,R.drawable.thread_manage,R.drawable.statistic_flows,
			R.drawable.advange_tool,R.drawable.setting_center};
	private String [] names={"手机防盗","通讯卫士","软件管家","手机杀毒","缓存清理","进程管理","流量统计"
			,"高级工具","设置中心"};
	private Context context;
	
	public HomeAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return image.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	/**
	 * 设置每一个条目页面
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view=View.inflate(context, R.layout.item_home, null);
		ImageView imageView=(ImageView) view.findViewById(R.id.iv_icon);
		TextView textView=(TextView) view.findViewById(R.id.tv_name);
		imageView.setImageResource(image[position]);
		textView.setText(names[position]);
		return view;
	}

}
