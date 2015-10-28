package markettracker.ui;

import com.rh.fang.zs.R;

import java.util.ArrayList;
import java.util.List;

import markettracker.util.Constants.AlertType;
import markettracker.util.Constants.ControlType;
import markettracker.util.CGrid;
import markettracker.util.Constants;
import markettracker.util.MsgDetailBuilder;
import markettracker.util.Tool;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Rms;
import markettracker.data.Sqlite;
import markettracker.data.UIItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Frm_MsgList extends Activity implements OnClickListener
{
	
	private Context context;
	private CGrid mCustomGrid;
	private List<UIItem> mRptDetail;
	private Button exit;
	private Handler mHandler;
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message);
		initHandler();
		init();
	}
	
	private void init()
	{
		exit = (Button) findViewById(R.id.back);
		exit.setOnClickListener(this);
		context = Frm_MsgList.this;
		getProductLine();
	}
	
	private void finishActivity()
	{
		
		// status='未读'
		// FieldsList list = Sqlite.getFieldsList(context, 12, "-1");
		if (!isOver())
			Tool.showToastMsg(context, "消息必须全部读完", AlertType.ERR);
		else
		{
			setResult(RESULT_OK);
			this.finish();
		}
		
	}
	
	private boolean isOver()
	{
		if (mCustomGrid.getFieldsList() != null && mCustomGrid.getFieldsList().getList() != null)
		{
			for (Fields fields : mCustomGrid.getFieldsList().getList())
			{
				if (fields.getStrValue("status").equals("未读"))
					return false;
			}
		}
		return true;
	}
	
	// 获取产品UIItem
	private List<UIItem> getItemList()
	{
		if (mRptDetail == null)
			mRptDetail = getMsgItem();
		return mRptDetail;
	}
	
	public static List<UIItem> getMsgItem()
	{
		List<UIItem> list = new ArrayList<UIItem>();
		
		UIItem item = new UIItem();
		item.setCaption("状态");
		item.setControlType(ControlType.NONE);
		item.setDataKey("status");
		item.setWidth((Tool.getScreenWidth()) / 5);
		list.add(item);
		
		item = new UIItem();
		item.setCaption("标题");
		item.setControlType(ControlType.NONE);
		item.setDataKey("title");
		item.setAlign(Align.LEFT);
		item.setWidth((Tool.getScreenWidth()) * 2 / 5);
		list.add(item);
		
		item = new UIItem();
		item.setCaption("发送时间");
		item.setControlType(ControlType.NONE);
		item.setDataKey("stime");
		item.setWidth((Tool.getScreenWidth()) * 2 / 5);
		list.add(item);
		
		return list;
	}
	
	// 获取产品UIItem数量
	private int getItemListCount()
	{
		if (getItemList() == null)
			return 0;
		else
			return getItemList().size();
	}
	
	private LinearLayout mProductLine;
	
	private LinearLayout getProductLine()
	{
		if (mProductLine == null)
		{
			mProductLine = (LinearLayout) findViewById(R.id.message_list);
			if (getItemListCount() > 0)
			{
				mProductLine.addView(getCustomGrid());
			}
		}
		return mProductLine;
	}
	
	private View getCustomGrid()
	{
		View view = LayoutInflater.from(context).inflate(R.layout.plan_detail_grid, null);
		
		mCustomGrid = (CGrid) view.findViewById(R.id.cgrid);
		initGrid();
		// mCustomGrid.setDataInfo(getItemList(), Sqlite.getMsgList(mContext));
		// mCustomGrid.setCustomGridType(Constants.CustomGridType.MESSAGE);
		// mCustomGrid.setLinkHandler(mHandler);
		
		return view;
	}
	
	private void initGrid()
	{
		FieldsList list = Sqlite.getFieldsList(context, 2, "-1");
		
		mCustomGrid.setDataInfo(getItemList(), list);
		mCustomGrid.setCustomGridType(Constants.CustomGridType.MESSAGE);
		mCustomGrid.setLinkHandler(mHandler);
	}
	
	private void resetGrid()
	{
		FieldsList list = Sqlite.getFieldsList(context, 2, "-1");
		
		mCustomGrid.setDataInfo(getItemList(), list);
		mCustomGrid.invalidate();
	}
	
	public void onClick(View v)
	{
		System.out.println(v.getId());
		finishActivity();
		
	}
	
	private void initHandler()
	{
		mHandler = new Handler()
		{
			// @Override
			public void handleMessage(Message msg)
			{
				// String strMsg = msg.obj.toString();
				Tool.stopProgress();
				switch (msg.what)
				{
				
					case Constants.CustomGridType.MESSAGE:
						if (mCustomGrid.getSelectData() != null)
							if (mCustomGrid.getSelectData().getStrValue("type").equals("1"))
								showMsgDetail(mCustomGrid.getSelectData());
							else
							{
								Intent i = new Intent(context, Frm_Survey.class);
								
								i.putExtra("key", mCustomGrid.getSelectData().getStrValue("serverid"));
								i.putExtra("teminalCode", "-1");
								startActivityForResult(i, 1000);
							}
						break;
					case 1:// 更新消息状态
						mCustomGrid.invalidate();
						break;
					
					default:
						super.handleMessage(msg);
						break;
				}
			}
		};
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1000)
		{
			resetGrid();
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			// sendRpt();
			
			finishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private AlertDialog mMsgAlertDialog;
	
	private void showMsgDetail(Fields field)
	{
		if (mMsgAlertDialog != null)
		{
			mMsgAlertDialog.dismiss();
			mMsgAlertDialog = null;
		}
		MsgDetailBuilder msg = new MsgDetailBuilder(context, mHandler, field);
		mMsgAlertDialog = msg.create();
		mMsgAlertDialog.show();
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
