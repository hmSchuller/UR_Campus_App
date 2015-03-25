package de.ur.mi.mspwddhs.campusapp.mensa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.GripsActivity;
import de.ur.mi.mspwddhs.campusapp.mail.MailActivity;
import de.ur.mi.mspwddhs.campusapp.mensa.MensaController.OnResultListener;
import de.ur.mi.mspwddhs.campusapp.secruity.InternetCheck;

public class MensaActivity extends OptionsActivity implements OnResultListener {

	private Database db;
	MensaController controller;
	ProgressDialog progressDialog;
	AlertDialog.Builder alertDialogBuilder;
	ExpandableListView listView;
	Calendar calendar;
	MensaListAdapter mensaListAdap;
	TextView text;
	Button buttonGrips;
	Button buttonMensa;
	Button buttonEmail;
	String monday;
	private static final int DIALOG_ALERT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mensa);
		db = new Database(this);
		db.open();
		makeButtons();
		createDialogs();
		progressDialog.show();
		controller = new MensaController(this, this, db);
		checkDatabase();
	}

	public void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
	}

	private void makeButtons() {

		buttonMensa = (Button) findViewById(R.id.mensaButton_mensa);
		buttonMensa.setClickable(false);

		buttonEmail = (Button) findViewById(R.id.mailButton_mensa);
		buttonEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MensaActivity.this,
						MailActivity.class);
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});

		buttonGrips = (Button) findViewById(R.id.gripsButton_mensa);
		buttonGrips.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MensaActivity.this,
						GripsActivity.class);
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});
	}

	private void createDialogs() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getString(R.string.updating_title));
		progressDialog.setMessage(getString(R.string.updating_text));
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);

		alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(getString(R.string.error_title));
		alertDialogBuilder.setMessage(getString(R.string.error_text));
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton(R.string.ok_button,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.removeItem(R.id.refresh_mail);
		menu.removeItem(R.id.refresh_grips);
		menu.removeItem(R.id.newMailOption);
		return true;
	}

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.mensa, container,
					false);
			return rootView;
		}
	}

	@Override
	public void OnUpdateCompleted() {
		progressDialog.dismiss();
		createLists();
	}

	public void showLegende(View w) {
		showDialog(DIALOG_ALERT);

	}

	protected Dialog onCreateDialog(int id) {
		ImageView image = new ImageView(this);
		image.setImageResource(R.drawable.legende);
		switch (id) {
		case DIALOG_ALERT:
			Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(true).setView(image);

			AlertDialog dialog = builder.create();
			dialog.show();

		}
		return super.onCreateDialog(id);
	}

	private void createLists() {

		TextView mensatitle = (TextView) findViewById(R.id.mensatitle);
		mensatitle.setText(getResources().getString(R.string.mensatitle));

		mensatitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TextView mensaschluss = (TextView) findViewById(R.id.mensaschluss);
				mensaschluss.setText(getResources().getString(R.string.mensaschluss));
			}
		});

		listView = (ExpandableListView) findViewById(R.id.mensa_week);
		mensaListAdap = new MensaListAdapter(datesOfWeek(), db, this);
		listView.setAdapter(mensaListAdap);
		listView.setGroupIndicator(null);

	}

	private ArrayList<String> datesOfWeek() {
		ArrayList<String> datesOfWeek = new ArrayList<String>();
		calendar = Calendar.getInstance();

		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		for (int i = 0; i < 5; i++) {
			Date currentDate = calendar.getTime();
			String currentDateString = df.format(currentDate);
			datesOfWeek.add(currentDateString);
			calendar.add(Calendar.DATE, 1);
		}
		return datesOfWeek;
	}

	private String getDateOfMonday() {
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date currentDate = calendar.getTime();
		String currentDateString = df.format(currentDate);

		return currentDateString;
	}

	private void checkDatabase() {
		if (db.getCount(getDateOfMonday()) != true) {
			if (InternetCheck.isInternetAvailiable(this, this)) {
				db.clearDatabaseMensa();
				controller.execute();   
				createLists();
			}
		} else {
			OnUpdateCompleted();
		}
	}
}