package ktHttp

import User
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Method
import java.lang.reflect.Proxy

data class RepoList(
    var count: Int?,
    var items: List<Repo>?,
    var msg: String?
)

data class Repo(
    var added_starts: String?,
    var avatars: List<String>?,
    var desc: String?,
    var forks: String?,
    var lang: String?,
    var repo: String?,
    var repo_link: String?,
    var starts: String?
)

interface ApiService {
    @GET("/repo")
    fun repos(
        @Field("lang")
        lang: String,
        @Field("since")
        since: String
    ): RepoList
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GET(val value: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Field(val value: String)


object KtHttpV1 {
    private val okHttpClient: OkHttpClient by lazy { OkHttpClient() }
    private val gson: Gson by lazy { Gson() }

    var baseUrl = "https://baseurl.com"
    inline fun <reified T> create(): T {
        return Proxy.newProxyInstance(
            T::class.java.classLoader,
            arrayOf(T::class.java)
        ) { proxy, method, args ->
            return@newProxyInstance method.annotations
                .filterIsInstance<GET>()
                .takeIf { it.size == 1 }
                ?.let { invoke("$baseUrl${it[0].value}", method, args) }
        } as T
    }

    fun invoke(path: String, method: Method, args: Array<Any>): Any? =
        method.parameterAnnotations.takeIf {
            method.parameterAnnotations.size == args.size
        }
            ?.mapIndexed { index, annotations -> Pair(annotations, args[index]) }
            ?.fold(path, ::parseUrl)
            ?.let { Request.Builder().url(it).build() }
            ?.let { okHttpClient.newCall(it).execute().body()?.string() }
            ?.let { gson.fromJson(it, method.genericReturnType) }

    private fun parseUrl(s: String, pair: Pair<Array<Annotation>, Any>): String =
        pair.first.filterIsInstance<Field>()
                  .first()
                  .let { field ->
                if (s.contains("?"))
                    "$s${field.value}=${pair.second}"
                else
                    "$s?${field.value}=${pair.second}"
            }
}


interface GitHubService {
    @GET("/search")
    fun search(@Field("id") id: String): User
}






















