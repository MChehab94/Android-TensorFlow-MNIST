package mchehab.com.mnist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class DrawView extends View {

    private Paint paint;
    private Path path;
    private Bitmap bitmap;

    private final int WIDTH = 28;
    private final int HEIGHT = 28;

    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
        path = new Path();
    }

    private void setupPaint(){
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.SQUARE);
    }

    public void clearCanvas(){
        bitmap.recycle();
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        path = new Path();
        invalidate();
    }

    private Bitmap getBitmap(){
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        draw(canvas);
        return Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, false);
    }

    public float[] getPixels(){
        Bitmap bitmap = getBitmap();
        int size = WIDTH * HEIGHT;
        int[] pixels = new int[size];
        bitmap.getPixels(pixels, 0, WIDTH, 0, 0, WIDTH, HEIGHT);
        float[] bitmapPixels = new float[pixels.length];
        for (int i = 0; i < pixels.length; ++i) {
            //0 if white and 255 if black
            int pixel = pixels[i];
            int xor = pixel & 0xff;
            //value between 0 and 1
            bitmapPixels[i] = (float)((0xff - xor)/255.0);
        }
        return bitmapPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        float pointX = motionEvent.getX();
        float pointY = motionEvent.getY();
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN: path.moveTo(pointX, pointY);break;
            case MotionEvent.ACTION_MOVE: path.lineTo(pointX, pointY);break;
            default: return false;
        }
        postInvalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
    }
}