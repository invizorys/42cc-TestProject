package com.invizorys.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.widget.Button;

import com.invizorys.cc.testproject.LoginActivity;
import com.invizorys.cc.testproject.R;

@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {

	private LoginActivity activity;
	private Button button;

	@Before
	public void setUp() {
		activity = Robolectric.buildActivity(LoginActivity.class).create().get();
		button = (Button) activity.findViewById(R.id.button_login);
	}
	
	@Test
	public void testButton() throws Exception {
		assertNotNull(button);
	}
	
	@Test
	public void testActivity() throws Exception {
		assertNotNull(activity);
	}

}
