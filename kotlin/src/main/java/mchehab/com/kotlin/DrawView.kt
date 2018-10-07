package mchehab.com.kotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class DrawView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var paint: Paint? = null
    private var path: Path? = null
    private var bitmap: Bitmap? = null

    private val WIDTH = 28
    private val HEIGHT = 28

    val pixels: FloatArray
        get() {
            val bitmap = getBitmap()
            val size = WIDTH * HEIGHT
            val pixels = IntArray(size)
            bitmap.getPixels(pixels, 0, WIDTH, 0, 0, WIDTH, HEIGHT)
            val bitmapPixels = FloatArray(pixels.size)
            for (i in pixels.indices) {
                //0 if white and 255 if black
                val pixel = pixels[i]
                val xor = pixel and 0xff
                //value between 0 and 1
                bitmapPixels[i] = ((0xff - xor) / 255.0).toFloat()
            }
            return bitmapPixels
        }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        setupPaint()
        path = Path()
    }

    private fun setupPaint() {
        paint = Paint()
        paint!!.color = Color.BLACK
        paint!!.isAntiAlias = true
        paint!!.strokeWidth = 30f
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeCap = Paint.Cap.SQUARE
    }

    fun clearCanvas() {
        bitmap!!.recycle()
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        path = Path()
        invalidate()
    }

    private fun getBitmap(): Bitmap {
        val canvas = Canvas(bitmap!!)
        canvas.drawColor(Color.WHITE)
        draw(canvas)
        return Bitmap.createScaledBitmap(bitmap!!, WIDTH, HEIGHT, false)
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        val pointX = motionEvent.x
        val pointY = motionEvent.y
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> path!!.moveTo(pointX, pointY)
            MotionEvent.ACTION_MOVE -> path!!.lineTo(pointX, pointY)
            else -> return false
        }
        postInvalidate()
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap!!, 0f, 0f, paint)
        canvas.drawPath(path!!, paint!!)
    }
}