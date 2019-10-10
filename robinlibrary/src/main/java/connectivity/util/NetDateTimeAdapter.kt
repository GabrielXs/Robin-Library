package connectivity.util

import Util.DateTime
import android.text.TextUtils
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.text.SimpleDateFormat
import java.util.*

class NetDateTimeAdapter : TypeAdapter<Date>() {
    override fun write(out: JsonWriter?, value: Date?) {

    }

    override fun read(valueIn: JsonReader?): Date? {
        if (valueIn?.peek() === JsonToken.NULL) {
            valueIn.nextNull()
            return null
        }

        var result: Date? = null
        var str = valueIn!!.nextString()
        str = str.replace("/Date(", "").replace(")/", "")

        var zona: String? = null
        if (!TextUtils.isEmpty(str)) {
            if (str.length > 5 && (str.indexOfLast { arrayOf('-', '+').contains(it) } == str.length - 5)) {
                zona = str.substring(str.length - 5, str.length)
                val length = zona.length
                zona = String.format("GMT%s:%s", zona.subSequence(0, 3), zona.substring(length - 2, length))

                str = str.substring(0, str.length - 5)
            }

            try {
                result = try {
                    val df = SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.getDefault())
                    if (zona != null) df.timeZone = TimeZone.getTimeZone(zona)
                    DateTime(df.format(Date(str.toLong()))).getData()
                } catch (_: Exception) {
                    DateTime(str).getData()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        return result
    }
}