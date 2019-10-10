package connectivity.util

class Response<T> {
    var objeto: T? = null
    var result: Boolean = false
    var message: String? = null
    var exception: String? = null
    var stacktrace:String? = null
}