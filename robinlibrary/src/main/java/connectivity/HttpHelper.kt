package connectivity

import android.accounts.NetworkErrorException
import android.content.Context
import android.net.ConnectivityManager
import br.com.core.robinlibrary.R
import com.google.gson.GsonBuilder
import connectivity.util.NetDateTimeAdapter
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HttpHelper<T>(val context: Context) {

    init {
        conectado(context)
    }


    private fun conectado(context: Context) {
        val connection =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //Verifica se 3G ou Wifi estão Habilitados
        val info = connection.activeNetworkInfo
        if (!info.isConnected)
            throw NetworkErrorException(context.getString(R.string.not_connected))
    }


    @Throws(IOException::class)
    fun GET(tipo: Type, url: String, timeOut: Long): T {
        val client = OkHttpClient.Builder().apply {
            connectTimeout(timeOut, TimeUnit.SECONDS)
            readTimeout(timeOut, TimeUnit.SECONDS)
            writeTimeout(timeOut, TimeUnit.SECONDS)
        }

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.build().newCall(request).execute()
        val gson = GsonBuilder().registerTypeAdapter(Date::class.java,
            NetDateTimeAdapter()
        ).create()
        val retorno = response.body().toString()

        return gson.fromJson(retorno,tipo)

    }

    @Throws(IOException::class)
    fun POST(tipo: Type,url:String, json:String? , seconds:Long):T{
        val client = OkHttpClient.Builder().apply {
            connectTimeout(seconds,TimeUnit.SECONDS)
            readTimeout(seconds, TimeUnit.SECONDS)
            writeTimeout(seconds,TimeUnit.SECONDS)
        }
        var request:Request?= null
        request = if(json != null){
            val body =  RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json)
            Request.Builder().url(url).post(body).build()
        }else{
            Request.Builder().url(url).build()
        }

        val response = client.build().newCall(request).execute()
        val jsonValue = response.body()?.string()

        val gson = GsonBuilder().registerTypeAdapter(Date::class.java,
            NetDateTimeAdapter()
        ).create()

        return gson.fromJson(jsonValue,tipo)
    }

}
