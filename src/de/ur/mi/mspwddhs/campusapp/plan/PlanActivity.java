package de.ur.mi.mspwddhs.campusapp.plan;

import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import uk.co.senab.photoview.PhotoViewAttacher;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

public class PlanActivity extends OptionsActivity {

	ImageView mImageView;
	PhotoViewAttacher mAttacher;
	Database db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plan);

		mImageView = (ImageView) findViewById(R.id.img);
		Drawable bitmap = getResources().getDrawable(R.drawable.campus);
		mImageView.setImageDrawable(bitmap);
		mAttacher = new PhotoViewAttacher(mImageView);
	}

	public void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.removeItem(R.id.refresh_mail);
		menu.removeItem(R.id.refresh_grips);
		menu.removeItem(R.id.refresh_mensa);
		menu.removeItem(R.id.newMailOption);
		return true;
	}
}
