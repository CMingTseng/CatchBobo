package c.min.tseng.util;

import c.min.tseng.Ocr;

import com.google.android.gms.common.api.a.c;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.View;

public class Guides extends View {
	Paint mPaint = new Paint();
	int sideLength;
	int deviceWidth;
	int deviceHeight;
	String mFace;
	private static String TAG = "ScanBarZBarActivity";
	
	
	public Guides(Context context, String face) {
		super(context);
		
		mFace = face;
		mPaint.setColor(Color.RED);
		//Main == LoadCube
		Display display = ((Ocr) context).getWindowManager().getDefaultDisplay();	
		//Display display = ((CameraShow) context).getWindowManager().getDefaultDisplay();
		deviceWidth = display.getWidth();
		deviceHeight = display.getHeight();
		

		
		sideLength = (int) (Math.min(deviceWidth, deviceHeight) * .9);
	}
	
	public String getSide() {
		return mFace;
	}

	@Override
	public void onDraw(Canvas canvas) {
		int x0,y0,x1,y1,margin;
		margin = (int) (Math.min(deviceWidth, deviceHeight) * .1);
		for (int i=1; i<3; i++) {
			x0 = ((deviceWidth - sideLength) / 2) + (sideLength / 2) * i;
			y0 = margin/2; y1 = y0+sideLength;
			canvas.drawLine(x0,y0,x0,y1,mPaint);
			

	}

  }
	
}
