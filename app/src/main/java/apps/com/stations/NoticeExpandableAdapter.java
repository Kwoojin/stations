//공지사항 어댑터
package apps.com.stations;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class NoticeExpandableAdapter extends BaseExpandableListAdapter {
	Context mContext;
	ArrayList<BoardData> mNoticedata;

	public NoticeExpandableAdapter(Context context, ArrayList<BoardData> data) {
		mContext = context;
		mNoticedata = data;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return mNoticedata.get(groupPosition).getContents();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}


	@Override
	public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listview_notice_memo, null);
		} else {
			view = convertView;
		}

		CustomTextView contents = (CustomTextView) view
				.findViewById(R.id.contents); //
		contents.setMovementMethod(new LinkMovementMethod());

		contents.setText(Html.fromHtml(mNoticedata.get(groupPosition).getContents()
				.replace("span style=\"color:", "font color=\"").replace("</span>", "</font>")));


		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mNoticedata.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mNoticedata.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// GroupView
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		View view;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listview_notice, null);
		} else {
			view = convertView;
		}

		ImageView upup = (ImageView) view.findViewById(R.id.upup);

		if (isExpanded) {

			upup.setBackgroundResource(R.drawable.up);
		} else {
			upup.setBackgroundResource(R.drawable.down);
		}

		CustomTextView title = (CustomTextView) view
				.findViewById(R.id.title);
		title.setText(mNoticedata.get(groupPosition).getTitle());

		CustomTextView reg_date = (CustomTextView) view
				.findViewById(R.id.reg_date);
		reg_date.setText(mNoticedata.get(groupPosition).getReg_date().substring(0, 10));

		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return super.areAllItemsEnabled();
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	// ChildView XML
	public View getChildGenericView() {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.listview_notice_memo, null);
		return view;
	}

	// Parent(Group) View XML
	public View getParentGenericView() {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.listview_notice, null);
		return view;
	}

}
