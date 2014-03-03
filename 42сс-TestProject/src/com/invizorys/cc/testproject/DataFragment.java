package com.invizorys.cc.testproject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.invizorys.cc.testproject.util.DBHelper;
import com.invizorys.cc.testproject.R;

public class DataFragment extends SherlockFragment{
	private TextView name, surname, birthday;
	private ImageView userPhoto;
	private DBHelper dbHelper;
	private long userId;
	private String photoPath;
	private File photoFile;
	
	final String LOG_TAG = "myLogs";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View rootView = inflater.inflate(R.layout.data_fragment, container, false);
		
		name = (TextView) rootView.findViewById(R.id.textView_name);
		surname = (TextView) rootView.findViewById(R.id.textView_surname);
		birthday = (TextView) rootView.findViewById(R.id.textView_birthday);
		userPhoto = (ImageView) rootView.findViewById(R.id.imageView_user_photo);
		
		dbHelper = new DBHelper(getActivity());
		readDataFromBd();
		
		photoPath = Environment.getExternalStorageDirectory() + "/Android/data/" + getActivity().getPackageName();
		photoFile = new File(photoPath + "/photo.jpg");
		if (!photoFile.exists()) {
			new DownloadPhoto().execute(userId);
		} else {
			userPhoto.setImageBitmap(BitmapFactory.decodeFile(photoPath + "/photo.jpg"));
		}
		
		return rootView;		
	}

	private void readDataFromBd() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Log.d(LOG_TAG, "--- Read data from DB: ---");

		Cursor c = db.query("dataTable", null, null, null, null, null, null);

		if (c.moveToFirst()) {

			int userIdColIndex = c.getColumnIndex("id");
			int nameColIndex = c.getColumnIndex("name");
			int surnameColIndex = c.getColumnIndex("surname");
			int birthdayColIndex = c.getColumnIndex("birthday");

			userId = Long.valueOf(c.getString(userIdColIndex)).longValue();
			name.setText("Name: " + c.getString(nameColIndex));
			surname.setText("Surname: " + c.getString(surnameColIndex));
			birthday.setText("date of birth: " + c.getString(birthdayColIndex));

		} else
			Log.d(LOG_TAG, "0 rows");
		c.close();
	}
	
	private void savePhotoOnSdcard(Bitmap bitmap) throws IOException
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

		new File(photoPath).mkdirs();
		photoFile.createNewFile();
		FileOutputStream fo = new FileOutputStream(photoFile);
		fo.write(bytes.toByteArray());
		Log.d(LOG_TAG, "photo saved on sdcard");

		fo.close();
	}
	
	private class DownloadPhoto extends AsyncTask<Long, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Long... params) {
				String url = String.format(
						"https://graph.facebook.com/%s/picture", params[0]);
				
				InputStream inputStream;
				try {
					inputStream = new URL(url).openStream();
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					savePhotoOnSdcard(bitmap);
					return bitmap;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
		}

		@Override
		protected void onPostExecute(Bitmap photo) {
			super.onPostExecute(photo);
			if(photo != null)
				userPhoto.setImageBitmap(photo);
		}

	}

}
