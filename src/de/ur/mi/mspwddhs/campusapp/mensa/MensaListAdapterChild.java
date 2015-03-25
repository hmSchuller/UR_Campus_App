package de.ur.mi.mspwddhs.campusapp.mensa;

import java.util.ArrayList;

import de.ur.mi.mspwddhs.campusapp.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MensaListAdapterChild implements ListAdapter {

	private ArrayList<MensaDay> mensaDayList;
	private int groupPos;
	private int childPos;
	private Context context;
	TextView foodName;
	TextView foodPrice;
	ListView listView;
	View line;
	int counter = 0;

	public MensaListAdapterChild(int groupPos, int childPos,
			ArrayList<MensaDay> mensaDayList, Context context) {
		this.mensaDayList = mensaDayList;
		this.groupPos = groupPos;
		this.context = context;
		this.childPos = childPos;
	}

	@Override
	public int getCount() {
		if (childPos == 0)
			return mensaDayList.get(groupPos).getSuppe().size();
		if (childPos == 1)
			return mensaDayList.get(groupPos).getHaupt().size();
		if (childPos == 2)
			return mensaDayList.get(groupPos).getBeilagen().size();
		return mensaDayList.get(groupPos).getNachtisch().size();
	}

	@Override
	public Object getItem(int arg0) {
		if (childPos == 0)
			return mensaDayList.get(groupPos).getSuppe().get(arg0);
		if (childPos == 1)
			return mensaDayList.get(groupPos).getHaupt().get(arg0);
		if (childPos == 2)
			return mensaDayList.get(groupPos).getBeilagen().get(arg0);
		return mensaDayList.get(groupPos).getNachtisch().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public int getItemViewType(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate
					.inflate(R.layout.mensa_newborn, parent, false);
		}

		listView = (ListView) convertView.findViewById(R.id.foods);

		int size = getCount();

		fillTheFoods(convertView, position, size);

		return convertView;

	}

	private void fillTheFoods(View convertView, int position, int size) {
		String food = "";

		if (size != 0) {

			for (int i = 0; i < size; i++) {

				if (childPos == 0)
					food = mensaDayList.get(groupPos).getSuppe().get(i);
				if (childPos == 1)
					food = mensaDayList.get(groupPos).getHaupt().get(i);
				if (childPos == 2)
					food = mensaDayList.get(groupPos).getBeilagen().get(i);
				if (childPos == 3)
					food = mensaDayList.get(groupPos).getNachtisch().get(i);

				String[] parts = food.split(";");
				String name = parts[0];
				String price = parts[1];
				String kennz;

				if (parts.length == 3) {
					kennz = parts[2];
				} else {
					kennz = " ";
				}

				ImageView foodImage = (ImageView) convertView
						.findViewById(findRightID(i + 1, "food_image"));

				if (kennz.contains("S") && kennz.contains("R")) {
					foodImage.setBackgroundResource(R.drawable.rind_schwein);
				} else if (kennz.contains("F")) {
					foodImage.setBackgroundResource(R.drawable.fischheide);
				} else if (kennz.contains("VG")) {
					foodImage.setBackgroundResource(R.drawable.vegan);
				} else if (kennz.contains("V")) {
					foodImage.setBackgroundResource(R.drawable.blatt);
				} else if (kennz.contains("S") && !kennz.contains("MSC")) {
					foodImage.setBackgroundResource(R.drawable.schweinheide);
				} else if (kennz.contains("R")) {
					foodImage.setBackgroundResource(R.drawable.rindheide);
				} else if (kennz.contains("G")) {
					foodImage.setBackgroundResource(R.drawable.huhnheide);
				} else if (kennz.contains("L") && !kennz.contains("MSC")) {
					foodImage.setBackgroundResource(R.drawable.schafheide);
				} else if (kennz.contains("W")) {
					foodImage.setBackgroundResource(R.drawable.deerheide);
				}

				foodName = (TextView) convertView.findViewById(findRightID(
						i + 1, "food_name"));
				foodName.setVisibility(View.VISIBLE);
				foodName.setText(name);

				foodPrice = (TextView) convertView.findViewById(findRightID(
						i + 1, "food_price"));
				foodPrice.setVisibility(View.VISIBLE);
				foodPrice.setText(price);

				if (i > 0) {
					line = (View) convertView.findViewById(findRightID(i + 1,
							"line"));
					line.setVisibility(View.VISIBLE);
				}
			}
		}

	}

	private int findRightID(int i, String label) {
		String name = label + i;

		int resID = context.getResources().getIdentifier(name, "id",
				context.getPackageName());

		return resID;
	}

	@Override
	public int getViewTypeCount() {

		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int arg0) {
		return false;
	}

}
