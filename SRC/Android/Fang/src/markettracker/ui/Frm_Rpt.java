package markettracker.ui;

import com.rh.fang.jf.R;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import markettracker.util.CButton;
import markettracker.util.CImageView;
import markettracker.util.CustomGrid;
import markettracker.util.NumericKeyboard;
import markettracker.util.RptTable;
import markettracker.util.TemplateFactory;
import markettracker.util.Tool;
import markettracker.util.Constants.AlertType;
import markettracker.util.NumericKeyboard.OnNumberClickListener;
import markettracker.data.ButtonConfig;
import markettracker.data.DicData;
import markettracker.data.Fields;
import markettracker.data.Panal;
import markettracker.data.Rms;
import markettracker.data.SObject;
import markettracker.data.Sqlite;
import markettracker.data.Template;
import markettracker.data.UIItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
public class Frm_Rpt extends Activity implements OnClickListener
{
	
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
	private CImageView clickImgView;
	private Button back, save;
	private AlertDialog backAlertDialog;
	private View view;
	
	private NumericKeyboard keyboard;
	
	private Hashtable<String, Fields> hashtable;
	
	private Template template;
	private SObject report;
	private static final int TAKE_PICTURE = 3021;
	
	private TextView title, title1;
	private Fields selectData;
	private Handler handler;
	private String key;
	private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
	private LinearLayout.LayoutParams layoutParamsPhoto = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 200);
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rpt_frm);
		init();
	}
	
	private void init()
	{
		initContext();
		initActivity();
		initHandler();
		key = this.getIntent().getStringExtra("key");
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);
		
		initTitle();
		initReport();
		initPage();
	}
	
	private void initHandler()
	{
		handler = new Handler()
		{
			// @Override
			public void handleMessage(Message msg)
			{
				// String strMsg = msg.obj.toString();
				Tool.stopProgress();
				switch (msg.what)
				{
				

					
					// case Constants.CustomGridType.RESETREPORT:
					// // initReport(strMsg);
					// // retRptButton();
					// resetRptHead();
					// // resetButton();
					// resetMainSView();
					// // resetPhotoLine();
					// break;
					//
					// case Constants.CustomGridType.ADDHOC:
					//
					// selectData = (Fields) msg.obj;
					//
					// // Tool.showMsg(getCurrContext(), strMsg);
					// break;
					
					default:
						super.handleMessage(msg);
						break;
				}
			}
		};
	}
	
	private void finish(int type)
	{
		Intent i = new Intent();
		i.putExtra("type", this.getIntent().getStringExtra("type"));
		setResult(type, i);
		this.finish();
	}
	
	// 设置title
	private void initTitle()
	{
		title = (TextView) findViewById(R.id.txt);
		title.setText(this.getIntent().getStringExtra("name"));
		
		title1 = (TextView) findViewById(R.id.txt1);
	}
	
	private void initReport()
	{
		template = TemplateFactory.getTemplate(this.getIntent().getStringExtra("type"));
		report = Sqlite.getReport(context, template, this.getIntent().getStringExtra("teminalCode"), 1, key);
		report.setField("ClientType", this.getIntent().getStringExtra("clienttype"));
		
		if (template.getOnlyType() == 20010||template.getOnlyType() == 20011)
		{
			
			
//			String date = Sqlite.getDate(context, "select date('" + Tool.getCurrDate() + "','-3 day') as date");
			if (template.getOnlyType() == 20010&&Tool.getSelectData().getStrValue("str5").equals("1"))
			{
				save.setVisibility(View.GONE);
				title1.setText("活动正在审批,不能发起变更");
			}
			report.setField("activitiescode", Tool.getSelectData().getStrValue("activitiescode"));
			report.setField("stime", Tool.getSelectData().getStrValue("stime"));
			report.setField("etime", Tool.getSelectData().getStrValue("etime"));
			report.setField("displaytype", Tool.getSelectData().getStrValue("displaytype"));
			report.setField("displayarea", Tool.getSelectData().getStrValue("displayarea"));
			report.setField("cost", Tool.getSelectData().getStrValue("cost"));
			report.setField("payment", Tool.getSelectData().getStrValue("payment"));
			if (!report.isSaved())
			{
				report.setField("str1", Tool.getSelectData().getStrValue("stime"));
				report.setField("str2", Tool.getSelectData().getStrValue("etime"));				
				report.setField("int1", Tool.getSelectData().getStrValue("serverid"));
			}
		}
		
	}
	
	private void initPage()
	{
		if (template.getOnlyType() != 1)
		{
			initMainView();
			initProductLine();
		}
		initPhotoLine();
		setupButton();
	}
	
	private void initButton()
	{
		if (buttonlist == null)
		
		{
			// if (template.getOnlyType() == 20010 || (template.getOnlyType() >
			// 1000 && template.getOnlyType() < 1050))
			// buttonlist = Tool.getRptTab2();
			if (template.getOnlyType() == 8)
				buttonlist = Tool.getRptTab3();
			
			else
				buttonlist = Tool.getRptTab2();
		}
	}
	
	private void initButtonLine()
	{
		if (lineButton == null)
			lineButton = (LinearLayout) findViewById(R.id.line_buttom);
		else
			lineButton.removeAllViews();
	}
	
	private void setupButton()
	{
		initButton();
		initButtonLine();
		
		int count = buttonlist.size();
		if (buttonlist != null && count > 0)
		{
			layoutParams.width = Tool.getBWidth(activity, count);
			
			for (int i = 0; i < count; i++)
				lineButton.addView(getRptButton(buttonlist.get(i)));
		}
		
		if (template.getOnlyType() == 20000 || template.getOnlyType() == 6001 || template.getOnlyType() == 1 || (template.getOnlyType() > 5000 && template.getOnlyType() < 5999))
			lineButton.setVisibility(View.GONE);
	}
	
	private CButton getRptButton(ButtonConfig config)
	{
		CButton button = new CButton(context, config, layoutParams, template, this);
		// button.setOnClickListener(this);
		return button;
	}
	
	private void initMainView()
	{
		if (template != null && template.havePanal())
		{
			if (mainSView == null)
			{
				mainSView = (ScrollView) findViewById(R.id.sv_detail_bottom);
				// mainSView.addView(getMainLine());
				getMainLine();
				mainSView.setVisibility(View.VISIBLE);
				// mainSView.setAnimation(Tool.getAnimation(context));
			}
		}
	}
	
	// 获取描述信息
	private LinearLayout getMainLine()
	{
		if (mainLine == null)
		{
			// mainLine = new LinearLayout(context);
			// mainLine.setOrientation(LinearLayout.VERTICAL);
			// mainLine.setPadding(20, 0, 20, 20);
			
			mainLine = (LinearLayout) findViewById(R.id.line_main);
		}
		// RptTitle txt;
		
		for (Panal panal : template.getPanalList())
		{
			
			mainLine.addView(new RptTable(context, panal, report, handler, this));
			
		}
		return mainLine;
	}
	
	private OnNumberClickListener mOnNumberClickListener = new OnNumberClickListener()
	{
		public void OnNumberClick(String numberStr)
		{
			customGrid.setNumeric(numberStr);
		}
	};
	
	private void initProductLine()
	{
		if (template != null && template.haveTable())
		{
			if (productLine == null)
			{
				productLine = (LinearLayout) findViewById(R.id.line_rpt_product);
				// productLine.setPadding(20, 0, 20, 20);
			}
			productLine.addView(getCustomGrid(template.getTableItem()));
			
			if (!template.havePanal())
			{
				productLine.setVisibility(View.VISIBLE);
				// productLine.setAnimation(Tool.getAnimation(context));
			}
		}
	}
	
	private void initView()
	{
		if (view == null)
			view = LayoutInflater.from(context).inflate(R.layout.rpt_detail_grid, null);
	}
	
	private void initKeyboard()
	{
		if (keyboard == null)
		{
			keyboard = new NumericKeyboard(view);
			keyboard.setOnNumberClickListener(mOnNumberClickListener);
		}
	}
	
	private void initCustomGrid()
	{
		if (customGrid == null)
		{
			customGrid = (CustomGrid) view.findViewById(R.id.parrow_grid);
			customGrid.setNumericKeyboard(keyboard);
		}
	}
	
	private void initData()
	{
		hashtable = new Hashtable<String, Fields>();
		if (report.getDetailCount() > 0)
		{
			
			if (template.getOnlyType() == 6001)
			{
				for (Fields data : report.getDetailfields().getList())
				{
					if (data.getIntValue("int1") == 1)
						hashtable.put(data.getStrValue("serverid"), data);
				}
			}
			else
			{
				for (Fields data : report.getDetailfields().getList())
				{
					if (data.getIntValue("int3") != 0)
						hashtable.put(data.getStrValue("serverid"), data);
				}
			}
			
		}
	}
	
	private View getCustomGrid(List<UIItem> itemlist)
	{
		initView();
		initKeyboard();
		initCustomGrid();
		initData();
		customGrid.setDataInfo(itemlist, report.getDetailfields());
		customGrid.setDataHashtable(hashtable);
		customGrid.setLinkHandler(handler);
		return view;
	}
	
	private int mIndex;
	
	// private ImageView getRptPhoto(int index, Bitmap bitmap) {
	// imageView[index] = new ImageView(context);
	// imageView[index].setId(index - 100);
	//
	// layoutParamsPhoto.topMargin = 10;
	//
	// imageView[index].setLayoutParams(layoutParamsPhoto);
	//
	// // imageView[index].setMinimumWidth(Tool.getScreenWidth()-20);
	// // imageView[index].setMinimumHeight(200);
	// // mButton[index].setBackgroundColor(R.color.selectrect);
	// if (bitmap == null)
	// imageView[index]
	// .setImageResource(R.drawable.camera1);
	// else
	// imageView[index].setImageBitmap(bitmap);
	//
	// imageView[index].setOnClickListener(this);
	// return imageView[index];
	// }
	
	private OnTouchListener getOnTouchListener()
	{
		
		OnTouchListener listener = new OnTouchListener()
		{
			
			public boolean onTouch(View v, MotionEvent event)
			{
				// TODO Auto-generated method stub
				
				// float x = event.getX();
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					// startx = x;
				}
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
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
					OpenCarmer();
					if (template.getOnlyType() > 5000 && template.getOnlyType() < 5999)
						clickImgView = (CImageView) v;
					
					// }
				}
				return true;
			}
		};
		return listener;
	}
	
	private OnClickListener getOnClickListener()
	{
		OnClickListener listener = new OnClickListener()
		{
			
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				delView(v.getId());
			}
		};
		return listener;
	}
	
	private void delView(int id)
	{
		CImageView view;
		for (int i = 0; i < photoLine.getChildCount(); i++)
		{
			view = (CImageView) photoLine.getChildAt(i);
			if (view.getId() == id)
			{
				photoLine.removeViewAt(i);
				photo.remove(id);
				break;
			}
		}
	}
	
	private CImageView getRptPhoto(int index)
	{
		
		imageView[index] = new CImageView(context, template, getOnTouchListener(), photo, index, getOnClickListener());
		layoutParamsPhoto.height = Tool.dip2px(context, 150);
		layoutParamsPhoto.topMargin = 5;
		imageView[index].setLayoutParams(layoutParamsPhoto);
		return imageView[index];
	}
	
	// public void OpenCarmer()
	// {
	// try
	// {
	// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//
	// "android.media.action.IMAGE_CAPTURE"
	// startActivityForResult(intent, TAKE_PICTURE);
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	
	public void OpenCarmer()
	{
		try
		{
			Tool.createPhotoFile();
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/inoherb/photo//test.JPEG")));
			startActivityForResult(intent, TAKE_PICTURE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// super.onActivityResult(requestCode, resultCode, data);
		String strErrMsg = "";
		try
		{
			if (resultCode == RESULT_OK)
			{
				// strErrMsg+="1";
				if (requestCode == TAKE_PICTURE)
				{
					// strErrMsg+="2";
					// final Fields photo = new Fields();
					// // strErrMsg+="3";
					// final Bitmap bm = (Bitmap) data.getExtras().get("data");
					// boolean bHave = report.isHavePhoto(mIndex);
					//
					// photo.setShotTime(Tool.getCurrPhotoTime());
					// photo.setPhoto(Tool.Bitmap2Bytes(bm));
					//
					// if (bHave)
					// report.setAttfield(mIndex, photo);
					// else
					// report.setAttfield(photo);
					// imageView[mIndex].setImageBitmap(bm);//
					// 想图像显示在ImageView视图上，private
					//
					// if (!bHave) {
					// photoLine.addView(getRptPhoto(mTotalPhoto, null));
					// mTotalPhoto++;
					// }
					
					// 1 final Bitmap bm = (Bitmap)
					// data.getExtras().get("data");
					final Bitmap bm = Tool.lessenUriImage();
					// photo.setShotTime(Tool.getCurrPhotoTime());
					// photo.setPhoto(Tool.Bitmap2Bytes(bm));
					//
					// if (bHave)
					// report.setAttfield(mIndex, photo);
					// else
					// report.setAttfield(photo);
					// imageView[mIndex].setImageBitmap(bm);//
					// 想图像显示在ImageView视图上，private
					
					String date = Tool.getCurrPhotoTime();
					Bitmap bm1 = Tool.generatorContactCountIcon(bm, date, this.getIntent().getStringExtra("terminalname"), context);
					
					if (template.getOnlyType() > 5000 && template.getOnlyType() < 5999)
					{
						clickImgView.setImageBitmap(bm1, date, 1);//
					}
					else
					{
						boolean bHave = report.isHavePhoto(mIndex);
						imageView[mIndex].setImageBitmap(bm1, date, 0);//
						if (!bHave)
						{
							photoLine.addView(getRptPhoto(mTotalPhoto));
							mTotalPhoto++;
						}
					}
					
				}
				else if (requestCode == 20000)
				{
					selectData.put("str10", "已保存");
					customGrid.invalidate();
				}
			}
		}
		catch (Exception e)
		{
			Tool.showToastMsg(context, "拍照错误" + strErrMsg + e.getMessage(), AlertType.ERR);
			
		}
		// super.onActivityResult(requestCode, resultCode, data);
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
	
	private Fields getPhotoData(DicData dicData)
	{
		// mTotalPhoto = report.getAttCount() + 1;
		Fields data;
		for (int i = 0; i < report.getAttCount(); i++)
		{
			
			data = report.getAttfields().getFields(i);
			if (data.getStrValue("displayobject").equals(dicData.getValue()))
				return data;
		}
		data = new Fields();
		data.put("displayobject", dicData.getValue());
		return data;
		
	}
	
	private HashMap<Integer, Fields> photo = new HashMap<Integer, Fields>();
	
	private void initPhotoLine()
	{
		if (photoSView == null)
			photoSView = (ScrollView) findViewById(R.id.sv_photo);
		
		photoLine = (LinearLayout) findViewById(R.id.line_rpt_photo);
		
		if (template.getOnlyType() > 5000 && template.getOnlyType() < 5999)
		{
			List<DicData> list = Sqlite.getDictDataList(context, template.getPhotodict(), "");
			int index = 0;
			
			for (DicData dictdata : list)
			{
				
				photo.put(index - 100, getPhotoData(dictdata));
				// setPhotoData(index,dictdata);
				photoLine.addView(new RptTable(context, dictdata, photo, getOnTouchListener(), index));
				index++;
			}
		}
		else
		{
			mTotalPhoto = report.getAttCount() + 1;
			imageView = new CImageView[100];
			// Bitmap bitmap = null;
			// byte[] bytes;
			Fields data;
			for (int i = 0; i < mTotalPhoto; i++)
			{
				try
				{
					data = report.getAttfields().getFields(i);
					
					photo.put(i - 100, data);
					// bytes = data.getPhoto();
					// bitmap = BitmapFactory.decodeByteArray(bytes, 0,
					// bytes.length);
				}
				catch (Exception ex)
				{
					// bitmap = null;
				}
				photoLine.addView(getRptPhoto(i));
			}
		}
		if (template.getOnlyType() == 1 || (template.getOnlyType() > 5000 && template.getOnlyType() < 5999))
			photoSView.setVisibility(View.VISIBLE);
	}
	
	private void hideKey(View v)
	{
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
	private void resetPage(int id)
	{
		
		switch (id)
		{
			case 1:
				if (productLine != null)
					productLine.setVisibility(View.GONE);
				if (photoSView != null)
					photoSView.setVisibility(View.GONE);
				if (mainSView != null)
				{
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
				if (productLine != null)
				{
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
				if (photoSView != null)
				{
					hideKey(photoSView);
					photoSView.setVisibility(View.VISIBLE);
					// photoSView.setAnimation(Tool.getAnimation(context));
				}
				break;
			
			default:
				break;
		}
	}
	
	public void onClick(View v)
	{
		
		if (v.getId() < 0)
		{
			mIndex = 100 + v.getId();
			OpenCarmer();
		}
		else
		{
			resetbutton(v.getId());
			
			switch (v.getId())
			{
				case R.id.back:
					showBackDialog(RESULT_CANCELED);
					// finishActivity();
					break;
				
				case R.id.save:
					SaveData();
					break;
				
				default:
					resetPage(v.getId());
					break;
			}
		}
	}
	
	private void showBackDialog(final int type)
	{
		stopDialog(backAlertDialog);
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("数据未保存，确认返回？");
		builder.setIcon(android.R.drawable.ic_dialog_info);
		
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			// @Override
			public void onClick(DialogInterface dialog, int which)
			{
				stopDialog(backAlertDialog);
				finish(type);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			// @Override
			public void onClick(DialogInterface dialog, int which)
			{
			}
		});
		// builder.create();
		backAlertDialog = builder.create();
		backAlertDialog.show();
	}
	
	private void stopDialog(AlertDialog dialog)
	{
		if (dialog != null)
		{
			dialog.dismiss();
			dialog = null;
		}
	}
	
	private void resetbutton(int id)
	{
		CButton b;
		for (int i = 0; i < lineButton.getChildCount(); i++)
		{
			b = ((CButton) lineButton.getChildAt(i));
			if (b.getId() == id)
			{
				
				// b.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_bg_click));
				b.setCompoundDrawablesWithIntrinsicBounds(null, Tool.getDrawable(context, b.getText().toString(), true), null, null);
			}
			else
			{
				// b.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_bg));
				b.setCompoundDrawablesWithIntrinsicBounds(null, Tool.getDrawable(context, b.getText().toString(), false), null, null);
			}
		}
	}
	
	private void SaveData()
	{
		report.setAttfield(photo);
		if (template.getOnlyType() == 20010)
		{
			if(report.getField("int2").equals("1")&&report.getAttCount()<=0)
			{
				Tool.showToastMsg(context, "照片必须拍摄", AlertType.ERR);
				return;
			}
		}
		
		
		String errMsg = report.checkData();
		if (errMsg.equals(""))
		{
			Tool.showProgress(context, "");
			if (template.haveTable())
				report.setDetailfields(customGrid.getDataList());
			long index = Sqlite.saveReport(context, report);
			
			Tool.stopProgress();
			if (index > 0)
			{
				Tool.showToastMsg(context, "报告保存成功", AlertType.INFO);
				finish(RESULT_OK);
			}
			else
				Tool.showToastMsg(context, "报告保存失败", AlertType.ERR);
		}
		else
			Tool.showToastMsg(context, errMsg, AlertType.ERR);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			
			showBackDialog(RESULT_CANCELED);
		}
		return false;
	}
	
	public Context getContext()
	{
		return context;
	}
	
	public void initContext()
	{
		this.context = Frm_Rpt.this;
	}
	
	public Activity getActivity()
	{
		return activity;
	}
	
	public void initActivity()
	{
		this.activity = Frm_Rpt.this;
	}
	
	// @Override
	// protected void onResume() {
	// super.onResume();
	// init();
	// }
	
	@Override
	protected void onRestart()
	{
		if (!Rms.getLoginDate(context).equals(Tool.getMoblieDate()))
		{
			showTimeoutDialog();
		}
		Tool.setAutoTime(context);
		super.onRestart();
	}
	
	private void showTimeoutDialog()
	{
		Builder dialog = new Builder(context);
		dialog.setTitle("警告");
		dialog.setMessage("登录超时,请重新登录！");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			
			// @Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				exit();
			}
		});
		dialog.show();
	}
	
	private void exit()
	{
		setResult(-100);
		this.finish();
	}
	
}
