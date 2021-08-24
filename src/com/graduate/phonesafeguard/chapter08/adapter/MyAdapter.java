package com.graduate.phonesafeguard.chapter08.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.graduate.phonesafeguard.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<HashMap<String, Object>> item;

	public MyAdapter(Context context, ArrayList<HashMap<String, Object>> item) {
		super();
		this.context = context;
		this.item = item;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return item.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return item.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null){
			convertView=View.inflate(context, R.layout.item_traffic_list, null);
			holder=new ViewHolder();
			holder.mimage = (ImageView) convertView.findViewById(R.id.showitum_image);
			holder.mnametextv = (TextView) convertView
					.findViewById(R.id.showitem_appname);
			holder.mrxtextv = (TextView) convertView
					.findViewById(R.id.showitem_rxdata);
			holder.mtxtextv = (TextView) convertView
					.findViewById(R.id.showitem_txdata);
			holder.mtotaltextv = (TextView) convertView
					.findViewById(R.id.showitem_totaldata);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.mimage.setImageDrawable((Drawable)item.get(position).get("appsimage"));
		holder.mnametextv.setText(item.get(position).get("appsname").toString());
		holder.mrxtextv.setText(item.get(position).get("rxdata").toString());
		holder.mtxtextv.setText(item.get(position).get("txdata").toString());
		holder.mtotaltextv.setText(item.get(position).get("alldata").toString());
		return convertView;
	}
	private class ViewHolder{
		public ImageView mimage;
		public TextView mnametextv, mrxtextv, mtxtextv, mtotaltextv;
	}

}
