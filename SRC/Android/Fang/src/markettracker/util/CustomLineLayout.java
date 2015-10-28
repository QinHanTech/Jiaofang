package markettracker.util;

import markettracker.data.Fields;
import markettracker.data.UIItem;
import com.rh.fang.jf.R;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener; //import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CustomLineLayout extends LinearLayout implements OnClickListener
{
	
	private Context context;
	private CustomLineLayoutContent customLineLayoutContent;
	private CustomTextView customTextView;
	
	public CustomLineLayout(Context context, UIItem item, Activity activity, Fields data, Handler handler)
	{
		super(context);
		this.context = context;
		init(context, item, activity, data, handler);
	}
	
	private LayoutParams getCurLayoutParams()
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.topMargin = 1;
		return layoutParams;
	}
	
	private void init(Context context, UIItem item, Activity activity, Fields data, Handler handler)
	{
		this.setOrientation(item.getOrientation());
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setLayoutParams(getCurLayoutParams());
		this.setBackgroundResource(R.drawable.tablemid);
		
		// //添加标题
		if (item.isShowLable())
		{
			customTextView = new CustomTextView(context, item, activity);
			// mCustomTextView.setOnClickListener(this);
			this.addView(customTextView);
		}
		customLineLayoutContent = new CustomLineLayoutContent(context, item, activity, data, handler);
		this.addView(customLineLayoutContent);
		
		// if (item.getSpinnerType() == 1 || item.getSpinnerType() == 3) {
		// mCustomLineLayoutContent.setListener(mItem, mData, null, handler);
		// }
		// else if (item.getSpinnerType() == 2) {
		// List<CustomLineLayoutContent> content = new
		// ArrayList<CustomLineLayoutContent>();
		// CustomLineLayoutContent cc = null;
		// CustomTextView ct;
		// for (UIItem item1 : item.getReItemlist()) {
		// cc = new CustomLineLayoutContent(context, item1, activity, data);
		// //
		// mCustomSpinner.setSelection(mCustomSpinner.getPosition(data.getStringMember((item.mRelDataId))));
		// content.add(cc);
		//
		// if (item1.isShowLable()) {
		// ct = new CustomTextView(context, item1, activity);
		// ct.setOnClickListener(this);
		// this.addView(ct);
		// }
		// this.addView(cc);
		// }
		// if (item.getControlType() == ControlType.SINGLECHOICE)
		// mCustomLineLayoutContent.setListener(mItem, mData, content,
		// null);
		// // for (UIItem item1 : item.mListReItem)
		// // cc.setSelection(data.getStringMember((item1.mRelDataId)));
		// // mCustomLineLayoutContent.setReItem(content);
		// }
		
		// else if (item.getSpinnerType() == 5) {
		// // mCustomLineLayoutContent.setListener(mItem, mData, null,
		// // handler);
		// List<CustomLineLayoutContent> content = new
		// ArrayList<CustomLineLayoutContent>();
		// CustomLineLayoutContent cc = null;
		// CustomTextView ct;
		// for (UIItem item1 : item.getReItemlist()) {
		// cc = new CustomLineLayoutContent(context, item1, activity, data);
		// //
		// mCustomSpinner.setSelection(mCustomSpinner.getPosition(data.getStringMember((item.mRelDataId))));
		// content.add(cc);
		//
		// if (item1.isShowLable()) {
		// ct = new CustomTextView(context, item1, activity);
		// ct.setOnClickListener(this);
		// this.addView(ct);
		// }
		// this.addView(cc);
		// }
		// if (item.getControlType() == ControlType.SINGLECHOICE)
		// mCustomLineLayoutContent.setListener(mItem, mData, content,
		// handler);
		// // for (UIItem item1 : item.mListReItem)
		// // cc.setSelection(data.getStringMember((item1.mRelDataId)));
		// // mCustomLineLayoutContent.setReItem(content);
		// }
		
		// else if (item.getSpinnerType() == 4) {
		// List<CustomLineLayoutContent> content = new
		// ArrayList<CustomLineLayoutContent>();
		// CustomLineLayoutContent cc = null;
		// CustomTextView ct;
		// for (UIItem item1 : item.getReItemlist()) {
		// cc = new CustomLineLayoutContent(context, item1, activity, data);
		// //
		// mCustomSpinner.setSelection(mCustomSpinner.getPosition(data.getStringMember((item.mRelDataId))));
		// content.add(cc);
		//
		// if (item1.isShowLable()) {
		// ct = new CustomTextView(context, item1, activity);
		// ct.setOnClickListener(this);
		// this.addView(ct);
		// }
		// if (data.getStrValue((item.getDataKey())).equals("否"))
		// cc.setVisibility(View.GONE);
		// else
		// cc.setVisibility(View.VISIBLE);
		//
		// this.addView(cc);
		//
		// }
		// if (item.getControlType() == ControlType.SINGLECHOICE)
		// mCustomLineLayoutContent.setListener(mItem, mData, content,
		// null);
		// }
	}
	
	public void refresh()
	{
		customLineLayoutContent.refresh();
	}
	
	public void resetData()
	{
		customLineLayoutContent.resetData();
	}
	
	public void onClick(View v)
	{
		try
		{
			Toast.makeText(context, customTextView.getText().toString().trim(), Toast.LENGTH_SHORT).show();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
}
