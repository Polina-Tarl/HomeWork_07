package otus.homework.customview.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import otus.homework.customview.R
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.math.sin

class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var totalText: String? = null

    data class PieEntry(val value: Float, val category: String)

    private var entries: List<PieEntry> = emptyList()
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

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
    }
    private val percentPaint = Paint().apply {
        color = Color.GRAY
        textAlign = Paint.Align.CENTER
        textSize = TEXT_SIZE_PERS
    }
    private val rectF = RectF()
    private val paint = Paint()
    private var savedStartAngle = 0f

    init {
        Log.i("PieChartView", "hashCode=${hashCode()} created")
    }

    fun setEntriesItems(items: List<PieEntry>) {
        entries = items
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)

        val desiredSize = 300
        val finalWidth = when (modeWidth) {
            MeasureSpec.EXACTLY -> width
            MeasureSpec.AT_MOST -> minOf(desiredSize, width)
            MeasureSpec.UNSPECIFIED -> desiredSize
            else -> desiredSize
        }

        val finalHeight = when (modeHeight) {
            MeasureSpec.EXACTLY -> height
            MeasureSpec.AT_MOST -> minOf(desiredSize, height)
            MeasureSpec.UNSPECIFIED -> desiredSize
            else -> desiredSize
        }

        val size = minOf(finalWidth, finalHeight)
        setMeasuredDimension(size, size)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (entries.isEmpty()) return

        val total = entries.sumOf { it.value.toDouble() }.toFloat()
        var startAngle = savedStartAngle
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width / 2f * 0.85f
        val innerRadius = radius * CENTER_CIRCLE_PERS

        rectF.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        entries.forEachIndexed { index, entry ->
            val sweepAngle = (entry.value / total) * CIRCLE_DEGREE
            paint.color = colorList.getOrElse(
                index % colorList.size,
                defaultValue = { context.getColor(R.color.yellow) }
            )
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint)
            val angle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
            val labelRadius = radius + PADDING_PERS
            val labelX = (centerX + cos(angle) * labelRadius).toFloat()
            val labelY = (centerY + sin(angle) * labelRadius).toFloat()
            val percentage = (entry.value / total * 100).roundToInt().toString() + "%"
            canvas.drawText(percentage, labelX, labelY, percentPaint)
            startAngle += sweepAngle
        }

        canvas.drawCircle(centerX, centerY, innerRadius, paint.apply {
            color = Color.WHITE
        })

        val totalFormatted =
            totalText ?: context.getString(R.string.total_amount_center, "%.2f".format(total))
        textPaint.textSize = TEXT_SIZE_TOTAL
        canvas.drawText(totalFormatted, centerX, centerY - (innerRadius / 10), textPaint)
        textPaint.textSize = TEXT_SIZE_MONTH
        canvas.drawText(
            context.getString(R.string.for_month),
            centerX,
            centerY + (innerRadius / 10),
            textPaint
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN) return false
        if (entries.isEmpty()) return false

        val x = event.x - width / 2f
        val y = event.y - height / 2f
        val touchRadius = hypot(x, y)
        val radius = width / 2f
        val innerRadius = radius * CENTER_CIRCLE_PERS

        if (touchRadius > radius || touchRadius < innerRadius) return false

        val touchAngle = (Math.toDegrees(atan2(y.toDouble(), x.toDouble())) + 360) % 360

        val total = entries.sumOf { it.value.toDouble() }.toFloat()
        var startAngle = savedStartAngle

        entries.forEachIndexed { index, entry ->
            val sweepAngle = (entry.value / total) * CIRCLE_DEGREE
            if (touchAngle >= startAngle && touchAngle < startAngle + sweepAngle) {
                val categoryAmount = context.getString(
                    R.string.total_amount_center, "%.2f".format(entry.value)
                )
                totalText = entry.category + " " + categoryAmount
                invalidate()
                return true
            }
            startAngle += sweepAngle
        }
        return false
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val bundle = Bundle()
        bundle.putParcelable("superState", superState)
        bundle.putFloat("startAngle", savedStartAngle)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            savedStartAngle = state.getFloat("startAngle", 0f)
            super.onRestoreInstanceState(state.getParcelable("superState"))
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    companion object {
        private const val CIRCLE_DEGREE = 360
        private const val TEXT_SIZE_TOTAL = 50f
        private const val TEXT_SIZE_MONTH = 40f
        private const val TEXT_SIZE_PERS = 30f
        private const val PADDING_PERS = 40
        private const val CENTER_CIRCLE_PERS = 0.6f
    }
}
