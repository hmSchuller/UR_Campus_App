package de.ur.mi.mspwddhs.campusapp.grips;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import de.ur.mi.mspwddhs.campusapp.R;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GradesListAdapter extends BaseAdapter {
	private static final int THREAD_MAX_LENGTH = 45;
	private ArrayList<Grades> data;
	private Context context;

	public GradesListAdapter(ArrayList<Grades> data, Context context, Activity activity) {
		this.context = context;
		
		if(data.isEmpty()){
			((CourseActivity)activity).showEmptyDataToast();
		}
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(R.layout.grips_grades_list_item,
					parent, false);
		}
		convertView.setClickable(false);
		TextView gradeTitle = (TextView) convertView
				.findViewById(R.id.grades_list_name);
		if (data.get(position).getTaskName().length() > THREAD_MAX_LENGTH) {
			gradeTitle.setText(data.get(position).getTaskName()
					.substring(0, THREAD_MAX_LENGTH)
					+ "...");
		} else {
			gradeTitle.setText(data.get(position).getTaskName());
		}

		TextView points = (TextView) convertView
				.findViewById(R.id.grades_list_points);
		points.setText(data.get(position).getPoints());

		ImageView feedback = (ImageView) convertView
				.findViewById(R.id.grades_list_feedback);

		TextView percentile = (TextView) convertView
				.findViewById(R.id.grades_list_percentile);
		percentile.setText(data.get(position).getPoints());
		if (data.get(position).getFeedback().length() > 1) {
			convertView.setClickable(true);
			feedback.setBackgroundResource(R.drawable.feedback);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder adb = new AlertDialog.Builder(context);
					adb.setTitle(R.string.feedback_titel);
					adb.setMessage(data.get(position).getFeedback());
					adb.setNeutralButton(R.string.ok_button,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					adb.show();
				}
			});
		}
		return convertView;
	}

}
