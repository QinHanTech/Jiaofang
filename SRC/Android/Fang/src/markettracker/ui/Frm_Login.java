package markettracker.ui;

import com.rh.fang.jf.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import markettracker.util.Constants;
import markettracker.util.SyncData; //import markettracker.util.SyncData;
import markettracker.util.Tool;
import markettracker.util.Constants.AlertType;
import markettracker.data.LoginConfig;
import markettracker.data.QueryConfig;
import markettracker.data.QueryDataResult;
import markettracker.data.Rms;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

@SuppressLint("HandlerLeak")
public class Frm_Login extends Activity implements OnClickListener,
		OnEditorActionListener {

	private Button login;
	private EditText eName, ePwd;

	private CheckBox checkBox;
	private String sName, sPwd;
	private Context context;
	private Handler handler;
//	private FieldsList queryList;
	private int index = 0;
//	private QueryConfig config;
	private Activity activity;

	private QueryDataResult loginResult;

//	private String downLoadUrl;

	/** Called when the activity is first created. */
	// @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		init();
		Tool.setAutoTime(context);

	}

	private void init() {

		initContext();
		initActivity();
		initScreen();
		initHandler();

		login = (Button) findViewById(R.id.btn_login);
		login.setOnClickListener(this);

		eName = (EditText) findViewById(R.id.edt_name);
		eName.setText(Rms.getUserName(context));
		// eName.setText("liuhm");

		ePwd = (EditText) findViewById(R.id.edt_pwd);
		ePwd.setOnEditorActionListener(this);
		if (Rms.isSavePwd(context))
			ePwd.setText(Rms.getPwd(context));

		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		checkBox.setChecked(Rms.isSavePwd(context));
	}

	private void checkLogin(QueryDataResult result) {
		try {

			// Tool.setQueryList(list);
			if (result.size() == 0
					|| (!Rms.getFirst(context) && Tool.getCurrDate().equals(
							Rms.getLoginDate(context))))
				toMenu();
			else {
				Tool.showProgress(context, result.getMenthodInfo(index));
				QueryConfig.getNewInstance()
						.setMethod(result.getMenthod(index));
				QueryConfig.getInstance().setData(QueryConfig.STARTROW, "0");
				QueryConfig.getInstance().setData(QueryConfig.USERID,
						result.getString(QueryDataResult.USERID));
				if (Rms.getFirst(context)) {
					QueryConfig.getInstance().setData(QueryConfig.LASTTIME,
							Tool.getCurrDate());
					QueryConfig.getInstance().setData(QueryConfig.ISALL, "0");
				} else {
					QueryConfig.getInstance().setData(QueryConfig.LASTTIME,
							Rms.getLoginDate(context));
					QueryConfig.getInstance().setData(QueryConfig.ISALL, "1");
				}
				SyncData.Query(QueryConfig.getInstance(), handler, activity);
			}
		} catch (Exception e) {
			Tool.showErrMsg(context, e.getMessage());
		}
	}

	private void initHandler() {
		handler = new Handler() {
			// @Override
			public void handleMessage(Message msg) {
				Tool.stopProgress();
				if (msg != null && msg.obj != null) {
					switch (msg.what) {
					case Constants.PropertyKey.ERR:
						String errMsg = msg.obj.toString();
						Tool.showErrMsg(context, errMsg);
						break;

					case Constants.PropertyKey.LOGIN:
						if (msg.obj instanceof QueryDataResult) {
							loginResult = (QueryDataResult) msg.obj;
							if (loginResult.getInt(QueryDataResult.SUCCESS) == QueryDataResult.STATUS_OK) {

								Rms.setUserId(context, loginResult
										.getString(QueryDataResult.USERID));

								Rms.setUserName(context, Tool.getString(eName));
								Rms.setPwd(context, Tool.getString(ePwd));
								Rms.setSavePwd(context, checkBox.isChecked());

								checkLogin(loginResult);
							} else if (loginResult
									.getInt(QueryDataResult.SUCCESS) == QueryDataResult.STATUS_UPDATE) {

//								downLoadUrl = loginResult
//										.getString(QueryDataResult.DOWNLOADURL);
//								Tool.showErrMsg(context, "请先升级");
								
								showUpdateDialog(loginResult
										.getString(QueryDataResult.DOWNLOADURL));
							} else
								Tool.showErrMsg(context, "用户名或密码错");
						} else
							Tool.showErrMsg(context, "网络异常");
						break;

					case Constants.PropertyKey.QUERY:
						if (msg.obj instanceof QueryDataResult) {
							QueryDataResult result = (QueryDataResult) msg.obj;
							if (result.getInt(QueryDataResult.SUCCESS) == QueryDataResult.STATUS_OK) {

								if (result.getInt(QueryDataResult.DONE) != QueryDataResult.STATUS_DONE) {
									QueryConfig
											.getInstance()
											.setData(
													QueryConfig.STARTROW,
													result.getString(QueryDataResult.NEETSTARTROW));
									SyncData.Query(QueryConfig.getInstance(),
											handler, activity);
								} else {
									index++;
									if (index < loginResult.size()) {
										Tool.showProgress(context, loginResult
												.getMenthodInfo(index));
										QueryConfig.getInstance().setMethod(
												loginResult.getMenthod(index));
										QueryConfig.getInstance().setData(
												QueryConfig.STARTROW, "0");
										SyncData.Query(
												QueryConfig.getInstance(),
												handler, activity);
									} else {
										Rms.setFirst(context, false);
										Rms.setLoginDate(context,
												Tool.getCurrDate());
										toMenu();
									}
								}
							} else {
								Tool.showToastMsg(context, result
										.getString(QueryDataResult.ERRMESSAGE),
										AlertType.ERR);
							}
						} else
							Tool.showToastMsg(context, "更新数据异常", AlertType.ERR);
						break;

					default:
						super.handleMessage(msg);
						break;
					}
				} else
					Tool.showErrMsg(context, "网络异常");
			}
		};
	}

	// @Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch (actionId) {
		case EditorInfo.IME_ACTION_DONE:
			if (v.getId() == R.id.edt_pwd) {
				Login();
			}
			break;
		}
		return true;
	}

	private String checklog() {
		if (Tool.isEmpty(eName))
			return "请输入用户名";
		else if (Tool.isEmpty(ePwd))
			return "请输入密码";
		else if (!Tool.isConnect(context))
			return "网络连接失败，请确认网络连接！";
		// else if (!Tool.isSameUser(context, Tool.getString(eName))) {
		// return "此用户和之前登陆用户不符，不能登陆！";
		// }
		return "";
	}

	private void Login() {
		try {

			String err = checklog();
			if (err.equals("")) {
				// Tool.showProgress(context, "正在校验手机时间");
				//
				// SyncData.GetServerTime(handler, activity);

				Tool.showProgress(context, "正在验证用户名密码");
				sName = Tool.getString(eName);
				sPwd = Tool.getString(ePwd);
				index = 0;

				Rms.setUserName(context, sName);
				Rms.setPwd(context, sPwd);
				Rms.setSavePwd(context, checkBox.isChecked());

				LoginConfig.getInstance().setMethod(LoginConfig.LOGINMETHOD);
				LoginConfig.getInstance().setData(LoginConfig.USERNAME, sName);
				LoginConfig.getInstance().setData(LoginConfig.PWD, sPwd);
				LoginConfig.getInstance().setData(LoginConfig.APPVERSION,
						Constants.CurVersion.VERSION);
				// LoginConfig.getInstance().setData(LoginConfig.DEVICETYPE,
				// "android");
				// LoginConfig.getInstance().setData(LoginConfig.IMEI, "ada");
				// if (Rms.getFirst(context))
				// LoginConfig.getInstance().setData(LoginConfig.ISINITMOBILE,
				// 0);
				// else
				// LoginConfig.getInstance().setData(LoginConfig.ISINITMOBILE,
				// 1);
				// LoginConfig.getInstance().setData(LoginConfig.LASTTIME,
				// Rms.getLoginDate(context));
				SyncData.Login(LoginConfig.getInstance(), handler, activity);

			} else
				Tool.showErrMsg(context, err);
		} catch (Exception e) {
			Tool.showErrMsg(context, e.getMessage());
		}
	}

