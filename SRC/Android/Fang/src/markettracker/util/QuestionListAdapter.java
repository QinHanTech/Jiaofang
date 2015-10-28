package markettracker.util;

import java.util.ArrayList;
import java.util.List;

import com.rh.fang.jf.R;

import markettracker.data.RowData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuestionListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<RowData> list;
	private ViewHolder view;
	private RowData question;

	public QuestionListAdapter(Context context, List<RowData> list) {
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
			convertView = inflater.inflate(R.layout.cell_question, null);
			view = new ViewHolder();
			view.tv_content = (TextView) convertView.findViewById(R.id.name);
			
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		question = list.get(position);
		if (null != question) {
			view.tv_content.setText(question.getString("wenti"));
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_content;
	}
	
	public RowData getRowData (int index)
	{
		return this.list.get(index);
	}

}
