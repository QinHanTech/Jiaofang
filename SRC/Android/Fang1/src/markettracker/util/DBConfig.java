package markettracker.util;

import java.util.ArrayList;
import java.util.List;

import markettracker.data.DBDetailConfig;
import markettracker.data.DBMainConfig;

public class DBConfig {

	private static List<DBMainConfig> tableList;

	public static DBMainConfig getDBMainConfig(String type) {
		for (DBMainConfig dc : getTableList()) {
			if (dc.getTableName().equalsIgnoreCase(type))
				return dc;
		}
		return null;
	}

	private static List<DBMainConfig> getDBConfig() {
		tableList = new ArrayList<DBMainConfig>();
		
		
		tableList.add(getDictionary());
		tableList.add(getPlan());
		
		tableList.add(getCallReport());
		tableList.add(getCallDetail());
		tableList.add(getCallPhoto());

		tableList.add(getMessage());
		
		tableList.add(getJxc_xq());
		tableList.add(getJxc_loudong());
		
		tableList.add(getJxc_hu());
		
		tableList.add(getJxc_base());
		
		
		return tableList;
	}
	
	/**
	 * 
	 */
	private static DBMainConfig getJxc_base() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("jxc_base");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("basename");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("type");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}
	
	/**
	 * 
	 */
	private static DBMainConfig getJxc_hu() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("jxc_hu");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("huname");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("hzname");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("xqid");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ldid");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("mobile");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("beizhu");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("huxing");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("jfzt");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("louceng");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("husn");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("lastusername");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("jishu");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("wentisn");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("pdfjishu");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	/**
	 * 
	 */
	private static DBMainConfig getJxc_loudong() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("jxc_loudong");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ldname");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("xqid");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("status");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("mianji");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("gaodu");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("cenggao");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("lhl");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("jfl");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("jfrq");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("hunums");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}
	/**
	 * 
	 */
	private static DBMainConfig getJxc_xq() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("jxc_xq");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("xmmc");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("gg");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("address");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("beizhu");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("status");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("kfgs");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("dongshu");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("zonghushu");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		
		d = new DBDetailConfig();
		d.setFieldName("fzr");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}
	

	/**
	 * 
	 */
	private static DBMainConfig getMessage() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_Message_Detail");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("ServerId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Title");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("errmsg");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Content");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Stime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Sender");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("status");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	/**
	 * 报告附件
	 */
	private static DBMainConfig getCallPhoto() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("t_data_callReportPhoto");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("serverId");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ShotTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("lShotTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("QuestionnaireId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("loginmobileTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("mobileTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("loginTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("PhotoType");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ProductId");
		d.setType(Constants.FieldType.STRING);
//		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DisplayType");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("callReportId");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Photo");
		d.setType(Constants.FieldType.BLOB);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DisplayObject");
		d.setType(Constants.FieldType.STRING);
//		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		d.setUpload(false);
		m.setField(d);

		return m;
	}

	/**
	 * 报告明细
	 */
	private static DBMainConfig getCallDetail() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("t_data_callReportDetail");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("serverId");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ProductId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("callReportId");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("remark");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT1");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT2");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT3");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT4");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT5");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT6");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT7");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT8");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT9");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT10");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("STR1");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR2");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR3");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR4");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR5");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR6");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR7");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR8");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR9");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR10");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("decimal1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	/**
	 * 报告主表
	 */
	private static DBMainConfig getCallReport() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("t_data_callReport");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("serverId");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		d.setQuery(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("onlyType");
		d.setType(Constants.FieldType.STRING);
		d.setUpload(false);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("TemplateId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("errmsg");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ClientType");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ClientId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ReportDate");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("remark");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT1");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT2");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT3");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT4");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT5");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT6");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT7");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT8");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT9");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT10");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT11");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT12");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT13");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT14");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT15");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT16");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT17");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT18");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("INT20");
		d.setType(Constants.FieldType.INT);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("INT19");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("STR1");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR2");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR3");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR4");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR5");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR6");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR7");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR8");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR9");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("STR10");
		d.setType(Constants.FieldType.STRING);
		d.setReplace(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("decimal1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("decimal10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	/**
	 * 计划表
	 */
	private static DBMainConfig getPlan() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_Visit_Plan_Detail");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("serverId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("PlanId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ClientType");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("ClientId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("VisitTime");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("IsApproval");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Remark");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Analysis");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("Edate");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("Sdate");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("Type");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	/**
	 * 参数数据字典
	 */
	private static DBMainConfig getDictionary() {
		DBMainConfig m = null;
		DBDetailConfig d = null;

		m = new DBMainConfig();
		m.setTableName("T_Sys_Dictionary");
		m.setKey("serverid");

		d = new DBDetailConfig();
		d.setFieldName("serverId");
		d.setType(Constants.FieldType.STRING);
		d.setUnique(true);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DictId");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DictType");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DictClass");
		d.setType(Constants.FieldType.INT);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DictName");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("Remark");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("DictValue");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str1");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str2");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str3");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str4");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str5");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		d = new DBDetailConfig();
		d.setFieldName("str6");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str7");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str8");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str9");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);
		d = new DBDetailConfig();
		d.setFieldName("str10");
		d.setType(Constants.FieldType.STRING);
		m.setField(d);

		return m;
	}

	

	public static List<DBMainConfig> getTableList() {
		if (tableList == null)
			return getDBConfig();
		return tableList;
	}

	public static void setTableList(List<DBMainConfig> tableList) {
		DBConfig.tableList = tableList;
	}

}