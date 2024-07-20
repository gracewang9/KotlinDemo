package ktHttp

import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import com.google.gson.internal.`$Gson$Types`.getRawType
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import ktHttp.KtHttpV4.asFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.sin

interface ApiServiceV4 {
    @GET("/repo")
    fun repos(
        @Field("lang") lang: String,
        @Field("since") since: String
    ): KtCall<RepoList>

    @GET("/repo")
    fun reposSync(
        @Field("lang") lang: String,
        @Field("since") since: String
    ): RepoList

    @GET("/repo")
    fun reposFlow(
        @Field("lang")
        lang: String,
        @Field("since")
        since: String
    ): Flow<RepoList>
}

object KtHttpV4 {
    private val okHttpClient = OkHttpClient()
    private val gson = Gson()
    var baseUrl = "https://trendings.herokuapp.com"

    fun <T : Any> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service)
        ) { proxy, method, args ->
            for (annotation in method.annotations) {
                if (annotation is GET) {
                    val url = baseUrl + annotation.value
                    return@newProxyInstance invoke<T>(url, method, args!!)
                }
            }
            return@newProxyInstance null
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
                flow<T> {
                    val json = okHttpClient.newCall(request).execute().body()?.string()
                    val result = gson.fromJson<T>(json, getTypeArgument(method))
                    emit(result)
                }
            }
            else -> {
                val json = okHttpClient.newCall(request).execute().body()?.string()
                gson.fromJson<T>(json, method.genericReturnType)
            }
        }
//        return if (isKtCallReturn(method)) {
//            KtCall<T>(call, gson, getTypeArgument(method))
//        } else {
//            val json = okHttpClient.newCall(request).execute().body()?.string()
//            gson.fromJson<Any?>(json, method.genericReturnType)
//        }
    }

    private fun getTypeArgument(method: Method) =
        (method.genericReturnType as ParameterizedType).actualTypeArguments[0]

    private fun isKtCallReturn(method: Method) =
        `$Gson$Types`.getRawType(method.genericReturnType) == KtCall::class.java

    private fun isFlowReturn(method: Method) =
        getRawType(method.genericReturnType) == Flow::class.java

    suspend fun <T : Any> KtCall<T>.await(): T = suspendCancellableCoroutine { continuation ->
        val call = call(object : Callback<T> {
            override fun onSuccess(data: T) {
                println("Request success")
                continuation.resume(data)
            }

            override fun onFail(throwable: Throwable) {
                println("Request fail : $throwable")
                continuation.resumeWithException(throwable)
            }

        })
        continuation.invokeOnCancellation {
            println("Call cancelled!")
            call.cancel()
        }
    }

    fun <T : Any> KtCall<T>.asFlow(): Flow<T> = callbackFlow {
        val call = call(object : Callback<T> {
            override fun onSuccess(data: T) {
                trySendBlocking(data)
                    .onSuccess { close() }
                    .onFailure { close(CancellationException("Send channel fail!", it)) }
            }

            override fun onFail(throwable: Throwable) {
                close(CancellationException("Request fail!", throwable))
            }

        })
        awaitClose { call.cancel() }
    }
}

private suspend fun testFlow() =
    KtHttpV4.create(ApiServiceV4::class.java)
        .reposFlow(lang = "Kotlin", since = "weekly")
//        .asFlow()
        .flowOn(Dispatchers.IO)
        .catch { println("Catch:$it") }
        .collect {
            println("${it.count}")
        }

fun main() = runBlocking {
    testFlow()
}