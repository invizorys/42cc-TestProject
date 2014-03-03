package com.invizorys.test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.widget.TextView;

import com.invizorys.cc.testproject.MainActivity;
import com.invizorys.cc.testproject.R;

@RunWith(RobolectricTestRunner.class) @Config(reportSdk = 10)
public class MainActivityTest {
	private MainActivity activity;
	private TextView name, surname, dateOfBirth;

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
}