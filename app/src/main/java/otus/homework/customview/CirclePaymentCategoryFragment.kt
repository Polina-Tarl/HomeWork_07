package otus.homework.customview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.json.JSONArray
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

        val jsonString =
            this.resources.openRawResource(R.raw.payload).bufferedReader().use { it.readText() }
        val data = parsePayload(jsonString)

        pieChartView.setEntriesItems(data)

        pieChartView.setOnCategoryClickListener { entry ->
            val categoryAmount = this.resources.getString(
                R.string.total_amount_center, "%.2f".format(entry.value)
            )
            val totalText = entry.category + " " + categoryAmount
            pieChartView.setTotalText(totalText)
        }
    }

    private fun parsePayload(jsonString: String): List<PieEntry> {
        val jsonArray = JSONArray(jsonString)
        val categorySums = mutableMapOf<String, Float>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val category = obj.getString("category")
            val amount = obj.getDouble("amount").toFloat()
            categorySums[category] = (categorySums[category] ?: 0f) + amount
        }

        // Преобразуем в список пар: категория - сумма
        return categorySums.map { PieEntry(it.value, it.key) }
    }
}