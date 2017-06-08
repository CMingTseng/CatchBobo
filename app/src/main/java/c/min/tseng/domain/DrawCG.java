package c.min.tseng.domain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class DrawCG extends View {
    private final static String TAG = "DrawCG";
    private Paint mPaint;
    private Canvas mCanv;
    private Bitmap mBitmap;

    public DrawCG(Context context) {
        super(context);
        // 畫筆
        mPaint = new Paint();
        // 顏色 
        mPaint.setColor(Color.RED);
        // 反鋸齒
        mPaint.setAntiAlias(true);
        // 線寬  
        mPaint.setStrokeWidth(3);
        // 畫布
        //mCanv = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    /**
     * 畫線
     */
    public Bitmap drawLine() {
        mCanv.drawLine(200, 50, 600, 50, mPaint);
        return mBitmap;
    }

    /**
     * 畫圓
     */
    public Bitmap drawCircle(float x, float y, int color) {
        // 填充
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(color);
        mCanv.drawCircle(x, y, 10.0f, mPaint);
        return mBitmap;
    }

    /**
     * 還原畫圓
     */
    public void restoreCircle(float x, float y, int count, int color) {
        Log.d(TAG, " restoreCircle : [" + count + "]");
    }

    /**
     * 畫三角形
     */
    public Bitmap drawTriangle() {
        final Path path = new Path();
        path.moveTo(300, 600);
        path.lineTo(600, 200);
        path.lineTo(900, 600);
        path.lineTo(300, 600);
        mCanv.drawPath(path, mPaint);
        return mBitmap;
    }

    /**
     * 畫矩型
     */
    public Bitmap drawRect(int left, int top, int right, int bottom, int color, int strokeWidth) {
        // 非填充
        mPaint.setStyle(Style.STROKE);
        mPaint.setColor(color);
        mPaint.setStrokeWidth(strokeWidth);
        mCanv.drawRect(new Rect(left, top, right, bottom), mPaint);
        return mBitmap;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            this.mBitmap = bitmap;
            mCanv = new Canvas(bitmap);
        }
    }
}