package com.invizorys.cc.testproject;

import java.util.ArrayList;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.invizorys.cc.testproject.R;

public class LoginActivity extends Activity {
	public static final String TAG = "LoginActivity";
	private UiLifecycleHelper uiHelper;
	private GraphUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			startMainActivity();
		}

		findViewById(R.id.button_login).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						login();
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
			Log.i(TAG, "session is open");
			getUser();
		} else {
			Log.i(TAG, "session is closed");
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
							startMainActivity();
						} else {
							Log.e(TAG, "GraphUser is null");
						}
					}

				});
		Request.executeBatchAsync(request);
	}
}
