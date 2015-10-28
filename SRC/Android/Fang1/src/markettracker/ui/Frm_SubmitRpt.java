package markettracker.ui;

import com.rh.fang.zs.R;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import markettracker.util.CButton;
import markettracker.util.CImageView;
import markettracker.util.Constants;
import markettracker.util.CustomGrid;
import markettracker.util.PhotoEditBuilder1;
import markettracker.util.RptTable;
import markettracker.util.SyncData;
import markettracker.util.SyncDataApp;
import markettracker.util.TemplateFactory;
import markettracker.util.Tool;
import markettracker.util.Constants.AlertType;
import markettracker.util.Constants.ButtonList;
import markettracker.data.ButtonConfig;
import markettracker.data.DicData;
import markettracker.data.Fields;
import markettracker.data.Panal;
import markettracker.data.QueryConfig;
import markettracker.data.QueryDataResult;
import markettracker.data.Rms;
import markettracker.data.RowData;
import markettracker.data.SObject;
import markettracker.data.Sqlite;
import markettracker.data.Template;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint({ "HandlerLeak", "UseSparseArrays", "SdCardPath" })
public class Frm_SubmitRpt extends Activity implements OnClickListener {

	private Context context;
	private Activity activity;
	private List<ButtonConfig> buttonlist;
	private LinearLayout lineButton;
	private LinearLayout mainLine, photoLine;
	private ScrollView mainSView;
	private CustomGrid customGrid;
	private LinearLayout productLine;
	private ScrollView photoSView;
	private CImageView[] imageView;
	// private CImageView clickImgView;
	private Button back, save;
	private AlertDialog backAlertDialog;
	// private View view;
	//
	// private NumericKeyboard keyboard;
	//
	// private Hashtable<String, Fields> hashtable;

	private Template template;
	private SObject report;
	private static final int TAKE_PICTURE = 3021;

