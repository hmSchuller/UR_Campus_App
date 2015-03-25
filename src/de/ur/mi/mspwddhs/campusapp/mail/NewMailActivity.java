package de.ur.mi.mspwddhs.campusapp.mail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.secruity.InternetCheck;
import de.ur.mi.mspwddhs.campusapp.secruity.SecurityMain;

public class NewMailActivity extends OptionsActivity{
	private static final int DIALOG_ALERT = 2;

	EditText mailEditTo;
	EditText mailEditText;
	EditText mailEditSub;
	Button sendButton;
	SendMailController controller;
	ExpandableListAdapter adapter;
	
	Database db;

	Bundle bundle;
	Intent intent;

	String mailText;
	String sender;
	String user;
	String password;
	String reciever;
	String text;
	String mailSubject;
	String subject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mail_new);
		controller = new SendMailController();
		db = new Database(this);
		db.open();
		intent = getIntent();
		
		mailEditTo = (EditText) findViewById(R.id.mailEditTo);
		mailEditText = (EditText) findViewById(R.id.mailEditText);
		mailEditSub = (EditText) findViewById(R.id.mailEditSub); 
		
		sender = db.getLoginData().get(2);
		user = db.getLoginData().get(0);
		password = db.getLoginData().get(1);
		try {
			password = SecurityMain.decrypt(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sendButton = (Button) findViewById(R.id.sendenButton);

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(internetAvailable()){
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(mailEditText.getWindowToken(),
						0);
				mailSubject = mailEditSub.getText().toString();
				reciever = mailEditTo.getText().toString();
				text = getMailText();
				
				
				controller.execute(sender, user, password, text, mailSubject, reciever);

				showDialog(DIALOG_ALERT);
				}
			}
		});

	}

	private boolean internetAvailable() {
		return InternetCheck.isInternetAvailiable(this, this);
	}

	private String getMailText() {
		mailText = mailEditText.getText().toString();
		return mailText;
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ALERT:
			Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(R.string.mail_gesendet));
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.ok_button, new OkOnClickListener());
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		return super.onCreateDialog(id);
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {

			Intent intent = new Intent(NewMailActivity.this, MailActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);

		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.removeItem(R.id.refresh_mensa);
		menu.removeItem(R.id.refresh_grips);
		menu.removeItem(R.id.refresh_mail);
		menu.removeItem(R.id.newMailOption);
		return true;
	}

}
