import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import java.util.*

fun Spinner.setUpListWithAdapter(list: MutableList<String>, callBack: (String) -> Unit) {
    val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    this.adapter = adapter
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//      p2=position
            callBack(
                list[p2]
            )
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

    }
}

fun MutableList<out Any>.getIndexFromList(item: Any): Int {
    var index = 0
    for (i in this.indices) {
        if (this[i] == item) {
            index = i
            break
        }
    }
    return index
}