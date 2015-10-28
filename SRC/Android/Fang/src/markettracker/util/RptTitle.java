package markettracker.util;

import markettracker.data.Panal;
import com.rh.fang.jf.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("ViewConstructor")
public class RptTitle extends TextView
{
	
	public RptTitle(Context context, String title)
	{
		super(context);
		this.setTextColor(Tool.getTextColor(context));
		this.setText(title);
		
		this.setTextSize(15);
		this.setPadding(Tool.dip2px(getContext(), 10), 1, Tool.dip2px(getContext(), 15), 1);
		this.setGravity(Gravity.CENTER_VERTICAL);
		
		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.tabletop);
	}
	
	public RptTitle(Context context, Panal panal)
	{
		super(context);
		this.setTextColor(Tool.getTextColor(context));
		this.setText(panal.getCaption());
		
		this.setTextSize(15);
		this.setPadding(Tool.dip2px(getContext(), 10), 1, Tool.dip2px(getContext(), 15), 1);
		this.setGravity(Gravity.CENTER_VERTICAL);
		
		this.setLayoutParams(getCurLayoutParams());
		
		// if (panal.getPanalType() == 1)
		// {
		// this.setLayoutParams(getSurveyLayoutParams());
		// this.setBackgroundResource(R.drawable.tabletop);
		// }
		// else
		// {
		this.setMinHeight(Tool.dip2px(getContext(), 40));
		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.tabletop);
		
	}
	
	private LayoutParams getCurLayoutParams()
	{
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, Tool.dip2px(getContext(), 35));
		layoutParams.topMargin = Tool.dip2px(getContext(), 1);
		return layoutParams;
	}
	
//	private LayoutParams getSurveyLayoutParams()
//	{
//		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//		layoutParams.topMargin = Tool.dip2px(getContext(), 1);
//		return layoutParams;
//	}
	
}
