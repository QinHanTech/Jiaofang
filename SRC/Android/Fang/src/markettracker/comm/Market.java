package markettracker.comm;

import android.content.Context;

public class Market
{
//	private String serverURL = "http://114.80.253.81/InoherbAndroidService/DataWebService.asmx";
	
	private String serverURL = "http://www.rymbus.com/fc/index.php/Api/";
	
	
	Context context;
	
	public Market(Context context)
	{
		this.context = context;
	}
	
	public String getServerURL(String type)
	{
		return serverURL;
	}
	
	public void setServerURL(String serverURL)
	{
		this.serverURL = serverURL;
	}
}
