package com.invizorys.cc.testproject.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static ContentValues cv;
	private static SQLiteDatabase db;
	
	final static String LOG_TAG = "myLogs";

	public DBHelper(Context context) {
		super(context, "myDB", null, 1);
	}
	
	private static final String DATA_TABLE_CREATE = "create table dataTable ("
			+ "id integer primary key," + "name text,"
			+ "surname text," + "birthday text," + "bio text," 
			+ "skype text," + "image Blob" + ");";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		setDb(db);
		Log.d(LOG_TAG, "--- onCreate database ---");
		db.execSQL(DATA_TABLE_CREATE);
		Log.d(LOG_TAG, "--- Table created ---");
		
		Log.d(LOG_TAG, "--- Insert in mytable: ---");
		insertData();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	public static void insertData()
	{
		cv = new ContentValues();
		Log.d(LOG_TAG, "--- Insert in dataTable: ---");

		cv.put("id", "0");
		cv.put("name", "Roman");
		cv.put("surname", "Paryshkura");
		cv.put("birthday", "23.07.94");
		cv.put("bio", "student");
		cv.put("skype", "invizorys");

		long rowID = getDb().insert("dataTable", null, cv);
		Log.d(LOG_TAG, "row inserted, ID = " + rowID);
	}

	public static SQLiteDatabase getDb() {
		return db;
	}

	public static void setDb(SQLiteDatabase db) {
		DBHelper.db = db;
	}
}

