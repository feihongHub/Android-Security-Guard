package com.graduate.phonesafeguard.chapter01.adapter;

import com.graduate.phonesafeguard.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeAdapter2 extends BaseAdapter {
	private int[]image={R.drawable.battery_observe,R.drawable.twodcode,R.drawable.information};
	private String[] name={"电量查询","二维码扫描","关于我们"};
	private Context context;
	public HomeAdapter2(Context context) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view=View.inflate(context, R.layout.item_home, null);
		ImageView imageView=(ImageView) view.findViewById(R.id.iv_icon);
		TextView textView=(TextView) view.findViewById(R.id.tv_name);
		imageView.setImageResource(image[position]);
		textView.setText(name[position]);
		return view;
	
	}

}
