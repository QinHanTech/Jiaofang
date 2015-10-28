package markettracker.util;

import java.util.ArrayList;

import com.rh.fang.jf.R;

import markettracker.data.RowData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

public class HuListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<RowData> list;
	private ViewHolder view;
	private RowData louceng;
	private Context mContext;
	// private int mHunums;
	private OnClickListener mClickListener;

	public HuListAdapter(Context context, ArrayList<RowData> list,
			OnClickListener listener) {
		mContext = context;
		// mHunums = hunums;
		mClickListener = listener;
		inflater = LayoutInflater.from(context);
		if (list != null)
			this.list = list;
		else
			this.list = new ArrayList<RowData>();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			// if (mHunums == 4)
			convertView = inflater.inflate(R.layout.cell_hu4, null);
			// else {
			// convertView = inflater.inflate(R.layout.cell_hu, null);
			// }
			view = new ViewHolder();
			view.line_hu = (LinearLayout) convertView
					.findViewById(R.id.line_hu);
			view.hu1 = (Button) convertView.findViewById(R.id.hu1);
			view.hu2 = (Button) convertView.findViewById(R.id.hu2);
			// if (mHunums == 4) {
			view.hu3 = (Button) convertView.findViewById(R.id.hu3);
			view.hu4 = (Button) convertView.findViewById(R.id.hu4);
			// }
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		louceng = list.get(position);

		if (null != louceng) {

			if (louceng.size() == 1) {
				view.hu2.setVisibility(View.GONE);
				view.hu3.setVisibility(View.GONE);
				view.hu4.setVisibility(View.GONE);
			}
			RowData rowData;
			for (int i = 0; i < louceng.size(); i++) {
				rowData = louceng.getRowData(i);
				if (i == 0) {
					view.hu1.setText(rowData.getString("huname"));
					view.hu1.setTag(rowData);
					if (rowData.getString("jfzt").equals("1")) {
						view.hu1.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_green));
						view.hu1.setTextColor(mContext.getResources().getColor(
								R.color.white));
					} else if (rowData.getString("jfzt").equals("-1")) {
						view.hu1.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_gray));
						view.hu1.setTextColor(mContext.getResources().getColor(
								R.color.black));
					} else if (rowData.getString("jfzt").equals("0")) {
						view.hu1.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_white));
						view.hu1.setTextColor(mContext.getResources().getColor(
								R.color.black));
					} else if (rowData.getString("jfzt").equals("2")) {
						view.hu1.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_lightgreen));
						view.hu1.setTextColor(mContext.getResources().getColor(
								R.color.black));
					} else if (rowData.getString("jfzt").equals("3")) {
						view.hu1.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_red));
						view.hu1.setTextColor(mContext.getResources().getColor(
								R.color.white));
					} else if (rowData.getString("jfzt").equals("5")) {
						view.hu1.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_fenhong));
						view.hu1.setTextColor(mContext.getResources().getColor(
								R.color.white));
					} else if (rowData.getString("jfzt").equals("4")) {
						view.hu1.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_blue));
						view.hu1.setTextColor(mContext.getResources().getColor(
								R.color.white));
					}
					view.hu1.setVisibility(View.VISIBLE);

					view.hu1.setOnClickListener(mClickListener);
				} else if (i == 1) {
					if (rowData.getString("jfzt").equals("1")) {
						view.hu2.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_green));
						view.hu2.setTextColor(mContext.getResources().getColor(
								R.color.white));
					} else if (rowData.getString("jfzt").equals("-1")) {
						view.hu2.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_gray));
						view.hu2.setTextColor(mContext.getResources().getColor(
								R.color.black));
					} else if (rowData.getString("jfzt").equals("0")) {
						view.hu2.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_white));
						view.hu2.setTextColor(mContext.getResources().getColor(
								R.color.black));
					} else if (rowData.getString("jfzt").equals("2")) {
						view.hu2.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_lightgreen));
						view.hu2.setTextColor(mContext.getResources().getColor(
								R.color.black));
					} else if (rowData.getString("jfzt").equals("3")) {
						view.hu2.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_red));
						view.hu2.setTextColor(mContext.getResources().getColor(
								R.color.white));
					} else if (rowData.getString("jfzt").equals("5")) {
						view.hu2.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_fenhong));
						view.hu2.setTextColor(mContext.getResources().getColor(
								R.color.white));
					} else if (rowData.getString("jfzt").equals("4")) {
						view.hu2.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_blue));
						view.hu2.setTextColor(mContext.getResources().getColor(
								R.color.white));
					}
					view.hu2.setText(rowData.getString("huname"));
					view.hu2.setTag(rowData);
					view.hu2.setVisibility(View.VISIBLE);
					view.hu2.setOnClickListener(mClickListener);
				} else if (i == 2) {
					if (rowData.getString("jfzt").equals("1")) {
						view.hu3.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_green));
						view.hu3.setTextColor(mContext.getResources().getColor(
								R.color.white));
					} else if (rowData.getString("jfzt").equals("-1")) {
						view.hu3.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_gray));
						view.hu3.setTextColor(mContext.getResources().getColor(
								R.color.black));
					} else if (rowData.getString("jfzt").equals("0")) {
						view.hu3.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_white));
						view.hu3.setTextColor(mContext.getResources().getColor(
								R.color.black));
					} else if (rowData.getString("jfzt").equals("2")) {
						view.hu3.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_lightgreen));
						view.hu3.setTextColor(mContext.getResources().getColor(
								R.color.black));
					} else if (rowData.getString("jfzt").equals("3")) {
						view.hu3.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_red));
						view.hu3.setTextColor(mContext.getResources().getColor(
								R.color.white));
					} else if (rowData.getString("jfzt").equals("5")) {
						view.hu3.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_fenhong));
						view.hu3.setTextColor(mContext.getResources().getColor(
								R.color.white));
					} else if (rowData.getString("jfzt").equals("4")) {
						view.hu3.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_blue));
						view.hu3.setTextColor(mContext.getResources().getColor(
								R.color.white));
					}
					view.hu3.setText(rowData.getString("huname"));
					view.hu3.setTag(rowData);
					view.hu3.setVisibility(View.VISIBLE);
					view.hu3.setOnClickListener(mClickListener);
				} else if (i == 3) {
					if (rowData.getString("jfzt").equals("1")) {
						view.hu4.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_green));
						view.hu4.setTextColor(mContext.getResources().getColor(
								R.color.white));
					} else if (rowData.getString("jfzt").equals("-1")) {
						view.hu4.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_gray));
						view.hu4.setTextColor(mContext.getResources().getColor(
								R.color.black));
					} else if (rowData.getString("jfzt").equals("0")) {
						view.hu4.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_white));
						view.hu4.setTextColor(mContext.getResources().getColor(
								R.color.black));
					} else if (rowData.getString("jfzt").equals("2")) {
						view.hu4.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_lightgreen));
						view.hu4.setTextColor(mContext.getResources().getColor(
								R.color.black));
					} else if (rowData.getString("jfzt").equals("3")) {
						view.hu4.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_red));
						view.hu4.setTextColor(mContext.getResources().getColor(
								R.color.white));
					} else if (rowData.getString("jfzt").equals("5")) {
						view.hu4.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_fenhong));
						view.hu4.setTextColor(mContext.getResources().getColor(
								R.color.white));
					} else if (rowData.getString("jfzt").equals("4")) {
						view.hu4.setBackgroundDrawable(Tool.getDrawable(
								mContext, R.drawable.status_blue));
						view.hu4.setTextColor(mContext.getResources().getColor(
								R.color.white));
					}
					view.hu4.setText(rowData.getString("huname"));
					view.hu4.setTag(rowData);
					view.hu4.setVisibility(View.VISIBLE);
					view.hu4.setOnClickListener(mClickListener);
				}
			}

		}
		return convertView;
	}

	class ViewHolder {
		LinearLayout line_hu;
		Button hu1, hu2, hu3, hu4;
	}

	public RowData getFields(int index) {
		return this.list.get(index);
	}

}
