package com.example.bookshare.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonUtils {
	
	private static NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}
	
	/** ����Ƿ������� */
	public static boolean isNetworkAvailable(Context context){
		NetworkInfo netinfo = getNetworkInfo(context);
		if(netinfo != null){
			return netinfo.isAvailable();
		}else{
			return false;
		}
	}
	
	/** ����Ƿ���WIFI */
	public static boolean isWifi(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI)
				return true;
		}
		return false;
	}
	
	/** ����Ƿ����ƶ����� */
	public static boolean isMobile(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE)
				return true;
		}
		return false;
	}
	
	/** ���SD���Ƿ���� */
	public static boolean checkSdCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

}
