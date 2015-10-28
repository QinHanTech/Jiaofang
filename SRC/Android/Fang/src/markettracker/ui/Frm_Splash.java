package markettracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import com.rh.fang.jf.R;

public class Frm_Splash extends Activity {

	/** Called when the activity is first created. */
	// @Override
	public void onCreate(Bundle savedInstanceState) {
		
//    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()     
//        .detectDiskReads()     
//        .detectDiskWrites()     
//        .detectNetwork()   // or .detectAll() for all detectable problems     
//        .penaltyLog()     
//        .build());     
//    	StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()     
//        .detectLeakedSqlLiteObjects()     
//        .detectLeakedClosableObjects()     
//        .penaltyLog()     
//        .penaltyDeath()     
//        .build());
    	
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		init();
	}

	private void init() {

		new Handler().postDelayed(new Runnable() {
			// @Override
			public void run() {
				Intent intent = new Intent(Frm_Splash.this, Frm_Login.class);
				startActivity(intent);
				Frm_Splash.this.finish();
			}
		}, 2000);
	}
}