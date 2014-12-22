package com.stifler.ccsv;

import android.content.Context;
import android.util.DisplayMetrics;


public class DisplayMetricsUtils
{
	private static final Context mContext;
	private static DisplayMetrics dm;

	static
	{
		mContext = MyApplication.getApplication();
		dm = mContext.getResources().getDisplayMetrics();
	}

	public static float getHeight()
	{
		return dm.heightPixels;
	}

	public static float getWidth()
	{
		return dm.widthPixels;
	}

	public static float getDensity()
	{
		return dm.density;
	}

	public static float dp2px(float dpValue)
	{
		return dpValue * dm.density + 0.5f;
	}

	public static float px2dp(float pxValue)
	{
		return pxValue / dm.density + 0.5f;
	}
}
