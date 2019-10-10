package connectivity

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.core.content.pm.PackageInfoCompat
import br.com.core.robinlibrary.R
import connectivity.util.Calls
import connectivity.util.Response
import java.io.IOException
import java.lang.reflect.Type

class Server<T>(
    private var context: Context,
    private var url: String,
    private var tentativas: Int = 1
) {
    private var timeOut: Long = 120

    fun setUrl(urlServer: String): Server<T> {
        url = urlServer
        return this
    }

    fun setTentativas(values: Int): Server<T> {
        tentativas = values
        return this
    }

    fun setTimeOut(value: Long): Server<T> {
        timeOut = value
        return this
    }

    private fun getVersionApi(urlServidor: String): String {
        var code = 0L
        try {
            code = PackageInfoCompat.getLongVersionCode(
                context.packageManager.getPackageInfo(
                    context.packageName,
                    0
                )
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return String.format("%sv%s/", urlServidor, code.toString())
    }

    @Throws(IOException::class, NetworkErrorException::class)
    fun GET(tipoRetorno: Type, chamada: Calls, vararg parametros: String): Response<T> {
        var tentativa = 0
        var retorno: Response<T>? = null

        val endereco = StringBuilder(String.format("%s%s", getVersionApi(url), chamada.toString()))

        if (parametros.isNotEmpty()) endereco.append("?")
        parametros.forEach {
            endereco.append(String.format("%s&", it))
        }

        while (tentativa < tentativas) {
            tentativa++
            try {
                retorno =
                    HttpHelper<Response<T>>(context).GET(tipoRetorno, endereco.toString(), timeOut)
                if (retorno.result)
                    tentativa = tentativas + 1
                else if ((!(retorno.result)) && ((retorno.exception != null && retorno.exception.isNullOrEmpty()) || (retorno.exception == null && retorno.message != null)))
                    tentativa = tentativas + 1
                else
                    retorno = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (retorno == null)
            throw NetworkErrorException(context.getString(R.string.not_connected))

        return retorno
    }

    @Throws(Exception::class)
    fun GET_GENERIC(tipoRetorno: Type, chamada: Calls, vararg parametros: String): T {
        var tentativa = 0
        var retorno: T? = null

        val endereco = StringBuilder(String.format("%s%s", url, chamada.toString()))
        if (parametros.isNotEmpty()) endereco.append("?")
        parametros.forEach {
            endereco.append(String.format("%s&", it))
        }


        while (tentativa < tentativas) {
            tentativa++
            try {
                retorno = HttpHelper<T>(context).GET(tipoRetorno, endereco.toString(), timeOut)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        if (retorno == null) {
            throw NetworkErrorException(context.getString(R.string.not_connected))
        }

        return retorno

    }

    @Throws(IOException::class, NetworkErrorException::class)
    fun POST(tipoRetorno: Type, chamada: Calls, json: String): Response<T> {
        var tentativa = 0
        var retorno: Response<T>? = null

        val endereco = String.format("%s%s", getVersionApi(url), chamada.toString())

        while (tentativa < tentativas) {
            tentativa++
            try {
                retorno =
                    HttpHelper<Response<T>>(context).POST(tipoRetorno, endereco, json, timeOut)

                if (retorno.result)
                    tentativa = tentativas + 1
                else if ((!(retorno.result)) && ((retorno.exception != null && retorno.exception.isNullOrEmpty()) || (retorno.exception == null && retorno.message != null)))
                    tentativa = tentativas + 1
                else
                    retorno = null
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        if (retorno == null) {
            throw  Exception(context.getString(R.string.not_connected))
        }
        return retorno
    }

    @Throws(NetworkErrorException::class)
    fun POST_GENERIC(tipoRetorno: Type, chamada: Calls, json: String): T {
        var tentativa = 0
        var retorno: T? = null
        val endereco = String.format("%s%s", url, chamada.toString())

        while (tentativa < tentativas) {
            tentativa++
            try {
                retorno = HttpHelper<T>(context).POST(tipoRetorno, endereco, json, this.timeOut)

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        if (retorno == null)
            throw NetworkErrorException(context.getString(R.string.not_connected))

        return retorno

    }


}