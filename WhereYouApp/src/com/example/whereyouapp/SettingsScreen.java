package com.example.whereyouapp;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.widget.Spinner;
import java.util.List;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.TextView;
import android.widget.Toast;
public class SettingsScreen extends Activity {
	private Spinner spinner1;
	//changed batteryLevel to an int value
	private int batteryLevel;
	public int timesClicked;
	public static SettingsDataSource setdbHandle;
	private static final String TAG = "WhereYouApp";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_screen);
		
		setdbHandle = MainScreen.setdbHandle;
		
		spinner1 = (Spinner) findViewById(R.id.battery_level);
		List<String> list = new ArrayList<String>();
		list.add("Select a threshold battery level percentage for notification purposes (in %)");
		list.add("5");
		list.add("10");
		list.add("15");
		list.add("20");
		list.add("25");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
		addListenerOnSpinnerItemSelection();
		//if the settings database holds a battery threshold level set
		//the batter lvl spinner... "spinner1" to that level
		//values are hard coded...
		if(setdbHandle.containsValue()) {
			int savedBatteryLevel = setdbHandle.getSavedBatteryLevel();
			if(savedBatteryLevel > -1)
				if(setdbHandle.getSavedBatteryLevel()==5)
					spinner1.setSelection(1);
				if(setdbHandle.getSavedBatteryLevel()==10)
					spinner1.setSelection(2);
				if(setdbHandle.getSavedBatteryLevel()==15)
					spinner1.setSelection(3);
				if(setdbHandle.getSavedBatteryLevel()==20)
					spinner1.setSelection(4);
				if(setdbHandle.getSavedBatteryLevel()==25)
					spinner1.setSelection(5);
		}
		EditText editText = (EditText) findViewById(R.id.enter_contact1);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		editText = (EditText) findViewById(R.id.enter_contact2);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		editText = (EditText) findViewById(R.id.enter_contact3);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		editText = (EditText) findViewById(R.id.enter_contact4);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		editText = (EditText) findViewById(R.id.enter_contact5);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		timesClicked = 0;
		((Button)findViewById(R.id.contact_list_button)).setOnClickListener( new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	timesClicked++;
	        	Toast.makeText(v.getContext(), "Times clicked: " + timesClicked, Toast.LENGTH_LONG).show();
	    	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
	            startActivityForResult(intent, 1); 	            
	        } 
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_screen, menu);
		return true;
	}
	public void addListenerOnSpinnerItemSelection()
	{
		spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener ());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (data != null) {
	        Uri uri = data.getData();

	        if (uri != null) {
	            Cursor c = null;
	            try {
	                c = getContentResolver().query(uri, new String[]{ 
	                            ContactsContract.CommonDataKinds.Phone.NUMBER,  
	                            ContactsContract.CommonDataKinds.Phone.TYPE },
	                        null, null, null);

	                if (c != null && c.moveToFirst()) {
	                    String number = c.getString(0);
	                    int type = c.getInt(1);
	                    showSelectedNumber(type, number);
	                }
	            } finally {
	                if (c != null) {
	                    c.close();
	                }
	            }
	        }
	    }
	}
	public void showSelectedNumber(int type, String number) {
	    Toast.makeText(this, type + ": " + number, Toast.LENGTH_LONG).show();
	    if (timesClicked % 5 == 1)
	    {
	    	EditText editText = (EditText) findViewById(R.id.enter_contact1);
	    	editText.setText(number, TextView.BufferType.EDITABLE);
	    }
	    else if (timesClicked % 5 == 2)
	    {
	    	EditText editText = (EditText) findViewById(R.id.enter_contact2);
	    	editText.setText(number, TextView.BufferType.EDITABLE);
	    }
	    else if (timesClicked % 5 == 3)
	    {
	    	EditText editText = (EditText) findViewById(R.id.enter_contact3);
	    	editText.setText(number, TextView.BufferType.EDITABLE);
	    }
	    else if (timesClicked % 5 == 4)
	    {
	    	EditText editText = (EditText) findViewById(R.id.enter_contact4);
	    	editText.setText(number, TextView.BufferType.EDITABLE);
	    }
	    else
	    {
	    	EditText editText = (EditText) findViewById(R.id.enter_contact5);
	    	editText.setText(number, TextView.BufferType.EDITABLE);
	    }
	}
	public void saveMySettings (View view)
	{
		Intent intent = new Intent(this, MainScreen.class);
		EditText editText = (EditText) findViewById(R.id.enter_contact1);
		timesClicked = 0;
		String [] phoneNumbers = new String [5];
		String phoneNum = editText.getText().toString();
		String delims = "()-+";
		String [] tokens = phoneNum.split(delims);
		for (int i = 0; i < tokens.length; i ++)
		{
			phoneNumbers [0] += tokens [i];
		}
		boolean error = false;
		editText = (EditText) findViewById(R.id.enter_contact2);
		String phoneNum1 = editText.getText().toString();
		tokens = phoneNum1.split(delims);
		for (int i = 0; i < tokens.length; i ++)
		{
			phoneNumbers [1] += tokens [i];
		}
		editText = (EditText) findViewById(R.id.enter_contact3);
		String phoneNum2 = editText.getText().toString();
		tokens = phoneNum2.split(delims);
		for (int i = 0; i < tokens.length; i ++)
		{
			phoneNumbers [2] += tokens [i];
		}
		editText = (EditText) findViewById(R.id.enter_contact4);
		String phoneNum3 = editText.getText().toString();
		tokens = phoneNum3.split(delims);
		for (int i = 0; i < tokens.length; i ++)
		{
			phoneNumbers [3] += tokens [i];
		}
		editText = (EditText) findViewById(R.id.enter_contact5);
		String phoneNum4 = editText.getText().toString();
		tokens = phoneNum4.split(delims);
		for (int i = 0; i < tokens.length; i ++)
		{
			phoneNumbers [4] += tokens [i];
		}
//		try
//		{
//			int part1 = Integer.parseInt(phoneNumbers [0].substring(4));
//			Log.d(TAG, "" + part1);
//		}catch(NumberFormatException e)
//		{
//			error = true;
//		}
//		try
//		{
//			int part2 = Integer.parseInt(phoneNumbers [1].substring(4));
//			Log.d(TAG, "" + part2);
//		}catch(NumberFormatException e)
//		{
//			error = true;
//		}
//		try
//		{
//			int part3 = Integer.parseInt(phoneNumbers [2].substring(4));
//			Log.d(TAG, "" + part3);
//		}catch(NumberFormatException e)
//		{
//			error = true;
//		}
//		try
//		{
//			int part4 = Integer.parseInt(phoneNumbers [3].substring(4));
//			Log.d(TAG, "" + part4);
//		}catch(NumberFormatException e)
//		{
//			error = true;
//		}
//		try
//		{
//			int part5 = Integer.parseInt(phoneNumbers [4].substring(4));
//			Log.d(TAG, "" + part5);
//		}catch(NumberFormatException e)
//		{
//			error = true;
//		}
		try
		{
			batteryLevel = Integer.parseInt(String.valueOf(spinner1.getSelectedItem()));
		}
		catch (NumberFormatException e)
		{
			Toast.makeText(this, "Error with battery level", Toast.LENGTH_LONG).show();
		}
		if (error)
		{
			Toast.makeText(this, "Error with at least phone number", Toast.LENGTH_LONG).show();
			intent = new Intent (this, SettingsScreen.class);
		}
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setAutoCancel(true);
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setContentTitle("Your battery level is below " + batteryLevel + "%.");
		
		setdbHandle.deleteBatterySetting();
		setdbHandle.insertBatteryLevel(batteryLevel);
		
		if (phoneNumbers [0].equals("null"))
		{
			mBuilder.setContentText("Your phone is about to die. Are you sure you don't want to let someone know your location?");
		}
		else if (phoneNumbers [1].equals("null"))
		{
			mBuilder.setContentText("Your location has been sent to " + phoneNumbers [0].substring(4) + " before your phone dies.");
		}
		else if (phoneNumbers [2].equals ("null"))
		{
			mBuilder.setContentText("Your location has been sent to " + phoneNumbers [0].substring(4) + " and " + phoneNumbers [1].substring(4) + " before your phone dies.");
		}
		else if (phoneNumbers [3].equals ("null"))
		{
			mBuilder.setContentText("Your location has been sent to " + phoneNumbers [0].substring(4) + " , " + phoneNumbers [1].substring(4) + " , " + " and " + phoneNumbers [2].substring(4) +  " before your phone dies.");
		}
		else if (phoneNumbers [4].equals("null"))
		{
			mBuilder.setContentText("Your location has been sent to " + phoneNumbers [0].substring(4) + " , " + phoneNumbers [1].substring(4) + " , " + phoneNumbers [2].substring(4) + " and " + phoneNumbers [3].substring(4) +  " before your phone dies.");
		}
		else
		{
			mBuilder.setContentText("Your location has been sent to " + phoneNumbers [0].substring(4) + " , " + phoneNumbers [1].substring(4) + " , " + phoneNumbers [2].substring(4) + " , " + phoneNumbers [3].substring(4) + " and " + phoneNumbers[4].substring(4) + " before your phone dies.");
		}
		Intent resultIntent = new Intent (this, MainScreen.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainScreen.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder.setDefaults(Notification.DEFAULT_ALL);
		mNotificationManager.notify(1, mBuilder.build());
		startActivity(intent);
	}
	public void cancelSettings (View view)
	{
		spinner1.setSelection(0);
		timesClicked = 0;
		EditText editText = (EditText) findViewById(R.id.enter_contact1);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact2);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact3);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact4);
		editText.setText("", TextView.BufferType.EDITABLE);
		editText = (EditText) findViewById(R.id.enter_contact5);
		editText.setText("", TextView.BufferType.EDITABLE);
		Intent intent = new Intent (this, MainScreen.class);
		startActivity(intent);
	}
}


