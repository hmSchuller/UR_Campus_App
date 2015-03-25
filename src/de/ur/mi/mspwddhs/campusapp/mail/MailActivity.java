package de.ur.mi.mspwddhs.campusapp.mail;

import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Toast;
import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.GripsActivity;
import de.ur.mi.mspwddhs.campusapp.mail.GetMailController.emailListener;
import de.ur.mi.mspwddhs.campusapp.mensa.MensaActivity;
import de.ur.mi.mspwddhs.campusapp.secruity.InternetCheck;
import de.ur.mi.mspwddhs.campusapp.secruity.SecurityMain;

public class MailActivity extends OptionsActivity implements emailListener {
	public static final String SUBJECT_KEY = "SUBJECT_KEY";
	public static final String RECIPIENTS_KEY = "RECIPIENTS_KEY";
	private static final int RELOAD_INTERVALL = 15;

	Button checkForMails;
	ExpandableListView listView;
	ExpandableListAdapter listAdapter;

	Database db;

	Button grips;
	Button mensa;
	Button mail;

	FrameLayout footerLayout;
	Button loadMore;

	ProgressDialog dialog;
	GetMailController controller;
	SendMailController controller2;
	AnswerActivity answer;

	private int mailCounter = 0;
	private String user;
	private String password;
	private String userAdress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mail);
		setupNavigationButtons();
		db = new Database(this);
		db.open();
		user = db.getLoginData().get(0);
		password = db.getLoginData().get(1);
		try {
			password = SecurityMain.decrypt(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		userAdress = db.getLoginData().get(2);
		controller = new GetMailController(this, db);
		listView = (ExpandableListView) findViewById(R.id.expand);
		footerLayout = (FrameLayout) getLayoutInflater().inflate(
				R.layout.button_footer_view, null);
		loadMore = (Button) footerLayout.findViewById(R.id.button_moreMails);
		listView.addFooterView(footerLayout);
		loadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				increaseMailCount();
				setupList();
			}
		});
		setupDialog();
		increaseMailCount();

		if (db.isMailTableEmpty()) {
			dialog.show();
			initialize();

		} else {
			setupList();
		}

	}

	private void initialize() {
		new GetMailController(this, db).execute(user, password, userAdress);
	}

	private void setupNavigationButtons() {
		mail = (Button) findViewById(R.id.mailButton_mail);
		mail.setClickable(false);
		mail.setBackgroundColor(getResources().getColor(R.color.heidenelke));

		mensa = (Button) findViewById(R.id.mensaButton_mail);
		mensa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MailActivity.this,
						MensaActivity.class);
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);

			}
		});

		grips = (Button) findViewById(R.id.gripsButton_mail);
		grips.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MailActivity.this,
						GripsActivity.class);
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);

			}
		});
	}

	private void setupDialog() {
		dialog = new ProgressDialog(this);
		dialog.setTitle(R.string.download_dialog_title);
		dialog.setMessage(getResources().getString(R.string.mail_download_text));
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.removeItem(R.id.refresh_mensa);
		menu.removeItem(R.id.refresh_grips);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.refresh_mail) {
			if (InternetCheck.isInternetAvailiable(this, this)) {
				dialog.show();
				initialize();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	public static class PlaceholderFragment extends Fragment {
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.mail_fragment_main,
					container, false);
			return rootView;
		}
	}

	@Override
	public void onUpdateCompleted() {
		loadMore.setVisibility(View.VISIBLE);
		setupList();
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	private void increaseMailCount() {
		mailCounter += RELOAD_INTERVALL;
	}

	private void setupList() {
		ArrayList<Email> mails = db.getContentMail();
		ArrayList<Email> show = new ArrayList<Email>();
		Collections.reverse(mails);
		if (mails.size() > mailCounter) {
			for (int i = 0; i < mailCounter; i++) {
				show.add(mails.get(i));
			}
		} else {
			show = mails;
			loadMore.setVisibility(View.GONE);
		}
		listAdapter = new ExpandableListAdapter(show, this, this);
		listView.setAdapter(listAdapter);
		if (mailCounter > RELOAD_INTERVALL) {
			listView.setSelectionFromTop(mailCounter - RELOAD_INTERVALL + 1, 0);
		}

	}

	protected void mailAnswer(String subject, String to) {
		Intent intent = new Intent(MailActivity.this, AnswerActivity.class);
		intent.putExtra(RECIPIENTS_KEY, to);
		intent.putExtra(SUBJECT_KEY, subject);
		startActivity(intent);
	}

	@Override
	public void wrongPass() {
		passwordChangeDialog();
	}

}