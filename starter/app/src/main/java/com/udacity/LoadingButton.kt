package com.udacity

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private var backgroundColorValue = 0
    private var textColorValue = 0

    private val buttonText = resources.getString(R.string.button_name)
    private var textBounds = Rect()

    // progress value when loading
    // between 0.0 and 1.0
    private var progress: Float = 0.0F

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        if (buttonState == ButtonState.Initial || buttonState == ButtonState.Completed) {
            reset()
        } else if (buttonState == ButtonState.Loading) {
            startAnimator()
            isClickable = false
        }
    }

    private fun startAnimator() {
        valueAnimator.setFloatValues(0.0F, 1.0F)
        valueAnimator.duration = 2300
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.addUpdateListener { animator ->
            progress = animator.animatedValue as Float
            invalidate()
        }

        valueAnimator.start()
    }

    private fun reset() {
        progress = 0.0F
        isClickable = true
        cancelAnimator()
        invalidate()
    }

    private fun cancelAnimator() {
        TODO("Not yet implemented")
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }


    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            backgroundColorValue = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            textColorValue = getColor(R.styleable.LoadingButton_textColor, 0)
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.save()
        drawButton(ButtonState.Initial, canvas)
        canvas?.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private fun drawButton(state: ButtonState, canvas: Canvas?) {
        if (state == ButtonState.Initial || state == ButtonState.Completed) {
            paint.color = backgroundColorValue
            canvas?.drawRect(0.0F, 0.0F, widthSize.toFloat(), heightSize.toFloat(), paint)

            drawButtonText(canvas)
        } else if (state == ButtonState.Loading) {
            canvas?.drawRect(0.0F, 0.0F, progress * width.toFloat(), height.toFloat(), paint)

            drawButtonText(canvas)
            drawCircle(canvas)
        }
    }

    private fun drawButtonText(canvas: Canvas?) {
        paint.color = textColorValue
        paint.getTextBounds(buttonText, 0, buttonText.length, textBounds);
        canvas?.drawText(buttonText, (widthSize / 2).toFloat(), (heightSize / 2 - textBounds.exactCenterY()), paint)
    }

    private fun drawCircle(canvas: Canvas?) {
        val textBounds = paint.getTextBounds(buttonText, 0, buttonText.length, textBounds);

        //canvas.drawArc()
    }

}