package com.invizorys.cc.testproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static SQLiteDatabase db;
	private ContentValues cv;
	
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
	
	public User readUserData()
	{
		User user = new User();
		db = this.getWritableDatabase();

		Log.d(LOG_TAG, "--- Read data from DB: ---");

		Cursor c = db.query("dataTable", null, null, null, null, null, null);

		if (c.moveToFirst()) {

			int userIdColIndex = c.getColumnIndex("id");
			int nameColIndex = c.getColumnIndex("name");
			int surnameColIndex = c.getColumnIndex("surname");
			int birthdayColIndex = c.getColumnIndex("birthday");

			user.setId(Long.valueOf(c.getString(userIdColIndex)).longValue());
			user.setName(c.getString(nameColIndex));
			user.setSurname(c.getString(surnameColIndex));
			user.setBirthday(c.getString(birthdayColIndex));

		} else
			Log.d(LOG_TAG, "0 rows");
		c.close();
		
		return user;
	}
	
	public void updateData(User user)
	{
		db = this.getWritableDatabase();
		cv = new ContentValues();
		Log.d(LOG_TAG, "--- Insert in dataTable: ---");

		cv.put("id", user.getId());
		cv.put("name", user.getName());
		cv.put("surname", user.getSurname());
		cv.put("birthday", user.getBirthday());

		long rowID = db.update(DATA_TABLE_NAME, cv, "id = ?", new String[] { String.valueOf(user.getId()) });
		Log.d(LOG_TAG, "row updated, ID = " + rowID);
		db.close();
	}

	public static SQLiteDatabase getDb() {
		return db;
	}

	public static void setDb(SQLiteDatabase db) {
		DBHelper.db = db;
	}
}

