package otus.homework.customview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import otus.homework.customview.view.PieChartView
import otus.homework.customview.view.PieChartView.PieEntry

class CirclePaymentCategoryFragment : Fragment(R.layout.fragment_circle_payment_category) {

    private val pieChartItems = listOf(
        PieEntry(5000f, "Кошка"),
        PieEntry(25000f, "Продукты"),
        PieEntry(10000f, "Развлечения"),
        PieEntry(5000f, "Еда вне дома"),
        PieEntry(2300f, "Медицина"),
        PieEntry(4000f, "Транспорт"),
        PieEntry(1200f, "Подписки"),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pieChartView = view.findViewById<PieChartView>(R.id.pie_chart)

        pieChartView.setEntriesItems(pieChartItems)
    }
}