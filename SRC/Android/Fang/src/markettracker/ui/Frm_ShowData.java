package markettracker.ui;

import com.rh.fang.jf.R;
import markettracker.util.Tool;
import markettracker.data.Rms;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

@SuppressLint("SetJavaScriptEnabled")
public class Frm_ShowData extends Activity implements OnClickListener {

	private Context context;
	private Activity activity;
	private Button back;

	private WebView mWebView;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showdata);
		init();
	}

	private void init() {
		initContext();
		initActivity();
		// initHandler();
		// key = this.getIntent().getStringExtra("key");
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
//		Intent intent=this.getIntent();
//		String type =intent.getExtras().getString("type");
//		if(type!=null)
//		{
//		if (type.equals("1"))
//			back.setText("门店KPI");
//		else if (type.equals("2"))
//			back.setText("个人KPI");
//		}
//		else {
//			back.setText(Tool.getDicData().getItemname());
//		}

		mWebView = (WebView) findViewById(R.id.webView);

		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		mWebView.setWebViewClient(new WebViewClient() {

			// // @Override
			 public boolean shouldOverrideUrlLoading(WebView view, String url)
			 {
			 // TODO Auto-generated method stub
			 // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
			 view.loadUrl(url);
			 return true;
			 }
//			public void onProgressChanged(WebView view, int newProgress) {
//				// TODO Auto-generated method stub
//				if (newProgress == 100) {
//					// 网页加载完成
//					CPDialog.dismiss();
//				} else {
//					// 加载中
//					CPDialog.showProgress(context, "正在加载");
//				}
//			}

		});

		// mWebView.getSettings()
		// .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		// settings.setUseWideViewPort(true);

//		settings.setJavaScriptEnabled(true); 
		// 设置可以支持缩放 
		settings.setSupportZoom(true); 
		// 设置出现缩放工具 
		settings.setBuiltInZoomControls(true);
		//扩大比例的缩放
		settings.setUseWideViewPort(true);
		//自适应屏幕
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		settings.setLoadWithOverviewMode(true);
		
//		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//		settings.setUseWideViewPort(true);
		// settings.setLoadWithOverviewMode(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//		settings.setSupportZoom(true);
//		 DisplayMetrics metrics = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(metrics);
		// int mDensity = metrics.densityDpi;
		//
		// if (mDensity == 120) {
		// settings.setDefaultZoom(ZoomDensity.CLOSE);
		// } else if (mDensity == 160) {
		// settings.setDefaultZoom(ZoomDensity.MEDIUM);
		// } else if (mDensity == 240) {
		// settings.setDefaultZoom(ZoomDensity.FAR);
		// }
		// mWebView.setWebChromeClient(new WebChromeClient() {
		// // @Override
		// public void onProgressChanged(WebView view, int newProgress) {
		// // TODO Auto-generated method stub
		// if (newProgress == 100) {
		// // 网页加载完成
		//
		// } else {
		// // 加载中
		//
		// }
		//
		// }
		// });
//		if(type!=null)
//		{
//		if (type.equals("1"))
//			mWebView.loadUrl("http://223.4.23.60:8081/clientkpi.html");
//		else if (type.equals("2"))
//			mWebView.loadUrl("http://223.4.23.60:8081/pskpi.html");
//		}
//		else if (Tool.getDicData().getValue().equals("1210"))
//			mWebView.loadUrl("http://223.4.23.60:8081/prodetail.html");
//		else 
			mWebView.loadUrl("http://139.196.14.15/fc/index.php/Ld/ldlist3/id/1");

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();// 返回上一页面
				return true;
			} else {
				System.exit(0);// 退出程序
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void finish(int type) {
		Intent i = new Intent();
		i.putExtra("type", this.getIntent().getStringExtra("type"));
		setResult(type, i);
		this.finish();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:

			finish(RESULT_OK);
			break;

		default:
			break;
		}
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// finish(RESULT_OK);
	// }
	// return false;
	// }

	public Context getContext() {
		return context;
	}

	public void initContext() {
		this.context = Frm_ShowData.this;
	}

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_ShowData.this;
	}

	@Override
	protected void onRestart() {
		if (!Rms.getLoginDate(context).equals(Tool.getCurrDate())) {
			showTimeoutDialog();
		}
		Tool.setAutoTime(context);
		super.onRestart();
	}

	private void showTimeoutDialog() {
		Builder dialog = new Builder(context);
		dialog.setTitle("警告");
		dialog.setMessage("登录超时,请重新登录！");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			// @Override
			public void onClick(DialogInterface arg0, int arg1) {
				exit();
			}
		});
		dialog.show();
	}

	private void exit() {
		setResult(-100);
		this.finish();
	}
}
