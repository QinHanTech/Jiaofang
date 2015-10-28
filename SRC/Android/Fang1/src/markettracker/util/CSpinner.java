package markettracker.util;

import markettracker.data.Fields;
import markettracker.data.UIItem;
import com.rh.fang.zs.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class CSpinner extends TextView
{
	
	private UIItem item;
	private AlertDialog singleChoiceDialog;
	private Fields data;
	private CSpinner cSpinner;
	private ChoiceGroupAdapter choiceGroupAdapter;
	
	public CSpinner(Context context, UIItem item, Fields data,final Handler handler)
	{
		super(context);
		
//		this.setBackgroundDrawable(getDrawable(1));
//		this.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(2), null);
//		this.setCompoundDrawablePadding(10);
//		this.item = item;
//		this.setLayoutParams(getCurLayoutParams());
//		this.setTextColor(Tool.getTextColor(context));
//		this.setTextSize(Tool.getTextSize());
//		this.setGravity(Gravity.CENTER_VERTICAL);
//		this.setPadding(20, 0, 10, 0);
		
		
		this.setBackgroundDrawable(getDrawable(1));
		this.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(2), null);
		this.setCompoundDrawablePadding(Tool.dip2px(context, 10));
		this.item = item;
		this.setMinimumHeight(Tool.dip2px(context, 40));
		this.setLayoutParams(getCurLayoutParams());
		this.setTextColor(Tool.getTextColor(context));
		this.setTextSize(Tool.getTextSize());
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setPadding(Tool.dip2px(context, 10), 0, Tool.dip2px(context, 10), 0);
		
		this.cSpinner = this;
		
		this.data = data;
		this.choiceGroupAdapter = new ChoiceGroupAdapter(getContext(), item);
		this.setText(choiceGroupAdapter.getName(getResult()));
		this.setOnClickListener(new OnClickListener()
		{
			
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				showSingleChoiceDialog(handler);
			}
		});
	}
	
	// private void setText() {
	// this.setText(choiceGroupAdapter.getName(getResult()));
	// }
	
	private Drawable getDrawable(int type)
	{
		if (type == 1)
			return getResources().getDrawable(R.drawable.round);
		else
			return getResources().getDrawable(R.drawable.triangle);
	}
	
	private String getResult()
	{
		return data.getStrValue(item.getDataKey());
	}
	
	private void setData(String value)
	{
		data.put(item.getDataKey(), value);
	}
	
	private void setData1(String value)
	{
		data.put(item.getDataKey()+"name", value);
	}
	
	// private LinearLayout getTitleView(String msg) {
	// LinearLayout mainLine = new LinearLayout(getContext());
	// mainLine.setOrientation(LinearLayout.HORIZONTAL);
	// LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
	// 60);
	// mainLine.setLayoutParams(layoutParams);
	// mainLine.setGravity(Gravity.CENTER_VERTICAL);
	// mainLine.setBackgroundDrawable(getContext().getResources().getDrawable(
	// R.drawable.mentou_bg));
	//
	// TextView textView = new TextView(getContext());
	// textView.setTextColor(getContext().getResources().getColor(R.color.black));
	// textView.setTextSize(16);
	// textView.setText(msg);
	// mainLine.addView(textView);
	// return mainLine;
	// }
	
	private void showSingleChoiceDialog(final Handler handler)
	{
		if (null != singleChoiceDialog)
		{
			singleChoiceDialog.dismiss();
			singleChoiceDialog = null;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		// builder.setTitle(item.getCaption());
		// builder.setCustomTitle(getTitleView("请选择："+item.getCaption()));
		builder.setSingleChoiceItems(choiceGroupAdapter, choiceGroupAdapter.getPosition(getResult()), new DialogInterface.OnClickListener()
		{
			// @Override
			public void onClick(DialogInterface dialog, int which)
			{
				setData(choiceGroupAdapter.getSaveValue(which));
				
				setData1(choiceGroupAdapter.getName(which));
				cSpinner.setText(choiceGroupAdapter.getName(which));
				if(item.isRefreshItem())
					handler.sendMessage(handler.obtainMessage(Constants.CustomGridType.REFRESHITEM, choiceGroupAdapter.getSaveValue(which)));
				
				invalidate();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			// @Override
			public void onClick(DialogInterface dialog, int which)
			{
				
			}
		});
		singleChoiceDialog = builder.create();
		singleChoiceDialog.show();
	}
	
	private LayoutParams getCurLayoutParams()
	{
		LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		return layoutParams;
	}
	
}
