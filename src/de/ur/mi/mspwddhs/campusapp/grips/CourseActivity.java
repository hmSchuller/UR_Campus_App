package de.ur.mi.mspwddhs.campusapp.grips;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.ForumParseController.ForumListener;
import de.ur.mi.mspwddhs.campusapp.grips.GradesParseController.GradesParseListener;
import de.ur.mi.mspwddhs.campusapp.secruity.InternetCheck;
import de.ur.mi.mspwddhs.campusapp.secruity.SecurityMain;

public class CourseActivity extends OptionsActivity implements ForumListener,
		GradesParseListener {

	public static final String URL_KEY = "URL_KEY";
	public static final String NAME_KEY = "NAME_KEY";
	TextView title;
	String pass;
	String user;
	ForumParseController forumsParse;
	GradesParseController gradesParse;
	ListView list;
	ForumListAdapter listAdaptForum;
	GradesListAdapter listAdaptGrades;
	Database db;
	ProgressDialog dialog;
	private boolean change;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grips_acivity_course);
		Intent intent = getIntent();
		db = new Database(this);
		db.open();
		title = (TextView) findViewById(R.id.forum_title);
		title.setText(intent.getStringExtra(GripsActivity.NAME_KEY));
		user = intent.getStringExtra(GripsActivity.USER_KEY);
		pass = intent.getStringExtra(GripsActivity.PW_KEY);

		dialog = new ProgressDialog(this);
		if (intent.getStringExtra(GripsActivity.NAME_KEY).contains("Bewertung")) {
			gradesParse = new GradesParseController(this);
			dialog.setMessage(getResources().getString(
					R.string.course_bewertung));
			gradesParse
					.initialize(intent.getStringExtra(GripsActivity.URL_KEY));
		} else {
			forumsParse = new ForumParseController(this);
			dialog.setMessage(getResources().getString(R.string.course_forum));
			forumsParse
					.initialize(intent.getStringExtra(GripsActivity.URL_KEY));
		}

		dialog.setTitle(R.string.download_dialog_title);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
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

	public void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
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
	public void onForumDownloadCompleted(ArrayList<Forum> result) {
		list = (ListView) findViewById(R.id.forum_list);
		listAdaptForum = new ForumListAdapter(result, this, this);
		list.setAdapter(listAdaptForum);
		dialog.dismiss();
	}

	public void switchToThreadActivity(String url, String name) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		if (InternetCheck.isInternetAvailiable(this, this)) {
			Intent intent = new Intent(CourseActivity.this,
					ThreadActivity.class);
			intent.putExtra(URL_KEY, url);
			intent.putExtra(NAME_KEY, name);
			startActivity(intent);
		}
	}

	public void showEmptyDataToast() {
		if (change) {
			if (dialog.isShowing())
				dialog.dismiss();
			Toast toast = Toast.makeText(this, getString(R.string.emptyData),
					Toast.LENGTH_LONG);
			toast.show();
		}
	}

	@Override
	public void onGradesDownloadCompleted(ArrayList<Grades> data) {
		dialog.dismiss();
		list = (ListView) findViewById(R.id.forum_list);
		listAdaptGrades = new GradesListAdapter(data, this, this);
		list.setAdapter(listAdaptGrades);

	}

	@Override
	public void cancel() {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	public void passwordChange() {
		change = true;
		passwordChangeDialog();
	}

}
