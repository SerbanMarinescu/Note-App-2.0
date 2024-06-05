package com.example.newnotesapp.presentation.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.ColorUtils

class CustomCanvasView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        isAntiAlias = true
    }
    private val cornerRadius = 20f
    private val cutCornerSize = 100f

    var noteColor: Int = Color.RED
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val path = Path().apply {
            lineTo(width - cutCornerSize, 0f)
            lineTo(width.toFloat(), cutCornerSize)
            lineTo(width.toFloat(), height.toFloat())
            lineTo(0f, height.toFloat())
            close()
        }

        canvas.save()
        canvas.clipPath(path)

        paint.color = noteColor
        canvas.drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            cornerRadius,
            cornerRadius,
            paint
        )

        paint.color = ColorUtils.blendARGB(noteColor, Color.BLACK, 0.2f)
        canvas.drawRoundRect(
            width - cutCornerSize,
            -100f,
            width + 100f,
            cutCornerSize,
            cornerRadius,
            cornerRadius,
            paint
        )

        canvas.restore()
    }
}