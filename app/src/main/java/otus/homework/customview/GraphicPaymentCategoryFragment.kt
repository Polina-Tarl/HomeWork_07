package otus.homework.customview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import otus.homework.customview.view.GraphicPaymentCategoryView
import otus.homework.customview.view.PieChartView

class GraphicPaymentCategoryFragment : Fragment(R.layout.fragment_graphic_payment_category) {

    private val data = mapOf(
        "Кошка" to listOf(
            GraphicPaymentCategoryView.CategoryPoint(1, 100f),
            GraphicPaymentCategoryView.CategoryPoint(4, 500f),
            GraphicPaymentCategoryView.CategoryPoint(7, 1000f),
            GraphicPaymentCategoryView.CategoryPoint(10, 180f),
            GraphicPaymentCategoryView.CategoryPoint(17, 1600f),
            GraphicPaymentCategoryView.CategoryPoint(20, 500f)
        ),
        "Еда" to listOf(
            GraphicPaymentCategoryView.CategoryPoint(1, 2000f),
            GraphicPaymentCategoryView.CategoryPoint(4, 4000f),
            GraphicPaymentCategoryView.CategoryPoint(7, 9000f),
            GraphicPaymentCategoryView.CategoryPoint(10, 1200f),
            GraphicPaymentCategoryView.CategoryPoint(17, 1000f),
            GraphicPaymentCategoryView.CategoryPoint(20, 3000f)
        ),
        "Развлечения" to listOf(
            GraphicPaymentCategoryView.CategoryPoint(7, 5000f),
            GraphicPaymentCategoryView.CategoryPoint(10, 1100f),
            GraphicPaymentCategoryView.CategoryPoint(14, 1500f),
            GraphicPaymentCategoryView.CategoryPoint(17, 12000f),
            GraphicPaymentCategoryView.CategoryPoint(19, 700f),
            GraphicPaymentCategoryView.CategoryPoint(20, 2000f)
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pieChartView = view.findViewById<GraphicPaymentCategoryView>(R.id.graphic)

        pieChartView.setCategoryData(data)
    }

}