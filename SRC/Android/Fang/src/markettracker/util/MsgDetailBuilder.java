package markettracker.util;

import markettracker.data.Fields;
import markettracker.data.Sqlite;
import com.rh.fang.jf.R;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MsgDetailBuilder extends Builder {

	// private Context mContext;

	public MsgDetailBuilder(final Context context, final Handler handler,
			final Fields data) {
		super(context);
		// mContext = context;

		// this.setTitle("消息");
		this.setIcon(android.R.drawable.ic_dialog_info);

		init(context, handler, data);

		this.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			// @Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (!data.getStrValue("status").equals("已读")) {
					Sqlite.execSingleSql(context,
							"update t_message_detail set status='已读',issubmit=0 where serverid ='"
									+ data.getStrValue("serverid") + "'");
					data.put("status", "已读");
					handler.sendMessage(handler.obtainMessage(1, ""));
				}
			}
		});
	}

	// private void showSingleChoiceDialog() {
	// if (null != mSingleChoiceDialog) {
	// mSingleChoiceDialog.dismiss();
	// mSingleChoiceDialog = null;
	// }
	// // final String[] choiceArr = getItem(mContext,);
	// // final List<Data> list = Sqlite.getAttchmentList(mContext, msgId);
	// AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	// builder.setSingleChoiceItems(mChoiceArr, -1,
	// new DialogInterface.OnClickListener() {
	// // @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// openFile(mList.get(which).getStringMember(2));
	// // if (mList.size() == 1)
	// // mSingleChoiceDialog.dismiss();
	// }
	// });
	// mSingleChoiceDialog = builder.create();
	// mSingleChoiceDialog.show();
	// }

	private void init(Context context, Handler handler, Fields data) {
		View view = LayoutInflater.from(context).inflate(R.layout.msg_detail,
				null);
		// List<Data> list = Sqlite.getAttchmentList(context, data
		// .getStringMember(0));

		TextView txt_title = (TextView) view.findViewById(R.id.msg_title);
		txt_title.setText(data.getStrValue("title"));

		TextView txt_sendboy = (TextView) view.findViewById(R.id.msg_sendboy);
		txt_sendboy.setText(data.getStrValue("sender"));

		TextView txt_date = (TextView) view.findViewById(R.id.msg_date);

		txt_date.setText(data.getStrValue("stime"));

		// TextView txt_attchment = (TextView) view
		// .findViewById(R.id.msg_attchment);
		// getItem(mContext, data.getStringMember(0));
		// if (mList != null && mList.size() > 0)
		// txt_attchment.setText("请选择");
		// txt_attchment.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// showSingleChoiceDialog();
		// }
		// });
		// txt_attchment.setSelection(-1);
		// txt_attchment.setAdapter(getCurDictAdapter(context, data
		// .getStringMember(0)));
		// txt_attchment.setOnItemSelectedListener(new OnItemSelectedListener()
		// {
		//
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// // TODO Auto-generated method stub
		// // if (arg2 > 0)
		// openFile(mMsgAdapter.getValue(arg2));
		//
		// }
		//
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// txt_attchment.setText(data.getStringMember(iKey));

		TextView txt_content = (TextView) view.findViewById(R.id.msg_content);
		txt_content.setText("内容:\r\n    " + data.getStrValue("content"));

		// txt_title.setText(data.getStringMember(iKey));

		this.setView(view);
	}

	// private void openFile(String uri) {
	// Intent intent = new Intent();
	// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// intent.setAction(android.content.Intent.ACTION_VIEW);
	//
	// File file = new File("sdcard/" + uri);
	//
	// /* 调用getMIMEType()来取得MimeType */
	// String type = getMIMEType(file);
	// /* 设置intent的file与MimeType */
	// intent.setDataAndType(Uri.fromFile(file), type);
	// mContext.startActivity(intent);
	// }

	/* 判断文件MimeType的method */
	// private String getMIMEType(File f) {
	// String type = "";
	// String fName = f.getName();
	// /* 取得扩展名 */
	// String end = fName
	// .substring(fName.lastIndexOf(".") + 1, fName.length())
	// .toLowerCase();
	//
	// /* 依扩展名的类型决定MimeType */
	// if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
	// || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
	// type = "audio/*";
	// } else if (end.equals("3gp") || end.equals("mp4")) {
	// type = "video/*";
	// } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
	// || end.equals("jpeg") || end.equals("bmp")) {
	// type = "image/*";
	// } else if (end.equals("xlsx")) {
	// type = "application/vnd.ms-excel";
	// } else if (end.equals("docx")) {
	// type = "application/msword";
	// } else if (end.equals("txt")) {
	// type = "text/plain";
	// } else if (end.equals("pptx")) {
	// type = "application/vnd.ms-powerpoint";
	// } else if (end.equals("apk")) {
	// /* android.permission.INSTALL_PACKAGES */
	// type = "application/vnd.android.package-archive";
	// } else {
	// type = "*/*";
	// }
	// /* 如果无法直接打开，就跳出软件列表给用户选择 */
	// // if (end.equals("apk")) {
	// // } else {
	// // type += "/*";
	// // }
	// return type;
	// }

}
