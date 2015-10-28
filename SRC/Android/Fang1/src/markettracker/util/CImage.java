package markettracker.util;

import android.content.Context;
import android.widget.ImageView;

public class CImage extends ImageView {
	// private Rpt_Table table;
	private int index;

	private CDeleteButton button;

	public CImage(Context context, CDeleteButton button,int index) {
		super(context);
		this.button = button;
		this.index=index;
		init(context);
	}

	private void init(Context context) {

		// initLine(context, temp);
	}
	public int getIndex()
	{
		return index;
	}
	
	public CDeleteButton getDelete()
	{
		return button;
	}

}
