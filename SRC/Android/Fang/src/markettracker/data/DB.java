package markettracker.data;

import java.util.ArrayList;
import java.util.List;

import markettracker.util.Constants.ControlType;
import markettracker.util.Constants.TableInfo;
import markettracker.util.Constants.DataBaseName;
import markettracker.util.Constants;
import markettracker.util.DBConfig;
import markettracker.util.Tool;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase; //import android.database.sqlite.SQLiteException;

import android.util.Base64;
import android.view.Gravity;
import android.widget.LinearLayout;

/**
 * @author Bruce
 * 
 */
@SuppressLint("DefaultLocale")
public class DB {

	// private static final String TAG = "parrow";

	private static final String SERVERID = "ServerId";

	private SQLiteDatabase db;
	private static final int TRANCOUNT = 50;
	private Context context;

	public DB(Context context) {
		try {
			init(context);
		} finally {
			if (db != null)
				db.close();
		}
	}

	private void init(Context context) {
		initContext(context);
		initDB();
		setUpDb();
	}

	public synchronized String addDate(String date, String days) {

		Cursor c = null;

		try {
			openDatabase();
			c = db.rawQuery("SELECT date('" + date + "','" + days
					+ " days') as date", null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					date = getString(c, "date");
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return date;
	}

	private synchronized void initDB() {
		db = openOrCreateDatabase();
		db.setLockingEnabled(false);
	}

	public synchronized List<String> getTemplateIdList(String code, String key) {
		List<String> list = new ArrayList<String>();
		String templateid = "";
		String strSql = "select onlytype from t_data_callreport where reportdate='"
				+ Tool.getCurrDate()
				+ "' and ClientId='"
				+ code
				+ "' and decimal10='" + key + "'";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					templateid = getString(c, "onlyType");
					list.add(templateid);
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

	public synchronized List<Fields> getProductIdList() {
		List<Fields> list = new ArrayList<Fields>();
		Fields tempFields;
		String strSql = "select ClientId,issubmit from t_data_callreport where reportdate='"
				+ Tool.getCurrDate() + "' and onlyType=6001";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					tempFields = new Fields();
					tempFields.put("ClientId", getString(c, "ClientId"));
					tempFields.put("issubmit", getString(c, "issubmit"));
					// templateid = getString(c, "ClientId");
					list.add(tempFields);
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

	private synchronized SQLiteDatabase openOrCreateDatabase() {
		SQLiteDatabase db = null;
		// try {
		db = context.openOrCreateDatabase(DataBaseName.DATABASE_NAME,
				Context.MODE_PRIVATE, null);

		// } catch (SQLiteException ex) {
		// String err = ex.getMessage();
		// }

		return db;
	}

	private synchronized void setUpDb() {
		String strSql = "";
		List<String> listSql = new ArrayList<String>();
		List<DBMainConfig> list = DBConfig.getTableList();
		for (DBMainConfig dm : list) {
			strSql = "CREATE TABLE IF NOT EXISTS "
					+ Tool.toLowerCase(dm.getTableName()) + "( "
					+ TableInfo.TABLE_KEY + " INTEGER PRIMARY KEY not null ,";
			for (DBDetailConfig dc : dm.getFieldList()) {
				strSql += Tool.toLowerCase(dc.getFieldName()) + " "
						+ dc.getType();
				if (!dc.isNull())
					strSql += " not null ";
				if (dc.isUnique())
					strSql += " UNIQUE ";
				strSql += ",";
			}
			strSql += "issubmit INTEGER DEFAULT -1, "
					+ "isdel INTEGER DEFAULT 0, "

					+ "updatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
					+ "inserttime TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
			listSql.add(strSql);
		}
		execSqlList(listSql);
	}

	public synchronized int getUnSubmitRptCount() {
		int count = 0;
		String strSql = "select count(*) as count from t_data_callreport where issubmit=0 and ReportDate='"
				+ Tool.getCurrDate() + "'";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					count = c.getInt(c.getColumnIndex("count"));

				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return count;
	}

	public synchronized int getUnReadMsgCount() {
		int count = 0;
		String strSql = "select sum(count) as count from (select count(*) as count from t_message_detail where status!='已读' union "
				+ "select count(*) as count from t_psq_payout where clienttype=0 and issubmit !=1)";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					count = c.getInt(c.getColumnIndex("count"));

				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return count;
	}

	// private SQLiteDatabase getDB() {
	// if (mDB == null) {
	// mDB = openOrCreateDatabase();
	// mDB.setLockingEnabled(false);
	// }
	// return mDB;
	// }

	private synchronized void openDatabase() {
		if (!isOpen())
			db = openOrCreateDatabase();
	}

	private synchronized void closeDatabase() {
		if (isOpen())
			db.close();
	}

	private synchronized void beginTransaction() {
		try {
			if (inTransaction())
				endTransaction();
			db.beginTransaction();
		} catch (IllegalStateException e) {
		}
	}

	private boolean isOpen() {
		return db.isOpen();
	}

	private boolean inTransaction() {
		return db.inTransaction();
	}

	private synchronized void setTransactionSuccessful() {
		if (inTransaction())
			db.setTransactionSuccessful();
	}

	private synchronized void endTransaction() {
		if (inTransaction())
			db.endTransaction();
	}

	private String replaceSql(RowData rowData, DBMainConfig dc) {
		String replaceSql = "REPLACE into ";
		replaceSql += dc.getTableName() + "( " + SERVERID + ", ";
		for (DBDetailConfig detail : dc.getFieldList()) {
			if (detail.isDownLoad())
				replaceSql += Tool.toLowerCase(detail.getFieldName()) + ",";
		}
		replaceSql = Tool.getSubString(replaceSql) + ") VALUES (";
		replaceSql += "'" + rowData.getString(SERVERID) + "' ,";
		for (DBDetailConfig detail : dc.getFieldList()) {
			if (detail.isDownLoad()) {
				replaceSql += "'" + rowData.getString(detail.getFieldName())
						+ "' ,";
			}

		}
		replaceSql = Tool.getSubString(replaceSql) + ");";
		return replaceSql;
	}

	private String deletSql(RowData object, DBMainConfig dc) {
		String deleteSql = "DELETE FROM " + dc.getTableName() + " where "
				+ Tool.toLowerCase(dc.getKey()) + "='"
				+ object.getString(SERVERID) + "'";
		return deleteSql;
	}

	public synchronized long upsertData(QueryDataResult queryDataResult)
			throws Exception {
		long rowId = -1;
		String type = queryDataResult.getString(QueryDataResult.TYPE);
		// if (type.equalsIgnoreCase("T_Message_Detail")) {
		// rowId = upsertMsg(queryDataResult.getDataList());
		// } else {

		DBMainConfig dc = null;
		// if (type.equalsIgnoreCase("T_DBFrams"))
		dc = DBConfig.getDBMainConfig(type);
		// else
		// dc = getDbMainConfig(type, true);

		if (dc == null)
			return -1;
		List<String> listSql = new ArrayList<String>();
		if (queryDataResult.size() > 0) {
			for (RowData object : queryDataResult.getDataList()) {
				if (object != null) {
					if (Tool.isDel(object.getString("IsDel")))
						listSql.add(deletSql(object, dc));
					else
						listSql.add(replaceSql(object, dc));
				}
			}
			execSqlList(listSql);

			// if (type.equalsIgnoreCase("T_DBFrams"))
			// updateDb();
		}
		rowId = 1;
		// }
		return rowId;
	}

	// public synchronized long upsertData(List<SObject> list, String type)
	// throws Exception {
	// long rowId = -1;
	// if (type.equalsIgnoreCase("T_Message_Detail")) {
	// rowId = upsertMsg(list);
	// } else {
	// DBMainConfig dc = DBConfig.getDBMainConfig(type);
	// if (dc == null)
	// return -1;
	// List<String> listSql = new ArrayList<String>();
	//
	// for (SObject object : list) {
	// if (object != null) {
	// if (Tool.isDel(getSObjectValue(object, "IsDel")))
	// listSql.add(deletSql(object, dc));
	// else
	// listSql.add(replaceSql(object, dc));
	// }
	// }
	// // for (String sql : listSql)
	// // System.out.println(sql);
	// execSqlList(listSql);
	// rowId = 1;
	// }
	// return rowId;
	// }

	public synchronized String getDate(String str) {

		String date = Tool.getCurrDate();
		Cursor c = null;

		try {
			openDatabase();
			c = db.rawQuery(str, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					date = getString(c, "date");
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return date;
	}

	public synchronized List<Louceng> getLouceng(String loudong) {
		List<Louceng> list = new ArrayList<Louceng>();
		Louceng louceng = null;
		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(
					"select * from jxc_hu where ldid='"+loudong+"' order by cast( louceng as int ) desc,husn",
					null);
			if (c != null && c.getCount() > 0) {

				String oldLouceng = "", newLouceng = "";
				Fields fields;
				c.moveToFirst();
				do {
					newLouceng = getString(c, "louceng");

					if (!newLouceng.equals(oldLouceng)) {
						louceng = new Louceng();
						louceng.setName(newLouceng);
						list.add(louceng);
					}
					fields = new Fields();
					for (int i = 0; i < c.getColumnCount(); i++) {
						fields.put(c.getColumnName(i), c.getString(i));
					}
					louceng.addFields(fields);
					oldLouceng = newLouceng;
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

	public synchronized FieldsList getFieldsListBySql(String sql) {
		Fields fields;
		FieldsList list = new FieldsList();

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(sql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					fields = new Fields();
					for (int i = 0; i < c.getColumnCount(); i++) {
						fields.put(c.getColumnName(i), c.getString(i));
					}
					list.setFields(fields);
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

	public synchronized FieldsList getFieldsList(int type, String code) {
		Fields fields;
		FieldsList list = new FieldsList();
		String strSql = "";
		if (type == 0)
			// strSql =
			// "select t.outlettype, t.serverid, t.fullname,plan.str1 from t_visit_plan_detail plan left join t_outlet_main t on plan.clientid=t.serverid";
			strSql = "SELECT case when  sum(c.issubmit is not null and c.issubmit is not -1) is 0 then '未拜访' else  sum(c.issubmit is 1)||'/'||sum(c.issubmit is not null and c.issubmit is not -1 ) end as str1,  o.outlettype as outlettype, o.serverid as serverid, o.fullname as fullname FROM (select * from t_visit_plan_detail where strftime('%Y-%m-%d',VisitTime)  ='"
					+ Tool.getCurrDate()
					+ "')  vd left join (select * from t_data_callreport where ReportDate='"
					+ Tool.getCurrDate()
					+ "') c on c.clientid=vd.clientid left join t_outlet_main o on o.outletid=vd.clientid group by vd.clientid order by str1 desc ";
		else if (type == 2) {
			// 1 strSql =

			// "select serverid ,title,content, strftime('%Y-%m-%d',stime) as stime,sender,status from t_message_detail  order by status desc,stime desc limit 50 ";
			strSql = "select 1 as type, serverid ,title,content, strftime('%Y-%m-%d',stime) as stime,sender,status from t_message_detail  "
					+ " union  SELECT 2 as type, psq.serverid , psq.title ,'' as content , strftime('%Y-%m-%d',pay.updatetime) as stime ,'' as sender,case when pay.issubmit is '-1' then '未填' else '已填' end as status"
					+ " from (select * FROM t_psq where psqid in (select psqid from t_psq_payout where clienttype=0 and clientid ='"
					+ code
					+ "')) psq left join (select * from t_psq_payout where clienttype=0 and clientid ='"
					+ code
					+ "') pay on psq.serverid =pay.psqid order by status desc,stime desc ";
		}

		else if (type == 12) {
			// 1 strSql =

			// "select serverid ,title,content, strftime('%Y-%m-%d',stime) as stime,sender,status from t_message_detail  order by status desc,stime desc limit 50 ";
			strSql = "select * from t_message_detail   where status='未读' ";
		} else if (type == 3) {
			strSql = "select  psq.* , case when  pay.issubmit  is '-1' then '未填' else '已填' end as status from (SELECT * FROM t_psq where psqid in (select psqid from t_psq_payout where clienttype=1 and clientid='"
					+ code
					+ "')) psq left join (select * from t_psq_payout where clienttype=1 and clientid='"
					+ code
					+ "') pay on psq.serverid=pay.psqid order by status desc,updatetime desc";
		} else if (type == 13) {
			strSql = "SELECT p.*,o.fullname as fullname FROM t_client_rlt_activitypromoter p left join t_outlet_Main o on p.clientid=o.serverid order by key_id desc";
		}

		else if (type == 14) {
			if (code.equals(""))
				strSql = "SELECT * FROM t_outlet_main where outlettype=1 and str1 >0   order by  fullname ";
			else
				strSql = "SELECT * FROM t_outlet_main where fullname like  '%"
						+ code
						+ "%' or outletcode like '%"
						+ code
						+ "%' and  outlettype=1  and str1 >0 order by  fullname ";
		} else if (type == 20) {
			strSql = "SELECT * FROM t_outlet_main where outlettype=1  order by  fullname ";
		} else if (type == 15) {
			// strSql = "select * from t_activity_main where clientid='" + code
			// + "' and stime<='" + Tool.getCurrDate() + "' and etime >='" +
			// Tool.getCurrDate() + "'";
			strSql = "select * from t_activity_main where clientid='" + code
					+ "' ";
		}

		else if (type == 16) {
			if (code.equals(""))
				strSql = "SELECT * FROM t_outlet_main where outlettype=1 and serverid in (select clientid from t_activity_main)  order by  fullname ";
			else
				strSql = "SELECT * FROM t_outlet_main where fullname like  '%"
						+ code
						+ "%' or outletcode like '%"
						+ code
						+ "%' and  outlettype=1 and serverid in (select clientid from t_activity_main)  order by  fullname ";
		}

		else {
			if (code.equals(""))
				strSql = "SELECT * FROM t_outlet_main where serverid not in ( select clientid from t_visit_plan_detail where strftime('%Y-%m-%d',VisitTime) ='"
						+ Tool.getCurrDate()
						+ "') order by outlettype desc, fullname ";
			else
				strSql = "SELECT * FROM t_outlet_main where fullname like  '%"
						+ code
						+ "%' or outletcode like '%"
						+ code
						+ "%' and serverid not in ( select clientid from t_visit_plan_detail where strftime('%Y-%m-%d',VisitTime) ='"
						+ Tool.getCurrDate()
						+ "') order by outlettype desc, fullname ";
		}
		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					fields = new Fields();
					for (int i = 0; i < c.getColumnCount(); i++) {
						fields.put(c.getColumnName(i), c.getString(i));
					}
					list.setFields(fields);
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

	private ControlType getContralType(String type) {
		if (type.equalsIgnoreCase("3"))
			return ControlType.TEXT;
		else if (type.equalsIgnoreCase("2"))
			return ControlType.SINGLECHOICE;
		else if (type.equalsIgnoreCase("1"))
			return ControlType.MULTICHOICE;
		else
			return ControlType.NONE;
	}

	public synchronized Template getTemplate(String type) {
		Template template = new Template();
		template.setType("-100");
		// template.setVersion("-100");
		String strSql = "select s.title as title, s.isphoto as isphoto, q.*  from (select * from t_psq_question where psqid='"
				+ type + "' ) q left join t_psq s where q.psqid=s.serverid";
		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			Panal panal;
			UIItem item;
			if (c != null && c.getCount() > 0) {

				c.moveToFirst();
				do {

					panal = new Panal();

					template.setVersion(getString(c, "psqid"));
					template.setName(getString(c, "title"));
					template.setPhoto(getString(c, "isphoto").equals("1") ? true
							: false);

					panal.setCaption(getString(c, "title"));
					panal.setType(Constants.PanalType.PANEL);
					panal.setId(getString(c, "serverid"));

					panal.setPanalType(1);
					item = new UIItem();
					item.setCaption("");

					item.setDicId(getString(c, "serverid"));
					item.setControlType(getContralType(getString(c, "type")));
					if (getString(c, "type").equals("3"))
						item.setVerifytype("text");
					item.setDataKey("str3");
					item.setItemType(1);
					item.setTitleWidth((Tool.getScreenWidth() - 40));
					item.setShowLable(false);
					item.setWidth(Tool.getScreenWidth() / 3);
					item.setOrientation(LinearLayout.HORIZONTAL);
					item.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
					panal.setItem(item);
					template.setPanal(panal);
				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return template;
	}

	private String getReportDetialSql1(String reportId, String clientid) {
		DBMainConfig dc = DBConfig.getDBMainConfig("t_outlet_main");
		DBMainConfig dc1 = DBConfig.getDBMainConfig("t_data_callReportDetail");
		if (dc == null || dc1 == null)
			return "";
		String reportDetialSql = "SELECT ";

		for (DBDetailConfig detail : dc.getFieldList()) {
			if (detail.isQuery()) {
				reportDetialSql += " p."
						+ Tool.toLowerCase(detail.getFieldName()) + " as "
						+ Tool.toLowerCase(detail.getFieldName()) + " ,";
			}
		}
		for (DBDetailConfig detail : dc1.getFieldList()) {
			if (detail.isQuery())
				reportDetialSql += " d."
						+ Tool.toLowerCase(detail.getFieldName()) + " as "
						+ Tool.toLowerCase(detail.getFieldName()) + " ,";
		}

		reportDetialSql = Tool.getSubString(reportDetialSql)
				+ " , case when r.issubmit is null then '' when r.issubmit=0 then '已保存' when r.issubmit=1 then '已提交' when r.issubmit=2 then '上传失败' end as str10 FROM (select * from  t_outlet_main where outlettype=1) p  left join (select * from t_data_callreportdetail where callReportId='"
				+ reportId
				+ "') d on p.serverid=d.productid left join (select * from t_data_callreport where onlyType='1' and reportdate='"
				+ Tool.getCurrDate() + "' and int8='" + clientid
				+ "' ) r on p.serverid=r.clientid order by p.ShortName ";
		return reportDetialSql;
	}

	private String getReportDetialSql(String reportId) {
		DBMainConfig dc = DBConfig.getDBMainConfig("T_Product");
		DBMainConfig dc1 = DBConfig.getDBMainConfig("t_data_callReportDetail");
		if (dc == null || dc1 == null)
			return "";
		String reportDetialSql = "SELECT ";

		for (DBDetailConfig detail : dc1.getFieldList()) {
			if (detail.isQuery())
				reportDetialSql += " d."
						+ Tool.toLowerCase(detail.getFieldName()) + " as "
						+ Tool.toLowerCase(detail.getFieldName()) + " ,";
		}
		for (DBDetailConfig detail : dc.getFieldList()) {
			if (detail.isQuery()) {
				if (detail.getFieldName().equalsIgnoreCase("AfterTaxPrice"))
					reportDetialSql += "round(p.aftertaxprice*100,1) as int1 ,";
				else
					reportDetialSql += " p."
							+ Tool.toLowerCase(detail.getFieldName()) + " as "
							+ Tool.toLowerCase(detail.getFieldName()) + " ,";
			}
		}

		reportDetialSql = Tool.getSubString(reportDetialSql)
				+ "FROM (select * from  T_Product where isSensitive=1) p  left join (select * from t_data_callreportdetail where callReportId='"
				+ reportId
				+ "') d on p.serverid=d.productid  order by p.ShortName ";
		return reportDetialSql;
	}

	public synchronized List<SObject> getSubmitObject() {
		List<SObject> list = new ArrayList<SObject>();
		//
		DBMainConfig dc = DBConfig.getDBMainConfig("t_data_callReport");
		DBMainConfig dc1 = DBConfig.getDBMainConfig("t_data_callReportDetail");
		DBMainConfig dc2 = DBConfig.getDBMainConfig("t_data_callReportPhoto");
		Fields data;
		SObject rpt;
		String strSql = "SELECT " + TableInfo.TABLE_KEY + ", ";

		for (DBDetailConfig detail : dc.getFieldList()) {
			if (detail.isUpload()) {
				if (detail.isReplace())
					strSql += "replace (" + detail.getFieldName()
							+ ",' ','') as "
							+ Tool.toLowerCase(detail.getFieldName()) + ",";
				else
					strSql += detail.getFieldName() + ",";
			}
		}
		strSql = strSql.substring(0, strSql.length() - 1);
		strSql += " FROM t_data_callreport where issubmit=0 and ReportDate='"
				+ Tool.getCurrDate() + "'  limit 1";

		// strSql =
		// "SELECT *, replace (str1,' ','') as str1  FROM t_data_callreport where issubmit=0 and ReportDate='"
		// + Tool.getCurrDate() + "'  limit 1";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				do {
					rpt = new SObject();
					rpt.setType("report");
					// rpt.setField("opetype", "upsert");

					for (DBDetailConfig detail : dc.getFieldList()) {
						if (detail.isUpload())
							rpt.set(detail.getFieldName(),
									getString(c, detail.getFieldName()));
					}
					// rpt.set("ClientType", "1");
					rpt.setField("userid", Rms.getUserId(context));
					rpt.setField(TableInfo.TABLE_KEY,
							getString(c, TableInfo.TABLE_KEY));
					list.add(rpt);
				} while (c.moveToNext());
			}
			c.close();
			for (SObject object : list) {

				strSql = "SELECT ";
				for (DBDetailConfig detail : dc2.getFieldList()) {
					if (detail.isUpload()) {
						if (detail.isReplace())
							strSql += "replace (" + detail.getFieldName()
									+ ",' ','') as "
									+ Tool.toLowerCase(detail.getFieldName())
									+ ",";
						else
							strSql += detail.getFieldName() + ",";
					}
				}
				strSql = strSql.substring(0, strSql.length() - 1);
				strSql += " FROM t_data_callreportphoto where callReportId='"
						+ object.getField(TableInfo.TABLE_KEY) + "'";

				// strSql =
				// "SELECT * FROM t_data_callreportphoto where callReportId='"
				// + object.getField(TableInfo.TABLE_KEY) + "'";
				c = db.rawQuery(strSql, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						data = new Fields();
						for (DBDetailConfig detail : dc2.getFieldList()) {
							if (detail.isUpload()) {
								if (detail.getType().equals(
										Constants.FieldType.BLOB))
									data.Set(
											detail.getFieldName(),
											getByteString(c,
													detail.getFieldName()));
								else
									data.Set(detail.getFieldName(),
											getString(c, detail.getFieldName()));
							}
						}
						object.setAttfield(data);
					} while (c.moveToNext());
				}
				c.close();

				strSql = "SELECT ";

				for (DBDetailConfig detail : dc1.getFieldList()) {
					if (detail.isUpload()) {
						if (detail.isReplace())
							strSql += "replace (" + detail.getFieldName()
									+ ",' ','') as "
									+ Tool.toLowerCase(detail.getFieldName())
									+ ",";
						else
							strSql += detail.getFieldName() + ",";
					}
				}
				strSql = strSql.substring(0, strSql.length() - 1);
				strSql += " FROM t_data_callreportdetail where callReportId='"
						+ object.getField(TableInfo.TABLE_KEY) + "'";
				// strSql =
				// "SELECT * FROM t_data_callreportdetail where callReportId='"
				// + object.getField(TableInfo.TABLE_KEY) + "'";
				c = db.rawQuery(strSql, null);
				if (c != null && c.getCount() > 0) {
					c.moveToFirst();
					do {
						data = new Fields();
						for (DBDetailConfig detail : dc1.getFieldList()) {
							if (detail.isUpload())
								data.Set(detail.getFieldName(),
										getString(c, detail.getFieldName()));
						}

						// for (int i = 0; i < c.getColumnCount(); i++) {
						// data.put(c.getColumnName(i), c.getString(i));
						// }
						object.setDetailfield(data);

					} while (c.moveToNext());
				}
				c.close();
			}

		}
		// catch (Exception e) {
		// // TODO: handle exception
		// String sssString=e.getMessage();
		// String sssString1=e.getMessage();
		// // Log.println(priority, tag, msg)
		//
		// }
		finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

	public synchronized List<SObject> getReadMsgId() {
		List<SObject> list = new ArrayList<SObject>();
		String strSql = "select serverid from t_message_detail  where issubmit=0";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				SObject data;

				c.moveToFirst();
				do {
					data = new SObject();
					data.setField("userid", Rms.getUserId(context));
					data.setType("msg");
					data.setField("MsgId", getString(c, "serverid"));
					list.add(data);

				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

	public synchronized SObject getDPReport(Template temp, String promotionid) {
		SObject rpt = new SObject(temp);
		String strSql = "SELECT * FROM T_Client_Rlt_ActivityPromoter where PromoterId='"
				+ promotionid + "' ";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				Fields data;
				c.moveToFirst();
				// do
				// {
				// for (int i = 0; i < c.getColumnCount(); i++)
				// {
				// rpt.setField(c.getColumnName(i), c.getString(i));
				// }
				// }

				rpt.setField("int1", getString(c, "ClientId"));
				rpt.setField("str10", getString(c, "PromoterId"));
				rpt.setField("str2", getString(c, "Mobile"));
				rpt.setField("str1", getString(c, "PromoterName"));
				rpt.setField("int2", getString(c, "WorkDay"));

				rpt.setField(TableInfo.TABLE_KEY,
						getString(c, TableInfo.TABLE_KEY));

				while (c.moveToNext())
					;

				c.close();

				if (!rpt.getField(TableInfo.TABLE_KEY).equals("")) {
					strSql = "SELECT * FROM t_data_DPPhoto where callReportId='"
							+ rpt.getField(TableInfo.TABLE_KEY) + "'";
					c = db.rawQuery(strSql, null);
					if (c != null && c.getCount() > 0) {
						c.moveToFirst();
						do {
							data = new Fields();
							for (int i = 0; i < c.getColumnCount(); i++) {
								if (c.getColumnName(i)
										.equalsIgnoreCase("Photo"))
									data.put(c.getColumnName(i), c.getBlob(i));
								else
									data.put(c.getColumnName(i), c.getString(i));
							}
							rpt.setAttfield(data);
						} while (c.moveToNext());
					}
				}
			}
			c.close();

			rpt.setField("onlyType", temp.getOnlyType() + "");
			rpt.setTemplateId(temp.getType());
			rpt.setTerminalCode("-1");
			// if (rpt.getCallDate().equals(""))
			rpt.setCallDate(Tool.getCurrDate());

		}
		// catch (Exception ex)
		// {
		// String sss = ex.getMessage();
		// }
		finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return rpt;
	}

	public synchronized SObject getReport(Template temp, String code, int type,
			String key) {
		SObject rpt = new SObject(temp);
		String strSql = "";
		if (type == 1) {
			// if (!temp.getType().equals("4"))
			if (temp.getOnlyType() == 1)
				strSql = "SELECT rpt.*, m.facialdiscount as facialdiscount,m.facediscount as facediscount,m.milkdiscount as milkdiscount,m.newdiscount as newdiscount FROM  (select * from t_outlet_main where outletid='"
						+ code
						+ "') m LEFT join (select * from t_data_callreport where onlyType='1' and int8='"
						+ key + "') rpt on m.outletid =rpt.clientid";
			else if (temp.getOnlyType() == 3001)
				strSql = "SELECT rpt.*, m.demiwarecount as demiwarecount FROM  (select * from t_outlet_main where outletid='"
						+ code
						+ "') m LEFT join (select * from t_data_callreport where onlyType='3001' ) rpt on m.outletid =rpt.clientid";

			else if (temp.getOnlyType() == -1 || temp.getOnlyType() == -2)
				strSql = "SELECT * FROM t_data_callreport where onlyType='"
						+ temp.getOnlyType() + "' and clientId='" + code
						+ "' and ReportDate='" + Tool.getCurrDate() + "'";
			else if (temp.getOnlyType() == 9)
				strSql = "SELECT * FROM t_data_callreport where onlyType='"
						+ temp.getOnlyType() + "' and ReportDate='"
						+ Tool.getCurrDate() + "' and clientId='" + code
						+ "' and decimal10='" + key + "'";
			else if (temp.getOnlyType() == 6001)
				strSql = "SELECT * FROM t_data_callreport where onlyType='"
						+ temp.getOnlyType() + "' and clientId='" + code
						+ "'  ";
			else if (temp.getOnlyType() == 20001)
				strSql = "SELECT * FROM t_data_callreport where onlyType='"
						+ temp.getOnlyType() + "' and ReportDate='" + key
						+ "' and int2='" + code + "' ";
			else if (temp.getOnlyType() == 20010)
				strSql = "SELECT * FROM t_data_callreport where onlyType='"
						+ temp.getOnlyType() + "' and ReportDate='"
						+ Tool.getCurrDate() + "' and int1='"
						+ Tool.getSelectData().getStrValue("serverid") + "' ";

			else
				strSql = "SELECT * FROM t_data_callreport where onlyType='"
						+ temp.getOnlyType() + "' and clientId='" + code + "'";
		} else if (temp.getOnlyType() == -1 || temp.getOnlyType() == -2)
			strSql = "SELECT * FROM t_data_callreport where onlyType='"
					+ temp.getOnlyType() + "' and clientId='" + code
					+ "' and ReportDate='" + Tool.getCurrDate()
					+ "' and decimal10='" + key + "'";

		if (temp.getType().equals("4")) {
			if (!temp.getName().equals("8"))
				rpt.setField("int1", temp.getName());
			else {
				rpt.setField("int1", temp.getName());
				rpt.setField("int8", temp.getPltype());
			}
		} else if (temp.getType().equals("5") || temp.getType().equals("6"))
			rpt.setField("int1", temp.getName());

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				rpt.setSaved(true);
				Fields data;
				c.moveToFirst();
				do {
					for (int i = 0; i < c.getColumnCount(); i++) {
						rpt.setField(c.getColumnName(i), c.getString(i));
					}
					if (temp.getOnlyType() == 1) {
						if (rpt.getField(TableInfo.TABLE_KEY).equals(""))
							rpt.setField(TableInfo.TABLE_KEY, "-1");
						rpt.setField("int1", getString(c, "facediscount"));
						rpt.setField("int3", getString(c, "facialdiscount"));
						rpt.setField("int5", getString(c, "milkdiscount"));
						rpt.setField("int7", getString(c, "newdiscount"));

						// rpt.setCallDate(Tool.getCurrDate());
					} else if (temp.getOnlyType() == 3001) {
						if (rpt.getField(TableInfo.TABLE_KEY).equals(""))
							rpt.setField(TableInfo.TABLE_KEY, "-1");
						rpt.setField("int6", getString(c, "demiwarecount"));
						rpt.setField("int1", temp.getName());

						// rpt.setCallDate(Tool.getCurrDate());
					}
				} while (c.moveToNext());
				c.close();

				if (!rpt.getField(TableInfo.TABLE_KEY).equals("")
						&& rpt.getField("ReportDate")
								.equals(Tool.getCurrDate())) {
					strSql = "SELECT * FROM t_data_callreportphoto where callReportId='"
							+ rpt.getField(TableInfo.TABLE_KEY) + "'";
					c = db.rawQuery(strSql, null);
					if (c != null && c.getCount() > 0) {
						c.moveToFirst();
						do {
							data = new Fields();
							for (int i = 0; i < c.getColumnCount(); i++) {
								if (c.getColumnName(i)
										.equalsIgnoreCase("Photo"))
									data.put(c.getColumnName(i), c.getBlob(i));
								else
									data.put(c.getColumnName(i), c.getString(i));
							}
							rpt.setAttfield(data);
						} while (c.moveToNext());
					}
				}
			}
			c.close();
			if (temp.haveTable()) {

				if (temp.getOnlyType() == 6001)
					strSql = getReportDetialSql1(
							rpt.getField(TableInfo.TABLE_KEY), code);
				else
					strSql = getReportDetialSql(rpt
							.getField(TableInfo.TABLE_KEY));
				c = db.rawQuery(strSql, null);

				if (c != null && c.getCount() > 0) {
					Fields data;

					c.moveToFirst();
					do {
						data = new Fields();
						for (int i = 0; i < c.getColumnCount(); i++) {
							data.put(c.getColumnName(i), c.getString(i));
						}

						rpt.setDetailfield(data);

					} while (c.moveToNext());
				}
			}
			rpt.setField("onlyType", temp.getOnlyType() + "");
			rpt.setTemplateId(temp.getType());
			rpt.setTerminalCode(code);
			// if (rpt.getCallDate().equals(""))
			rpt.setCallDate(Tool.getCurrDate());

			// rpt.setCallDate(Tool.getCurrDate());

			rpt.setField("decimal10", key);

		}
		// catch (Exception ex)
		// {
		// String sss = ex.getMessage();
		// }
		finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return rpt;
	}

	public synchronized SObject getSurveyRpt(Template temp, String code) {
		SObject rpt = new SObject(temp);
		String strSql = "select * from t_data_callreport where templateid=-100 and clientid='"
				+ code
				+ "' and reportdate ='"
				+ Tool.getCurrDate()
				+ "' and str1='" + temp.getVersion() + "'";

		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {
				Fields data;
				c.moveToFirst();
				do {
					for (int i = 0; i < c.getColumnCount(); i++) {
						rpt.setField(c.getColumnName(i), c.getString(i));
					}
				} while (c.moveToNext());
				c.close();

				if (!rpt.getField(TableInfo.TABLE_KEY).equals("")
						&& rpt.getField("ReportDate")
								.equals(Tool.getCurrDate())) {
					strSql = "SELECT * FROM t_data_callreportphoto where callReportId='"
							+ rpt.getField(TableInfo.TABLE_KEY) + "'";
					c = db.rawQuery(strSql, null);
					if (c != null && c.getCount() > 0) {
						c.moveToFirst();
						do {
							data = new Fields();
							for (int i = 0; i < c.getColumnCount(); i++) {
								if (c.getColumnName(i)
										.equalsIgnoreCase("Photo"))
									data.put(c.getColumnName(i), c.getBlob(i));
								else
									data.put(c.getColumnName(i), c.getString(i));
							}
							rpt.setAttfield(data);
						} while (c.moveToNext());
					}
				}

				c.close();
				// 1 if (temp.haveTable()) {
				strSql = "select * from t_data_callReportDetail where callReportId='"
						+ rpt.getField(TableInfo.TABLE_KEY) + "'";
				c = db.rawQuery(strSql, null);

				if (c != null && c.getCount() > 0) {

					c.moveToFirst();
					do {
						data = new Fields();
						for (int i = 0; i < c.getColumnCount(); i++) {
							data.put(c.getColumnName(i), c.getString(i));
						}

						rpt.setDetailfield(data);

					} while (c.moveToNext());
				}
				// }

			} else {
				Fields data = null;
				for (Panal panal : temp.getPanalList()) {
					data = new Fields();
					data.put("str1", temp.getVersion());
					data.put("str2", panal.getId());

					data.put("str4", code);
					// data.put("str1", template.getVersion());
					rpt.setDetailfield(data);
				}

				rpt.setField("onlyType", "-100");

				rpt.setField("str1", temp.getVersion());
				rpt.setTemplateId("-100");
				rpt.setTerminalCode(code);
				if (code.equals("-1"))
					// rpt.setField("ClientType", "3");
					rpt.setField("ClientType", "0");
				else
					// rpt.setField("ClientType", "4");
					rpt.setField("ClientType", "1");
				rpt.setCallDate(Tool.getCurrDate());

			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return rpt;
	}

	public synchronized long saveDP(SObject rpt) {
		long rowId = -1;
		try {
			// if (rpt.getKeyId() > 0)
			// {
			// execSingleSql("DELETE FROM t_data_callReportPhoto where callReportId='"
			// + rpt.getField(TableInfo.TABLE_KEY) + "'");
			// execSingleSql("DELETE FROM t_data_callreportdetail where callReportId='"
			// + rpt.getField(TableInfo.TABLE_KEY) + "'");
			// execSingleSql("DELETE FROM t_data_callreport where key_id='" +
			// rpt.getField(TableInfo.TABLE_KEY) + "'");
			// }
			openDatabase();
			beginTransaction();

			ContentValues values = new ContentValues();
			// DBMainConfig dc =
			// DBConfig.getDBMainConfig("T_Client_Rlt_ActivityPromoter");

			values.put("ClientId", rpt.getField("int1"));
			values.put("PromoterId", rpt.getField("id"));
			values.put("Mobile", rpt.getField("str2"));
			values.put("PromoterName", rpt.getField("str1"));
			values.put("WorkDay", rpt.getField("int2"));

			values.put("serverid", rpt.getField("id"));
			values.put("issubmit", 1);
			values.put("UpDateTime", Tool.getCurrDateTime());
			values.put("InSertTime", Tool.getCurrDateTime());
			rowId = db.insert("T_Client_Rlt_ActivityPromoter", null, values);

			createDPPhoto(rpt.getAttfields(), rowId);

			setTransactionSuccessful();

		} finally {
			endTransaction();
			closeDatabase();
		}
		return rowId;
	}

	private synchronized long createDPPhoto(FieldsList list, long rptId) {
		if (list == null || list.getList() == null)
			return -1;
		long rowId = -1;
		ContentValues values = null;
		DBMainConfig dc = DBConfig.getDBMainConfig("t_data_DPPhoto");
		for (Fields data : list.getList()) {
			values = new ContentValues();

			for (DBDetailConfig detail : dc.getFieldList()) {
				if (detail.getFieldName().equalsIgnoreCase("callReportId"))
					values.put(detail.getFieldName(), rptId);
				else if (detail.getType() == Constants.FieldType.BLOB)
					values.put(detail.getFieldName(),
							data.getBValue(detail.getFieldName()));
				else
					values.put(detail.getFieldName(),
							data.getStrValue(detail.getFieldName()));
			}
			// values.put("callreportid", rptId);
			values.put("UpDateTime", Tool.getCurrDateTime());
			values.put("InSertTime", Tool.getCurrDateTime());

			rowId = db.insert("t_data_DPPhoto", null, values);
		}

		return rowId;
	}

	public synchronized long saveReport(SObject rpt) {
		long rowId = -1;
		try {
			if (rpt.getKeyId() > 0) {
				execSingleSql("DELETE FROM t_data_callReportPhoto where callReportId='"
						+ rpt.getField(TableInfo.TABLE_KEY) + "'");
				execSingleSql("DELETE FROM t_data_callreportdetail where callReportId='"
						+ rpt.getField(TableInfo.TABLE_KEY) + "'");
				execSingleSql("DELETE FROM t_data_callreport where key_id='"
						+ rpt.getField(TableInfo.TABLE_KEY) + "'");
			}
			openDatabase();
			beginTransaction();

			ContentValues values = new ContentValues();
			DBMainConfig dc = DBConfig.getDBMainConfig("t_data_callreport");
			for (DBDetailConfig detail : dc.getFieldList()) {
				values.put(detail.getFieldName(),
						rpt.getField(detail.getFieldName()));
			}

			if (rpt.getTemplateId().equals("2003")
					|| rpt.getTemplateId().equals("3002")
					|| rpt.getTemplateId().equals("4001"))
				values.put("issubmit", -1);
			else
				values.put("issubmit", 0);
			values.put("UpDateTime", Tool.getCurrDateTime());
			values.put("InSertTime", Tool.getCurrDateTime());
			rowId = db.insert("t_data_callreport", null, values);

			createReportDetail(rpt.getDetailfields(), rowId);
			createPhoto(rpt.getAttfields(), rowId);

			setTransactionSuccessful();

		} finally {
			endTransaction();
			closeDatabase();
		}
		return rowId;
	}

	private synchronized long createReportDetail(FieldsList list, long rptId) {
		if (list == null || list.getList() == null)
			return -1;
		long rowId = -1;
		ContentValues values = null;
		DBMainConfig dc = DBConfig.getDBMainConfig("t_data_callreportdetail");
		for (Fields data : list.getList()) {
			values = new ContentValues();
			for (DBDetailConfig detail : dc.getFieldList()) {
				if (detail.getFieldName().equalsIgnoreCase("productid"))
					values.put(detail.getFieldName(),
							data.getStrValue("serverid"));
				else if (detail.getFieldName().equalsIgnoreCase("callReportId"))
					values.put(detail.getFieldName(), rptId);
				else
					values.put(detail.getFieldName(),
							data.getStrValue(detail.getFieldName()));
			}

			// values.put("callReportId", rptId);
			values.put("UpDateTime", Tool.getCurrDateTime());
			values.put("InSertTime", Tool.getCurrDateTime());
			rowId = db.insert("t_data_callreportdetail", null, values);
		}

		return rowId;
	}

	private synchronized long createPhoto(FieldsList list, long rptId) {
		if (list == null || list.getList() == null)
			return -1;
		long rowId = -1;
		ContentValues values = null;
		DBMainConfig dc = DBConfig.getDBMainConfig("t_data_callReportPhoto");
		for (Fields data : list.getList()) {
			values = new ContentValues();

			for (DBDetailConfig detail : dc.getFieldList()) {
				if (detail.getFieldName().equalsIgnoreCase("callReportId"))
					values.put(detail.getFieldName(), rptId);
				else if (detail.getType() == Constants.FieldType.BLOB)
					values.put(detail.getFieldName(),
							data.getBValue(detail.getFieldName()));
				else
					values.put(detail.getFieldName(),
							data.getStrValue(detail.getFieldName()));
			}
			// values.put("callreportid", rptId);
			values.put("UpDateTime", Tool.getCurrDateTime());
			values.put("InSertTime", Tool.getCurrDateTime());

			rowId = db.insert("t_data_callReportPhoto", null, values);
		}

		return rowId;
	}

	public synchronized long upsertMsg(List<SObject> list) {
		if (list == null)
			return -1;
		long rowId = -1;
		try {
			ContentValues values = null;
			openDatabase();
			beginTransaction();
			for (SObject object : list) {
				if (object != null) {
					values = new ContentValues();

					values.put("ServerId", object.getField("ServerId"));
					values.put("Title", object.getField("Title"));
					values.put("Content", object.getField("Content"));
					values.put("Stime", object.getField("Stime"));
					values.put("Sender", object.getField("Sender"));
					values.put("UpDateTime", Tool.getCurrDateTime());

					rowId = db.update("T_Message_Detail", values, "ServerId='"
							+ object.getField("ServerId") + "'", null);
					if (rowId <= 0) {
						values.put("status", "未读");
						rowId = db.insert("T_Message_Detail", null, values);
					}
				}
			}
			setTransactionSuccessful();
		} finally {
			endTransaction();
			closeDatabase();
		}
		return rowId;
	}

	// private boolean str2boolean(String result) {
	// if (result.equals("on"))
	// return true;
	// else
	// return false;
	// }

	// private ControlType getContralType(String type) {
	// if (type.equalsIgnoreCase("text"))
	// return ControlType.TEXT;
	// else if (type.equalsIgnoreCase("combox"))
	// return ControlType.SINGLECHOICE;
	// else if (type.equalsIgnoreCase("checkbox"))
	// return ControlType.MULTICHOICE;
	// else
	// return ControlType.NONE;
	// }

	public synchronized List<DicData> getDicList(String dictCode,
			String linkdictId) {
		DicData data = null;
		List<DicData> items = new ArrayList<DicData>();
		String strSql = "";
		if (linkdictId.equals("")) {

			strSql = "select * from jxc_base where type='" + dictCode + "' ";
		} else {
			strSql = "SELECT * FROM jxc_base where dictmainid='" + dictCode
					+ "' and linkvalues='" + linkdictId + "'";
		}
		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);

			if (c != null && c.getCount() > 0) {

				c.moveToFirst();
				do {
					data = new DicData();
					data.setValue(getString(c, "serverid"));
					data.setItemname(getString(c, "basename"));
					// data.setRemark(getString(c, "remark"));
					// data.setCode(getString(c, "dictcode"));
					// data.setName(getString(c, "dictname"));

					items.add(data);
				} while (c.moveToNext());
			} else {
				data = new DicData();
				data.setItemname("");
				data.setValue("");
				items.add(data);
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return items;
	}

	public synchronized List<DicData> getAnswerList(String questionId) {
		DicData data = null;
		List<DicData> items = new ArrayList<DicData>();
		String strSql = "SELECT * FROM t_psq_options where questionid='"
				+ questionId + "'";
		Cursor c = null;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);

			if (c != null && c.getCount() > 0) {

				c.moveToFirst();
				do {
					data = new DicData();
					data.setValue(getString(c, "serverid"));
					data.setItemname(getString(c, "value"));

					items.add(data);
				} while (c.moveToNext());
			} else {
				data = new DicData();
				data.setItemname("");
				data.setValue("");
				items.add(data);
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return items;
	}

	public synchronized void execSqlList(List<String> sqlList) {
		try {
			openDatabase();
			if (sqlList.size() > TRANCOUNT)
				beginTransaction();
			for (String sql : sqlList)
				execSql(sql);
			if (sqlList.size() > TRANCOUNT)
				setTransactionSuccessful();

		} finally {
			if (sqlList.size() > TRANCOUNT)
				endTransaction();
			closeDatabase();
		}
	}

	private synchronized void execSql(String sql) {
		db.execSQL(sql);
	}

	public synchronized void execSingleSql(String sql) {
		try {
			openDatabase();
			execSql(sql);
		} finally {
			closeDatabase();
		}
	}

	private String getString(Cursor c, String columnName) {
		int index = -1;
		try {
			index = c.getColumnIndex((columnName.toLowerCase()));
		} catch (Exception ex) {
			index = -1;
		}
		if (index == -1)
			return "";
		else
			return getString(c, index);
	}

	private String getByteString(Cursor c, String columnName) {
		int index = -1;
		try {
			index = c.getColumnIndex((columnName.toLowerCase()));
		} catch (Exception ex) {
			index = -1;
		}
		if (index == -1)
			return "";
		else
			return Base64.encodeToString(getByte(c, index), Base64.DEFAULT);
	}

	// private int getInt(Cursor c, String columnName) {
	// int index = -1;
	// try {
	// index = c.getColumnIndex((columnName.toLowerCase()));
	// } catch (Exception ex) {
	// index = -1;
	// }
	// if (index == -1)
	// return 0;
	// else
	// return getInt(c, index);
	// }

	// private int getInt(Cursor c, int index) {
	// return c.getString(index) == null ? 0 : c.getInt(index);
	// }

	private String getString(Cursor c, int index) {
		return c.getString(index) == null ? "" : c.getString(index);
	}

	private byte[] getByte(Cursor c, int index) {
		// byte[] bytes=c.getBlob(index);
		// Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
		// bytes.length);
		// return Tool.Bitmap2Bytes(bitmap);
		return c.getBlob(index);
	}

	public Context getContext() {
		return context;
	}

	private void initContext(Context context) {
		this.context = context;
	}

	public synchronized FieldsList getReportList(Fields condition) {
		FieldsList list = new FieldsList();
		String strSql = "";
		strSql = "SELECT *, strftime('%H:%M:%S',updatetime) as updatetime ,case when issubmit is 0 then '已保存'  when issubmit is 1 then '已上传'  when issubmit is 2 then '上传失败' end as issubmit, '' as dictname FROM t_data_callreport where onlyType='"
				+ condition.getStrValue("onlytype")
				+ "' and ReportDate='"
				+ Tool.getCurrDate()
				+ "' and clientId='"
				+ condition.getStrValue("clientid")
				+ "' order by issubmit desc,updatetime desc ";

		Cursor c = null;
		Fields reslut;
		try {
			openDatabase();
			c = db.rawQuery(strSql, null);
			if (c != null && c.getCount() > 0) {

				c.moveToFirst();
				do {
					reslut = new Fields();
					for (int i = 0; i < c.getColumnCount(); i++) {
						reslut.put(c.getColumnName(i), c.getString(i));
					}
					list.setFields(reslut);

				} while (c.moveToNext());
			}
		} finally {
			if (c != null)
				c.close();
			closeDatabase();
		}
		return list;
	}

}
