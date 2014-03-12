package com.invizorys.cc.testproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.invizorys.cc.testproject.R;
import com.invizorys.cc.testproject.db.DBHelper;
import com.invizorys.cc.testproject.entity.User;
import com.invizorys.cc.testproject.util.Util;

public class LoginActivity extends Activity {
	private UiLifecycleHelper uiHelper;
	private GraphUser user;
	private DBHelper dbHelper;
	private Context context = this;
	private ProgressDialog dialog;
	
	final static String LOG_TAG = "myLogs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		dbHelper = new DBHelper(this);

		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			startMainActivity();
		}

		findViewById(R.id.button_login).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(Util.isNetworkConnected(context))
						{
							dialog = ProgressDialog.show(context, "", "Loading. Please wait...", true);
							login();
						}
						else
							Toast.makeText(context, "check your internet connection", Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void startMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void login() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this).setCallback(
					callback).setPermissions(Arrays.asList("email", "user_birthday")));
		} else {
			Session.openActiveSession(this, true, callback);
		}
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (session != null && session.isOpened()) {
			Log.i(LOG_TAG, "session is open");
			if(Util.isNetworkConnected(context))
				getUser();
		} else {
			Log.i(LOG_TAG, "session is closed");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	public void getUser() {
		Request request = Request.newMeRequest(Session.getActiveSession(),
				new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser currentUser,
							Response response) {
						if (currentUser != null) {
							user = currentUser;
							long userId = Long.valueOf(user.getId()).longValue();
							String birthday = changeDataFormat(user.getBirthday());
							dbHelper.saveUserData(new User(userId, user.getFirstName(), user.getLastName(), birthday, (String) user.asMap().get("email"))); 
							dialog.dismiss();
							startMainActivity();
						} else {
							Log.e(LOG_TAG, "GraphUser is null");
						}
					}

				});
		Request.executeBatchAsync(request);
	}
	
	private String changeDataFormat(String birthday)
	{
		SimpleDateFormat USFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		SimpleDateFormat UKFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
	    Date date = null;
		try {
			date = USFormat.parse(user.getBirthday());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return UKFormat.format(date);		
	}
}
