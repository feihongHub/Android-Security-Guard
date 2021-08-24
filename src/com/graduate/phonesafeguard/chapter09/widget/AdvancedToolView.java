package com.graduate.phonesafeguard.chapter09.widget;

import com.graduate.phonesafeguard.R;

import android.R.attr;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AdvancedToolView extends RelativeLayout {
//	private static final String nameSpace = "http://schemas.android.com/apk/res/com.graduate.phonesafeguard";
	private TextView mDescriptionTV;
	private ImageView mLeftImgv;
	private String desc;
	private Drawable drawable;
	public AdvancedToolView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	public AdvancedToolView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//收到属性对象
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.AdvancedToolsView);
		desc = mTypedArray.getString(R.styleable.AdvancedToolsView_desc);
		drawable = mTypedArray.getDrawable(R.styleable.AdvancedToolsView_android_src);
		//回收属性对象
		mTypedArray.recycle();
		init(context);
		
	}

	public AdvancedToolView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}
	private void init(Context context){
		View child=View.inflate(context, R.layout.ui_advancetools_view, null);
		this.addView(child);
		mDescriptionTV = (TextView) findViewById(R.id.tv_decription);
		mLeftImgv = (ImageView) findViewById(R.id.imgv_left);
		mDescriptionTV.setText(desc);
		if(drawable!=null){
			mLeftImgv.setImageDrawable(drawable);
		}
	}

}
