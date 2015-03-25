package de.ur.mi.mspwddhs.campusapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.GripsActivity;
import de.ur.mi.mspwddhs.campusapp.help.HelpActivity;
import de.ur.mi.mspwddhs.campusapp.mail.NewMailActivity;
import de.ur.mi.mspwddhs.campusapp.mensa.MensaActivity;
import de.ur.mi.mspwddhs.campusapp.plan.PlanActivity;
import de.ur.mi.mspwddhs.campusapp.secruity.InternetCheck;
import de.ur.mi.mspwddhs.campusapp.secruity.SecurityMain;

public class OptionsActivity extends Activity {

	Database db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new Database(this);
		db.open();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.newMailOption) {
			Intent intent = new Intent(OptionsActivity.this,
					NewMailActivity.class);
			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return true;
		}
		if (id == R.id.help) {
			Intent intent = new Intent(OptionsActivity.this, HelpActivity.class);
			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return true;
		}
		if (id == R.id.campusplan) {
			startActivity(new Intent(this, PlanActivity.class)
					.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION));
		}
		if (id == R.id.logout) {
			db.clearDatabaseLogin();
			db.clearDatabaseGrips();
			db.clearDatabaseMail();
			db.clearDatabaseMensa();
			Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
			return true;
		}
		if (id == R.id.refresh_grips) {
			if (InternetCheck.isInternetAvailiable(this, this)) {
				db.clearDatabaseGrips();
				Intent intent = new Intent(OptionsActivity.this,
						GripsActivity.class);
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				finish();
			}
			return true;
		}
		if (id == R.id.refresh_mensa) {
			if (InternetCheck.isInternetAvailiable(this, this)) {
				db.clearDatabaseMensa();
				Intent intent = new Intent(OptionsActivity.this,
						MensaActivity.class);
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void internetCheckToast() {
		Toast toast = Toast.makeText(this, R.string.internetCheck_toast,
				Toast.LENGTH_LONG);
		toast.show();
	}
	
	public void passwordChangeDialog(){
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    LayoutInflater inflater = getLayoutInflater();

	    View v = inflater.inflate(R.layout.password_dialog, null);
	    builder.setView(v); 
	        
	    final EditText passw = (EditText) v.findViewById(R.id.password_dialog1);
	    
	    AlertDialog dialog = builder.create();
	    dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.passwort_dialog_ok), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				updatePasswordInDb(passw.getText().toString());
				Toast toast = Toast.makeText(getApplication(), getString(R.string.passwort_dialog_message_onSuccess), Toast.LENGTH_LONG);
				toast.show();
			}
		});
	    dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.passwort_dialog_cancel), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast toast = Toast.makeText(getApplication(), getString(R.string.passwort_dialog_message_onCancel), Toast.LENGTH_LONG);
				toast.show();
			}
		});
	    dialog.show();
	}

	protected void updatePasswordInDb(String pw) {
		String user = db.getLoginData().get(0);
		String email = db.getLoginData().get(2);
		db.clearDatabaseLogin();
		try {
			pw = SecurityMain.encrypt(pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.saveLoginData(user, pw, email);
		

	}
}