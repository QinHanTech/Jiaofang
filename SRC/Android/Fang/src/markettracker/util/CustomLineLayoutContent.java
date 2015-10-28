package markettracker.util;


import markettracker.util.Constants.ControlType;
import markettracker.data.Fields;
import markettracker.data.UIItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.LinearLayout;

@SuppressLint("ViewConstructor")
public class CustomLineLayoutContent extends LinearLayout
{
	
	private CEditText cEditText;
	
	private CSpinner spinner;
	private CSelectBox selectBox;
	private CMutiSpinner cMutiSpinner;
	
	private CContent content;
	
	private CDatePicker cdatePicker;
	
	private UIItem mUIItem;
	
	private CSpinnerList cSpinnerList;
	
	private Fields fields;
	
	public CustomLineLayoutContent(Context context, UIItem item, Activity activity, Fields data, Handler handler)
	{
		super(context);
		setUIItem(item);
		fields = data;
		init(context, item, activity, data, handler);
	}
	
	private void setUIItem(UIItem item)
	{
		this.mUIItem = item;
	}
	
	public UIItem getCurUIItem()
	{
		return this.mUIItem;
	}
	
	private LayoutParams getCurLayoutParams()
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.rightMargin = Tool.dip2px(getContext(), 5);
		layoutParams.topMargin = Tool.dip2px(getContext(), 5);
		layoutParams.bottomMargin = Tool.dip2px(getContext(), 5);
		layoutParams.leftMargin = Tool.dip2px(getContext(), 5);
		return layoutParams;
	}
	
	private int getCurGravity()
	{
		return Gravity.CENTER_VERTICAL;
	}
	
	private void init(Context context, UIItem item, Activity activity, Fields data, Handler handler)
	{
		this.setGravity(getCurGravity());
		this.setLayoutParams(getCurLayoutParams());
		if (item.isMustInput())
			this.addView(new CustomImageView(context, null));
		
		// 添加具体控件
		if (item.getControlType() == ControlType.TEXT)
		{
			cEditText = new CEditText(context, item);
			cEditText.setText(data.getStrValue(item.getDataKey()));
			cEditText.addTextChangedListener(new EditTextWatcher(item, cEditText, data, context));
			this.addView(cEditText);
		}
		else if (item.getControlType() == ControlType.SINGLECHOICE)
		{
			spinner = new CSpinner(context, item, data, handler);
			this.addView(spinner);
		}
		else if (item.getControlType() == ControlType.SINGLECHOICELIST)
		{
			cSpinnerList = new CSpinnerList(context, item, data, handler);
			this.addView(cSpinnerList);
		}
		else if (item.getControlType() == ControlType.RADIOBUTTON)
		{
			selectBox = new CSelectBox(context, item, data);
			this.addView(selectBox);
		}
		
		else if (item.getControlType() == ControlType.MULTICHOICE)
		{
			cMutiSpinner = new CMutiSpinner(context, item, data, "");
			this.addView(cMutiSpinner);
		}
		else if (item.getControlType() == ControlType.NONE)
		{
			content = new CContent(context, item);
			content.setText(data.getStrValue(item.getDataKey()));
			this.addView(content);
		}
		else if (item.getControlType() == ControlType.DATATIME || item.getControlType() == ControlType.TIME || item.getControlType() == ControlType.DATE)
		{
			cdatePicker = new CDatePicker(context, item, data, handler);
			this.addView(cdatePicker);
		}
		
	}
	
	public void resetData()
	{
		fields.put(mUIItem.getDataKey(), "");
	}
	
	public void refresh()
	{
		cdatePicker.refresh();
	}
}
