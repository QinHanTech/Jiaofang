package markettracker.comm;


import markettracker.comm.MarketTracker.ResponseListener;

public abstract class BaseResponseListener implements ResponseListener {
	public static final String MARKET="Market";
	
	public abstract void onComplete(Object response);
	public abstract void onException(String e);
}
