package com.invizorys.test;

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

@RunWith(RobolectricTestRunner.class) @Config(reportSdk = 10)
public class MainActivityTest {
	private MainActivity activity;
	private TextView name, surname, dateOfBirth;
	
	private static final String TEXTVIEW_NAME_VALUE = "Name: Roman";
	private static final String TEXTVIEW_SURNAME_VALUE = "Surname: Paryshkura";
	private static final String TEXTVIEW_DATE_OF_BIRTH_VALUE = "date of birth: 23.07.94";

	@Before
    public void setUp() {
		activity = Robolectric.buildActivity(MainActivity.class).create().get();
		
		name = (TextView) activity.findViewById(R.id.textView_name);
		surname = (TextView) activity.findViewById(R.id.textView_surname);
		dateOfBirth = (TextView) activity.findViewById(R.id.textView_birthday);
	}
	
	@Test
	public void testTextViewsInfoIsCorrect() throws Exception {
		assertEquals(TEXTVIEW_NAME_VALUE, name.getText().toString());
		assertEquals(TEXTVIEW_SURNAME_VALUE, surname.getText().toString());
		assertEquals(TEXTVIEW_DATE_OF_BIRTH_VALUE, dateOfBirth.getText().toString());
	}
}