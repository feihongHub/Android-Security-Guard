package com.graduate.phonesafeguard.chapter09.widget;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter04.utils.DensityUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class MyCircleProgress extends Button {

	private Paint paint;
	private Context context;
	private int progress;
	private int max;
	private int mCircleColor;
	private int mProgressColor;
	private int roundWidth;
	private int mDistanceOFbg;
	private int mProgressTextSize;
	public MyCircleProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
//		this(context, null);
		init(context, attrs);
	}

	public MyCircleProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		/// TODO Auto-generated constructor stub
		init(context, attrs);
	}

	public MyCircleProgress(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context,null);
	}
	private void init(Context context,AttributeSet attrs){
		paint = new Paint();
		this.context=context;
		TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.MyCircleProgress);
		progress=typedArray.getInteger(R.styleable.MyCircleProgress_progress, 0);
		max = typedArray.getInteger(R.styleable.MyCircleProgress_max, 100);
		mCircleColor = typedArray.getColor(R.styleable.MyCircleProgress_circleColor, Color.RED);
		mProgressColor = typedArray.getColor(R.styleable.MyCircleProgress_progressColor, Color.WHITE);
		roundWidth = DensityUtils.dip2px(context, 5);
		mDistanceOFbg = DensityUtils.dip2px(context, 5);
		mProgressTextSize = DensityUtils.dip2px(context, 16);
		typedArray.recycle();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		//??????????????
		int centerX=getWidth()/2;
		int centerY=getHeight()/2;
		int radius=(int)(centerX-mDistanceOFbg);
		//??????????????
		paint.setColor(mCircleColor);//????????
		paint.setAntiAlias(true);//??Paint????????
		paint.setStyle(Paint.Style.STROKE);//??????
		paint.setStrokeWidth(roundWidth);//????????????
		canvas.drawCircle(centerX, centerY, radius, paint);//????
		//??????????????
		paint.setColor(mProgressColor);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(roundWidth);
		//??????????????????????????????
		RectF oval=new RectF(centerX-radius, centerY-radius, centerX+radius, centerY+radius);
		paint.setStyle(Paint.Style.STROKE);
		//??????,????????????
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		//arg1:???????????????????????? 	arg2:????????????????????????????????????
		//arg3:??????true????????????????????????????????????????????????   	arg4:????
		canvas.drawArc(oval, 0, 360*progress/max, false, paint);
		//??????????????
		paint.setStrokeWidth(0);
		paint.setColor(mProgressColor);
		paint.setTextSize(mProgressTextSize);
		//????????
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		int percent=(int)(((float)progress/(float)max)*100);
		//??????????????????????????float??????????????
		float textWidth=paint.measureText(percent+"%");
		if(percent>0){
			//??????????????
			canvas.drawText(percent+"%", centerX-textWidth/2, 
					(float)(centerY+radius-mDistanceOFbg*6), paint);
		}
	}
	//????????,????????????????????????????????????????????????????

	public synchronized void setProcess(int process){
		if(process<0){
			throw new IllegalArgumentException("progress not less than 0");
		}
		if(process>max){
			process=max;
		}
		if(process<=max){
			this.progress=process;
			postInvalidate();
		}
	}
	public synchronized void setMax(int max){
		if(max<0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max=max;
	}
}
