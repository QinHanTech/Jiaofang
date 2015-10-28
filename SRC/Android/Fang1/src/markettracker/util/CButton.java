package markettracker.util;

import com.rh.fang.zs.R;
import markettracker.data.ButtonConfig;
import markettracker.data.Fields;
import android.content.Context;
import android.graphics.Typeface;
import markettracker.data.Template;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class CButton extends TextView
{
	private Fields data;
	
	public CButton(Context context, ButtonConfig config, LayoutParams lay, Template temp, OnClickListener l)
	{
		super(context);
		init(config, lay, context, temp,l);
	}
	
	public CButton(Context context, ButtonConfig config, LayoutParams lay)
	{
		super(context);
		init(config, lay, context);
	}
	
	private void init(ButtonConfig config, LayoutParams lay, Context context, Template temp, OnClickListener l)
	{
		this.setId(config.getId());
		this.setCompoundDrawablesWithIntrinsicBounds(null, Tool.getDrawable(context, config.getName(), false), null, null);
		this.setPadding(0, Tool.dip2px(context, 5), 0, 0);
		this.setLayoutParams(lay);
		this.setGravity(Gravity.CENTER);
		
		this.setText(config.getName());
		this.setTextSize(10);
		if (isSameName(config.getName(), temp))
			this.setEnabled(false);
		if (config.getName().equals("门店销量") || config.getName().equals("美顾考勤"))
			startFlick(this);
		this.setOnClickListener(l);
		// this.setClickable(false);
	}
	
	private void startFlick(View view)
	{
		if (null == view)
		{
			return;
		}
		Animation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setDuration(1000);
		alphaAnimation.setInterpolator(new LinearInterpolator());
		alphaAnimation.setRepeatCount(Animation.INFINITE);
		alphaAnimation.setRepeatMode(Animation.REVERSE);
		view.startAnimation(alphaAnimation);
	}
	
	public void stopFlick(View view)
	{
		if (null == view)
		{
			return;
		}
		view.clearAnimation();
	}
	
//	private void highlight(int start, int end)
//	{
//		SpannableStringBuilder spannable = new SpannableStringBuilder(getText().toString());// 用于可变字符串
//		ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
//		spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		setText(spannable);
//	}
	
	private void init(ButtonConfig config, LayoutParams lay, Context context)
	{
		this.setId(config.getId());
		this.setCompoundDrawablesWithIntrinsicBounds(null, Tool.getDrawable(context, config.getName(), false), null, null);
		this.setPadding(0, 2, 0, 0);
		this.setLayoutParams(lay);
		this.setGravity(Gravity.CENTER);
		this.setText(config.getName());
		this.setTextSize(10);
		// this.setClickable(false);
	}
	
	private boolean isSameName(String name, Template temp)
	{
		if (temp == null)
			return false;
		else if (!temp.havePanal() && name.equals("基本信息"))
			return true;
		else if (!temp.haveTable() && name.equals("点库存"))
			return true;
		else if (name.equals("签名"))
			return true;
		return false;
	}
	
	public CButton(Context context, Fields data, OnClickListener listener, OnLongClickListener longClickListener)
	{
		super(context);
		this.data = data;
		init(context, data, listener, longClickListener);
	}
	
	private void init(Context context, Fields data, OnClickListener listener, OnLongClickListener longClickListener)
	{
		this.setText(data.getStrValue("date"));
		this.setTextSize(12);
		this.setTypeface(Typeface.DEFAULT_BOLD);
		// if (data.getStrValue("week").equals("周六")
		// || data.getStrValue("week").equals("周日"))
		// this.setTextColor(getResources().getColor(R.color.txtcolor));
		// else
		
		// if (Rms.getCompanyId(context).equals("1"))
		// this.setTextColor(getResources().getColor(R.color.button_text));
		// else {
		this.setTextColor(getResources().getColor(R.color.black));
		// }
		this.setLayoutParams(new LinearLayout.LayoutParams(Tool.dip2px(getContext(), 38), Tool.dip2px(getContext(), 32)));
		if (data.getStrValue("select").equals("1"))
			this.setBackgroundDrawable(getResources().getDrawable(R.drawable.addhoc));
		else
			this.setBackgroundDrawable(getResources().getDrawable(R.drawable.round));
		this.setGravity(Gravity.CENTER);
		this.setOnClickListener(listener);
		this.setOnLongClickListener(longClickListener);
		// this.seto
		
	}
	
	public String getdate()
	{
		return data.getStrValue("dateofyear");
	}
}
