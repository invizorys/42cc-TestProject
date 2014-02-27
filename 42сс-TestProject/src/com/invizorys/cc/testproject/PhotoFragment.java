package com.invizorys.cc.testproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.invizorys.cc.testproject.util.DBHelper;


public class PhotoFragment extends SherlockFragment{
	private ImageView photo;
	private DBHelper dbHelper;
	final String LOG_TAG = "myLogs";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.photo_fragment, container, false);
		
		photo = (ImageView) rootView.findViewById(R.id.imageView_photo);
		
		dbHelper = new DBHelper(getActivity());
        readPhotoFromBd();
        
        return rootView;
	}
	
	private void readPhotoFromBd() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Log.d(LOG_TAG, "--- Read photo from DB: ---");

		Cursor c = db.query("dataTable", null, null, null, null, null, null);

		if (c.moveToFirst()) {

			int imageColIndex = c.getColumnIndex("image");

			byte[] imgByte = c.getBlob(imageColIndex);
			Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);

			photo.setImageBitmap(bitmap);

		} else
			Log.d(LOG_TAG, "0 rows");
		c.close();
	}

}
