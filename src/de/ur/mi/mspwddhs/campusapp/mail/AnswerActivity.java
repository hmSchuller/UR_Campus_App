package de.ur.mi.mspwddhs.campusapp.mail;

import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.secruity.SecurityMain;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
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
import android.widget.TextView;

public class AnswerActivity extends OptionsActivity {

	private static final int DIALOG_ALERT = 1;

	TextView toView;
	TextView sendView;
	EditText editAnswer;
	Button sendButton;
	SendMailController controller;
	ExpandableListAdapter adapter;

	Bundle bundle;
	Intent intent;
	
	Database db;

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
		setContentView(R.layout.mail_answer_layout);

		db = new Database(this);
		db.open();
		
		controller = new SendMailController();
		intent = getIntent();
		bundle = intent.getExtras();

		toView = (TextView) findViewById(R.id.an);
		sendView = (TextView) findViewById(R.id.sendSubject);
		editAnswer = (EditText) findViewById(R.id.answerEdit);
		sendButton = (Button) findViewById(R.id.answerButton);

		sender = db.getLoginData().get(2);
		user = db.getLoginData().get(0);
		password = db.getLoginData().get(1);
		try {
			password = SecurityMain.decrypt(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		reciever = (String) bundle.get(MailActivity.RECIPIENTS_KEY);
		subject = (String) bundle.get(MailActivity.SUBJECT_KEY);

		mailSubject = getString(R.string.answer) + subject;
		toView.setText(getString(R.string.to) + reciever);
		sendView.setText(mailSubject);

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(editAnswer.getWindowToken(),
						0);
				text = getMailText();
				controller.execute(sender, user, password, text, mailSubject,
						reciever);

				showDialog(DIALOG_ALERT);

			}
		});

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

	private String getMailText() {

		mailText = editAnswer.getText().toString();

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
			Intent intent = new Intent(AnswerActivity.this, MailActivity.class);
			
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);

		}
	}
}
