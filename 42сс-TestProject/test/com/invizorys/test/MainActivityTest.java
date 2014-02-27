package com.invizorys.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.invizorys.cc.testproject.MainActivity;
import com.invizorys.cc.testproject.R;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

	@Test
	public void shouldHaveHappySmiles() throws Exception {
		String hello = new MainActivity().getResources().getString(
				R.string.hello_world);
		assertThat(hello, equalTo("Hello world!"));
	}
}