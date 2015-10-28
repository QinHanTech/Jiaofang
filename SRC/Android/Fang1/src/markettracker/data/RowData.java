package markettracker.data;

import java.util.ArrayList;
import java.util.Iterator;

import markettracker.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RowData {
	private Fields fields;

	public static final String METHOD = "msgtype";

	public static final String METHODINFO = "msginfo";

	private static RowData mInstance = null;

	private ArrayList<RowData> dataList;

	private static final String TABLELIST = "hulist";

	public static RowData getInstance(JSONObject step) {
		if (mInstance == null) {
			mInstance = new RowData(step);
		}
		return mInstance;
	}

	public static RowData getNewInstance(JSONObject step) {
		mInstance = new RowData(step);
		return mInstance;
	}

	@SuppressWarnings("unchecked")
	public RowData(JSONObject jsonObject) {
		try {
			Iterator<String> it = jsonObject.keys();
			String key;
			while (it.hasNext()) {
				key = it.next();
				java.lang.System.out.println(key);

				if (key.equalsIgnoreCase(TABLELIST))
					setDataList(jsonObject.getJSONArray(key));
				else
					put(StringUtils.jsonString(jsonObject, key), key);
				// put(StringUtils.jsonString(jsonObject, key), key);
			}
			System.out.println("1111-------");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setDataList(JSONArray jsonArray) {
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				setRowData(RowData.getNewInstance(jsonArray.getJSONObject(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setRowData(RowData rowData) {
		if (dataList == null)
			dataList = new ArrayList<RowData>();
		this.dataList.add(rowData);
	}

	/**
	 * 返回 dataList 的值
	 * 
	 * @return dataList
	 */

	public ArrayList<RowData> getDataList() {
		return dataList;
	}

	/**
	 * 返回 dataList 的值
	 * 
	 * @return dataList
	 */

	public RowData getRowData(int index) {
		return dataList.get(index);
	}
	
	public int size()
	{
		return dataList==null?0:dataList.size();
	}
	
	public void put(String value, String key) {
		if (fields == null)
			fields = new Fields();
		fields.put(key, value);
	}

	public String getString(String key) {
		return this.fields.getStrValue(key);
	}
}
