package markettracker.util;

import com.rh.fang.zs.R;

import markettracker.data.SObject;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class PhotoEditBuilder1 implements OnClickListener {
	private Dialog alert;
	private Context context;
	private Handler handler;
	private View view;
	private Button cancel, ok;
	private ElectronWriteName electronWriteName;

	public PhotoEditBuilder1(Context context, SObject rpt, Handler handler,
			Bitmap bitmap) {
		this.handler = handler;
		this.context = context;
		initView(rpt, bitmap);
	}

	public void dismiss() {
		if (alert != null) {
			if (view != null) {

				// view.clearAnimation();
				// view.startAnimation(mButtomOutAnimation);
			}
			alert.dismiss();
			alert = null;
		}
	}

	private void initView(SObject rpt, Bitmap bitmap) {
		view = LayoutInflater.from(context).inflate(R.layout.sign, null);

		cancel = (Button) view.findViewById(R.id.back);
		cancel.setOnClickListener(this);
		ok = (Button) view.findViewById(R.id.ok);
		ok.setOnClickListener(this);

		electronWriteName = new ElectronWriteName(context, bitmap);

		LinearLayout WriteUserName = (LinearLayout) view
				.findViewById(R.id.WriteUserNameSpace);
		WriteUserName.addView(electronWriteName);

		alert = new Dialog(context, R.style.showinfo_dialog);
		alert.setContentView(view);
	}

	public void show() {
		alert.show();
		alert.getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println(v.getId());
		switch (v.getId()) {

		case R.id.ok:
			// resetPwd();
			Bitmap bmp = electronWriteName.getImgInfo();
			handler.sendMessage(handler.obtainMessage(
					Constants.CustomGridType.PHOTOEDIT, bmp));
			dismiss();
			break;

		case R.id.back:
			// resetPwd();
			dismiss();
			break;

		default:
			break;
		}
	}

}
