package markettracker.ui;

import com.rh.fang.jf.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import markettracker.util.CButton;
import markettracker.util.CTable;
import markettracker.util.CTextView; //import markettracker.util.SyncData;
import markettracker.util.SyncDataApp;
import markettracker.util.TemplateFactory;
import markettracker.util.Tool;
import markettracker.util.Constants.PhotoType;
import markettracker.util.Constants.AlertType;
import markettracker.data.ButtonConfig;
import markettracker.data.Fields;
import markettracker.data.Rms;
import markettracker.data.RowData;
import markettracker.data.SObject;
import markettracker.data.Sqlite;
import markettracker.data.TemGroupList;
import markettracker.data.Template;
import markettracker.data.TemplateGroup;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("SdCardPath")
public class Frm_Menu extends Activity implements OnClickListener {

	private Context context;
	private Activity activity;
	private TemGroupList temGroupList;
	private LinearLayout mainLine;
	private Button exit, start, leave;
	private ImageView startPhoto, leavePhoto;
	private LinearLayout lineButton;
	private List<ButtonConfig> buttonlist;
	private ScrollView mainView;
	private TextView title;
	private boolean startCall = false, endCall = false;
	private static final int TAKE_PICTURE_START = 3021,
			TAKE_PICTURE_LEAVE = 3022, SURVEY = 3023;

	private Fields comleteRpt = new Fields();

	private RowData mCurHuFields;
	private CTextView selectTView;
	private String clientId, type, key;
	// private Handler handler;
	private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rpt_form);
		init();
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
//
//		dialog.setCancelable(false);
//		dialog.show();
//	}