//	private void clearData() {
//		List<String> list = new ArrayList<String>();
//
//		for (DBMainConfig db : DBConfig.getTableList()) {
//			list.add("delete from " + db.getTableName());
//		}
//		Sqlite.execSqlList(context, list);
//		Rms.setFirst(context, true);
//		Rms.setUserId(context, "");
//	}

	private void toMenu() {

		Intent i = new Intent(Frm_Login.this, Frm_MainMenu.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Frm_Login.this.startActivity(i);
		// overridePendingTransition(android.R.anim.slide_in_left,
		// android.R.anim.slide_out_right);
		this.finish();
	}

	// @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showExitDialog();
			// startService(new Intent(this, MyService.class));
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showExitDialog() {
		Builder dialog = new Builder(context);
		dialog.setTitle("提示");
		dialog.setMessage("确认退出？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			// @Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
				android.os.Process.killProcess(android.os.Process.myPid());
				// finish();
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.show();
	}

	public void showUpdateDialog(final String downloadUrl) {
		Builder mDialog = new Builder(context);
		mDialog.setTitle("提示");
		mDialog.setMessage(Constants.CurVersion.UPDATEMSG);
		mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				/* User clicked OK so do some stuff */
				dialog.dismiss();
				downFile(downloadUrl);

			}
		});
		mDialog.show();
	}

	void downFile(final String url) {
		Tool.showProgress(context, "正在升级，请稍候");
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						// File file = new File("/sdcard/android/kowa.apk");

						File file = null;
						file = new File(
								Environment.getExternalStorageDirectory(),
								Constants.CurVersion.APPNAME);

						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						// int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							// count += ch;
							if (length > 0) {
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	void down() {
		handler.post(new Runnable() {
			public void run() {
				Tool.stopProgress();
				update();
			}
		});
	}

	void update() {

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), Constants.CurVersion.APPNAME)),
				"application/vnd.android.package-archive");

		startActivity(intent);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			Login();
			break;

		default:
			break;
		}
	}

	private void initScreen() {
		Tool.getScreen(activity);
	}

	public Context getContext() {
		return context;
	}

	public Handler getHandler() {
		return handler;
	}

	public void initActivity() {
		this.activity = Frm_Login.this;
	}

	public void initContext() {
		this.context = Frm_Login.this;
	}

}
