/**
 * AsyncSforce.java
 *
 * Main class that executes the Sforce SOAP requests asynchronously
 * 
 */

package markettracker.comm;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import markettracker.data.LoginConfig;
import markettracker.data.QueryConfig;
import markettracker.data.QueryDataResult;
import markettracker.data.SObject;
import markettracker.util.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


public class AsyncWeb
{
//	private static final String ANDROID = "Android";
//	private static final String SOAP_RESPONSE = "response";
	private static final String CONTENT_TYPE = "Content-Type";
//	private static final String RESPONSE_TYPE = "responseType";
	
//	private final Market mMarket;
	
	 
	
	public AsyncWeb(Market market)
	{
//		this.mMarket = market;
	}
	
	/**
	 * Gets the request parameters in HashMap. Sends the appropriate SOAP Sforce
	 * SOAP request and then returns the appropriate SOAP response. Based on
	 * success or failure, it invokes fault processing or success processing in
	 * the listener.
	 * 
	 * @return void
	 */
	
	public void request(final QueryConfig requestFields, final RequestListener listener)
	{
		request(Constants.SyncType.QUERY, requestFields, listener);
	}
	
	public void request(final RequestListener listener)
	{
		request(Constants.SyncType.GETSERVERTIME, null, listener);
	}
	
	public void request(final List<SObject> list, final RequestListener listener)
	{
		request(Constants.SyncType.UPLOAD, list, listener);
	}
	
	public void request(final LoginConfig config, final RequestListener listener)
	{
		request(Constants.SyncType.LOGIN, config, listener);
	}
	
	private URI getUri(final String type, final Object object) {
		String uri = "";
		try {
			if (type.equals(Constants.SyncType.QUERY))
				uri = Constants.URL
						+ ((QueryConfig) object).getMethod();
			else if (type.equals(Constants.SyncType.LOGIN))
				uri =  Constants.URL+"loginapi";
//						+ ((LoginConfig) object).getMethod();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return URI.create(uri);
	}

	private StringEntity getEntity(final String type, final Object object) {
		StringEntity entity = null;
		try {
			if (type.equals(Constants.SyncType.QUERY))
				entity = new StringEntity(((QueryConfig) object).getQueryJson()
						.toString(), HTTP.UTF_8);
			else if (type.equals(Constants.SyncType.LOGIN))
				entity = new StringEntity(((LoginConfig) object).getLoginJson()
						.toString(), HTTP.UTF_8);
		} catch (Exception ex) {

		}
		return entity;
	}
	
	private void request(final String type, final Object object,
			final RequestListener listener) {
		new Thread() {
			public void run() {
				HttpClient httpClient = new DefaultHttpClient();
				QueryDataResult result = null;
				httpClient.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 20 * 1000);
				httpClient.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 60 * 1000);
				try {
					HttpPost httpRequest = new HttpPost();
					httpRequest.setHeader(CONTENT_TYPE,
							"application/json; charset=utf-8");
					httpRequest.setURI(getUri(type, object));
					
					httpRequest.setEntity(getEntity(type, object));
					String strResult = "";
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						// 取得返回的数据
						strResult = EntityUtils.toString(
								httpResponse.getEntity(), "UTF-8");
						result = QueryDataResult.getNewInstance(strResult);
						result.put("200", QueryDataResult.STATUSCODE);
					} else {
						result = QueryDataResult.getNewInstance();
						result.put(httpResponse.getStatusLine().getStatusCode()
								+ "", QueryDataResult.STATUSCODE);
						result.put("网络异常"+httpResponse.getStatusLine().toString(), QueryDataResult.ERRMESSAGE);
					}
				} catch (ClientProtocolException e) {
					result = QueryDataResult.getNewInstance();
					result.put("-200", QueryDataResult.STATUSCODE);
					result.put("网络异常" + e.getLocalizedMessage(), QueryDataResult.ERRMESSAGE);
				} catch (IOException ioe) {
					result = QueryDataResult.getNewInstance();
					result.put("-200", QueryDataResult.STATUSCODE);
					result.put("网络异常" + ioe.getLocalizedMessage(), QueryDataResult.ERRMESSAGE);

				} catch (Exception e) {
					result = QueryDataResult.getNewInstance();
					result.put("-200", QueryDataResult.STATUSCODE);
					result.put("网络异常" + e.getLocalizedMessage(), QueryDataResult.ERRMESSAGE);
				} finally {
					if (httpClient != null) {
						// httpClient.close();
						httpClient.getConnectionManager().shutdown();
						httpClient = null;
					}
				}
				if (!result.getString(QueryDataResult.STATUSCODE).equals("200")) {
					listener.onException(result
							.getString(QueryDataResult.ERRMESSAGE));
				} else {
					listener.onComplete(result);
				}
			}
		}.start();
	}
	
	public static interface RequestListener
	{
		public void onComplete(Object response);
		public void onException(String e);
	}
}
