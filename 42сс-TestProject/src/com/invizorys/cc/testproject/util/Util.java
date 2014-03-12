package com.invizorys.cc.testproject.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class Util {
	final static String LOG_TAG = "myLogs";

	public static void savePhotoOnSdcard(Bitmap bitmap, String name, Context context) {
		String photoPath = Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName();
		File photoFile = new File(photoPath + "/" + name + ".jpg");
		File dir = new File(photoPath);
		if (!dir.exists())
			dir.mkdirs();

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

		try {
			photoFile.createNewFile();
			FileOutputStream fo = new FileOutputStream(photoFile);
			fo.write(bytes.toByteArray());
			fo.close();
			Log.d(LOG_TAG, "photo " + name + ".jpg saved on sdcard");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnected();
	}
	
	public static boolean saveFriendPriority(Context context, String friendId, int priority)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(friendId, priority);
		return editor.commit();
	}
	
	public static int loadFriendPriority(Context context, String friendId)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context); 
	    return sp.getInt(friendId, 0);
	}

}
