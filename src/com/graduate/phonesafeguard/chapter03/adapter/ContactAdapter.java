package com.graduate.phonesafeguard.chapter03.adapter;

import java.util.List; 

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter03.entity.ContactInfo;
import com.graduate.phonesafeguard.chapter03.entity.BlackContactInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {
	private Context context;
	private List<ContactInfo> contactInfos;
	
	public ContactAdapter(Context context,List<ContactInfo> contactInfos) {
		super();
		// TODO Auto-generated constructor stub
		this.context=context;
		this.contactInfos=contactInfos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contactInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contactInfos.get(position);
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
			convertView=View.inflate(context, R.layout.item_list_contact_select, null);
			holder=new ViewHolder();
			holder.mNameTV=(TextView) convertView.findViewById(R.id.tv_name);
			holder.mPhoneTV=(TextView) convertView.findViewById(R.id.tv_phone);
			holder.mContactImgv=convertView.findViewById(R.id.view1);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
//		holder.mimage.setImageResource(context.getResources().getDrawable(R.drawable.brightpurple_contact_icon));
		holder.mNameTV.setText(contactInfos.get(position).name);
		holder.mPhoneTV.setText(contactInfos.get(position).phone);
		holder.mNameTV.setTextColor(context.getResources().getColor(R.color.purple_red));
		holder.mPhoneTV.setTextColor(context.getResources().getColor(R.color.purple_red));
		holder.mContactImgv.setBackgroundResource(R.drawable.brightpurple_contact_icon);
		return convertView;
	}
	static class ViewHolder{
		
		TextView mNameTV;
		TextView mPhoneTV;
		View mContactImgv;
	}

}
