package de.ur.mi.mspwddhs.campusapp.mail;

import java.util.ArrayList;

import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.secruity.InternetCheck;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
public class ExpandableListAdapter extends BaseExpandableListAdapter 
{
	private ArrayList<Email> data;
	private Context context;
	private Activity activity;
	private String subject;
		
	public ExpandableListAdapter(ArrayList<Email> data, Context context,
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
		return 1;
	}
	@Override
	public Object getGroup(int groupPosition) {
		return data.get(groupPosition);
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return data.get(groupPosition);
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
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
				
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.mail_list_group, parent,
					false);
		}
		
		ImageView collapse = (ImageView) convertView.findViewById(R.id.collapse);
		if (isExpanded) collapse.setBackgroundResource(R.drawable.collapse);
		if (!isExpanded) collapse.setBackgroundResource(R.drawable.collapse_not);		
		
		
		TextView fromEmail = (TextView) convertView.findViewById(R.id.fromEmail);
		fromEmail.setText(getLastFrom(groupPosition));
		
		TextView from = (TextView) convertView.findViewById(R.id.from);
		from.setText(context.getResources().getString(R.string.from) + " " + makeRightFrom(groupPosition, fromEmail));
		
		TextView date = (TextView) convertView.findViewById(R.id.date);
		date.setText(makeDateString(groupPosition));
		
		TextView subject = (TextView) convertView.findViewById(R.id.subject);
		subject.setText(context.getResources().getString(R.string.subject) + " " + data.get(groupPosition).getSubject());
		return convertView;
	}
	
	private String makeRightFrom(int groupPosition, TextView fromEmail) {
		String textFrom = data.get(groupPosition).getFrom();
		String[] sepFrom = textFrom.split(" ");
		int lastSep = sepFrom.length;
		String rightFrom = "";

		if (lastSep > 1) {
			for (int i = 0; i < lastSep-1; i++) {
				fromEmail.setVisibility(View.VISIBLE);
				rightFrom = rightFrom + sepFrom[i] + " ";
			}
		} else {
			rightFrom = "<" + sepFrom[0] + ">";
			fromEmail.setVisibility(View.GONE);
		}
		return rightFrom;
	}
	@Override
	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.mail_list_item, parent,
					false);
		}
		Button answerButton = (Button) convertView
				.findViewById(R.id.answerButton);
		answerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(InternetCheck.isInternetAvailiable(context, (OptionsActivity)activity)){
				((MailActivity) activity).mailAnswer(data.get(groupPosition)
						.getSubject(), data.get(groupPosition).getFrom());
				}
			}
		});
		TextView textView = (TextView) convertView.findViewById(R.id.textView);
		String text = data.get(groupPosition).getContent();
		textView.setText(Html.fromHtml(text));
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		return convertView;
	}
	
	private String getLastFrom(int groupPosition) {
		String textFrom = data.get(groupPosition).getFrom();
		String[] sepFrom = textFrom.split(" ");
		int lastSep = sepFrom.length;
		String rightFrom = sepFrom[lastSep-1];
		return rightFrom;
	}
	private String makeDateString (int groupPosition) {
		String dateString = data.get(groupPosition).getDate();
		String[] sep = dateString.split(" ");
		
		dateString = context.getResources().getString(R.string.am) + " " + sep[0]+ " "  + context.getResources().getString(R.string.um) + " " + sep[1] + " " +  context.getResources().getString(R.string.uhr);
		return dateString;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	public String getSubject() {
		return subject;
	}
}
