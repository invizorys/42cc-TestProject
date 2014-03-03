package com.invizorys.cc.testproject;

import java.util.ArrayList;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.invizorys.cc.testproject.R;
import com.invizorys.cc.testproject.util.DBHelper;

public class LoginActivity extends Activity {
	private UiLifecycleHelper uiHelper;
	private GraphUser user;
	private ContentValues cv;
	private SQLiteDatabase db;
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
						if(isNetworkConnected())
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
			ArrayList<String> permissions = new ArrayList<String>();
			permissions.add("user_birthday");

			session.openForRead(new Session.OpenRequest(this).setCallback(
					callback).setPermissions(permissions));
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
			if(isNetworkConnected())
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
							saveUserData();
							dialog.dismiss();
							startMainActivity();
						} else {
							Log.e(LOG_TAG, "GraphUser is null");
						}
					}

				});
		Request.executeBatchAsync(request);
	}

	private void saveUserData() {
		db = dbHelper.getWritableDatabase();

		cv = new ContentValues();
		cv.put("id", user.getId());
		cv.put("name", user.getFirstName());
		cv.put("surname", user.getLastName());
		cv.put("birthday", user.getBirthday());
		
		long rowID = db.insert("dataTable", null, cv);
		Log.d(LOG_TAG, "user: id - " + user.getId() + ", name - " + user.getFirstName() 
				+ ", surname - " + user.getLastName() + ", birthday - " + user.getBirthday() + ", - saved on DB");
		Log.d(LOG_TAG, "row inserted, ID = " + rowID);
	}
	
	private boolean isNetworkConnected() {
		  ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo ni = cm.getActiveNetworkInfo();
		  return ni != null && ni.isConnected();
		 }
}
