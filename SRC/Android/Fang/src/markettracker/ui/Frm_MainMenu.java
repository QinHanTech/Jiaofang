package markettracker.ui;

//import java.util.ArrayList;
import com.rh.fang.jf.R;
import markettracker.util.LoudongListAdapter;
import markettracker.util.SyncDataApp;
import markettracker.util.Tool;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Rms;
import markettracker.data.Sqlite;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("HandlerLeak")
public class Frm_MainMenu extends Activity implements OnClickListener,
		OnItemClickListener {

	private ListView loudongList;
	private Context context;
	private Activity activity;
	private Button exit ,addhoc;
	private TextView title;
	private Fields selectData;

	private LoudongListAdapter loudongListAdapter;

	// private LinearLayout.LayoutParams layoutParams = new
	// LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	// LayoutParams.WRAP_CONTENT);

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_menu);

		init();
		Tool.setAutoTime(context);
	}

	@Override
	protected void onRestart() {
		if (!Rms.getLoginDate(context).equals(Tool.getMoblieDate())) {
//			showTimeoutDialog();
		}
		Tool.setAutoTime(context);
		super.onRestart();
	}

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
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private void initScreen() {
		Tool.getScreen(activity);
	}

	private void init() {
		initContext();
		initActivity();
		initScreen();
		// initHandler();
		initTitle();
		loudongList = (ListView) findViewById(R.id.main_menu_list);
		exit = (Button) findViewById(R.id.exit);
		exit.setOnClickListener(this);
		 addhoc = (Button) findViewById(R.id.addhoc);
		 addhoc.setOnClickListener(this);
		initGrid();

	}

	private void initTitle() {
		title = (TextView) findViewById(R.id.title);
		title.setOnClickListener(this);
		// title.setText("当日拜访");
	}

	private void initGrid() {

		FieldsList list = Sqlite.getFieldsListBySql(context,
				"select * from  jxc_loudong order by ldname");
		loudongListAdapter = new LoudongListAdapter(context, list);
		loudongList.setAdapter(loudongListAdapter);
		loudongList.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		// TODO Auto-generated method stub
		selectData = loudongListAdapter.getFields(index);
		((SyncDataApp) getApplication()).setCurFields(selectData);
		toHuList();
	}

	private void toHuList() {
		Intent intent = new Intent(context, Frm_Hulist.class);
		startActivityForResult(intent, 6);
	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.exit:
			showExitDialog();

			break;
			
		case R.id.addhoc:
			Intent intent = new Intent(context, Frm_ShowData.class);
			startActivity(intent);

			break;
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// linearLayout.startAnimation(Tool.getRightAnimation(context));
	}

	// @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showExitDialog();
		}
		return false;// super.onKeyDown(keyCode, event);
	}

	private void showExitDialog() {
		Builder dialog = new Builder(context);
		dialog.setTitle("提示");
		dialog.setMessage("确认退出？");

		dialog.setNeutralButton("退出", new DialogInterface.OnClickListener() {
			// @Override
			public void onClick(DialogInterface arg0, int arg1) {
				exit();
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.show();
	}

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_MainMenu.this;
	}

	public Context getContext() {
		return context;
	}

	public void initContext() {
		this.context = Frm_MainMenu.this;
	}

}
