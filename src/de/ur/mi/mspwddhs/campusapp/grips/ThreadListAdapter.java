package de.ur.mi.mspwddhs.campusapp.grips;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import de.ur.mi.mspwddhs.campusapp.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class ThreadListAdapter extends BaseAdapter{
	private ArrayList<MyThread> data;
	private Context context;
	
	public ThreadListAdapter(ArrayList<MyThread> data, Context context){
		this.data = data;
		this.context = context;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(R.layout.grips_thread_list_item, parent,
					false);
		}
		TextView author = (TextView) convertView.findViewById(R.id.thread_list_author);
		author.setText(data.get(position).getAuthor());
		
		TextView date = (TextView) convertView.findViewById(R.id.thread_list_date);
		date.setText(data.get(position).getDate());
		
		TextView content = (TextView) convertView.findViewById(R.id.thread_list_content);
		content.setText(data.get(position).getMessage());
		
		return convertView;
	}
}