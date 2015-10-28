package markettracker.data;

import org.json.JSONArray;
import org.json.JSONObject;

public class QueryConfig {

	public static final String LASTTIME = "lastTime";
	public static final String USERID = "userId";
	public static final String ISALL = "isAll";
	public static final String STARTROW = "startRow";

	JSONObject queryJson;
	
//	JSONArray queryArray;
	private String method;
	private boolean back = false;

	private static QueryConfig mInstance = null;

	public static QueryConfig getNewInstance() {
		mInstance = new QueryConfig();
		return mInstance;
	}

	public static QueryConfig getInstance() {
		if (mInstance == null)
			mInstance = new QueryConfig();
		return mInstance;
	}

	public QueryConfig() {
		queryJson = new JSONObject();
	}
	
	public void setQueryArray(String key, JSONArray queryArray) {
		try {
			queryJson.put(key, queryArray);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void setData(String key, String value) {
		try {
			queryJson.put(key, value);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void setData(String key, int value) {
		try {
			queryJson.put(key, value);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public String getString(String key) {
		try {
			return queryJson.getString(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public int getInt(String key) {
		try {
			return queryJson.getInt(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public JSONObject getQueryJson() {
		return queryJson;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isBack() {
		return back;
	}

	public void setBack(boolean back) {
		this.back = back;
	}
}
