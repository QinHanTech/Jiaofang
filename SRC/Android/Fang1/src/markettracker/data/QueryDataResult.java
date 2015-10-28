package markettracker.data;

import java.util.ArrayList;
import java.util.Iterator;

import markettracker.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryDataResult {

	public static final int STATUS_OK = 1;
	public static final int STATUS_UPDATE = -2;

	public static final int STATUS_DONE = 1;

	// 网络状态
	public static final String STATUSCODE = "StatusCode";

	// 共有
	public static final String ERRMESSAGE = "ErrorMsg";
	public static final String SUCCESS = "Success";
	public static final String ERRCODE = "ErrorCode";

	// 登录
	public static final String COMANYID = "CompanyId";
	public static final String USERID = "userId";
	public static final String SERVERURL = "ServiceURL";
	public static final String APPURL = "AppURL";
	
	public static final String DOWNLOADURL = "downloadurl";

	// 下载
	public static final String TYPE = "Type";
	public static final String ISALL = "isAll";
	public static final String NEETSTARTROW = "NextStartRow";
	public static final String DONE = "Done";

	private static final String MSGTYPELIST = "msgtypelist";
	private static final String CLIENTTABLE = "datatable";

	private Fields fields;
	private ArrayList<RowData> dataList;

	public static QueryDataResult mInstance = null;

	public static QueryDataResult getInstance(String josnString) {
		if (mInstance == null) {
			mInstance = new QueryDataResult(josnString);
		}
		return mInstance;
	}

	public static QueryDataResult getNewInstance(String josnString) {
		mInstance = new QueryDataResult(josnString);
		return mInstance;
	}

	public static QueryDataResult getNewInstance() {
		mInstance = new QueryDataResult();
		return mInstance;
	}

	public QueryDataResult() {
	}

	@SuppressWarnings("unchecked")
	public QueryDataResult(String josnString) {
		try {
			JSONObject jsonObject = new JSONObject(josnString);
			Iterator<String> it = jsonObject.keys();
			String key = "";
			while (it.hasNext()) {
				key = it.next();
				java.lang.System.out.println(key);

				if (key.equalsIgnoreCase(MSGTYPELIST)
						|| key.equals(CLIENTTABLE))
					setDataList(jsonObject.getJSONArray(key));
				else
					put(StringUtils.jsonString(jsonObject, key), key);
			}

			java.lang.System.out.println(key);

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

	public String getMenthod(int index) {
		try {
			return getRowData(index).getString(RowData.METHOD);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public String getMenthodInfo(int index) {
		try {
			return getRowData(index).getString(RowData.METHODINFO);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public int size() {
		try {
			return dataList == null ? 0 : dataList.size();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	/**
	 * 设置 dataList 的值
	 * 
	 * @param dataList
	 */

	public void setDataList(ArrayList<RowData> dataList) {
		this.dataList = dataList;
	}

	public void setRowData(RowData rowData) {
		if (dataList == null)
			dataList = new ArrayList<RowData>();
		this.dataList.add(rowData);
	}

	/**
	 * 返回 fields 的值
	 * 
	 * @return fields
	 */

	public Fields getFields() {
		return fields;
	}

	/**
	 * 设置 fields 的值
	 * 
	 * @param fields
	 */

	public void setFields(Fields fields) {
		this.fields = fields;
	}

	public void put(String value, String key) {
		if (fields == null)
			fields = new Fields();
		fields.put(key, value);
	}

	public String getString(String key) {
		return this.fields.getStrValue(key);
	}

	public int getInt(String key) {
		return this.fields.getIntValue(key);
	}

	public boolean isSuccess() {
		if (getInt(QueryDataResult.SUCCESS) == QueryDataResult.STATUS_OK)
			return true;
		return false;
	}

}
