package ktHttp

import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`.getRawType
import coroutines.logX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy

interface ApiServiceV5 {
    @GET("/repo")
    fun repos(
        @Field("lang")
        lang: String,
        @Field("since")
        since: String
    ): KtCall<RepoList>

    @GET("/repo")
    fun reposeFlow(
        @Field("lang") lang: String,
        @Field("since") since: String
    ): Flow<RepoList>
}

object KtHttpV5 {
    private var okHttpClient: OkHttpClient = OkHttpClient()
    private var gson: Gson = Gson()
    private val baseUrl = "https://baseUrl.com"

    fun <T : Any> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service)
        ) { proxy, method, args ->
            val annotations = method.annotations
            for (annotation in annotations) {
                if (annotation is GET) {
                    val url = baseUrl + annotation.value
                    return@newProxyInstance invoke<T>(url, method, args!!)
                }
            }
        } as T
    }

    fun <T : Any> invoke(path: String, method: Method, args: Array<Any>): Any? {
        if (method.parameterAnnotations.size != args.size) return null
        var url = path
        val parameterAnnotations = method.parameterAnnotations
        for (i in parameterAnnotations.indices) {
            for (parameterAnnotation in parameterAnnotations[i]) {
                if (parameterAnnotation is Field) {
                    val key = parameterAnnotation.value
                    val value = args[i].toString()
                    url += if (!url.contains("?")) {
                        "?$key=$value"
                    } else {
                        "&$key=$value"
                    }
                }
            }
        }
        val request = Request.Builder().url(url).build()
        val call = okHttpClient.newCall(request)
        return when {
            isKtCallReturn(method) -> {
                KtCall<T>(call, gson, getTypeArgument(method))
            }
            isFlowReturn(method) -> {
                logX("Start out")
                flow<T> {
                    logX("Start in")
                    val json = okHttpClient.newCall(request).execute().body()?.string()
                    val result = gson.fromJson<T>(json, getTypeArgument(method))
                    logX("Start emit")
                    emit(result)
                    logX("end emit")
                }
            }
            else -> {
                val json = okHttpClient.newCall(request).execute().body()?.string()
                gson.fromJson(json, method.genericReturnType)
            }
        }
    }

    private fun getTypeArgument(method: Method) =
        (method.genericReturnType as ParameterizedType).actualTypeArguments[0]

    private fun isKtCallReturn(method: Method) =
        getRawType(method.genericReturnType) == KtCall::class.java

    private fun isFlowReturn(method: Method) = getRawType(method.genericReturnType) == Flow::class.java
}

fun main() = runBlocking {
    testFlow()
}

private suspend fun testFlow() {
    KtHttpV5.create(ApiServiceV5::class.java)
        .reposeFlow(lang = "Kotlin", since = "weekly")
        .flowOn(Dispatchers.IO)
        .catch { println("Catch: $it") }
        .collect{
            logX("${it.count}")
        }
}





















