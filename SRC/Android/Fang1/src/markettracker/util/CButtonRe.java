package markettracker.util;

import com.rh.fang.zs.R;
import markettracker.data.ButtonConfig;
import markettracker.data.Fields;
import android.annotation.SuppressLint;
import android.content.Context;
import markettracker.data.Template;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
public class CButtonRe extends RelativeLayout
{
	private Fields data;
	
	public CButtonRe(Context context, ButtonConfig config, LayoutParams lay, Template temp, OnClickListener l)
	{
		super(context);
		init(config, lay, context, temp, l);
	}
	
	private void init(ButtonConfig config, LayoutParams lay, Context context, Template temp, OnClickListener l)
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		CButton button = new CButton(context, config, layoutParams, temp, l);
		
		this.setLayoutParams(lay);
		
		this.addView(button);
		
		if (config.getName().equals("消息/问卷"))
		{
			refreshCount();
		}
	}
	
	public void refreshCount()
	{
		int iCount =0;// Sqlite.getUnReadMsgCount(getContext());
		if (iCount > 0)
		{
			TextView txt = new TextView(getContext());
			txt.setText(iCount + "");
			txt.setTextColor(getResources().getColor(R.color.white));
			txt.setTextSize(10.0f);
			txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_circle_red));
			txt.setGravity(Gravity.CENTER);
			
			RelativeLayout.LayoutParams params_br = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// params_br.addRule(RelativeLayout.BELOW, listID2);
			params_br.rightMargin = Tool.dip2px(getContext(), 8);
			params_br.topMargin = Tool.dip2px(getContext(), 1);
			// params_br.rightMargin = Tool.dip2px(getContext(), 20);
			// params_br.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			// params_br.addRule(RelativeLayout.ALIGN_RIGHT,TRUE);
			params_br.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			txt.setLayoutParams(params_br);
			
			this.addView(txt);
		}
		else
		{
			if (this.getChildCount() > 1)
				this.removeViewAt(1);
		}
	}
	
	public String getdate()
	{
		return data.getStrValue("dateofyear");
	}
}
