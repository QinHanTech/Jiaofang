package markettracker.comm;


import markettracker.comm.AsyncWeb.RequestListener;
import markettracker.comm.MarketTracker.ResponseListener;

public abstract class BaseRequestListener implements RequestListener {
//	private static final String MARKET="Market";
	private static ResponseListener listener;

	public abstract void onComplete(Object response);

	public void onException(String e)
	{
		listener.onException(e);
	}

	public ResponseListener getResponseListener()
	{
		return BaseRequestListener.listener;
	}

	public void setResponseListener(ResponseListener listener)
	{
		BaseRequestListener.listener = listener;
	}
}
