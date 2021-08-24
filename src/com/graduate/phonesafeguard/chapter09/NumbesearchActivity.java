package com.graduate.phonesafeguard.chapter09;

import java.util.List;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter09.db.dao.NumSearchDao;
import com.graduate.phonesafeguard.chapter09.entity.ChildInfo;
import com.graduate.phonesafeguard.chapter09.entity.GroupInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class NumbesearchActivity extends Activity implements OnClickListener {
	private ExpandableListView evList;
	private NumberSearchAdapter mAdapter;
	private List<GroupInfo> mCommonSearchList;
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_numbesearch);
			initView();
		}
	private void initView(){
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.deep_red));
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView)findViewById(R.id.tv_title)).setText("常用号码查询");
		mLeftImgv.setImageResource(R.drawable.title_back);
		mLeftImgv.setOnClickListener(this);
		evList = (ExpandableListView) findViewById(R.id.elv_list);
		//从数据库中读取号码
		mCommonSearchList = NumSearchDao.getNumberGroup();
		mAdapter = new NumberSearchAdapter();
		evList.setAdapter(mAdapter);
		evList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				ChildInfo child = mAdapter.getChild(groupPosition, childPosition);
				Intent intent=new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+child.number));
				startActivity(intent);
				return false;
			}
		});
	}
	//适配器
	class NumberSearchAdapter extends BaseExpandableListAdapter{
		//返回组的数量
		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return mCommonSearchList.size();
		}
		//返回孩子的数量
		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return mCommonSearchList.get(groupPosition).childInfo.size();
		}
		//返回组的对象
		@Override
		public GroupInfo getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return mCommonSearchList.get(groupPosition);
		}
		//返回组孩子的对象
		@Override
		public ChildInfo getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return mCommonSearchList.get(groupPosition).childInfo.get(childPosition);
		}
		//返回组id
		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}
		//返回孩子id
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}
		//是否有稳定id
		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}
		//组的布局getView
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView textView=new TextView(getApplicationContext());
			textView.setTextColor(Color.RED);
			textView.setTextSize(30);
			GroupInfo groupInfo = getGroup(groupPosition);
			textView.setText("     "+groupInfo.name);
			return textView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView textView=new TextView(getApplicationContext());
			textView.setTextColor(Color.RED);
			textView.setTextSize(20);
//			textView.setText("第"+groupPosition+"组"+"第"+childPosition+"项");
			ChildInfo childInfo = getChild(groupPosition, childPosition);
			textView.setText("   "+childInfo.name+"\n"+"   "+childInfo.number);
			return textView;
		}
		//表示孩子是否可以点击
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		}
	}

}
