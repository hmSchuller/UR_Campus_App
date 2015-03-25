package de.ur.mi.mspwddhs.campusapp.help;

import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class HelpActivity extends OptionsActivity{
	
	int counter = 1;
	
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        showHelp();
	}
	
	public void onPause() {
	     super.onPause();
	     overridePendingTransition(0, 0);
	 }
	
	private void showHelp() {
		Button go = (Button) findViewById(R.id.go);
		TextView goText = (TextView) findViewById(R.id.go_text_button);
		go.setBackgroundResource(R.drawable.go);
		goText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (counter == 9) {
					finish();
					overridePendingTransition(0, 0);
				}
				counter++;
				showHelp();	
			}
		});
		
		
		Button back = (Button) findViewById(R.id.back);
		TextView backText = (TextView) findViewById(R.id.back_text_button);
		backText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (counter == 1) {
					finish();
					overridePendingTransition(0, 0);
				}
				counter--;
				showHelp();	
			}
		});
		back.setBackgroundResource(R.drawable.back);
		
		ImageView help = (ImageView) findViewById(R.id.helpImage);
		help.setBackgroundResource(findRightID());

	}
	
	private int findRightID() {
		String name = "folie" + counter;
		int resID = getResources().getIdentifier(name, "drawable", getPackageName());
		return resID;
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
