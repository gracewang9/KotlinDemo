package ktHttp

import com.google.gson.Gson
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import com.google.gson.internal.`$Gson$Types`.getRawType
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.reflect.Proxy
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface Callback<T : Any> {
    //请求成功
    fun onSuccess(data: T)

    //请求失败
    fun onFail(throwable: Throwable)
}

 interface ApiServiceV3 {
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
}

/**
 * suspendCoroutine{}
 * suspendCancellableCoroutine{}
 * 优势：1、可以避免不必要的挂起，提升运行效率；
 * 2、可以避免不必要的资源浪费，改善软件的综合指标。
 */
suspend fun <T : Any> KtCall<T>.await(): T =
    suspendCancellableCoroutine { continuation ->
        val call = call(object : Callback<T> {
            override fun onSuccess(data: T) {
                println("Request success!")
                continuation.resume(data)
            }

            override fun onFail(throwable: Throwable) {
                println("Request fail! $throwable")
                continuation.resumeWithException(throwable)
            }

        })
        continuation.invokeOnCancellation {
            println("Call cancelled!")
            call.cancel()
        }
    }

class KtCall<T : Any>(
    private val call: Call,
    private val gson: Gson,
    private val type: Type
) {
    fun call(callback: Callback<T>): Call {
        call.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFail(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val data = gson.fromJson<T>(response.body()?.string(), type)
                    callback.onSuccess(data)
                } catch (e: java.lang.Exception) {
                    callback.onFail(e)
                }

            }

        })
        return call
    }
}

object KtHttpV3 {
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
        return if (isKtCallReturn(method)) {
            KtCall<T>(call, gson, getTypeArgument(method))
        } else {
            val json = okHttpClient.newCall(request).execute().body()?.string()
            gson.fromJson<Any?>(json, method.genericReturnType)
        }
    }

    private fun getTypeArgument(method: Method) =
        (method.genericReturnType as ParameterizedType).actualTypeArguments[0]

    private fun isKtCallReturn(method: Method) =
        getRawType(method.genericReturnType) == KtCall::class.java
}


private fun testAsync() {
    val api = KtHttpV3.create(ApiServiceV3::class.java)
    api.repos(lang = "Kotlin", since = "weekly")
        .call(object : Callback<RepoList> {
            override fun onSuccess(data: RepoList) {
                println(data)
            }

            override fun onFail(throwable: Throwable) {
                println(throwable)
            }

        })
//    val data = api.reposSync(lang = "Kotlin", since = "weely")
//    println(data)

}

//fun main() {
//    testAsync()
//}
fun main() = runBlocking {
    val start = System.currentTimeMillis()
    val deferred = async {
        KtHttpV3.create(ApiServiceV3::class.java)
            .repos("Kotlin", "weekly").await()
    }
    deferred.invokeOnCompletion { println("invokeOnCompletion!") }
    delay(50L)
    deferred.cancel()
    println("Time cancel : ${System.currentTimeMillis() - start}")
    try {
        println(deferred)
    } catch (e: java.lang.Exception) {
        println("Time exception: ${System.currentTimeMillis() - start}")
        println("Catch exception:$e")
    } finally {
        println("Time total : ${System.currentTimeMillis() - start}")
    }
}

















