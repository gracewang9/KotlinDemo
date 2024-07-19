import coroutines.logX
import coroutines.mySingleDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.Executors

/**
 * flow{} 是一个高阶函数，创建一个新的Flow在它的Lambda中可以使用emit()往下游发送数据。
 * filter{},map{},take{}它们是中间操作符。
 * collect{} 终止操作符或者末端操作符。作用是终止Flow数据流并且接收这些数据。
 */

//fun main() = runBlocking {
//    flow {
//        emit(1)
//        emit(2)
//        emit(3)
//        emit(4)
//    }.filter { it>2 }
//        .map { it * 2 }
//        .take(2)
//        .collect{
//            println(it)
//        }
//
//}
/**
 * flowOf()
 * Flow可以当作集合来用，List可以当作Flow来用
 *
 * 创建Flow方式
 * 1、flow{} 未知数据
 * 2、flowOf() 已知数据
 * 3、asFlow() 已知数据来源集合
 */
//fun main() = runBlocking {
//    flowOf(1, 2, 3, 4, 5)
////        .toList()
//        .filter { it > 2 }
//        .map { it * 2 }
//        .take(2)
//        .collect {
//            println("flowOf : $it")
//        }
//    listOf(1, 2, 3, 4, 5)
////        .asFlow()
//        .filter { it > 2 }
//        .map { it * 2 }
//        .take(2)
//        .forEach {
//            println("listOf $it")
//        }
//}

/**
 * 中间操作符
 */
//fun main() = runBlocking {
//    flowOf(1, 2, 3, 4, 5)
//        .filter {
//            println("filter : $it")
//            it > 2
//        }
//        .map {
//            println("map : $it")
//            it * 2
//        }
//        .take(2)
//        .onStart {
//            println("onStart")
//        }
//        .onCompletion {
//            println("onCompletion ${this.emit(10)}")
//        }
//        .collect {
//            println("collect :$it")
//        }
//}


/**
 * Flow 中的异常处理主要有两种：
 *  1、使用catch操作符，主要用于上游异常的捕获。
 *  2、try-catch更多的是用于下游异常的捕获。
 */
//fun main() = runBlocking {
//    val flow = flow<Int> {
//        emit(1)
//        emit(2)
//        emit(3)
//        throw IllegalStateException()
//        emit(4)
//        emit(5)
//    }
//    flow.map { it * 2 }
//        .catch { println("catch : $it") }
//        .collect{
//            println("$it")
//        }
//}
/**
 * flowOn 切换线程
 * flowOn仅限于它的上游
 * launchIn
 * withContext
 *
 */
//fun main() = runBlocking {
//    val scope= CoroutineScope(mySingleDispatcher)
//    val flow = flow {
//        logX("Start")
//        emit(1)
//        logX("Emit:1")
//        emit(2)
//        logX("Emit:2")
//        emit(3)
//        logX("Emit:3")
//
//    }
//    flow.filter {
//        logX("Filter :$it")
//        it > 2
//    }.flowOn(Dispatchers.IO)
//        .filter {
//            logX("Filter: $it")
//            it > 2
//        }
//        .onEach {
//            logX("Collect $it")
//        }.launchIn(scope)
//    logX("end")
//}


/**
 * 下游 - 终止操作符
 * collect()
 * single()
 * fold{}
 * reduce{}
 *
 * Channel :不管有没有接收方，发送方都会工作。
 * Flow :只有调用终止操作符之后，Flow才会开始工作。
 */
//fun main() = runBlocking {
//    flow {
//        println("emit :3")
//        emit(3)
//        println("emit :4")
//        emit(4)
//        println("emit :5")
//        emit(5)
//        println("emit :6")
//        emit(6)
//    }.filter {
//        println("filter : $it")
//        it > 2
//    }.map {
//        println("map:$it")
//        it * 2
//    }.collect {
//        println("collect : $it")
//    }
//}

//fun main() = runBlocking {
//    fun loadData() = flow<Int> {
//        repeat(3) {
//            delay(100L)
//            emit(it)
//            logX("emit $it")
//        }
//    }
//
//    val uiScope = CoroutineScope(mySingleDispatcher)
//    loadData()
//        .map { it * 2 }
//        .flowOn(Dispatchers.IO)
//        .onEach {
//            logX("onEach $it")
//        }.launchIn(uiScope)
//    delay(1000L)
//}

//fun main() = runBlocking {
//    fun loadData() = flow<Int> {
//        repeat(3) {
//            delay(100L)
//            emit(it)
//            logX("emit : $it")
//        }
//    }
//
//    fun updateUI(it: Int) {
//        println("update UI $it")
//    }
//
//    fun showLoading() {
//        println("Show loading")
//    }
//
//    fun hideLoading() {
//        println("Hide Loading")
//    }
//
//    val uiScope = CoroutineScope(mySingleDispatcher)
//    loadData().onStart {
//        showLoading()
//    }.map { it * 2 }
//        .flowOn(Dispatchers.IO)
//        .catch { throwable ->
//            println(throwable)
//            hideLoading()
//            emit(-1)
//        }.onEach { updateUI(it) }
//        .onCompletion { hideLoading() }
//        .launchIn(uiScope)
//
//    delay(1000L)
//}


