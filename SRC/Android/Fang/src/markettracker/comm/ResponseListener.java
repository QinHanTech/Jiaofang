package markettracker.comm;

import java.util.ArrayList;
import java.util.List;

import markettracker.comm.BaseResponseListener;
import markettracker.data.QueryConfig;
import markettracker.data.QueryDataResult;
import markettracker.data.SObject;
import markettracker.data.Sqlite;
import markettracker.data.UpsertResult;
import markettracker.util.Constants;
import markettracker.util.Constants.PropertyKey;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;

public class ResponseListener extends BaseResponseListener {

	private int type;
	private Context context;
	private Handler handler;
//	private QueryConfig queryConfig;

//	private Activity activity;
	private List<SObject> sObjectsSub;// 发送出去的

	// private List<SObject> sObjects;// 剩下的

	// public ResponseListener(int iType, Context context, Handler handler) {
	// mType = iType;
	// mContext = context;
	// mHandler = handler;
	// }

	public ResponseListener(Context context, Handler handler) {
		init(context, handler, Constants.PropertyKey.LOGIN, null, null, null);
	}

	public ResponseListener(List<SObject> list, Context context, Handler handler) {
		init(context, handler, Constants.PropertyKey.UPLOAD, null, null, list);
	}

	private void init(Context context, Handler handler, int type,
			QueryConfig queryConfig, Activity activity, List<SObject> list) {
		this.type = type;
		this.context = context;
		this.handler = handler;
//		this.queryConfig = queryConfig;
//		this.activity = activity;
		this.sObjectsSub = list;
	}

	public ResponseListener(Handler handler, Context context,
			QueryConfig queryConfig, Activity activity) {
		init(context, handler, Constants.PropertyKey.QUERY, queryConfig,
				activity, null);
	}

	public ResponseListener(Handler handler, Context context, Activity activity) {
		init(context, handler, Constants.PropertyKey.GETSERVERTIME, null,
				activity, null);
	}

	// public ResponseListener(int iType, Context context, Handler handler,
	// ArrayList<SObject> objs, ArrayList<SObject> sObjects) {
	// this.type = iType;
	// this.context = context;
	// this.handler = handler;
	// mSObjectsSub = sObjects;
	// mSObjects = objs;
	// }

