package com.invizorys.cc.testproject.db;

import com.invizorys.cc.testproject.entity.User;

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
		super(context, "myDB", null, 3);
	}
	
	private static final String DATA_TABLE_CREATE = "create table " +  DATA_TABLE_NAME + " ("
			+ "id integer primary key," + "name text,"
			+ "surname text," + "birthday text," + "email text" +");";
	
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

		Cursor c = db.query(DATA_TABLE_NAME, null, null, null, null, null, null);

		if (c.moveToLast()) {

			int userIdColIndex = c.getColumnIndex("id");
			int nameColIndex = c.getColumnIndex("name");
			int surnameColIndex = c.getColumnIndex("surname");
			int birthdayColIndex = c.getColumnIndex("birthday");
			int emailColIndex = c.getColumnIndex("email");
			
			user.setId(Long.valueOf(c.getString(userIdColIndex)).longValue());
			user.setName(c.getString(nameColIndex));
			user.setSurname(c.getString(surnameColIndex));
			user.setBirthday(c.getString(birthdayColIndex));
			user.setEmail(c.getString(emailColIndex));

		} else
			Log.d(LOG_TAG, "0 rows");
		c.close();
		
		return user;
	}
	
	public void updateData(User user)
	{
		db = this.getWritableDatabase();
		cv = new ContentValues();
		Log.d(LOG_TAG, "--- Update row in dataTable: ---");

		cv = contentValuesFilling(user);

		long rowID = db.update(DATA_TABLE_NAME, cv, "id = ?", new String[] { String.valueOf(user.getId()) });
		Log.d(LOG_TAG, "row updated, ID = " + rowID);
		db.close();
	}
	
	public void saveUserData(User user)
	{
		db = this.getWritableDatabase();
		cv = contentValuesFilling(user);
		Log.d(LOG_TAG, "--- Save row in dataTable: ---");

		long rowID = db.insert(DATA_TABLE_NAME, null, cv);
		Log.d(LOG_TAG, "row inserted, ID = " + rowID);
		Log.d(LOG_TAG, "user: id - " + user.getId() + ", name - " + user.getName() 
				+ ", surname - " + user.getSurname() + ", birthday - " + user.getBirthday() + ", - saved on DB");
		db.close();
	}
	
	public void deleteUserData(String id) {
		Log.d(LOG_TAG, "--- Delete from dataTabe: ---");
		int delCount = db.delete(DATA_TABLE_NAME, "id = " + id, null);
		Log.d(LOG_TAG, "deleted rows count = " + delCount);
	}
	
	private ContentValues contentValuesFilling(User user)
	{
		cv = new ContentValues();
		cv.put("id", user.getId());
		cv.put("name", user.getName());
		cv.put("surname", user.getSurname());
		cv.put("birthday", user.getBirthday());
		cv.put("email", user.getEmail());
		return cv;
	}

	public static SQLiteDatabase getDb() {
		return db;
	}

	public static void setDb(SQLiteDatabase db) {
		DBHelper.db = db;
	}
}

