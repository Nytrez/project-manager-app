import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

fun isValidDateFormat(dateString: String, format: String = "yyyy-MM-dd"): Boolean {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    dateFormat.isLenient = false
    return try {
        dateFormat.parse(dateString)
        true
    } catch (e: ParseException) {
        false
    }
}