package com.invizorys.cc.testproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.invizorys.cc.testproject.util.DBHelper;
import com.invizorys.cc.testproject.R;

public class DataFragment extends SherlockFragment{
	private TextView name, surname, birthday, bio, skype;
	private DBHelper dbHelper;
	final String LOG_TAG = "myLogs";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.data_fragment, container, false);
		
		name = (TextView) rootView.findViewById(R.id.textView_name);
		surname = (TextView) rootView.findViewById(R.id.textView_surname);
		birthday = (TextView) rootView.findViewById(R.id.textView_birthday);
		bio = (TextView) rootView.findViewById(R.id.textView_bio);
		skype = (TextView) rootView.findViewById(R.id.textView_skype);
		
		dbHelper = new DBHelper(getActivity());
		readDataFromBd();
		
		return rootView;
		
	}
	
	private void readDataFromBd() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Log.d(LOG_TAG, "--- Read data from DB: ---");

		Cursor c = db.query("dataTable", null, null, null, null, null, null);

		if (c.moveToFirst()) {

			int nameColIndex = c.getColumnIndex("name");
			int surnameColIndex = c.getColumnIndex("surname");
			int birthdayColIndex = c.getColumnIndex("birthday");
			int bioColIndex = c.getColumnIndex("bio");
			int skypeColIndex = c.getColumnIndex("skype");

			name.setText("Name: " + c.getString(nameColIndex));
			surname.setText("Surname: " + c.getString(surnameColIndex));
			birthday.setText("date of birth: " + c.getString(birthdayColIndex));
			bio.setText("bio: " + c.getString(bioColIndex));
			skype.setText("skype: " + c.getString(skypeColIndex));

		} else
			Log.d(LOG_TAG, "0 rows");
		c.close();
	}

}
