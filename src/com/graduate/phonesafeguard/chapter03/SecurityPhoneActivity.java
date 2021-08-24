package com.graduate.phonesafeguard.chapter03;

import java.util.ArrayList;

import java.util.List;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter03.adapter.BlackContactAdapter;
import com.graduate.phonesafeguard.chapter03.adapter.BlackContactAdapter.BlackContactCallBack;
import com.graduate.phonesafeguard.chapter03.db.dao.BlackNumberDao;
import com.graduate.phonesafeguard.chapter03.entity.BlackContactInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SecurityPhoneActivity extends Activity implements OnClickListener {
	//当有黑名单时，显示帧布局
	private FrameLayout mHaveBlackNumber;
	//当没有黑名单时，不显示帧布局
	private FrameLayout mNoBlackNumber;
	//数据库操作
	private BlackNumberDao dao;
	private ListView mListView;
	private int pagenumber=0;
	private int pagesize=15;
	private int totalNumber;
	private List<BlackContactInfo> pageBlackNumber=new ArrayList<BlackContactInfo>();
	private BlackContactAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_securityphone);
		initView();
		fillData();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(totalNumber!=dao.getTotalNumber()){
			if(dao.getTotalNumber()>0){
				mHaveBlackNumber.setVisibility(View.VISIBLE);
				mNoBlackNumber.setVisibility(View.GONE);
			}
		}else{
			mHaveBlackNumber.setVisibility(View.GONE);
			mNoBlackNumber.setVisibility(View.VISIBLE);
		}
		pagenumber=0;
		pageBlackNumber.clear();
		pageBlackNumber.addAll(dao.getPageBlackNumber(pagenumber, pagesize));
		//页面重新加载后
		System.out.println("适配功能在运行");
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}else{
			adapter=new BlackContactAdapter(pageBlackNumber, SecurityPhoneActivity.this);
			adapter.setCallBack(new BlackContactCallBack() {
				@Override
				public void DataSizeChanged() {
					// TODO Auto-generated method stub
					fillData();
				}
			});
			mListView.setAdapter(adapter);
		}

	}
	//用于填充数据,重新刷新页面
	private void fillData(){
		dao=new BlackNumberDao(SecurityPhoneActivity.this);
		int totalNumber = dao.getTotalNumber();
		if(totalNumber==0){
			//数据库中没有黑名单数据
			mHaveBlackNumber.setVisibility(View.GONE);
			mNoBlackNumber.setVisibility(View.VISIBLE);

		}else if(totalNumber>0){
			//数据库中有数据
			mHaveBlackNumber.setVisibility(View.VISIBLE);
			mNoBlackNumber.setVisibility(View.GONE);
			pagenumber=0;
			if(pageBlackNumber.size()>0){
				pageBlackNumber.clear();
			}
			pageBlackNumber.addAll(dao.getPageBlackNumber(pagenumber, pagesize));
			if(adapter==null){
				adapter=new BlackContactAdapter(pageBlackNumber, SecurityPhoneActivity.this);
				adapter.setCallBack(new BlackContactCallBack() {

					@Override
					public void DataSizeChanged() {
						// TODO Auto-generated method stub
						fillData();
					}
				});
				mListView.setAdapter(adapter);
//				adapter.notifyDataSetChanged();
			}else{
				adapter.notifyDataSetChanged();
			}
		}
	}

	private void initView(){
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple_red));
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView) findViewById(R.id.tv_title)).setText("通讯卫士");
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.title_back);
		mHaveBlackNumber=(FrameLayout) findViewById(R.id.fl_haveblacknumber);
		mNoBlackNumber=(FrameLayout) findViewById(R.id.fl_noblacknumber);
		findViewById(R.id.btn_addblacknumber).setOnClickListener(this);
		mListView=(ListView) findViewById(R.id.lv_blacknumbers);
		//列表的点击事件
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE://没有滑动的状态
					//获取最后一个可见条目
					int lastVisiblePosition = mListView.getLastVisiblePosition();
					//如果当前条目是最后一个则查询更多数据
					if(lastVisiblePosition==pageBlackNumber.size()-1){
						pagenumber++;
						if(pagesize*pagenumber>=totalNumber){
							Toast.makeText(SecurityPhoneActivity.this, "没有更多的数据了", Toast.LENGTH_SHORT).show();
						}else{
							pageBlackNumber.addAll(dao.getPageBlackNumber(pagenumber, pagesize));
							adapter.notifyDataSetChanged();
						}
					}

					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:	
			finish();
			break;
		case R.id.btn_addblacknumber:	
			startActivity(new Intent(this,AddBlackNumberActivity.class));
			break;
		}
	}

}
