package markettracker.util;

import java.util.List;

import markettracker.data.LoginConfig;
import markettracker.data.QueryConfig;
import markettracker.data.SObject;
import android.app.Activity;
import android.os.Handler;

public class SyncData {

	public static void Login(LoginConfig config, Handler handler,
			Activity activity) {
		((SyncDataApp) activity.getApplication()).Login(config, handler);
	}

	public static void Query(QueryConfig config, Handler handler,
			Activity activity) {
		((SyncDataApp) activity.getApplication()).Query(config, handler,
				activity);
	}

	public static void Upload(List<SObject> list, Handler handler,
			Activity activity) {
		((SyncDataApp) activity.getApplication()).Upload(list, handler);
	}

//	public static void syncData(Handler handler, Activity activity) {
//		((SyncDataApp) activity.getApplication()).syncRpt(handler);
//	}
//
//
//	public static void startSyncData(Activity activity) {
//		((SyncDataApp) activity.getApplication()).startSyncData();
//	}

	public static void startSync(Activity activity) {
		((SyncDataApp) activity.getApplication()).startSync();
	}

}
