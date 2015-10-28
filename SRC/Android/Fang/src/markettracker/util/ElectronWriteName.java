package markettracker.util;

import com.rh.fang.jf.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class ElectronWriteName extends View {
	private Paint paint, bitmapPaint;
	private Bitmap bitmap, startBitmap;
	private Canvas canvas;
	private Path path;

	// public static boolean firsttime = false;

	public ElectronWriteName(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private int getTextColor(Context c) {
		return getContext().getResources().getColor(R.color.red);
	}

	private int getBackColor(Context c) {
		return getContext().getResources().getColor(R.color.white);
	}

	public Bitmap getImgInfo() {
		return bitmap;
	}

	public void resetSign() {
		initContral();

		invalidate();
	}

	public void setSingImg(Bitmap bmap) {
		bitmap = bmap;
		// mCanvas = new Canvas(bitmap);
		invalidate();
	}

	private void initContral() {
		bitmap = null;
		canvas = null;
		path = null;
		bitmapPaint = null;
		bitmap = startBitmap;
		canvas = new Canvas(bitmap);
		path = new Path();

		bitmapPaint = new Paint(Paint.DITHER_FLAG);

	}

	public ElectronWriteName(Context c, Bitmap bitmap) {
		super(c);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(getTextColor(c));
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(2);

		Bitmap zoomBitmap = Tool.zoomPhoto(c, bitmap).copy(
				Bitmap.Config.ARGB_8888, true);
		// Bitmap zoomBitmap=bitmap.copy(Bitmap.Config.ARGB_8888, true);
		startBitmap = zoomBitmap;
		this.setLayoutParams(new LayoutParams(zoomBitmap.getWidth(), zoomBitmap
				.getHeight()));
		initContral();
		
	}

	public ElectronWriteName(Context c) {
		super(c);

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(getTextColor(c));
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(2);
		startBitmap = Bitmap.createBitmap(360, 480, Bitmap.Config.ARGB_8888);
		initContral();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(getBackColor(getContext()));
		canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
		canvas.drawPath(path, paint);
	}

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y) {
		path.reset();
		path.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
			// firsttime = true;
		}
	}

	private void touch_up() {
		path.lineTo(mX, mY);
		// commit the path to our offscreen
		canvas.drawPath(path, paint);
		// kill this so we don't double draw
		path.reset();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		}
		return true;
	}
}
