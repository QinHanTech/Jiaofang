package markettracker.ui;

//import java.util.ArrayList;
import com.rh.fang.zs.R;

import java.util.ArrayList;
import markettracker.util.Constants.AlertType;
import markettracker.util.Constants;
import markettracker.util.HuListAdapter;
import markettracker.util.SyncData;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import markettracker.data.Fields;
import markettracker.data.QueryConfig;
import markettracker.data.QueryDataResult;
import markettracker.data.Rms;
import markettracker.data.RowData;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
@SuppressLint("HandlerLeak")
public class Frm_Hulist extends Activity implements OnClickListener {

	private ListView huList;
	private Context context;

	private Activity activity;
	private Handler handler;
	private Button exit;

	private TextView title;
	private Fields fromFileds;
	private RowData selectData;

	private HuListAdapter huListAdapter;

	// private LinearLayout.LayoutParams layoutParams = new
	// LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	// LayoutParams.WRAP_CONTENT);

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_hulist);

		init();
		Tool.setAutoTime(context);
	}

	@Override
	protected void onRestart() {
		if (!Rms.getLoginDate(context).equals(Tool.getMoblieDate())) {
			// showTimeoutDialog();
		}
		Tool.setAutoTime(context);
		super.onRestart();
	}

	// @Override
	// protected void onStart() {
	// // if (!Rms.getLoginDate(context).equals(Tool.getMoblieDate())) {
	// // showTimeoutDialog();
	// // } else
	// // Tool.startMyTime();
	//
	//
	//
	// Tool.showToastMsg(context, "111111111111", AlertType.ERR);
	// super.onStart();
	// }

//	private void showTimeoutDialog() {
//		Builder dialog = new Builder(context);
//		dialog.setTitle("警告");
//		dialog.setMessage("登录超时,请重新登录！");
//		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//			// @Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				exit();
//			}
//		});
//		dialog.setCancelable(false);
//		dialog.show();
//	}

	private void exit() {
		setResult(1000);
		this.finish();
	}

	private void initScreen() {
		Tool.getScreen(activity);
	}

	private void init() {
		initContext();
		initActivity();
		initScreen();
		initHandler();
		fromFileds = ((SyncDataApp) getApplication()).getCurFields();

		initTitle();
		huList = (ListView) findViewById(R.id.main_menu_list);
		exit = (Button) findViewById(R.id.exit);
		exit.setOnClickListener(this);

		queryHulist();
		// initGrid();

	}

	private void queryHulist() {
		Tool.showProgress(context, "查询楼栋信息");
		QueryConfig.getNewInstance().setMethod("getHuLoucengs");
		QueryConfig.getInstance().setBack(true);
		QueryConfig.getInstance().setData("id",
				fromFileds.getStrValue("serverid"));

		SyncData.Query(QueryConfig.getInstance(), handler, Frm_Hulist.this);
	}

	private void initTitle() {
		title = (TextView) findViewById(R.id.title);
		title.setOnClickListener(this);
		title.setText(fromFileds.getStrValue("ldname"));
	}

	private void initList(ArrayList<RowData> list) {

		// List<Louceng> list = Sqlite.getLouceng(context,
		// fromFileds.getStrValue("serverid"));
		huListAdapter = new HuListAdapter(context, list, getOnClickListener());
		huList.setAdapter(huListAdapter);
		// huList.setOnItemClickListener(this);
	}

	private OnClickListener getOnClickListener() {
		OnClickListener listener = new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectData = (RowData) v.getTag();
				((SyncDataApp) getApplication()).setCurHuFields(selectData);

				if (!selectData.getString("jfzt").equals("-1")) {
					if (selectData.getString("jfzt").equals("1")
							|| selectData.getString("jfzt").equals("2"))
						toQuestionList();
					else
						toMenuList();
				} else {
					Tool.showToastMsg(context, "还未完成分户验收,不能开始业主交付",
							AlertType.INFO);
				}
			}
		};
		return listener;
	}

	private void toQuestionList() {
		Intent i = new Intent(context, Frm_HDList.class);

		i.putExtra("teminalCode", selectData.getString("serverid"));
		startActivityForResult(i, 1000);
	}

	private void toMenuList() {

		Tool.showProgress(context, "正在开始");
		QueryConfig.getNewInstance().setMethod("setjf");
		QueryConfig.getInstance().setBack(true);
		QueryConfig.getInstance().setData("type", "-1");
		QueryConfig.getInstance().setData("huid",
				selectData.getString("serverid"));
		QueryConfig.getInstance().setData("userid", Rms.getUserId(context));

		SyncData.Query(QueryConfig.getInstance(), handler, Frm_Hulist.this);

		// Intent intent = new Intent(context, Frm_Menu.class);
		// startActivityForResult(intent, 6);
	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.exit:

			setResult(-100);
			this.finish();

			break;

		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// linearLayout.startAnimation(Tool.getRightAnimation(context));

		queryHulist();
	}

	// @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
		}
		return false;//

	}

	private void initHandler() {
		handler = new Handler() {
			// @Override
			public void handleMessage(Message msg) {

				String errMsg = "";
				if (msg != null && msg.obj != null) {
					switch (msg.what) {
					case Constants.PropertyKey.ERR:
						Tool.stopProgress();
						errMsg = msg.obj.toString();
						Tool.showErrMsg(context, errMsg);
						break;
					// case Constants.PropertyKey.SYNCERR:
					// errMsg = msg.obj.toString();
					// Tool.showToastMsg(context, "数据上传失败", 0);
					// break;

					case Constants.PropertyKey.QUERY:
						Tool.stopProgress();
						if (msg.obj instanceof QueryDataResult) {
							QueryDataResult result = (QueryDataResult) msg.obj;
							if (result.getInt(QueryDataResult.SUCCESS) == QueryDataResult.STATUS_OK) {

								if (QueryConfig.getInstance().getMethod()
										.equals("setjf"))
									toQuestionList();
								else
									initList(result.getDataList());

							} else {
								Tool.showToastMsg(context, result
										.getString(QueryDataResult.ERRMESSAGE),
										AlertType.ERR);
							}
						}
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

	// private void toCall(String key) {
	// Intent intent = new Intent(getContext(), Frm_Menu.class);
	// intent.putExtra("teminalCode", selectData.getStrValue("serverid"));
	// intent.putExtra("name", selectData.getStrValue("huname"));
	// intent.putExtra("type", selectData.getStrValue("outlettype"));
	// intent.putExtra("key", key);
	// // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// startActivityForResult(intent, 1000);
	// }

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_Hulist.this;
	}

	public Context getContext() {
		return context;
	}

	public void initContext() {
		this.context = Frm_Hulist.this;
	}

}
