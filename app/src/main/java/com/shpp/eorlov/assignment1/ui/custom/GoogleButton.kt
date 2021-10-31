package com.shpp.eorlov.assignment1.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.annotation.Px
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toRectF
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.utils.ext.dpToPx
import com.shpp.eorlov.assignment1.utils.ext.drawableToBitmap
import android.graphics.RectF

import android.graphics.PorterDuff

import android.graphics.PorterDuffXfermode

import android.graphics.Bitmap


class GoogleButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatImageButton(context, attrs) {

    companion object {
        private const val DEFAULT_ICON: Int = R.drawable.ic_google_icon
        private const val DEFAULT_RADIUS = 6
        private const val DEFAULT_SIZE = 40
    }

    @Px
    var radius: Float = context.dpToPx(DEFAULT_RADIUS)

    private val text = context.getString(R.string.google)

    private var maskBitmap: Bitmap? = null
    private var maskPaint: Paint? = null
    private var rect: Rect = Rect()
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var iconBitmap: Bitmap? = null

    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.GoogleButton)
            radius = ta.getDimension(
                R.styleable.GoogleButton_radius,
                context.dpToPx(DEFAULT_RADIUS)
            )

            val drawableIcon: Drawable? =
                ta.getDrawable(R.styleable.GoogleButton_src) ?: ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_google_icon,
                    null
                )

            iconBitmap = drawableIcon?.drawableToBitmap()
            ta.recycle()
        }

        maskPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        maskPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun performClick(): Boolean {
        println("WORK!")
        return super.performClick()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        rect.run {
            left = 0
            right = w
            top = 0
            bottom = context.dpToPx(DEFAULT_SIZE).toInt()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentViewWidth = MeasureSpec.getSize(widthMeasureSpec)
        // ... take into account the parent's size as needed ...
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(parentViewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(context.dpToPx(DEFAULT_SIZE).toInt(), MeasureSpec.EXACTLY)
        )
    }

    override fun onDraw(canvas: Canvas) {

        if (maskBitmap == null) {
            maskBitmap = createMask(width, height)
        }

        canvas.drawBitmap(maskBitmap!!, 0f, 0f, maskPaint)

        background = null


        paint.color = Color.WHITE

        canvas.drawRoundRect(rect.toRectF(), radius, radius, paint)

        paint.color = Color.BLACK

        paint.textSize = (rect.bottom - rect.top) / 2f

        val textPosition = ((rect.right - rect.left - paint.textSize) / 2f)

        canvas.drawText(
            text,
            ((rect.right - rect.left - paint.textSize) / 2f),
            (height - paint.descent() - paint.ascent()) / 2f,
            paint,
        )

        iconBitmap?.let {
            canvas.drawBitmap(it, textPosition - 100, height / 4f, null)
        }

    }

    private fun createMask(width: Int, height: Int): Bitmap? {
        val mask = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
        val canvas = Canvas(mask)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            radius,
            radius,
            paint
        )
        return mask
    }
}