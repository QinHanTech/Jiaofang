package markettracker.util;

import markettracker.data.Template;
import com.rh.fang.zs.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class CTextView extends TextView
{
	
	private Template template;
	
	public CTextView(Context context, Template temp)
	{
		super(context);
		template = temp;
		this.setTextColor(Tool.getTextColor(context));
		// this.setText("  " + temp.getName());
		this.setTextSize(16.0f);
		if (temp.getDescription() != null)
		{
			this.setText(temp.getName() + "\r\n    " + temp.getDescription());
			highlight(template.getName().length(), this.getText().toString().length());
		}
		else
		{
			if(temp.isSubmit())
			{
				this.setText(temp.getName()+"(已上传)");
				highlight(template.getName().length(), this.getText().toString().length());
			}
			
			else {
				this.setText(temp.getName());
			}
			
		}
		
//		this.setGravity(Gravity.CENTER_VERTICAL);
//		this.setPadding(10, 0, 20, 0);
//		this.setCompoundDrawablePadding(10);
//		this.setLayoutParams(getCurLayoutParams());
//		this.setBackgroundResource(R.drawable.table_mid);
//		if (template.isComplete())
//			this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(1), null, getDrawable(2), null);
//		else
//			this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(0), null, getDrawable(2), null);
		
//		this.setText(temp.getName());
		this.setCompoundDrawablePadding(getH(5));
		this.setTextSize(16.0f);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setPadding(getH(20), 0, getH(15), 0);
		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.tablemid);
		if (template.isComplete())
			this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(1), null, getDrawable(2), null);
		else
			this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(0), null, getDrawable(2), null);
		// this.setOnClickListener(this);
	}
	
	private int getH(int dip)// Tool.getFontHeight(16)*2
	{
		return Tool.dip2px(getContext(), dip);
	}
	
	private void highlight(int start, int end)
	{
		SpannableStringBuilder spannable = new SpannableStringBuilder(getText().toString());// 用于可变字符串
		spannable.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new RelativeSizeSpan(0.6f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		setText(spannable);
	}
	
	private Drawable getDrawable(int type)
	{
		if (type == 0)
			return getResources().getDrawable(R.drawable.checkbox_off);
		else if (type == 1)
			return getResources().getDrawable(R.drawable.checkbox_click2);
		else
			return getResources().getDrawable(R.drawable.arrow);
	}
	
	private LayoutParams getCurLayoutParams()
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, Tool.dip2px(getContext(), 50));
		layoutParams.topMargin=1;
		return layoutParams;
	}
	
	public void change2Complete()
	{
		this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(1), null, getDrawable(2), null);
		template.setComplete(true);
	}
	
	private void change2pending()
	{
		this.setCompoundDrawablesWithIntrinsicBounds(getDrawable(0), null, getDrawable(2), null);
		template.setComplete(false);
	}
	
	public void changeStatus()
	{
		if (template.isComplete())
			change2pending();
		else
			change2Complete();
	}
	
	public boolean isComplete()
	{
		return template.isComplete();
	}
	
	public String getTemplateType()
	{
		return template.getType();
	}
	
	public String getTemplateValue()
	{
		return template.getValue();
	}
	
	public String getOnlyType()
	{
		return template.getOnlyType() + "";
	}
	
	public int getInputType()
	{
		return template.getInputType();
	}
	
	public String getTemplateName()
	{
		return template.getName();
	}
}
