package de.ur.mi.mspwddhs.campusapp.grips;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.GripsParseController.OnParseListener;
import de.ur.mi.mspwddhs.campusapp.mail.MailActivity;
import de.ur.mi.mspwddhs.campusapp.mensa.MensaActivity;
import de.ur.mi.mspwddhs.campusapp.secruity.InternetCheck;
import de.ur.mi.mspwddhs.campusapp.secruity.SecurityMain;

public class GripsActivity extends OptionsActivity implements OnParseListener {

	public static final String NAME_KEY = "NAME_KEY";
	public static final String URL_KEY = "URL_KEY";
	public static final String USER_KEY = "USER_KEY";
	public static final String PW_KEY = "PW_KEY";

	CourseListAdapter courseAdapt;
	GripsParseController parse;
	ExpandableListView list;
	ProgressDialog updateDialog;
	Button grips;
	Button mensa;
	Button mail;
	Database db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grips);
		setupNavigationButtons();
		db = new Database(this);
		db.open();
		if (db.isGripsTableEmpty()) {
			if (InternetCheck.isInternetAvailiable(this, this)) {
				parse = new GripsParseController(this, this);
				parse.login();
			}
		} else {
			setupList();
		}
	}

	public void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
	}

	private void setupNavigationButtons() {
		grips = (Button) findViewById(R.id.gripsButton_grips);
		grips.setClickable(false);

		mensa = (Button) findViewById(R.id.mensaButton_grips);
		mensa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GripsActivity.this,
						MensaActivity.class);
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});

		mail = (Button) findViewById(R.id.mailButton_grips);
		mail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GripsActivity.this,
						MailActivity.class);
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.removeItem(R.id.refresh_mail);
		menu.removeItem(R.id.refresh_mensa);
		menu.removeItem(R.id.newMailOption);
		return true;
	}

	@Override
	public String getPass() {
		String pass = db.getLoginData().get(1);
		try {
			pass = SecurityMain.encrypt(pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pass;
	}

	@Override
	public String getUser() {
		return db.getLoginData().get(0);
	}

	@Override
	public void onDownloadInitiated() {
		updateDialog = new ProgressDialog(this);
		updateDialog.setTitle(R.string.download_dialog_title);
		updateDialog.setMessage(getResources().getString(R.string.grips_download_text));
		updateDialog.setCanceledOnTouchOutside(false);
		updateDialog.setCancelable(false);
		updateDialog.show();
	}

	@Override
	public void onDownloadFinished(ArrayList<Course> courses) {
		updateDialog.dismiss();
		setupList();

	}

	private void setupList() {
		list = (ExpandableListView) findViewById(R.id.list);
		list.setVisibility(View.VISIBLE);
		courseAdapt = new CourseListAdapter(db.getContentGrips(), this, this);
		list.setAdapter(courseAdapt);
		list.setGroupIndicator(null);
	}

	@Override
	public void onDownloadUpdated(int current, int max) {
		updateDialog.setMessage(getString(R.string.grips_download_text_progress) + " " + current + "/" + max);

	}

	protected void parseSingleItem(Link link) {
		if (InternetCheck.isInternetAvailiable(this, this)) {
			Intent intent = new Intent(GripsActivity.this, CourseActivity.class);
			intent.putExtra(NAME_KEY, link.getName());
			intent.putExtra(URL_KEY, link.getUrl());
			startActivity(intent);
		}
	}

	@Override
	public void passwordChange() {
		passwordChangeDialog();
	}
}