/**
 * 本地加载和网络加载
 */
//fun main() = runBlocking {
//    suspend fun getCacheInfo(id: String): Product? {
//        delay(100L)
//        return Product(id, 9.9)
//    }
//
//    suspend fun getNetworkInfo(id: String): Product? {
//        delay(200L)
//        return Product(id, 9.8)
//    }
//
//    fun updateUI(product: Product) {
//        println("${product.id} == ${product.price}")
//    }
//
//    val startTime = System.currentTimeMillis()
//    val productId = "xxxId"
//    val cacheIndo = getCacheInfo(productId)
//    if (cacheIndo != null) {
//        updateUI(cacheIndo)
//        println("Time cost : ${System.currentTimeMillis() - startTime}")
//    }
//    val latestInfo = getNetworkInfo(productId)
//    if (latestInfo != null) {
//        updateUI(latestInfo)
//        println("Time cost:${System.currentTimeMillis() - startTime}")
//    }
//
//}
/**
 * select和async
 * 灵活选择，两个挂起函数同时执行，谁返回的速度快就选择谁。
 */

//fun main() = runBlocking {
//
//    suspend fun getCacheInfo(id: String): Product? {
//        delay(100L)
//        return Product(id, 9.9)
//    }
//
//    suspend fun getNetworkInfo(id: String): Product? {
//        delay(200L)
//        return Product(id, 9.8)
//    }
//
//    fun updateUI(product: Product) {
//        println("${product.id} == ${product.price} ; ${product.isCache}")
//    }
//
//    val startTime = System.currentTimeMillis()
//    val productId = "23499434"
//    val cacheDeferred = async { getCacheInfo(productId) }
//    val latestDeferred = async { getNetworkInfo(productId) }
//
//
//    val product = select<Product?> {
//        cacheDeferred.onAwait { it?.copy(isCache = true) }
//        latestDeferred.onAwait { it?.copy(isCache = false) }
//    }
//
//    if (product != null) {
//        updateUI(product)
//        println("Time cost : ${System.currentTimeMillis() - startTime}")
//    }
//    if (product != null && product.isCache) {
//        val latest =latestDeferred.await()?:return@runBlocking
//        updateUI(latest)
//        println("Time cost :${System.currentTimeMillis() - startTime}")
//    }
//}
/**
 * select 配合channel
 */
//fun main() = runBlocking {
//    val startTime = System.currentTimeMillis()
//    val channel1 = produce {
//        send("1")
//        delay(200L)
//        send("2")
//        delay(200L)
//        send("3")
//        delay(150L)
//    }
//    val channel2 = produce {
//        delay(100L)
//        send("a")
//        delay(200L)
//        send("b")
//        delay(200L)
//        send("c")
//    }
//
//    suspend fun selectChannel(channel1: ReceiveChannel<String>, channel2: ReceiveChannel<String>): String =
//        select<String> {
//            channel1.onReceiveCatching {
//                it.getOrNull() ?: "channel1 is closed!"
//            }
//            channel2.onReceiveCatching { it.getOrNull() ?: "channel2 is closed!" }
//        }
//    repeat(10) {
//        println(selectChannel(channel1, channel2))
//    }
//    channel1.cancel()
//    channel2.cancel()
//    println("")
//}

data class Product(
    val id: String,
    val price: Double,
    val isCache: Boolean = false
)
/**
 * 单线程并发
 */
//fun main() = runBlocking {
//    val mySingleDispatcher = Executors.newSingleThreadExecutor {
//        Thread(it, "MySingleThread").apply { isDaemon = true }
//    }.asCoroutineDispatcher()
//    var i = 0
//    val jobs = mutableListOf<Job>()
//    repeat(10) {
//        val job = launch(mySingleDispatcher) {
//            repeat(10000) {
//                i++
//            }
//        }
//        jobs.add(job)
//    }
//    jobs.joinAll()
//    println("i = $i")
//}


/**
 *  Mutex 锁 支持挂起和恢复
 *  mutex.lock() 和 mutex.unlock()实现多线程同步。
 *
 */

//fun main() = runBlocking {
//    val mutex=Mutex()
//    var j=0
//    val jobs= mutableListOf<Job>()
//    repeat(10){
//        val job=launch(Dispatchers.Default) {
//            repeat(10000){
//                //应这样写
//                mutex.withLock {
//                    j++
//                }
////                这样写不安全
////                mutex.lock()
////                j++
////                mutex.unlock()
//            }
//        }
//        jobs.add(job)
//    }
//    jobs.joinAll()
//    println("j = $j")
//}


/**
 * 函数式思维
 */
fun main() = runBlocking {
    val result=(1..10).map {
        async (Dispatchers.Default){
            var i=0
            repeat(1000){
                i++
            }
            return@async i
        }
    }.awaitAll().sum()
    println("result = $result")
}
















