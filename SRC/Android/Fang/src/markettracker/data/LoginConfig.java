package markettracker.data;

import org.json.JSONObject;

public class LoginConfig {

	JSONObject loginJson;
	private String Method;
	
	public static final String LOGINMETHOD = "UserLogin";
	public static final String USERNAME = "userName";
	public static final String PWD = "pwd";
	public static final String IMEI = "imei";
	public static final String APPVERSION = "appVersion";
	public static final String DEVICETYPE = "deviceType";
	public static final String ISINITMOBILE = "isInitMobile";
	public static final String LASTTIME = "lastTime";

	private static LoginConfig mInstance = null;

	public static LoginConfig getNewInstance() {
		mInstance = new LoginConfig();
		return mInstance;
	}

	public static LoginConfig getInstance() {
		if (mInstance == null)
			mInstance = new LoginConfig();
		return mInstance;
	}

	public LoginConfig() {
		loginJson = new JSONObject();
	}

	public void setData(String key, String value) {
		try {
			loginJson.put(key, value);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void setData(String key, int value) {
		try {
			loginJson.put(key, value);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public String getString(String key) {
		try {
			return loginJson.getString(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public int getInt(String key) {
		try {
			return loginJson.getInt(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public JSONObject getLoginJson() {
		return loginJson;
	}

	public String getMethod() {
		return Method;
	}

	public void setMethod(String method) {
		Method = method;
	}

}
