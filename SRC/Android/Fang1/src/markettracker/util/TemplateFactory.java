package markettracker.util;

import com.rh.fang.zs.R;

import java.util.List;

import markettracker.data.DicData;
import markettracker.data.Panal;
import markettracker.data.Sqlite;
import markettracker.data.TemGroupList;
import markettracker.data.Template;
import markettracker.data.TemplateGroup;
import markettracker.data.UIItem;
import markettracker.util.Constants.ControlType;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

public class TemplateFactory {

	public static int getCallPlanTitleWidth() {
		return Tool.getScreenWidth() / 5;
	}

	// 进店模板
	public static Template getStartTemplate() {
		Template t = new Template();
		t.setType("-1");
		t.setOnlyType(-1);
		t.setName("进店");

		return t;
	}

	// 出店模板
	public static Template getLeaveTemplate() {
		Template t = new Template();
		t.setType("-2");
		t.setOnlyType(-2);
		t.setName("出店");

		return t;
	}

	public static TemGroupList getTASKTemplateGroupList() {
		TemGroupList list = new TemGroupList();
		TemplateGroup group = new TemplateGroup();
		group.setName("事件填写");
		Template t = new Template();
		t.setType("9");
		t.setOnlyType(9);
		t.setName("事件报告");
		group.setTemplate(t);
		list.setTempGroup(group);

		return list;
	}


	public static TemGroupList getTemplateGroupList(Context context) {
		TemGroupList list = new TemGroupList();

		TemplateGroup group = new TemplateGroup();
		group.setName("问题");

		group.setImgId(R.drawable.ppxs);
		Template t = new Template();
		t.setType("10000");
		t.setOnlyType(10000);
		// t.setMustComplete(true);
		t.setName("问题列表");
		group.setTemplate(t);

		list.setTempGroup(group);

		return list;
	}

	public static TemGroupList getProductGroupList(Context context) {
		TemGroupList list = new TemGroupList();
		TemplateGroup group = new TemplateGroup();
		group.setImgId(R.drawable.jptx);
		group.setName("新品列表");
		addTemplate(context, group, "-10");

		list.setTempGroup(group);

		return list;
	}

	private static void addTemplate(Context context, TemplateGroup group,
			String dictId) {
		List<DicData> dictList = Sqlite.getDictDataList(context, dictId, "");

		Template t = null;
		for (DicData dic : dictList) {
			t = new Template();
			t.setType("6001");
			t.setOnlyType(6001);
			t.setName(dic.getItemname());
			t.setValue(dic.getValue());

			// if (type == 1 || type == 6)
			// t.setMutiInput(true);
			group.setTemplate(t);
		}
	}



	// 请假
	public static Template getQJTemplate() {
		Template t = new Template();
		t.setType("51");
		t.setName("请假");
		t.setOnlyType(51);
		// t.setPhoto(true);
		Panal panal = new Panal();
		panal.setCaption("时间");
		panal.setType(Constants.PanalType.PANEL);
		UIItem item = new UIItem();
		item.setCaption("开始时间");
		item.setControlType(ControlType.DATATIME);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setVerifytype("text");
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("str1");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("结束时间");
		item.setControlType(ControlType.DATATIME);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setVerifytype("text");
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("str2");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		t.setPanal(panal);

		panal = new Panal();
		panal.setCaption("原因说明");
		panal.setType(Constants.PanalType.PANEL);
		item = new UIItem();
		item.setCaption("开始时间");
		item.setControlType(ControlType.TEXT);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setVerifytype("text");
		item.setShowLable(false);
		item.setMaxLength(50);
		item.setGravity(Gravity.CENTER_VERTICAL);
		item.setDataKey("str3");
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		t.setPanal(panal);

		return t;
	}


	private static Template getQuestionTemplate() {
		Template t = new Template();
		t.setType("1");
		t.setName("问题报告");
		t.setOnlyType(1);

		Panal panal = new Panal();
		panal.setCaption("问题");
		panal.setType(Constants.PanalType.PANEL);
		UIItem item = new UIItem();
		item.setCaption("名称");
		item.setControlType(ControlType.TEXT);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("wenti");
		item.setMustInput(true);
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("分类");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setVerifytype("phone");
		item.setDicId("1");
		item.setDataKey("fenleiname");
		item.setMustInput(true);
		item.setOrientation(LinearLayout.HORIZONTAL);
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setTitleWidth(getCallPlanTitleWidth());
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("等级");
		item.setControlType(ControlType.SINGLECHOICE);
		item.setDicId("2");
		item.setMustInput(true);
		item.setDataKey("dengji");
		item.setOrientation(LinearLayout.HORIZONTAL);
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setTitleWidth(getCallPlanTitleWidth());
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("备注");
		item.setControlType(ControlType.TEXT);
		item.setDicId("2");
//		item.setMustInput(true);
		item.setDataKey("beizhu");
		item.setOrientation(LinearLayout.HORIZONTAL);
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setTitleWidth(getCallPlanTitleWidth());
		panal.setItem(item);
		
		t.setPanal(panal);

		//
		// DBConfig
		// 1

		return t;
	}
	private static Template getQuestionOKTemplate() {
		Template t = new Template();
		t.setType("2");
		t.setName("确认问题");
		t.setOnlyType(2);

		Panal panal = new Panal();
		panal.setCaption("问题");
		panal.setType(Constants.PanalType.PANEL);
		UIItem item = new UIItem();
		item.setCaption("名称");
		item.setControlType(ControlType.NONE);
		item.setTitleWidth(getCallPlanTitleWidth());
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setDataKey("wenti");
//		item.setMustInput(true);
		item.setOrientation(LinearLayout.HORIZONTAL);
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("分类");
		item.setControlType(ControlType.NONE);
		item.setVerifytype("phone");
		item.setDicId("1");
		item.setDataKey("fenleiname");
//		item.setMustInput(true);
		item.setOrientation(LinearLayout.HORIZONTAL);
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setTitleWidth(getCallPlanTitleWidth());
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("等级");
		item.setControlType(ControlType.NONE);
		item.setDicId("2");
//		item.setMustInput(true);
		item.setDataKey("dengji");
		item.setOrientation(LinearLayout.HORIZONTAL);
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setTitleWidth(getCallPlanTitleWidth());
		panal.setItem(item);

		item = new UIItem();
		item.setCaption("备注");
		item.setControlType(ControlType.TEXT);
		item.setDicId("2");
//		item.setMustInput(true);
		item.setDataKey("beizhu");
		item.setOrientation(LinearLayout.HORIZONTAL);
		item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		item.setTitleWidth(getCallPlanTitleWidth());
		panal.setItem(item);
		
		t.setPanal(panal);

		//
		// DBConfig
		// 1

		return t;
	}
	public static Template getTemplate(String type) {
		if (type.equals("1")) {
			return getQuestionTemplate();
		} 
		else if (type.equals("2")) {
			return getQuestionOKTemplate();
		} 
		
		

		return null;
	}
}
