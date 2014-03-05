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
import android.database.sqlite.SQLiteDatabase;

import com.invizorys.cc.testproject.db.DBHelper;

@UsingDatabaseMap(SQLiteMap.class)
@RunWith(RobolectricTestRunner.class)
public class SQLiteTest {

	private SQLiteDatabase _db;
	private DBHelper dbHelper;
	
	@Before
    public void setUp() throws Exception {
		Context context = new Activity();
		dbHelper = new DBHelper(context);
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
	public void testOnUpgrade() {
		dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 2);
		assertEquals(2, dbHelper.getWritableDatabase().getVersion());
	}
	
	@After
    public void teardown() {
        _db.close();
    }

	
	
}
