package markettracker.comm;

import java.util.List;

import markettracker.data.LoginConfig;
import markettracker.data.QueryConfig;
import markettracker.data.SObject;
import android.content.Context;

public class MarketTracker {
	private static Market mMarket;
	private static AsyncWeb asf;

	public static void init(Context context) {
		mMarket = new Market(context);
		asf = new AsyncWeb(mMarket);
	}

	public static void query(QueryConfig config,
			ResponseListener queryResponseListener) {
		BaseRequestListener listener = new QueryRequestListener();
		listener.setResponseListener(queryResponseListener);
		asf.request(config, listener);
	}

	public static void login(LoginConfig config,
			ResponseListener loginResponseListener) {
		BaseRequestListener listener = new LoginRequestListener();
		listener.setResponseListener(loginResponseListener);
		asf.request(config, listener);
	}

	public static void upload(List<SObject> sobject,
			ResponseListener upsertRequestListener) {
		BaseRequestListener listener = new UpsertRequestListener();
		listener.setResponseListener(upsertRequestListener);
		asf.request(sobject, listener);
	}

	public static class UpsertRequestListener extends BaseRequestListener {
		public void onComplete(final Object qresponse) {
			getResponseListener().onComplete(qresponse);
		}
	}

	public static class QueryRequestListener extends BaseRequestListener {
		public void onComplete(final Object qresponse) {
			getResponseListener().onComplete(qresponse);
		}
	}

	public static class LoginRequestListener extends BaseRequestListener {
		public void onComplete(final Object userponse) {
			getResponseListener().onComplete(userponse);
		}
	}

	public static interface ResponseListener {
		public void onComplete(Object response);

		public void onException(String e);

	}

	public static Market getMarket() {
		return mMarket;
	}

	public static void setMarket(Market market) {
		MarketTracker.mMarket = market;
	}

}
