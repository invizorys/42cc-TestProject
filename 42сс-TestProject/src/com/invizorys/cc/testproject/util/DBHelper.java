package com.invizorys.cc.testproject.util;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.invizorys.cc.testproject.R;

public class DBHelper extends SQLiteOpenHelper {
	private ContentValues cv;
	private SQLiteDatabase db;
	private Context context;
	
	final String LOG_TAG = "myLogs";

	public DBHelper(Context context) {
		super(context, "myDB", null, 1);
		this.context = context;
	}
	
	private static final String DATA_TABLE_CREATE = "create table dataTable ("
			+ "id integer primary key," + "name text,"
			+ "surname text," + "birthday text,"
			+ "image Blob" + ");";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		Log.d(LOG_TAG, "--- onCreate database ---");
		db.execSQL(DATA_TABLE_CREATE);
		Log.d(LOG_TAG, "--- Table created ---");
		
		Log.d(LOG_TAG, "--- Insert in mytable: ---");
		insertData();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	private void insertData()
	{
		cv = new ContentValues();
		Log.d(LOG_TAG, "--- Insert in dataTable: ---");

		cv.put("id", "0");
		cv.put("name", "Roman");
		cv.put("surname", "Paryshkura");
		cv.put("birthday", "23.07.94");
		cv.put("bio", "student");
		cv.put("skype", "invizorys");
		
		Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.photo);

		byte[] image = getBitmapAsByteArray(icon);
		cv.put("image", image);

		long rowID = db.insert("dataTable", null, cv);
		Log.d(LOG_TAG, "row inserted, ID = " + rowID);
	}
	
	private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, outputStream);
		return outputStream.toByteArray();
	}	
}

