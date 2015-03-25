package de.ur.mi.mspwddhs.campusapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import de.ur.mi.mspwddhs.campusapp.LoginValidate.LoginListener;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.mensa.MensaActivity;
import de.ur.mi.mspwddhs.campusapp.secruity.InternetCheck;

public class MainActivity extends OptionsActivity implements LoginListener {
	EditText user;
	EditText pass;
	ImageView logo;
	Button button;
	Database db;
	LoginValidate login;
	ProgressDialog updateDialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		db = new Database(this);
		db.open();
		login = new LoginValidate(this, this);
		if (db.isLoginTableEmpty()) {
			logo = (ImageView) findViewById(R.id.logo_login);
			logo.setBackgroundResource(R.drawable.logo_ur);
			pass = (EditText) findViewById(R.id.editTextPass);
			user = (EditText) findViewById(R.id.editTextUser);
			button = (Button) findViewById(R.id.button1);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(internetAvailable()){
					onDownloadInitiation();
					login.initiate(user.getText().toString(), pass.getText()
							.toString());
					}
				}
			});

		} else {
			changeToMensa();
		}

	}
	

	private boolean internetAvailable() {
		return InternetCheck.isInternetAvailiable(this, this);
	}


	public void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
	}

	private void changeToMensa() {
		Intent intent = new Intent(MainActivity.this, MensaActivity.class);
//		//NÄCHSTE ZEILE?
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
		overridePendingTransition(0, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.removeItem(R.id.refresh_mail);
		menu.removeItem(R.id.refresh_grips);
		menu.removeItem(R.id.refresh_mensa);
		menu.removeItem(R.id.logout);
		menu.removeItem(R.id.newMailOption);
		return true;
	}

	public void onDownloadInitiation() {
		updateDialog = new ProgressDialog(this);
		updateDialog.setTitle(R.string.login_titel);
		updateDialog.setMessage(getResources().getString(R.string.login_message));
		updateDialog.show();

	}

	@Override
	public void onFinish(boolean response) {
		updateDialog.dismiss();
		if (response) {
			Toast toast = Toast.makeText(this, getResources().getString(R.string.login_true),
					Toast.LENGTH_SHORT);
			toast.show();
			changeToMensa();
		} else {
			Toast toast = Toast.makeText(this, getResources().getString(R.string.login_false),
					Toast.LENGTH_SHORT);
			db.clearDatabaseLogin();
			pass.setText("");
			user.setText("");
			toast.show();
		}
	}
}
