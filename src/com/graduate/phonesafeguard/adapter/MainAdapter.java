package com.graduate.phonesafeguard.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MainAdapter extends PagerAdapter{
	public List<View> listvp;
	public MainAdapter(List<View> listvp) {
		// TODO Auto-generated constructor stub
		this.listvp=listvp;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listvp.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view==object;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
		container.removeView(listvp.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(listvp.get(position));
		return listvp.get(position);
	}

}
