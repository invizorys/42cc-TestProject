package com.invizorys.cc.testproject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.invizorys.cc.testproject.db.DBHelper;
import com.invizorys.cc.testproject.db.User;
import com.invizorys.cc.testproject.R;

public class DataFragment extends SherlockFragment{
	private TextView name, surname, birthday;
	private Button buttonEditBirthday;
	private ImageView userPhoto;
	private EditText editName, editSurname;
	private DBHelper dbHelper;
	private String photoPath;
	private File photoFile;
	private User user;
	
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
		
		rootView.findViewById(R.id.button_edit_data).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showEditDataDialog();
			}
		});
		
		dbHelper = new DBHelper(getActivity());
		user = dbHelper.readUserData();
		fillTextViews();
		
		photoPath = Environment.getExternalStorageDirectory() + "/Android/data/" + getActivity().getPackageName();
		photoFile = new File(photoPath + "/photo.jpg");
		if (!photoFile.exists()) {
			new DownloadPhoto().execute(user.getId());
		} else {
			userPhoto.setImageBitmap(BitmapFactory.decodeFile(photoPath + "/photo.jpg"));
		}
		
		return rootView;		
	}
	
	private void showEditDataDialog() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.edit_data_dialog);
		dialog.setTitle("Edit data");
		
		editName = (EditText) dialog.findViewById(R.id.editText_name);
		editSurname = (EditText) dialog.findViewById(R.id.editText_surname);
		editName.setText(user.getName());
		editSurname.setText(user.getSurname());

		dialog.findViewById(R.id.button_ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isDataCorrect())
				{
					saveUserData();
					fillTextViews();
					dialog.dismiss();
				}
			}
		});
		
		dialog.findViewById(R.id.button_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		buttonEditBirthday = (Button) dialog.findViewById(R.id.button_edit_birthday);
		buttonEditBirthday.setText(user.getBirthday());
		buttonEditBirthday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePicker();
			}
		});
		dialog.show();
	}
	
	public void showDatePicker() {
		LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		View customView = inflater.inflate(R.layout.datepicker_layout, null);
		dialogBuilder.setView(customView);
		final Calendar now = Calendar.getInstance();
		final DatePicker datePicker = (DatePicker) customView.findViewById(R.id.dialog_datepicker);
		final TextView dateTextView = (TextView) customView.findViewById(R.id.dialog_dateview);
		final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);

		dialogBuilder.setTitle("Choose a date");
		Calendar choosenDate = Calendar.getInstance();
		int year = choosenDate.get(Calendar.YEAR);
		int month = choosenDate.get(Calendar.MONTH);
		int day = choosenDate.get(Calendar.DAY_OF_MONTH);
		try {
			Date choosenDateFromUI = formatter.parse(buttonEditBirthday.getText().toString());
			choosenDate.setTime(choosenDateFromUI);
			year = choosenDate.get(Calendar.YEAR);
			month = choosenDate.get(Calendar.MONTH);
			day = choosenDate.get(Calendar.DAY_OF_MONTH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar dateToDisplay = Calendar.getInstance();
		dateToDisplay.set(year, month, day);
		dateTextView.setText(formatter.format(dateToDisplay.getTime()));

		dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Calendar choosen = Calendar.getInstance();
						choosen.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
						buttonEditBirthday.setText(formatter.format(choosen.getTime()));
						dialog.dismiss();
					}
				});
		final AlertDialog dialog = dialogBuilder.create();
		datePicker.init(year, month, day,
				new DatePicker.OnDateChangedListener() {
					public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						Calendar choosenDate = Calendar.getInstance();
						choosenDate.set(year, monthOfYear, dayOfMonth);
						dateTextView.setText(formatter.format(choosenDate.getTime()));
						if (now.compareTo(choosenDate) < 0) {
							dateTextView.setTextColor(Color.parseColor("#ff0000"));
							((Button) dialog.getButton(AlertDialog.BUTTON_POSITIVE)).setEnabled(false);
						} else {
							dateTextView.setTextColor(Color.parseColor("#000000"));
							((Button) dialog.getButton(AlertDialog.BUTTON_POSITIVE)).setEnabled(true);
						}
					}
				});
		dialog.show();
	}
	
	private boolean isDataCorrect()
	{
		if (editName.getText().toString().equals("")) {
			Toast.makeText(getActivity(), "name is not valid", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (editSurname.getText().toString().equals("")) {
			Toast.makeText(getActivity(), "surname is not valid", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private void saveUserData() {
		user.setName(editName.getText().toString().trim());
		user.setSurname(editSurname.getText().toString().trim());
		user.setBirthday(buttonEditBirthday.getText().toString());
		
		dbHelper.updateData(user);
	}
	
	private void fillTextViews()
	{
		name.setText(user.getName());
		surname.setText(user.getSurname());
		birthday.setText(user.getBirthday());
	}
	
	private void savePhotoOnSdcard(Bitmap bitmap) throws IOException
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

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
