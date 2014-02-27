package com.invizorys.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.widget.TextView;

import com.invizorys.cc.testproject.MainActivity;
import com.invizorys.cc.testproject.R;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
	private MainActivity activity;

	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(MainActivity.class).create().get();
	}

	@Test
	public void testTextViewsInfoIsCorrect() throws Exception {

		TextView name = (TextView) activity.findViewById(R.id.textView_name);
		assertThat(name.getText().toString(), equalTo("Name: Roman"));

		TextView surname = (TextView) activity.findViewById(R.id.textView_surname);
		assertThat(surname.getText().toString(), equalTo("Surname: Paryshkura"));

		TextView dateOfBirth = (TextView) activity.findViewById(R.id.textView_birthday);
		assertThat(dateOfBirth.getText().toString(), equalTo("date of birth: 23.07.94"));
	}
}