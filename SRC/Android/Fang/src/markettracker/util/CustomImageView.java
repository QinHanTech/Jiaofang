package markettracker.util;

import markettracker.data.UIItem;
import com.rh.fang.jf.R;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class CustomImageView extends ImageView {

	// private Context mContext;
	public CustomImageView(Context context, UIItem item) {
		super(context);
		initBackground(item);
	}

	private void initBackground(UIItem item) {
		LinearLayout.LayoutParams line = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(line);

		this.setBackgroundResource(R.drawable.mustinput);

	}

}
