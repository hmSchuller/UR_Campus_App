package de.ur.mi.mspwddhs.campusapp.grips;

import java.util.ArrayList;

import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.ThreadParseController.ThreadParseListener;
import de.ur.mi.mspwddhs.campusapp.secruity.SecurityMain;
import android.app.ProgressDialog;
import android.content.Intent;
import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class ThreadActivity extends OptionsActivity implements
		ThreadParseListener {

	private TextView title;
	private ThreadParseController parse;
	private ThreadListAdapter listAdapt;
	private ListView list;
	private Database db;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grips_activity_thread);
		dialog = new ProgressDialog(this);
		Intent intent = getIntent();
		db = new Database(this);
		db.open();
		parse = new ThreadParseController(this, this);
		title = (TextView) findViewById(R.id.thread_title);
		title.setText(intent.getStringExtra(CourseActivity.NAME_KEY));
		setupLoadingScreen();
		parse.initialize(intent.getStringExtra(CourseActivity.URL_KEY));
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

	private void setupLoadingScreen() {
		dialog.setTitle(getResources()
				.getString(R.string.download_dialog_title));
		dialog.setMessage(getString(R.string.thread_dialog_message));
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

	}

	public void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
	}

	@Override
	public void onThreadDownloadCompleted(ArrayList<MyThread> data) {
		dialog.dismiss();
		list = (ListView) findViewById(R.id.thread_List);
		listAdapt = new ThreadListAdapter(data, this);
		list.setAdapter(listAdapt);

	}

	@Override
	public String getUser() {
		return db.getLoginData().get(0);
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
}