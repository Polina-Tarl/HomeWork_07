package otus.homework.customview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import otus.homework.customview.view.PieChartView

class GraphicPaymentCategoryFragment : Fragment(R.layout.fragment_graphic_payment_category) {

    private val pieChartItems = listOf(
        PieChartView.PieEntry(5000f, "Кошка"),
        PieChartView.PieEntry(25000f, "Продукты"),
//        PieChartView.PieEntry(10000f, "Развлечения"),
//        PieChartView.PieEntry(5000f, "Еда вне дома"),
//        PieChartView.PieEntry(2300f, "Медицина"),
//        PieChartView.PieEntry(4000f, "Транспорт"),
//        PieChartView.PieEntry(1200f, "Подписки"),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pieChartView = view.findViewById<PieChartView>(R.id.pie_chart)

        pieChartView.setEntriesItems(pieChartItems)
    }

}