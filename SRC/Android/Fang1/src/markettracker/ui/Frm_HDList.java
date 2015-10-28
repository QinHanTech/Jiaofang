package markettracker.ui;

import java.util.List;
import com.rh.fang.zs.R;
import markettracker.util.Constants.AlertType;
import markettracker.util.Constants;
import markettracker.util.QuestionListAdapter;
import markettracker.util.SyncData;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import markettracker.data.QueryConfig;
import markettracker.data.QueryDataResult;
import markettracker.data.Rms;
import markettracker.data.RowData;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

@SuppressLint("HandlerLeak")
public class Frm_HDList extends Activity implements OnClickListener,
		OnItemClickListener {

	private Context context;
	private Button exit, add;
	private Handler mHandler;

	private RowData selectData;
	private QuestionListAdapter mQuestionListAdapter;
	private ListView mQuestionListView;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hd_list);
		initHandler();
		init();
	}

	private void init() {
		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(this);

		add = (Button) findViewById(R.id.addhoc);
		add.setOnClickListener(this);

//		if (((SyncDataApp) getApplication()).getCurHuFields().getString("jfzt")
//				.equals("1")
//				|| ((SyncDataApp) getApplication()).getCurHuFields()
//						.getString("jfzt").equals("2")) {
//			add.setVisibility(View.GONE);
//		}
		context = Frm_HDList.this;

		queryQuestion();

		// getProductLine();
	}

	private void queryQuestion() {
		Tool.showProgress(context, "查询问题信息");
		QueryConfig.getNewInstance().setMethod("wtlist");
		QueryConfig.getInstance().setData(
				"id",
				((SyncDataApp) getApplication()).getCurHuFields().getString(
						"serverid"));

		SyncData.Query(QueryConfig.getInstance(), mHandler, Frm_HDList.this);
	}

	private void finishActivity() {
		setResult(RESULT_OK);
		this.finish();
	}

	private void initList(List<RowData> list) {
		mQuestionListView = (ListView) findViewById(R.id.question_list);

		mQuestionListAdapter = new QuestionListAdapter(context, list);
		mQuestionListView.setAdapter(mQuestionListAdapter);
		mQuestionListView.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// showInfo();
		selectData = mQuestionListAdapter.getRowData(arg2);

		((SyncDataApp) getApplication()).setCurQuestionFields(selectData);
		toRpt2();
	}

	// private ShowInfoBuilder showInfoBuild;
	//
	// private void showInfo()
	// {
	// if (showInfoBuild != null)
	// {
	// showInfoBuild.dismiss();
	// showInfoBuild = null;
	// }
	// showInfoBuild = new ShowInfoBuilder(context, getOnClickListener(),
	// selectData.getStrValue("promotername"));
	// // restpwdAlertDialog = msg.create();
	// // restpwdAlertDialog.show();
	// }

	public void onClick(View v) {
		System.out.println(v.getId());

		switch (v.getId()) {
		case R.id.back:

			Tool.showProgress(context, "正在结束");
			QueryConfig.getNewInstance().setMethod("setjf");
			QueryConfig.getInstance().setBack(true);
			QueryConfig.getInstance().setData("type", "-2");
			QueryConfig.getInstance().setData(
					"huid",
					((SyncDataApp) getApplication()).getCurHuFields()
							.getString("serverid"));
			QueryConfig.getInstance().setData("userid", Rms.getUserId(context));

			SyncData.Query(QueryConfig.getInstance(), mHandler, Frm_HDList.this);

			// finishActivity();
			break;

		case R.id.addhoc:
			toRpt();
			break;
		}

	}

	private void toRpt() {
		Intent i = new Intent(context, Frm_SubmitRpt.class);
		i.putExtra("type", "1");
		i.putExtra("name", "新增问题");
		i.putExtra("teminalCode", "-1");

		// i.putExtra(Constants.MOBILEKEY, Tool.getMyUUID());
		i.putExtra("PromoterId", "");
		i.putExtra("clienttype", "1");

		Frm_HDList.this.startActivityForResult(i, 1000);
	}

	private void toRpt2() {

		Intent i = new Intent(context, Frm_SubmitRpt.class);
		i.putExtra("type", "2");
		i.putExtra("name", "整改确认");
		i.putExtra("teminalCode", "-1");

		// i.putExtra(Constants.MOBILEKEY, Tool.getMyUUID());
		i.putExtra("PromoterId", "");
		i.putExtra("clienttype", "1");

		Frm_HDList.this.startActivityForResult(i, 1000);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode == RESULT_OK) {
		
		super.onActivityResult(requestCode, resultCode, data);
			// resetGrid();
			
			queryQuestion();
//		}

		// super.onActivityResult(requestCode, resultCode, data);
	}

	private void initHandler() {
		mHandler = new Handler() {
			// @Override
			public void handleMessage(Message msg) {
				// String strMsg = msg.obj.toString();
				Tool.stopProgress();
				switch (msg.what) {

				case Constants.PropertyKey.QUERY:
					if (msg.obj instanceof QueryDataResult) {
						QueryDataResult result = (QueryDataResult) msg.obj;
						if (result.getInt(QueryDataResult.SUCCESS) == QueryDataResult.STATUS_OK) {

							if (QueryConfig.getInstance().getMethod()
									.equals("setjf"))
								finishActivity();
							else
								initList(result.getDataList());

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
			}
		};
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// sendRpt();

			finishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// private AlertDialog mMsgAlertDialog;

	// private void showMsgDetail(Fields field)
	// {
	// if (mMsgAlertDialog != null)
	// {
	// mMsgAlertDialog.dismiss();
	// mMsgAlertDialog = null;
	// }
	// MsgDetailBuilder msg = new MsgDetailBuilder(context, mHandler, field);
	// mMsgAlertDialog = msg.create();
	// mMsgAlertDialog.show();
	// }

	@Override
	protected void onResume() {
		Tool.setAutoTime(context);
		if (!Rms.getLoginDate(context).equals(Tool.getCurrDate())) {
			showTimeoutDialog();
		}
		super.onResume();
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
