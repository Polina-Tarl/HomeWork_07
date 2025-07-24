package otus.homework.customview.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import otus.homework.customview.R

class GraphicPaymentCategoryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    data class CategoryPoint(val day: Int, val amount: Float)

    private var categoryData: Map<String, List<CategoryPoint>> = emptyMap()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = 1f
    }
    private val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        textSize = 24f
    }

    private val colorList = ArrayList<Int>(10).apply {
        add(context.getColor(R.color.blue))
        add(context.getColor(R.color.pink_light))
        add(context.getColor(R.color.green))
        add(context.getColor(R.color.purple))
        add(context.getColor(R.color.orange))
        add(context.getColor(R.color.coral))
        add(context.getColor(R.color.purple_light))
        add(context.getColor(R.color.pink))
        add(context.getColor(R.color.blue_green))
        add(context.getColor(R.color.yellow))
    }

    private var savedColorIndex = 0

    fun setCategoryData(data: Map<String, List<CategoryPoint>>) {
        categoryData = data
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth = 600
        val desiredHeight = 400

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> minOf(desiredWidth, widthSize)
            MeasureSpec.UNSPECIFIED -> desiredWidth
            else -> desiredWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> minOf(desiredHeight, heightSize)
            MeasureSpec.UNSPECIFIED -> desiredHeight
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    @SuppressLint("DefaultLocale", "DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val legendStartY = 10f
        var legendOffsetY = 0f
        var legendColorIndex = 0
        categoryData.keys.forEach { category ->
            val color = colorList[legendColorIndex % colorList.size]
            legendColorIndex++
            val left = width - 200f
            val top = legendStartY + legendOffsetY
            val size = 20f

            paint.color = color
            paint.style = Paint.Style.FILL
            canvas.drawRect(left, top, left + size, top + size, paint)

            axisPaint.color = Color.GRAY
            canvas.drawText(category, left + size + 10f, top + size, axisPaint)
            legendOffsetY += 30f
        }
        super.onDraw(canvas)
        if (categoryData.isEmpty()) return

        val allPoints = categoryData.values.flatten()
        val maxY = (allPoints.maxOfOrNull { it.amount } ?: 0f).coerceAtLeast(1f)
        val minDay = allPoints.minOf { it.day }
        val maxDay = allPoints.maxOf { it.day }

        val chartLeft = 80f
        val chartRight = width.toFloat() - 20f
        val chartTop = 20f
        val chartBottom = height.toFloat() - 60f

        val chartHeight = chartBottom - chartTop
        val chartWidth = chartRight - chartLeft

        val ySteps = 4
        val yStepValue = maxY / ySteps
        for (i in 0..ySteps) {
            val y = chartBottom - i * chartHeight / ySteps
            canvas.drawLine(chartLeft, y, chartRight, y, gridPaint)
            val label = "${(yStepValue * i).toInt()} ₽"
            canvas.drawText(label, 0f, y, axisPaint)
        }

        val totalDays = maxDay - minDay + 1
        for (i in 0 until totalDays step 2) {
            val x = chartLeft + (i.toFloat() / totalDays) * chartWidth
            val label = String.format("%02d", minDay + i)
            canvas.drawText(label, x, height.toFloat() - 10f, axisPaint)
        }

        var colorIndex = 0
        categoryData.forEach { (category, points) ->
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 6f
            paint.color = colorList[colorIndex % colorList.size]
            colorIndex++

            val path = Path()
            points.sortedBy { it.day }.forEachIndexed { index, point ->
                val x = chartLeft + ((point.day - minDay).toFloat() / totalDays) * chartWidth
                val y = chartBottom - (point.amount / maxY) * chartHeight
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            canvas.drawPath(path, paint)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putParcelable("superState", superState)
        bundle.putInt("savedColorIndex", savedColorIndex)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            savedColorIndex = state.getInt("savedColorIndex", 0)
            super.onRestoreInstanceState(state.getParcelable("superState"))
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}

