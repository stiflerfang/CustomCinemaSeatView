package com.stifler.ccsv;

import android.app.Application;

public class MyApplication extends Application
{
	private static MyApplication instance = null;

	public static MyApplication getApplication()
	{
		return instance;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = (MyApplication) getApplicationContext();
	}

}