	@Override
	public void onComplete(Object sObjects) {
		if (sObjects == null) {
			sendErrMsg(handler, "数据同步失败，请重试！");
		} else {
			if (this.type == Constants.PropertyKey.LOGIN)
				loginResult(sObjects);
			else if (this.type == PropertyKey.QUERY) {
				if (QueryConfig.getInstance().isBack())
					handler.sendMessage(handler.obtainMessage(type, sObjects));
				else
					queryResult(sObjects);
			} else if (this.type == PropertyKey.UPLOAD) {
				if (sObjectsSub.get(0).getType().equalsIgnoreCase("msg"))
					uploadMsgResult(sObjects);
				else
					uploadResult(sObjects);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void uploadMsgResult(Object sObjects) {
		try {
			List<UpsertResult> list = (List<UpsertResult>) sObjects;
			UpsertResult reslut = null;
			SObject rpt;
			List<String> sqlList = new ArrayList<String>();
			for (int i = 0; i < sObjectsSub.size(); i++) {
				rpt = sObjectsSub.get(i);
				reslut = list.get(0);
				if (reslut.isSuccess() == 1) {
					sqlList.add("update t_message_detail set issubmit = 1"
							+ " where serverid =" + rpt.getField("MsgId"));
				} else {
					sqlList.add("update t_message_detail set issubmit = 2, errmsg='"
							+ reslut.getErrorMsg()
							+ "' where serverid ="
							+ rpt.getField("MsgId"));
				}
			}
			Sqlite.execSqlList(context, sqlList);
			handler.sendMessage(handler.obtainMessage(PropertyKey.UPLOADMSG,
					list));
		} catch (Exception ex) {
			sendSyncErrMsg(handler, "数据上传失败");
		}
	}

	@SuppressWarnings("unchecked")
	private void uploadResult(Object sObjects) {
		try {
			List<UpsertResult> list = (List<UpsertResult>) sObjects;

			UpsertResult reslut = null;

			if (sObjectsSub.get(0).getSValue("TemplateId").equals("51")) {
				reslut = list.get(0);
				if (reslut.isSuccess() == 1)
					handler.sendMessage(handler.obtainMessage(type, list));
				else
					sendSyncErrMsg(handler, reslut.getErrorMsg());
			} else if (sObjectsSub.get(0).getSValue("TemplateId")
					.equals("20000")
					|| sObjectsSub.get(0).getSValue("TemplateId")
							.equals("20001")) {
				// reslut = list.get(0);
				// if (reslut.isSuccess() == 1)
				// handler.sendMessage(handler.obtainMessage(type, list));
				// else
				handler.sendMessage(handler.obtainMessage(type, list.get(0)));
			} else {
				// String msg = "";
				SObject rpt;
				// boolean bOK=false;
				List<String> sqlList = new ArrayList<String>();
				for (int i = 0; i < sObjectsSub.size(); i++) {
					rpt = sObjectsSub.get(i);
					reslut = list.get(i);
					if (reslut.isSuccess() == 1) {
						sqlList.add("update t_data_callReport set issubmit = 1"
								+ " where " + Constants.TableInfo.TABLE_KEY
								+ " ="
								+ rpt.getField(Constants.TableInfo.TABLE_KEY));
						sqlList.add("update T_Visit_Plan_Detail set str1 ='1'"
								+ " where clientid ='"
								+ rpt.getSValue("ClientId") + "'");
						// bOK = true;
					} else {
						sqlList.add("update t_data_callReport set issubmit = 2, errmsg='"
								+ reslut.getErrorMsg()
								+ "'"
								+ " where "
								+ Constants.TableInfo.TABLE_KEY
								+ " ="
								+ rpt.getField(Constants.TableInfo.TABLE_KEY));
						// bOK = false;
					}
				}
				Sqlite.execSqlList(context, sqlList);
				// if (bOK)

				// List<SObject> slist = Sqlite.getSubmitObject(context);
				// if (slist != null && slist.size() > 0) {
				// SyncData.Upload(slist, handler, activity);
				// } else
				handler.sendMessage(handler.obtainMessage(type, list));
				// else
				// sendSyncErrMsg(handler, "部分数据上传失败，请重新上传");
			}
		} catch (Exception ex) {
			sendSyncErrMsg(handler, "数据上传失败");
		}
	}

	private void queryResult(Object sObjects) {
		try {
			final QueryDataResult qresult = (QueryDataResult) sObjects;
			if (qresult.getInt(QueryDataResult.SUCCESS) == QueryDataResult.STATUS_OK) {

				if (QueryConfig.getInstance().getInt(QueryConfig.ISALL) == 0)
					Sqlite.execSingleSql(
							context,
							"delete from "
									+ qresult.getString(QueryDataResult.TYPE));

				if (Sqlite.upsertData(context, qresult)) {
					handler.sendMessage(handler.obtainMessage(type, qresult));
				} else {
					sendErrMsg(handler, qresult.getString(QueryDataResult.TYPE)
							+ "保存失败！");
				}
			} else
				handler.sendMessage(handler.obtainMessage(type, qresult));
		} catch (Exception ex) {
			sendErrMsg(handler, "数据下载失败");
		}

	}

	private void loginResult(Object sObjects) {
		handler.sendMessage(handler.obtainMessage(type, sObjects));
	}

	public void onException(String err) {
		sendErrMsg(handler, err);

	}

	private void sendErrMsg(Handler handler, String errMsg) {
		handler.sendMessage(handler.obtainMessage(Constants.PropertyKey.ERR,
				errMsg));
	}

	private void sendSyncErrMsg(Handler handler, String errMsg) {
		handler.sendMessage(handler.obtainMessage(
				Constants.PropertyKey.SYNCERR, errMsg));
	}

}
