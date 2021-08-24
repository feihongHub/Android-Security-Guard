package com.graduate.phonesafeguard.chapter01.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class SetpagerAdapter extends PagerAdapter {
	private List<View> list;
	private Context context;
	
	public SetpagerAdapter(Context context,List<View> list) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view==object;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(list.get(position));
		return list.get(position);
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(list.get(position));
		
	}

}