	private TextView title;
	private Fields selectData;
	private Handler handler;
	// private String key;
	private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	private LinearLayout.LayoutParams layoutParamsPhoto = new LinearLayout.LayoutParams(
			LayoutParams.FILL_PARENT, Tool.getScreenHeight() / 2);

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rpt_frm);
		init();
	}

	private void init() {
		initContext();
		initActivity();
		initHandler();
		// key = this.getIntent().getStringExtra("key");
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		save = (Button) findViewById(R.id.save);

		save.setText("提交");
		save.setOnClickListener(this);
//		if (((SyncDataApp) getApplication()).getCurHuFields().getString("jfzt")
//				.equals("1")
//				|| ((SyncDataApp) getApplication()).getCurHuFields()
//						.getString("jfzt").equals("2")) {
//			save.setVisibility(View.GONE);
//		}
		initTitle();
		initReport();
		initPage();
	}

	private void initHandler() {
		handler = new Handler() {
			// @Override
			public void handleMessage(Message msg) {
				// String strMsg = msg.obj.toString();
				Tool.stopProgress();
				switch (msg.what) {

				// case Constants.PropertyKey.UPLOAD:
				// if (msg.obj instanceof UpsertResult) {
				// UpsertResult result = (UpsertResult) msg.obj;
				// if (result.isSuccess() == 1) {
				// report.setField("id", result.getRptId() + "");
				//
				// SaveData();
				// } else
				// Tool.showToastMsg(context, result.getErrorMsg(),
				// AlertType.ERR);
				// } else
				// Tool.showToastMsg(context, "网络异常", AlertType.ERR);
				// break;

				case Constants.CustomGridType.PHOTOEDIT:
					String name = report.getField("fenleinamename") + "("
							+ report.getField("dengjiname") + ")";
					if (template.getOnlyType() == 2) {
						name = report.getField("fenleiname") + "("
								+ report.getField("dengji") + ")";
					}

					Bitmap bm1 = Tool.generatorContactCountIcon(
							(Bitmap) msg.obj, currPhotoTime, name, context);
					boolean bHave = report.isHavePhoto(mIndex);
					imageView[mIndex].setImageBitmap(bm1, currPhotoTime, 0);//
					if (!bHave) {
						photoLine.addView(getRptPhoto(mTotalPhoto));
						mTotalPhoto++;
					}

					break;

				case Constants.PropertyKey.QUERY:
					if (msg.obj instanceof QueryDataResult) {

						QueryDataResult result = (QueryDataResult) msg.obj;
						if (result.isSuccess()) {
							finish(template.getOnlyType());
						} else {
							Tool.showErrMsg(context, "上传失败,请重试");
						}
					}
					break;

				default:
					super.handleMessage(msg);
					break;
				}
			}
		};
	}

	private void finish(int type) {
		Intent i = new Intent();
		// i.putExtra("type", type);
		setResult(type, i);
		this.finish();
	}

	// 设置title
	private void initTitle() {
		title = (TextView) findViewById(R.id.txt);
		title.setText(this.getIntent().getStringExtra("name"));
	}

	private void initReport() {
		template = TemplateFactory.getTemplate(this.getIntent().getStringExtra(
				"type"));

		if (template.getOnlyType() == 2)
			save.setText("完成");

		report = new SObject(template);// Sqlite.getDPReport(context, template,
										// "");
		if (template.getOnlyType() == 2) {
			RowData question = ((SyncDataApp) getApplication())
					.getCurQuestionFields();
			report.setField("wenti", question.getString("wenti"));

			report.setField("serverid", question.getString("serverid"));
			report.setField("fenleiname", question.getString("fenleiname"));
			report.setField("dengji", question.getString("dengji"));
			report.setField("beizhu", question.getString("beizhu"));
		} else {
			report.setField("dengji", "65");
			report.setField("dengjiname", "三类");
		}
		// report.setField("wenti", question.getString("wenti"));

		report.setField("ClientType", "1");

	}

	private void initPage() {
		initMainView();
		initPhotoLine();
		setupButton();
	}

	private void initButton() {
		if (buttonlist == null)

			buttonlist = Tool.getRptTab2();
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
		CButton button = new CButton(context, config, layoutParams, template,
				this);
		// button.setOnClickListener(this);
		return button;
	}

	private void initMainView() {
		if (template != null && template.havePanal()) {
			if (mainSView == null) {
				mainSView = (ScrollView) findViewById(R.id.sv_detail_bottom);
				// mainSView.addView(getMainLine());
				getMainLine();
				mainSView.setVisibility(View.VISIBLE);
				// mainSView.setAnimation(Tool.getAnimation(context));
			}
		}
	}

	// 获取描述信息
	private LinearLayout getMainLine() {
		if (mainLine == null) {
			mainLine = (LinearLayout) findViewById(R.id.line_main);
		}
		// RptTitle txt;

		for (Panal panal : template.getPanalList()) {
			mainLine.addView(new RptTable(context, panal, report, handler, this));
		}
		return mainLine;
	}

	private int mIndex;

	private OnTouchListener getOnTouchListener() {

		OnTouchListener listener = new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				// float x = event.getX();
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// startx = x;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					// endx = event.getX();
					// sx = (int) startx;
					// ex = (int) endx;
					// CDeleteButton button = ((CImage) v).getDelete();
					// boolean bHave = photo.get(v.getId()) == null ? false :
					// true;// report.isHavePhoto(100
					// +
					// v.getId());

					// if (Math.abs(sx - ex) > 10 && bHave) {
					// if (sx > ex) {
					//
					// // photoLine.removeViewAt(100 + v.getId());
					// button.setVisibility(View.VISIBLE);
					// // button.setAnimation(Tool.getAnimation(context));
					// } else {
					// button.setVisibility(View.GONE);
					// // button
					// // .setAnimation(Tool
					// // .getRightAnimation(context));
					// }
					// } else {
					// button.setVisibility(View.GONE);
					// button.setAnimation(Tool.getRightAnimation(context));
					mIndex = 100 + v.getId();

					// showStartPickPhoto();
					OpenCarmer();

					// }
				}
				return true;
			}
		};
		return listener;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, ButtonList.DELETE, 1, "删除");

		return true;
	}

	// private int syncType = 0;

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {

		case ButtonList.DELETE:
			DelData();
			break;

		}

		return true;
	}

	private void DelData() {
		// Sqlite.execSingleSql(context,
		// "delete from t_activity_main where PromoterId ='" +
		// this.getIntent().getStringExtra("PromoterId") + "'");
		Sqlite.execSingleSql(context,
				"delete from  t_client_rlt_activitypromoter  where PromoterId='"
						+ this.getIntent().getStringExtra("PromoterId") + "'");
		Tool.showToastMsg(context, "删除成功", AlertType.INFO);
		finish(RESULT_OK);

	}

	private OnClickListener getOnClickListener() {
		OnClickListener listener = new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				delView(v.getId());
			}
		};
		return listener;
	}

	private void delView(int id) {
		CImageView view;
		for (int i = 0; i < photoLine.getChildCount(); i++) {
			view = (CImageView) photoLine.getChildAt(i);
			if (view.getId() == id) {
				photoLine.removeViewAt(i);
				photo.remove(id);
				break;
			}
		}
	}

	private CImageView getRptPhoto(int index) {

		imageView[index] = new CImageView(context, template,
				getOnTouchListener(), photo, index, getOnClickListener());
		layoutParamsPhoto.height = Tool.dip2px(context, 200);
		layoutParamsPhoto.topMargin = 5;
		imageView[index].setLayoutParams(layoutParamsPhoto);
		return imageView[index];
	}

	public void OpenCarmer() {
		try {
			Tool.createPhotoFile();
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File("/sdcard/inoherb/photo//test.JPEG")));
			startActivityForResult(intent, TAKE_PICTURE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private AlertDialog startHocAlertDialog;

	// private void selectPhoto() {
	// Intent intent1 = new Intent(Intent.ACTION_PICK, null);
	// intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	// "image/*");
	//
	// // Intent intent1 = new Intent(Intent.ACTION_PICK, null);
	// intent1.setType("image/*");
	// startActivityForResult(intent1, 1111);
	// }

	// private void showStartPickPhoto() {
	// stopDialog(startHocAlertDialog);
	// Builder builder = new AlertDialog.Builder(context);
	// builder.setTitle("拍摄或者选择照片");
	// // builder.setMessage("确认开始计划外拜访  " + selectData.getStrValue("fullname")
	// // + "？");
	// builder.setIcon(android.R.drawable.ic_dialog_info);
	//
	// builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
	// // @Override
	// public void onClick(DialogInterface dialog, int which) {
	// // mIndex = 100 + v.getId();
	// OpenCarmer();
	// // stopDialog(hocListAlertDialog);
	// // toRpt(0);
	// // // Intent intent = new Intent(context, Frm_Menu.class);
	// // // intent.putExtra("teminalCode", selectData
	// // // .getStrValue("terminalcode"));
	// // // activity.startActivityForResult(intent, -100);
	// // //
	// //
	// overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	// }
	// });
	// builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
	// // @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// }
	// });
	//
	// builder.setNeutralButton("选择照片", new DialogInterface.OnClickListener() {
	// // @Override
	// public void onClick(DialogInterface dialog, int which) {
	// selectPhoto();
	// // stopDialog(hocListAlertDialog);
	// // toRpt(0);
	// // // Intent intent = new Intent(context, Frm_Menu.class);
	// // // intent.putExtra("teminalCode", selectData
	// // // .getStrValue("terminalcode"));
	// // // activity.startActivityForResult(intent, -100);
	// // //
	// //
	// overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	// }
	// });
	// // builder.create();
	// startHocAlertDialog = builder.create();
	// startHocAlertDialog.show();
	// }

	private String currPhotoTime;
	private PhotoEditBuilder1 mPhotoEditBuilder1;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		String strErrMsg = "";
		try {
			if (resultCode == RESULT_OK) {
				// strErrMsg+="1";
				if (requestCode == TAKE_PICTURE) {

					final Bitmap bm = Tool.lessenUriImage();

					currPhotoTime = Tool.getCurrPhotoTime();

					mPhotoEditBuilder1 = new PhotoEditBuilder1(context, report,
							handler, bm);
					mPhotoEditBuilder1.show();

				} else if (requestCode == 1111) {

					String path = getPath(data.getData());
					//
					final Bitmap bm = Tool.getUriImage(context, path);
					String date = Tool.getCurrPhotoTime();
					Bitmap bm1 = Tool.generatorContactCountIcon(bm, date, this
							.getIntent().getStringExtra("terminalname"),
							context);

					boolean bHave = report.isHavePhoto(mIndex);
					imageView[mIndex].setImageBitmap(bm1, date, 0, this
							.getIntent().getStringExtra("productid"));//
					if (!bHave) {
						photoLine.addView(getRptPhoto(mTotalPhoto));
						mTotalPhoto++;
					}

				} else if (requestCode == 20000) {
					selectData.put("str10", "已保存");
					customGrid.invalidate();
				}
			}
		} catch (Exception e) {
			Tool.showToastMsg(context, "拍照错误" + strErrMsg + e.getMessage(),
					AlertType.ERR);

		}
		// super.onActivityResult(requestCode, resultCode, data);
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		return cursor.getString(column_index);

	}

	private int mTotalPhoto = 1;

	// private void initPhotoLine() {
	// if (photoSView == null)
	// photoSView = (ScrollView) findViewById(R.id.sv_photo);
	//
	// photoLine = (LinearLayout) findViewById(R.id.line_rpt_photo);
	// mTotalPhoto = report.getAttCount() + 1;
	// imageView = new ImageView[100];
	// Bitmap bitmap = null;
	// byte[] bytes;
	// Fields data;
	// for (int i = 0; i < mTotalPhoto; i++) {
	// try {
	// data = report.getAttfields().getFields(i);
	// bytes = data.getPhoto();
	// bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	// } catch (Exception ex) {
	// bitmap = null;
	// }
	// photoLine.addView(getRptPhoto(i, bitmap));
	// }
	// }

	private Fields getPhotoData(DicData dicData) {
		// mTotalPhoto = report.getAttCount() + 1;
		Fields data;
		for (int i = 0; i < report.getAttCount(); i++) {

			data = report.getAttfields().getFields(i);
			if (data.getStrValue("displayobject").equals(dicData.getValue()))
				return data;
		}
		data = new Fields();
		data.put("displayobject", dicData.getValue());
		return data;

	}

	private HashMap<Integer, Fields> photo = new HashMap<Integer, Fields>();

	private void initPhotoLine() {
		if (photoSView == null)
			photoSView = (ScrollView) findViewById(R.id.sv_photo);

		photoLine = (LinearLayout) findViewById(R.id.line_rpt_photo);

		if (template.getOnlyType() > 5000 && template.getOnlyType() < 5999) {
			List<DicData> list = Sqlite.getDictDataList(context,
					template.getPhotodict(), "");
			int index = 0;

			for (DicData dictdata : list) {

				photo.put(index - 100, getPhotoData(dictdata));
				// setPhotoData(index,dictdata);
				photoLine.addView(new RptTable(context, dictdata, photo,
						getOnTouchListener(), index));
				index++;
			}
		} else {
			mTotalPhoto = report.getAttCount() + 1;
			imageView = new CImageView[100];
			// Bitmap bitmap = null;
			// byte[] bytes;
			Fields data;
			for (int i = 0; i < mTotalPhoto; i++) {
				try {
					data = report.getAttfields().getFields(i);

					photo.put(i - 100, data);
					// bytes = data.getPhoto();
					// bitmap = BitmapFactory.decodeByteArray(bytes, 0,
					// bytes.length);
				} catch (Exception ex) {
					// bitmap = null;
				}
				photoLine.addView(getRptPhoto(i));
			}
		}
		// if (template.getOnlyType() == 1 || (template.getOnlyType() > 5000 &&
		// template.getOnlyType() < 5999))
		// photoSView.setVisibility(View.VISIBLE);
	}

	private void hideKey(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

	}

	// private void showKey(View v)
	// {
	// InputMethodManager imm =
	// (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.showSoftInput(v, 0);
	//
	// }
	//
	private void resetPage(int id) {

		switch (id) {
		case 1:
			if (productLine != null)
				productLine.setVisibility(View.GONE);
			if (photoSView != null)
				photoSView.setVisibility(View.GONE);
			if (mainSView != null) {
				// showKey(mainSView);
				mainSView.setVisibility(View.VISIBLE);
				// mainSView.setAnimation(Tool.getAnimation(context));
			}
			break;
		case 2:
			if (photoSView != null)
				photoSView.setVisibility(View.GONE);
			if (mainSView != null)
				mainSView.setVisibility(View.GONE);
			if (productLine != null) {
				hideKey(productLine);
				productLine.setVisibility(View.VISIBLE);
				// productLine.setAnimation(Tool.getAnimation(context));
			}
			break;
		case 3:
			if (productLine != null)
				productLine.setVisibility(View.GONE);
			if (mainSView != null)
				mainSView.setVisibility(View.GONE);
			if (photoSView != null) {
				hideKey(photoSView);
				photoSView.setVisibility(View.VISIBLE);
				// photoSView.setAnimation(Tool.getAnimation(context));
			}
			break;

		default:
			break;
		}
	}

	public void onClick(View v) {

		if (v.getId() < 0) {
			mIndex = 100 + v.getId();
			OpenCarmer();
		} else {
			resetbutton(v.getId());

			switch (v.getId()) {
			case R.id.back:
				if (((SyncDataApp) getApplication()).getCurHuFields()
						.getString("jfzt").equals("1")
						|| ((SyncDataApp) getApplication()).getCurHuFields()
								.getString("jfzt").equals("2")) {
					finish(11);
				} else
					showBackDialog(RESULT_CANCELED);
				// finishActivity();
				break;

			case R.id.save:
				// SaveData();

				sendData();
				break;

			default:
				resetPage(v.getId());
				break;
			}
		}
	}

	private void showBackDialog(final int type) {
		stopDialog(backAlertDialog);
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("数据未保存，确认返回？");
		builder.setIcon(android.R.drawable.ic_dialog_info);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			// @Override
			public void onClick(DialogInterface dialog, int which) {
				stopDialog(backAlertDialog);

				finish(type);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			// @Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		// builder.create();
		backAlertDialog = builder.create();
		backAlertDialog.show();
	}

	private void stopDialog(AlertDialog dialog) {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
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
			} else {
				// b.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_bg));
				b.setCompoundDrawablesWithIntrinsicBounds(null, Tool
						.getDrawable(context, b.getText().toString(), false),
						null, null);
			}
		}
	}

	// private void SaveData() {
	// report.setAttfield(photo);
	// // String errMsg = report.checkData();
	// // if (errMsg.equals(""))
	// // {
	// // Tool.showProgress(context, "");
	// // if (template.haveTable())
	// // report.setDetailfields(customGrid.getDataList());
	//
	// Sqlite.execSingleSql(context,
	// "delete from  t_client_rlt_activitypromoter  where PromoterId='"
	// + this.getIntent().getStringExtra("PromoterId") + "'");
	// Sqlite.execSingleSql(
	// context,
	// "update  t_outlet_main set str1=str1-"
	// + report.getField("int2") + "  where serverid='"
	// + report.getField("int1") + "'");
	// long index = Sqlite.saveDP(context, report);
	//
	// Tool.stopProgress();
	// if (index > 0) {
	// Tool.showToastMsg(context, "美顾信息提交成功", AlertType.INFO);
	// finish(RESULT_OK);
	// } else
	// Tool.showToastMsg(context, "美顾信息提交失败", AlertType.ERR);
	// // }
	// // else
	// // Tool.showToastMsg(context, errMsg, AlertType.ERR);
	// }

	private void sendData() {

		String errMsg = report.checkData();
		if (errMsg.equals("")) {

			if (template.getOnlyType() == 1) {
				Tool.showProgress(context, "正在新建问题");
				QueryConfig.getNewInstance().setMethod("newwt");
				QueryConfig.getInstance().setData("wenti",
						report.getField("wenti"));
				QueryConfig.getInstance().setBack(true);
				QueryConfig.getInstance().setData("fenleiname",
						report.getField("fenleiname"));
				QueryConfig.getInstance().setData("dengji",
						report.getField("dengji"));

				QueryConfig.getInstance().setData(
						"huid",
						((SyncDataApp) getApplication()).getCurHuFields()
								.getString("serverid"));
				QueryConfig.getInstance().setData("userid",
						Rms.getUserId(context));
				QueryConfig.getInstance().setData("beizhu",
						report.getField("beizhu"));
			} else {
				Tool.showProgress(context, "正在更新问题");
				QueryConfig.getNewInstance().setMethod("updatewt");
				QueryConfig.getInstance().setData("id",
						report.getField("serverid"));
				QueryConfig.getInstance().setBack(true);
				QueryConfig.getInstance().setData(
						"huid",
						((SyncDataApp) getApplication()).getCurHuFields()
								.getString("serverid"));
				QueryConfig.getInstance().setData("userid",
						Rms.getUserId(context));
				QueryConfig.getInstance().setData("beizhu",
						report.getField("beizhu"));
			}

			if (photo != null) {
				Set<Integer> keyList = photo.keySet();
				Iterator<Integer> key = keyList.iterator();
				JSONArray array = new JSONArray();
				JSONObject object = new JSONObject();
				Fields data;
				try {
					while (key.hasNext()) {
						data = photo.get(key.next());
						object.put("shottime", data.getShotTime());
						object.put("photo", Base64.encodeToString(
								data.getPhoto(), Base64.DEFAULT));
						array.put(object);
						// QueryConfig.getInstance()
						// .setData("shottime", data.getShotTime());
						// QueryConfig.getInstance().setData("photo",
						// Base64.encodeToString(data.getPhoto(),
						// Base64.DEFAULT));

					}
					QueryConfig.getInstance().setQueryArray("pic", array);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			SyncData.Query(QueryConfig.getInstance(), handler,
					Frm_SubmitRpt.this);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (((SyncDataApp) getApplication()).getCurHuFields()
					.getString("jfzt").equals("1")
					|| ((SyncDataApp) getApplication()).getCurHuFields()
							.getString("jfzt").equals("2")) {
				finish(11);
			} else
				showBackDialog(RESULT_CANCELED);
		}
		return false;
	}

	public Context getContext() {
		return context;
	}

	public void initContext() {
		this.context = Frm_SubmitRpt.this;
	}

	public Activity getActivity() {
		return activity;
	}

	public void initActivity() {
		this.activity = Frm_SubmitRpt.this;
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// init();
	// }

	@Override
	protected void onRestart() {
		if (!Rms.getLoginDate(context).equals(Tool.getMoblieDate())) {
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
