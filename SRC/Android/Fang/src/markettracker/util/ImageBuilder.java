package markettracker.util;

import markettracker.data.Fields;
import com.rh.fang.jf.R;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class ImageBuilder extends Builder {

	// private Context mContext;

	public ImageBuilder(final Context context, final Fields data) {
		super(context);
		// mContext = context;

		// this.setTitle("消息");
		this.setIcon(android.R.drawable.ic_dialog_info);

		init(context, data);

		this.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			// @Override
			public void onClick(DialogInterface arg0, int arg1) {

			}

		});
	}
	
//	public boolean SaveMsgDetail(byte[] buffer, String strFileName) {
//		boolean bOk = false;
//		try {
//			String sFile_path = File.separator + "sdcard" + File.separator
//					+ "mjnMsr" + File.separator;
//			File destDir = new File(sFile_path + strFileName);
//			if (!destDir.exists()) {
//				destDir.getParentFile().mkdirs();
//				destDir.createNewFile();
//			}
//			// File myCaptureFile = new File("/sdcard/TESTTEST.txt");
//			FileOutputStream fos = new FileOutputStream(destDir);
//			fos.write(buffer);
//			fos.flush();
//			fos.close();
//			bOk = true;
//		} catch (Exception e) {
//			bOk = false;
//		}
//		return bOk;
//	}

	private void init(Context context, Fields data) {
		View view = LayoutInflater.from(context).inflate(R.layout.imageview,
				null);
		ImageView image = (ImageView) view.findViewById(R.id.image);
		byte[] bytes = Base64.decode(data.getStrValue("photo"), 0);
		
//		SaveMsgDetail(bytes,"tsasdasdasd.jpg");
		image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0,
				bytes.length));
		
		this.setView(view);
	}

}
