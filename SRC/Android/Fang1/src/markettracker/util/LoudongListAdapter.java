package markettracker.util;

import com.rh.fang.zs.R;

import markettracker.data.Fields;
import markettracker.data.FieldsList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LoudongListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private FieldsList list;
	private ViewHolder view;
	private Fields loudong;

	public LoudongListAdapter(Context context, FieldsList list) {
		inflater = LayoutInflater.from(context);
		if (list != null)
			this.list = list;
		else
			this.list = new FieldsList();
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
			convertView = inflater.inflate(R.layout.cell_loudong, null);
			view = new ViewHolder();
			view.tv_content = (TextView) convertView.findViewById(R.id.name);
			
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		loudong = list.getFields(position);
		if (null != loudong) {
			view.tv_content.setText(loudong.getStrValue("ldname"));
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_content;
	}
	
	public Fields getFields (int index)
	{
		return this.list.getFields(index);
	}

}
