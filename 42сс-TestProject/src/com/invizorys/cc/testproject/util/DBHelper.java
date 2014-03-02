package com.invizorys.cc.testproject.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static SQLiteDatabase db;
	
	private static final String DATA_TABLE_NAME = "dataTable";
	
	final static String LOG_TAG = "myLogs";

	public DBHelper(Context context) {
		super(context, "myDB", null, 2);
	}
	
	private static final String DATA_TABLE_CREATE = "create table " +  DATA_TABLE_NAME + " ("
			+ "id integer primary key," + "name text,"
			+ "surname text," + "birthday text" + ");";
	
	public final static String DATA_TABLE_DROP = "DROP TABLE IF EXISTS " + DATA_TABLE_NAME;
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		setDb(db);
		Log.d(LOG_TAG, "--- onCreate database ---");
		db.execSQL(DATA_TABLE_CREATE);
		Log.d(LOG_TAG, "--- Table created ---");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DATA_TABLE_DROP);
		db.execSQL(DATA_TABLE_CREATE);
	}

	public static SQLiteDatabase getDb() {
		return db;
	}

	public static void setDb(SQLiteDatabase db) {
		DBHelper.db = db;
	}
}

