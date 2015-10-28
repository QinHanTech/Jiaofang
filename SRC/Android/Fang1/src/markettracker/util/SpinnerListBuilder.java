package markettracker.util;

import com.rh.fang.zs.R;

import java.util.ArrayList;
import java.util.List;

import markettracker.util.Constants.ControlType;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Sqlite;
import markettracker.data.UIItem;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class SpinnerListBuilder implements OnClickListener
{
	
	private CGrid mCustomGrid;
	private LinearLayout mainLine;
	
	private EditText editText;
	
	private Button mButton;
	private Context mContext;
	
//	private UIItem item;
	
	private Dialog alert;
	
	public SpinnerListBuilder(Context context, UIItem item, Fields data, Handler handler)
	{
		// super(context);
		// initHandler();
		init(context, item, data, handler);
	}
	
	// private void initeditText(final Context context, final Handler handler)
	// {
	// LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
	// LayoutParams.WRAP_CONTENT);
	// editText = new EditText(context);
	// editText.setHint("至少输入2个字符");
	//
	// editText.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.round));
	// //
	// editText.setBackgroundColor(context.getResources().getColor(android.R.color.white));
	// // layoutParams.bottomMargin = 5;
	// editText.setLayoutParams(layoutParams);
	// editText.addTextChangedListener(new TextWatcher()
	// {
	//
	// public void onTextChanged(CharSequence s, int start, int before, int
	// count)
	// {
	// // TODO Auto-generated method stub
	// Editable editable = editText.getText();
	// int len = editable.length();
	// if (len >= 2)
	// {
	// FieldsList list = Sqlite.getFieldsList(context, 1, editable.toString());
	// mCustomGrid.setDataInfo(getHocItem(), list);
	// mCustomGrid.invalidate();
	// }
	//
	// }
	//
	// public void beforeTextChanged(CharSequence s, int start, int count, int
	// after)
	// {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// public void afterTextChanged(Editable s)
	// {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// }
	
	public Fields getSelectData()
	{
		return mCustomGrid.getSelectData();
	}
	
	private void init(Context context, UIItem item, Fields data, Handler handler)
	{
		mContext = context;
//		this.item = item;
		View view = LayoutInflater.from(context).inflate(R.layout.datalist_form, null);
		mButton = (Button) view.findViewById(R.id.back);
		mButton.setOnClickListener(this);
		
		editText = (EditText) view.findViewById(R.id.searchclient);
		editText.addTextChangedListener(new TextWatcher()
		{
			
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				// TODO Auto-generated method stub
				Editable editable = editText.getText();
				// int len = editable.length();
				// if (len >= 2)
				// {
				FieldsList list = Sqlite.getFieldsList(mContext, 14, editable.toString());
				mCustomGrid.setDataInfo(getHocItem(), list);
				mCustomGrid.invalidate();
				// }
				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub
				
			}
		});
		// this.setView(view);
		
		mainLine = (LinearLayout) view.findViewById(R.id.planlist);
		mCustomGrid = new CGrid(context);
		FieldsList list = Sqlite.getFieldsList(context, 14, "");
		mCustomGrid.setDataInfo(getHocItem(), list);
		mCustomGrid.setCustomGridType(Constants.CustomGridType.SELECTDATA);
		mCustomGrid.setLinkHandler(handler);
		
		mainLine.addView(mCustomGrid);
		
		alert = new Dialog(context, R.style.datalist_dialog);
		alert.setContentView(view);
		alert.show();
		alert.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
	}
	
	public void dismiss()
	{
		if (alert != null)
		{
			alert.dismiss();
			alert = null;
			// builder=null;
		}
	}
	
	private List<UIItem> getHocItem()
	{
		
		List<UIItem> itemList = new ArrayList<UIItem>();
		
		UIItem item;
		
		item = new UIItem();
		item.setCaption("门店名称");
		item.setWidth(Tool.getScreenWidth() * 4 / 5);
		item.setAlign(Align.LEFT);
		item.setControlType(ControlType.NONE);
		item.setDataKey("fullname");
		itemList.add(item);
		
		item = new UIItem();
		item.setCaption("次数");
		// item.setAlign(Align.LEFT);
		item.setWidth(Tool.getScreenWidth() / 5);
		item.setControlType(ControlType.NONE);
		item.setDataKey("str1");
		itemList.add(item);
		
		return itemList;
	}
	
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub
		switch (arg0.getId())
		{
		
			case R.id.back:
				dismiss();
				break;
		}
	}
	
}
