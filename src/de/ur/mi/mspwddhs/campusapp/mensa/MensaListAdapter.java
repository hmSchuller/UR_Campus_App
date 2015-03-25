package de.ur.mi.mspwddhs.campusapp.mensa;

import java.util.ArrayList;
import java.util.Calendar;

import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.database.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MensaListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private Database db;
	private ArrayList<MensaDay> mensaDaysArray;
	private MensaListAdapterChild adapAdap;
	Calendar calendar;

	public MensaListAdapter(ArrayList<String> dates, Database db,
			Context context) {
		this.db = db;
		this.context = context;
		mensaDaysArray = new ArrayList<MensaDay>();
		MensaDay m;
		for (int i = 0; i < dates.size(); i++) {
			m = new MensaDay(dates.get(i), this.db);
			mensaDaysArray.add(m);
		}
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		if (arg1 == 0)
			return mensaDaysArray.get(arg0).getSuppe();
		if (arg1 == 1)
			return mensaDaysArray.get(arg0).getHaupt();
		if (arg1 == 2)
			return mensaDaysArray.get(arg0).getBeilagen();
		return mensaDaysArray.get(arg0).getNachtisch();
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	@Override
	public int getChildrenCount(int arg0) {
		return 4;
	}

	@Override
	public Object getGroup(int arg0) {
		return mensaDaysArray.get(arg0);
	}

	@Override
	public int getGroupCount() {
		return mensaDaysArray.size();
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(R.layout.mensa_parent, parent, false);
		}

		TextView day = (TextView) convertView.findViewById(R.id.wochentag);

		ImageView collapse = (ImageView) convertView
				.findViewById(R.id.collapse);
		if (isExpanded) collapse.setBackgroundResource(R.drawable.collapse);
		if (!isExpanded) collapse.setBackgroundResource(R.drawable.collapse_not);

		giveMeTheDate(groupPosition, convertView);

		if (groupPosition == 0)
			day.setText(R.string.mon);
		if (groupPosition == 1)
			day.setText(R.string.tue);
		if (groupPosition == 2)
			day.setText(R.string.wed);
		if (groupPosition == 3)
			day.setText(R.string.thu);
		if (groupPosition == 4)
			day.setText(R.string.fri);

		return convertView;
	}

	private void giveMeTheDate(int groupPosition, View convertView) {

		TextView date = (TextView) convertView.findViewById(R.id.date);
		String dateString = mensaDaysArray.get(groupPosition).getDate();

		String currentDay = dateAndTodayView();

		String[] dateArr = dateString.split("\\.");
		dateString = (dateArr[0] + "." + dateArr[1]);

		date.setText(dateString);
		ImageView today = (ImageView) convertView.findViewById(R.id.today);

		if (currentDay.equals(dateString)) {
			today.setBackgroundResource(R.drawable.kreuzrot);
			today.setVisibility(View.VISIBLE);
		} else {
			today.setVisibility(View.INVISIBLE);
		}
	}

	private String dateAndTodayView() {
		Calendar c = Calendar.getInstance();
		int currentDate = c.get(Calendar.DATE);
		int currentMonth = c.get(Calendar.MONTH);
		String currentDateString;
		String currentMonthString;
		currentMonth++;
		if (currentMonth < 10) {
			currentMonthString = "0" + currentMonth;
		} else {
			currentMonthString = currentMonth + "";
		}
		if (currentDate < 10) {
			currentDateString = "0" + currentDate;
		} else {
			currentDateString = currentDate + "";
		}
		return currentDateString + "." + currentMonthString;

	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(R.layout.mensa_child, parent, false);
		}
		TextView header = (TextView) convertView.findViewById(R.id.headerfood);

		if (childPosition == 0)
			header.setText(R.string.suppen);
		if (childPosition == 1)
			header.setText(R.string.hauptgerichte);
		if (childPosition == 2)
			header.setText(R.string.beilagen);
		if (childPosition == 3)
			header.setText(R.string.nachspeisen);

		ListView foods = (ListView) convertView.findViewById(R.id.foods);

		adapAdap = new MensaListAdapterChild(groupPosition, childPosition,
				mensaDaysArray, this.context);
		foods.setAdapter(adapAdap);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}
}
