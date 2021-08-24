package com.graduate.phonesafeguard.chapter03.adapter;

import java.util.List;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter03.db.dao.BlackNumberDao;
import com.graduate.phonesafeguard.chapter03.entity.BlackContactInfo;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class BlackContactAdapter extends BaseAdapter {
	private List<BlackContactInfo> contactInfos;
	private Context context;
	private BlackNumberDao dao;
	private BlackContactCallBack callBack;
	public void setCallBack(BlackContactCallBack callBack){
		this.callBack=callBack;
	}

	public BlackContactAdapter(List<BlackContactInfo> systemContacts,Context context ) {
		super();
		// TODO Auto-generated constructor stub
		this.contactInfos=systemContacts;
		this.context=context;
		dao=new BlackNumberDao(context);

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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mholder=null;
		if(convertView==null){
			convertView=View.inflate(context, R.layout.item_list_blackcontact, null);
			mholder=new ViewHolder();
			mholder.mNameTV=(TextView) convertView.findViewById(R.id.tv_black_name);
			mholder.mModeTV=(TextView) convertView.findViewById(R.id.tv_black_mode);
			mholder.mContactImgv=convertView.findViewById(R.id.view_black_icon);
			mholder.mDeleteView=convertView.findViewById(R.id.view_black_delete);
			convertView.setTag(mholder);
		}else{
			mholder=(ViewHolder) convertView.getTag();
		}
		mholder.mNameTV.setText(contactInfos.get(position).contactName+"("+contactInfos.get(position).phoneNumber+")");
		mholder.mModeTV.setText(contactInfos.get(position).getModeString(contactInfos.get(position).mode));
		mholder.mNameTV.setTextColor(context.getResources().getColor(R.color.purple_red));
		mholder.mModeTV.setTextColor(context.getResources().getColor(R.color.purple_red));
		mholder.mContactImgv.setBackgroundResource(R.drawable.brightpurple_contact_icon);
		mholder.mDeleteView.setBackgroundResource(R.drawable.view_black_delete);
		mholder.mDeleteView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean delete = dao.delete(contactInfos.get(position));
				if(delete){
					contactInfos.remove(contactInfos.get(position));
					BlackContactAdapter.this.notifyDataSetChanged();
					//如果数据库中没有数据了，则执行回调函数
					if(dao.getTotalNumber()==0){
						callBack.DataSizeChanged();
					}
				}else{
					Toast.makeText(context, "删除失败", 0).show();
				}
			}
		});
		return convertView;
	}
	//创建一个ViewHolder
	static class ViewHolder{
		TextView mNameTV;
		TextView mModeTV;
		View mContactImgv;
		View mDeleteView;
	}
	//接口回调
	public interface BlackContactCallBack{
		void DataSizeChanged();
	}
}
