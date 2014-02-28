package com.invizorys.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.DatabaseConfig;
import org.robolectric.util.DatabaseConfig.UsingDatabaseMap;
import org.robolectric.util.SQLiteMap;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.invizorys.cc.testproject.util.DBHelper;

@UsingDatabaseMap(SQLiteMap.class)
@RunWith(RobolectricTestRunner.class)
public class SQLiteTest {

	private SQLiteDatabase _db;
	private String name, surname, birthday, bio, skype;
	
	private static final String NAME_VALUE = "Roman";
	private static final String SURNAME_VALUE = "Paryshkura";
	private static final String DATE_OF_BIRTH_VALUE = "23.07.94";
	private static final String BIO_VALUE = "student";
	private static final String SKYPE_VALUE = "invizorys";
	
	@Before
    public void setUp() throws Exception {
		Context context = new Activity();
		DBHelper dbHelper = new DBHelper(context);
        _db = dbHelper.getWritableDatabase();
    }
	
	@Test
	public void isSQLite(){
		Assert.assertTrue(DatabaseConfig.getDatabaseMap().getClass().getName().equals(SQLiteMap.class.getName()));
	}
	
	@Test
    public void canGetWriteableDB() {   
        assertTrue(_db != null);
    }
	
	@Test
	public void checkDbData()
	{
		DBHelper.insertData();
		
		Cursor c = _db.query("dataTable", null, null, null, null, null, null);
		if (c.moveToFirst()) {
			name = c.getString(c.getColumnIndex("name"));
			surname = c.getString(c.getColumnIndex("surname"));
			birthday =  c.getString(c.getColumnIndex("birthday"));
			bio = c.getString(c.getColumnIndex("bio"));
			skype = c.getString(c.getColumnIndex("skype"));
		}
		assertEquals(NAME_VALUE, name);
		assertEquals(SURNAME_VALUE, surname);
		assertEquals(DATE_OF_BIRTH_VALUE, birthday);
		assertEquals(BIO_VALUE, bio);
		assertEquals(SKYPE_VALUE, skype);
	}
	
	@After
    public void teardown() {
        _db.close();
    }

	
	
}
