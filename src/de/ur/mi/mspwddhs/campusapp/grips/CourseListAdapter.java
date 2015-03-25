package de.ur.mi.mspwddhs.campusapp.grips;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.ur.mi.mspwddhs.campusapp.R;

public class CourseListAdapter extends BaseExpandableListAdapter {
	private ArrayList<Course> data;
	private Context context;
	private Activity activity;

	public CourseListAdapter(ArrayList<Course> data, Context context,
			Activity activity) {
		this.data = data;
		this.context = context;
		this.activity = activity;
	}


	@Override
	public int getGroupCount() {
		return data.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return data.get(groupPosition).getListLinks().size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return data.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return data.get(groupPosition).getListLinks().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, final ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(R.layout.grips_list_title, parent, false);
		}
		final TextView title = (TextView) convertView
				.findViewById(R.id.list_title_name);
		title.setText(((Course) getGroup(groupPosition)).getCourseName());
		

		ImageView collapse = (ImageView) convertView.findViewById(R.id.collapse);
		if (getChildrenCount(groupPosition) != 0 ) {
		collapse.setVisibility(View.VISIBLE);
		if (isExpanded) collapse.setBackgroundResource(R.drawable.collapse);
		if (!isExpanded) collapse.setBackgroundResource(R.drawable.collapse_not);
		} else {
			collapse.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(R.layout.grips_list_item, parent, false);
		}
		TextView title = (TextView) convertView
				.findViewById(R.id.list_item_name);
		title.setText(((Link) getChild(groupPosition, childPosition)).getName());
		convertView.setClickable(true);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((GripsActivity) activity).parseSingleItem(((Link) getChild(
						groupPosition, childPosition)));
			}
		});

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
