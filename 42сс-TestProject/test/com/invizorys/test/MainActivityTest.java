package com.invizorys.test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.widget.TextView;

import com.invizorys.cc.testproject.MainActivity;
import com.invizorys.cc.testproject.R;
import com.invizorys.cc.testproject.db.DBHelper;
import com.invizorys.cc.testproject.db.User;

@RunWith(RobolectricTestRunner.class) @Config(reportSdk = 10)
public class MainActivityTest {
	private MainActivity activity;
	private TextView name, surname, dateOfBirth;
	private User user;

	@Before
    public void setUp() {
		activity = Robolectric.buildActivity(MainActivity.class).create().get();
		
		name = (TextView) activity.findViewById(R.id.textView_name);
		surname = (TextView) activity.findViewById(R.id.textView_surname);
		dateOfBirth = (TextView) activity.findViewById(R.id.textView_birthday);
	}
	
	@Test
	public void testFieldsCreated() {
		assertNotNull(name);
		assertNotNull(surname);
		assertNotNull(dateOfBirth);
	}
	
	@Test
	public void testFieldsNotEmpty() {
		assertNotSame("", name.getText().toString());
		assertNotNull("", surname.getText().toString());
		assertNotNull("", dateOfBirth.getText().toString());
	}
	
	@Test
	public void isDataFromDB()
	{
		DBHelper dbHelper = new DBHelper(activity);
		user = dbHelper.readUserData();
		assertEquals(user.getName(), name.getText().toString());
		assertEquals(user.getSurname(), surname.getText().toString());
		assertEquals(user.getBirthday(), dateOfBirth.getText().toString());
	}
}