//	private void exit() {
//		setResult(-100);
//		this.finish();
//	}

	private void init() {
		initContext();
		initActivity();
		// initHandler();

		mCurHuFields = ((SyncDataApp) getApplication()).getCurHuFields();
		clientId = mCurHuFields.getString("serverid");
		type = "1";

		// key = this.getIntent().getStringExtra("key");
		key = "";
		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(this);

		start = (Button) findViewById(R.id.start);

		leave = (Button) findViewById(R.id.leave);

		title = (TextView) findViewById(R.id.title);
		title.setText(mCurHuFields.getString("huname"));

		startPhoto = (ImageView) findViewById(R.id.startphoto);
		startPhoto.setImageBitmap(getBitmap(PhotoType.CHECKIN));

		leavePhoto = (ImageView) findViewById(R.id.leavephoto);
		leavePhoto.setImageBitmap(getBitmap(PhotoType.CHECKOUT));
		initTemGroupList();
		initPage();

	}

	private Bitmap getBitmap(PhotoType type) {
		SObject report = getRpt(type);
		try {
			Fields data = report.getAttfields().getFields(0);
			byte[] bytes = data.getPhoto();
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
					bytes.length);
			if (type == PhotoType.CHECKIN)
				startCall = true;
			else
				endCall = true;
			return bitmap;
		} catch (Exception ex) {
			if (type == PhotoType.CHECKIN)
				start.setOnClickListener(this);
			else
				leave.setOnClickListener(this);
			return Tool.getBimap(context, R.drawable.camera1);
		}
	}

	private void initPage() {
		initMainSView();
		setupButton();
	}

	private void initMainSView() {
		if (mainView == null) {
			mainView = (ScrollView) findViewById(R.id.sv_content);
			mainView.addView(getMainLine());
			// mainView.setAnimation(Tool.getAnimation(context));
		}
	}

	private void initTemGroupList() {
		temGroupList = TemplateFactory.getTemplateGroupList(context);
	}

	// 获取描述信息
	private LinearLayout getMainLine() {
		if (mainLine == null) {
			mainLine = new LinearLayout(context);
			// mainLine.setPadding(20, 0, 20, 20);
			mainLine.setOrientation(LinearLayout.VERTICAL);
		}

		// List<String> list = Sqlite.getTemplateIdList(context, clientId);
		List<String> list = new ArrayList<String>();
		list = Sqlite.getTemplateIdList(context, clientId, key);
		for (String template : list)
			comleteRpt.put(template, "true");

		if (temGroupList != null && temGroupList.getTempGroupList() != null) {
			for (TemplateGroup group : temGroupList.getTempGroupList()) {
				mainLine.addView(new CTable(context, group, list,
						getOnClickListener()));
			}
		}

		return mainLine;
	}

	private OnClickListener getOnClickListener() {
		OnClickListener listener = new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				toRpt(v);
			}
		};
		return listener;
	}

	private void initButton() {
		if (buttonlist == null)
			buttonlist = Tool.getMenuButton();
	}

	private void initButtonLine() {
		if (lineButton == null)
			lineButton = (LinearLayout) findViewById(R.id.line_buttom);
		else
			lineButton.removeAllViews();
	}

	private void setupButton() {
		initButton();
		initButtonLine();
		int count = buttonlist.size();
		if (buttonlist != null && count > 0) {
			layoutParams.width = Tool.getBWidth(activity, count);

			for (int i = 0; i < count; i++)
				lineButton.addView(getRptButton(buttonlist.get(i)));
		}
	}

	private CButton getRptButton(ButtonConfig config) {
		CButton button = new CButton(context, config, layoutParams, null, this);
		// button.setOnClickListener(this);
		return button;
	}

	// public void OpenCarmer(int type)
	// {
	// try
	// {
	// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//
	// "android.media.action.IMAGE_CAPTURE"
	// startActivityForResult(intent, type);
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public void OpenCarmer(int type) {
		try {
			Tool.createPhotoFile();
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File("/sdcard/inoherb/photo//test.JPEG")));
			startActivityForResult(intent, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onClick(View v) {
		resetbutton(v.getId());

		switch (v.getId()) {

		case 3:
			// Intent intent = new Intent(context, Frm_ChebliePhoto.class);
			// startActivityForResult(intent, v.getId());
			// Intent intent = new Intent(context, Frm_DPList.class);
			// // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivityForResult(intent, 10);
			break;

		case R.id.back:
			finishActivity();
			break;
		case R.id.start:
			OpenCarmer(TAKE_PICTURE_START);
			break;
		case R.id.leave:
			if (startCall) {
				OpenCarmer(TAKE_PICTURE_LEAVE);
			} else
				Tool.showToastMsg(context, "请先拍摄进店照片！", AlertType.ERR);
			break;
		default:
//			toRpt(v);
			break;
		}
	}

	private void saveRpt(String type) {
		Template template = TemplateFactory.getTemplate(type);
		SObject report = Sqlite.getReport(context, template, clientId, 1, key);
		report.setField("ClientType", this.type);
		Sqlite.saveReport(context, report);
	}

	private void toRpt(View v) {
		if (v instanceof CTextView) {
			if (startCall) {
				selectTView = (CTextView) v;

				if (selectTView.getInputType() == 1) {
					if (!selectTView.isComplete()) {
						if (selectTView.getTemplateType().equals("3002")) {
							if (comleteRpt.isTrue("3001"))
								Tool.showToastMsg(context, "人员信息不能重复填写",
										AlertType.ERR);
							else
								changeRptStatus();
						} else if (selectTView.getTemplateType().equals("2003")) {
							if (comleteRpt.isTrue("2001")
									|| comleteRpt.isTrue("2002"))
								Tool.showToastMsg(context, "活动信息不能重复填写",
										AlertType.ERR);
							else
								changeRptStatus();
						} else if (selectTView.getTemplateType().equals("4001")) {
							if (comleteRpt.isTrue("8"))
								Tool.showToastMsg(context, "价格异动不能重复填写",
										AlertType.ERR);
							else
								changeRptStatus();
						}

					} else
						changeRptStatus();
				} else {
					if (selectTView.getTemplateType().equals("3001")) {
						if (comleteRpt.isTrue("3002"))
							Tool.showToastMsg(context, "人员信息不能重复填写",
									AlertType.ERR);
						else
							toRptClass();
					} else if (selectTView.getTemplateType().equals("2001")
							|| selectTView.getTemplateType().equals("2002")) {
						if (comleteRpt.isTrue("2003"))
							Tool.showToastMsg(context, "活动信息不能重复填写",
									AlertType.ERR);
						else
							toRptClass();
					} else if (selectTView.getTemplateType().equals("8")) {
						if (comleteRpt.isTrue("4001"))
							Tool.showToastMsg(context, "价格异动不能重复填写",
									AlertType.ERR);
						else
							toRptClass();
					} else
						toRptClass();

				}
			} else
				Tool.showToastMsg(context, "请先拍照,再填写拜访内容", AlertType.ERR);
		}
	}

	private void changeRptStatus() {
		selectTView.changeStatus();
		if (selectTView.isComplete()) {
			comleteRpt.put(selectTView.getOnlyType(), "true");
			saveRpt(selectTView.getTemplateType());
		} else {
			comleteRpt.put(selectTView.getOnlyType(), "false");
			String sql = "DELETE FROM t_data_callreport where onlyType='"
					+ selectTView.getTemplateType() + "' and ReportDate='"
					+ Tool.getCurrDate() + "' and clientId='" + clientId + "'";
			Sqlite.execSingleSql(context, sql);
		}

	}

	private void toRptClass() {
		 if (selectTView.getTemplateType().equals("10000"))
		 {
		 Intent i = new Intent(context, Frm_HDList.class);
		
		 i.putExtra("teminalCode", clientId);
		 startActivityForResult(i, 1000);
		 }
		 else
		 {
		Intent i = new Intent(getContext(), Frm_SubmitRpt.class);
		i.putExtra("type", selectTView.getTemplateType());
		i.putExtra("name", selectTView.getTemplateName());
//		i.putExtra("teminalCode", clientId);
//		i.putExtra("terminalname", mCurHuFields.getStrValue("huname"));
//		i.putExtra("clienttype", type);
//
//		i.putExtra("key", key);
		startActivityForResult(i, 1000);
		 }
	}

	private SObject getRpt(PhotoType type) {
		Template template;
		if (type == PhotoType.CHECKIN)
			template = TemplateFactory.getStartTemplate();
		else
			template = TemplateFactory.getLeaveTemplate();
		SObject rpt;
		if (!this.type.equals("2")) {
			rpt = Sqlite.getReport(context, template, this.getIntent()
					.getStringExtra("teminalCode"), 1, key);
		} else
			rpt = Sqlite.getReport(context, template, this.getIntent()
					.getStringExtra("teminalCode"), 2, key);
		rpt.setField("ClientType", this.type);
		return rpt;
	}

	private void savePhoto(PhotoType type, Fields photo) {
		SObject rpt = getRpt(type);
		rpt.resetAttfield(photo);
		Sqlite.saveReport(context, rpt);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		// mainView.setAnimation(Tool.getAnimation(context));
		String strErrMsg = "";
		try {
			if (resultCode == RESULT_OK) {
				if (requestCode == TAKE_PICTURE_START) {
					final Fields photo = new Fields();
					// 1 final Bitmap bm = (Bitmap)
					// data.getExtras().get("data");
					final Bitmap bm = Tool.lessenUriImage();

					String date = Tool.getCurrPhotoTime();
					Bitmap bm1 = Tool.generatorContactCountIcon(bm, date, this
							.getIntent().getStringExtra("name"), context);

					photo.setShotTime(date);
					photo.setPhoto(Tool.Bitmap2Bytes(bm1));

					savePhoto(PhotoType.CHECKIN, photo);
					startPhoto.setImageBitmap(bm1);
					startCall = true;
				} else if (requestCode == TAKE_PICTURE_LEAVE) {
					final Fields photo = new Fields();
					// final Bitmap bm = (Bitmap) data.getExtras().get("data");

					final Bitmap bm = Tool.lessenUriImage();

					String date = Tool.getCurrPhotoTime();
					Bitmap bm1 = Tool.generatorContactCountIcon(bm, date, this
							.getIntent().getStringExtra("name"), context);

					photo.setShotTime(date);
					photo.setPhoto(Tool.Bitmap2Bytes(bm1));
					savePhoto(PhotoType.CHECKOUT, photo);
					leavePhoto.setImageBitmap(bm1);
					endCall = true;
				} else if (requestCode == SURVEY) {

				} else if (requestCode == 10) {

				} else
					changeStatus();
			}
		} catch (Exception e) {
			Tool.showToastMsg(context, "拍照错误" + strErrMsg + e.getMessage(),
					AlertType.ERR);

		}
		// super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivity();
		}
		return false;
	}

	private void changeStatus() {
		selectTView.change2Complete();
		if (selectTView.isComplete())
			comleteRpt.put(selectTView.getOnlyType(), "true");
		// for (TemplateGroup group : temGroupList.getTempGroupList()) {
		// for (Template t : group.getTemplateList()) {
		// if (t.getType().equals(type)) {
		// t.setComplete(true);
		// return;
		// }
		// }
		// }
	}

	private void resetbutton(int id) {
		CButton b;
		for (int i = 0; i < lineButton.getChildCount(); i++) {
			b = ((CButton) lineButton.getChildAt(i));
			if (b.getId() == id) {

				// b.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_bg_click));
				b.setCompoundDrawablesWithIntrinsicBounds(
						null,
						Tool.getDrawable(context, b.getText().toString(), true),
						null, null);
				b.stopFlick(b);
			} else {
				// b.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_bg));
				b.setCompoundDrawablesWithIntrinsicBounds(null, Tool
						.getDrawable(context, b.getText().toString(), false),
						null, null);
			}
		}
	}

	private boolean checkData() {
		boolean bok = false;
		boolean bok1 = false;
		boolean bok2 = false;
		boolean bok3 = false;
		for (TemplateGroup group : temGroupList.getTempGroupList()) {
			for (Template t : group.getTemplateList()) {
				if (t.isMustComplete()) {
					if (!comleteRpt.isTrue(t.getOnlyType() + "")) {
						Tool.showToastMsg(context, t.getName() + "必须填写",
								AlertType.ERR);
						return false;
					}
				} else if (t.getOnlyType() >= 2001 && t.getOnlyType() <= 2003) {
					if (!bok)
						bok = comleteRpt.isTrue(t.getOnlyType() + "");
				} else if (t.getOnlyType() >= 3001 && t.getOnlyType() <= 3002) {
					if (!bok1)
						bok1 = comleteRpt.isTrue(t.getOnlyType() + "");
				} else if (t.getOnlyType() >= 999 && t.getOnlyType() <= 1999) {
					if (!bok2)
						bok2 = comleteRpt.isTrue(t.getOnlyType() + "");
				} else if (t.getOnlyType() == 8 || t.getOnlyType() == 4001) {
					if (!bok3)
						bok3 = comleteRpt.isTrue(t.getOnlyType() + "");
				}
			}
		}
		if (!this.getIntent().getStringExtra("type").equals("2")) {
			// if (!bok)
			// {
			// Tool.showToastMsg(context, "活动内容" + "必须填写", AlertType.ERR);
			// return false;
			// }
			if (!bok1) {
				Tool.showToastMsg(context, "人员信息" + "必须填写", AlertType.ERR);
				return false;
			} else if (!bok2) {
				Tool.showToastMsg(context, "陈列报告" + "必须填写", AlertType.ERR);
				return false;
			} else if (!bok3) {
				Tool.showToastMsg(context, "价格异动" + "必须填写", AlertType.ERR);
				return false;
			}
		}
		return true;
	}

	private void finishActivity() {
		if (startCall) {
			if (checkData()) {
				if (endCall) {
					setResult(RESULT_OK);
					this.finish();
				} else {
					Tool.showToastMsg(context, "离店前,请拍摄收银台照片", AlertType.ERR);
					OpenCarmer(TAKE_PICTURE_LEAVE);
				}
			}
		} else {
			setResult(RESULT_OK);
			this.finish();
		}
	}

	public Context getContext() {
		return context;
	}

	public void initContext() {
		this.context = Frm_Menu.this;
	}

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_Menu.this;
	}

}
