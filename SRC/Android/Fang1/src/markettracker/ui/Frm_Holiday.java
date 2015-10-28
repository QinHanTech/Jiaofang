package markettracker.ui;

import com.rh.fang.zs.R;
import java.util.ArrayList;
import java.util.List;
import markettracker.util.Constants;
import markettracker.util.RptTable;
import markettracker.util.SyncData;
import markettracker.util.TemplateFactory;
import markettracker.util.Tool;
import markettracker.util.Constants.AlertType;
import markettracker.data.Fields;
import markettracker.data.Panal;
import markettracker.data.Rms;
import markettracker.data.SObject;
import markettracker.data.Template;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Frm_Holiday extends Activity implements OnClickListener
{
	
	private Context context;
	private Activity activity;
	private LinearLayout mainLine;
	private ScrollView mainSView;
	private Fields data;
	
	private Button back, save;
	
	private Template template;
	private SObject report;
	
	private TextView title;
	
	private Handler handler;
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.qingjia_frm);
		init();
	}
	
	private void init()
	{
		initContext();
		initActivity();
		initHandler();
		
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);
		
		initTitle();
		initReport();
		initPage();
	}
	
	@SuppressLint("HandlerLeak")
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
					case Constants.PropertyKey.SYNCERR:
						// errMsg = msg.obj.toString();
						Tool.showToastMsg(context, "请假失败，请再次提交", AlertType.ERR);
						break;
					case Constants.PropertyKey.UPLOAD:
						
						Tool.showToastMsg(context, "请假成功", AlertType.INFO);
						Tool.sendSMS(context, Rms.getManagerMobile(context), Rms.getUserName(context) + "需要在" + report.getSValue("STR1") + "到" + report.getSValue("STR2") + "请假。谢谢！");
						finishActivity();
						break;
					default:
						super.handleMessage(msg);
						break;
				}
			}
		};
	}
	
	// 设置title
	private void initTitle()
	{
		title = (TextView) findViewById(R.id.txt);
		title.setText("请假");
	}
	
	private void initReport()
	{
		template = TemplateFactory.getQJTemplate();
		report = new SObject(template);
		report.setType("report");
		data = new Fields();
		
		data.Set("TemplateId", "51");
		data.Set("userid", Rms.getUserId(context));
		data.Set("ReportDate", Tool.getCurrDate());
	}
	
	private void initPage()
	{
		initMainView();
	}
	
	private void initMainView()
	{
		if (template != null && template.havePanal())
		{
			if (mainSView == null)
			{
				mainSView = (ScrollView) findViewById(R.id.sv_detail_bottom);
				mainSView.addView(getMainLine());
				mainSView.setVisibility(View.VISIBLE);
//				mainSView.setAnimation(Tool.getAnimation(context));
			}
		}
	}
	
	// 获取描述信息
	private LinearLayout getMainLine()
	{
		if (mainLine == null)
		{
			mainLine = new LinearLayout(context);
			mainLine.setOrientation(LinearLayout.VERTICAL);
			// mainLine.setPadding(20, 0, 20, 20);
		}
		for (Panal panal : template.getPanalList())
		{
			
			mainLine.addView(new RptTable(context, panal, report, handler, this));
			
		}
		return mainLine;
	}
	
	public void onClick(View v)
	{
		
		switch (v.getId())
		{
			case R.id.back:
				finishActivity();
				break;
			
			case R.id.save:
				SaveData();
				break;
			
			default:
				break;
		}
		
	}
	
	private void SaveData()
	{
		
		if (report.getField("str1").compareTo(report.getField("str2")) > 0)
		{
			Tool.showToastMsg(context, "开始时间不能小于结束时间", AlertType.ERR);
		}
		else if (report.getField("str1").compareTo(Tool.getCurrDateTime1()) < 0)
		{
			Tool.showToastMsg(context, "开始时间不能小于当前时间", AlertType.ERR);
		}
		else if (Tool.getDateChazhi(report.getField("str1"), report.getField("str2")) > 1000 * 60 * 60 * 4)
		{
			Tool.showToastMsg(context, "请假时长不能超过4小时", AlertType.ERR);
		}
		else
		{
			List<SObject> slist = new ArrayList<SObject>();
			
			data.Set("STR1", report.getField("STR1"));
			data.Set("STR2", report.getField("STR2"));
			data.Set("STR3", report.getField("STR3"));
			report.setFields(data);
			slist.add(report);
			SyncData.Upload(slist, handler, activity);
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			
			finishActivity();
		}
		return false;
	}
	
	private void finishActivity()
	{
		setResult(RESULT_OK);
		this.finish();
	}
	
	public Context getContext()
	{
		return context;
	}
	
	public void initContext()
	{
		this.context = Frm_Holiday.this;
	}
	
	public Activity getActivity()
	{
		return activity;
	}
	
	public void initActivity()
	{
		this.activity = Frm_Holiday.this;
	}
	
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
