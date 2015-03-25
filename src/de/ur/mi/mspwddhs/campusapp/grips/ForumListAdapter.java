package de.ur.mi.mspwddhs.campusapp.grips;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.ur.mi.mspwddhs.campusapp.R;

public class ForumListAdapter extends BaseAdapter {
	private ArrayList<Forum> data;
	private Context context;
	private Activity activity;

	public ForumListAdapter(ArrayList<Forum> data, Context context,
			Activity activity) {
		this.context = context;
		this.activity = activity;
		
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
			convertView = inflate.inflate(R.layout.grips_forum_list, parent,
					false);
		}

		try {
			TextView author = (TextView) convertView
					.findViewById(R.id.forum_list_author);
			author.setText(data.get(position).getAuthor());

			TextView date = (TextView) convertView
					.findViewById(R.id.forum_list_date);
			date.setText(data.get(position).getDate());

			TextView thread = (TextView) convertView
					.findViewById(R.id.forum_list_thread);
			thread.setText(data.get(position).getLink().getName());

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((CourseActivity) activity).switchToThreadActivity(data
							.get(position).getLink().getUrl(),
							data.get(position).getLink().getName());
				}
			});
		} catch (Exception e) {
		}

		return convertView;

	}

}
