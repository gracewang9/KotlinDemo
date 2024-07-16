package coroutines

import User
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.log
import kotlin.random.Random
import kotlin.system.measureTimeMillis

/**
 * 协程
 *
 * 协程启动方式：
 * launch:不会阻塞当前程序的执行流程，无法获取协程的执行结果。
 * runBlocking:会阻塞当前程序执行流程，可以获取协程的执行结果
 * async:它像是结合了launch和runBlocking两者的优点；
 * 不会阻塞当前的执行流程，可以直接获取协程的执行结果。
 */
//
//fun main() {
//    /**
//     * launch启动协程：协程被launch启动后执行的任务中途不能被改变。
//     * launch函数返回值是一个Job，是协程的句柄（Handle）不能返回协程的执行结果。
//     */
//
//    GlobalScope.launch {
//        println("Coroutine started : ${Thread.currentThread().name}")
//        delay(1000L)
//        println("Hello World")
//    }
//    println("After launch :${Thread.currentThread().name}")
////    Thread.sleep(2000L)
//}


suspend fun func3(num: Int): Double {
    delay(100L)
    return num.toDouble()
}

/**挂起函数suspend (Int) -> Double 参数类型Int，返回类型Double
 * ::func3 函数引用
 */
val f3: suspend (Int) -> Double = ::func3


suspend fun CoroutineScope.func4(num: Int): Double {
    delay(100L)
    return num.toDouble()
}

/**
 * suspend CoroutineScope.(Int) -> Double 是挂起函数同时还是CoroutineScope类的成员方法或是扩展方法
 * 参数类型是Int类型，返回值类型是Double
 */
val f4: suspend CoroutineScope.(Int) -> Double = CoroutineScope::func4


//fun main() {
////    runBlocking {
////        println("First : ${Thread.currentThread().name}")
////        delay(1000L)
////        println("Hello First!")
////    }
////    runBlocking {
////        println("Second : ${Thread.currentThread().name}")
////        delay(1000L)
////        println("Hello Second!")
////    }
//   val result= runBlocking1 {
//        println("Third : ${Thread.currentThread().name}")
//        delay(1000L)
//        println("Hello Third!")
//       return@runBlocking1 "Coroutine done"
//    }
////    println("After runBlocking!")
////    Thread.sleep(2000L)
//    println("Process end! $result")
//}


//public actual fun <T> runBlocking(context:CoroutineContext,
//block:suspend CoroutineScope.() -> T):T{...}


/**
 * async 启动协程
 * 不会阻塞当前线程，可以获取协程的执行结果
 */
//fun main() = runBlocking {
//    println("In runBlocking :${Thread.currentThread().name}")
//    val deferred: Deferred<String> = async {
//        println("In async : ${Thread.currentThread().name}")
//        delay(1000L)
//        return@async "Task completed!"
//    }
//    println("After async : ${Thread.currentThread().name}")
//    val result = deferred.await()
//    println("Result is : $result")
//}

//public fun CoroutineScope.launch(
//    context: CoroutineContext = EmptyCoroutineContext,
//    start: CoroutineStart = CoroutineStart.DEFAULT,
//    block: suspend CoroutineScope.() -> Unit//返回Unit类型
//): Job {//返回Job
//}
//
//public fun <T> CoroutineScope.async(
//    context: CoroutineContext = EmptyCoroutineContext,
//    start: CoroutineStart = CoroutineStart.DEFAULT,
//    block: suspend CoroutineScope.() -> T//返回值是泛型T
//): Deferred<T> {//返回Deferred
//
//}


//fun main() = runBlocking {
//    val deferred:Deferred<String> = async{
//        println("In async : ${Thread.currentThread().name}")
//        delay(1000L)
//        println("In async after delay !")
//        return@async "Task completed!"
//}
//    delay(2000L)
//}


/**
 * 协程中的挂起函数 suspend关键字
 */

suspend fun getUserInfo(): String {
    withContext(Dispatchers.IO) {
        delay(1000L)
    }
    return "BoyCoder"
}

suspend fun getFriendList(user: String): String {
    withContext(Dispatchers.IO) {
        delay(1000L)
    }
    return "Tom, Jack"
}

suspend fun getFeedList(list: String): String {
    withContext(Dispatchers.IO) {
        delay(1000L)
    }
    return "{FeedList..}"
}

/**
 * Job句柄：
 * 使用Job监测协程的生命周期状态；
 * 使用Job操控协程。
 *
 * invokeOnCompletion{}的作用是监听协程结束的事件。
 * job.join()是挂起函数，作用是挂起当前的程序执行流程，等待job当中的协程任务执行完毕，然后再恢复当前的程序执行流程。
 * Deferred:Deferred只比Job多了一个await()挂起函数
 * await()挂起函数
 */

//fun main() = runBlocking {
//    suspend fun download() {
//        val time = (Random.nextDouble() * 1000).toLong()
//        logX("Delay time:=$time")
//        delay(time)
//    }
//
//
//    val job = launch(start = CoroutineStart.LAZY) {
//        logX("Coroutine start!")
//        delay(1000L)
//        logX("Coroutine end!")
//    }
//    delay(500L)
//    job.log()
////    job.cancel()
//    job.start()
//    job.log()
//    job.invokeOnCompletion {
//        job.log()
//    }
//    job.join()
//    logX("Process end!")
//}

fun Job.log() {
    logX(
        """
        isActive = $isActive 
        isCancelled = $isCancelled 
        isCompleted = $isCompleted
    """.trimIndent()
    )
}

fun logX(any: Any?) {
    println(
        """
        ================================
        $any
        Thread:  ${Thread.currentThread().name}
        ================================
    """.trimIndent()
    )
}

//fun main() = runBlocking {
//    val parentJob:Job
//    var job1:Job?=null
//    var job2:Job?=null
//    var job3:Job?=null
//    parentJob=launch {
//        job1=launch {
//            logX("Job1 start!")
//            delay(1000L)
//            logX("Job1 done!")
//        }
//        job1=launch {
//            logX("Job2 start!")
//            delay(3000L)
//            logX("Job2 done!")
//        }
//        job1=launch {
//            logX("Job3 start!")
//            delay(5000L)
//            logX("Job3 done!")
//        }
//    }
//
//    delay(500L)
//    parentJob.children.forEachIndexed{index, job ->
//        when(index){
//            0 -> println("job1 === job is ${job1 === job}")
//            1 -> println("job2 === job is ${job2 === job}")
//            2 -> println("job3 === job is ${job3 === job}")
//        }
//    }
//    parentJob.cancel()
//    logX("Process end!")
//}


//fun main() = runBlocking {
//    suspend fun getResult():String{
//        delay(1000L)
//        return "Result1"
//    }
//    suspend fun getResult2():String{
//        delay(1000L)
//        return "Result2"
//    }
//    suspend fun getResult3():String{
//        delay(1000L)
//        return "Result3"
//    }
//    val results:List<String>
//    val time= measureTimeMillis {
//        val result1=async { getResult() }
//        val result2=async { getResult2() }
//        val result3=async { getResult3() }
//        results= listOf(result1.await(),result2.await(),result3.await())
//    }
//    println("Time:$time")
//    println(results)
//}

//fun main() = runBlocking {
//    val job = launch {
//        logX("First coroutine start!")
//        delay(10000L)
//        logX("First coroutine end!")
//    }
//    job.join()
//    val job2=launch {
//        logX("Second coroutine start!")
//        delay(1000L)
//        logX("Second coroutine end!")
//    }
//    job2.join()
//    logX("Process end!")
//}


/**
 * 协程的CoroutineContext 上下文，用来切换线程池。
 * Dispatchers.Main 在UI平台才有意义
 * Dispatchers.Unconfined 当前协程可能运行在任意线程之上
 * Dispatchers.Default 用于CPU密集型任务的线程池，最小限制2
 * Dispatchers.IO 用于IO密集型任务的线程池。它内部的线程数量一般会更多些。
 * 具体线程的数量可以通过参数来配置：kotlinx.coroutine.io.parallelism
 */

//fun main() = runBlocking(Dispatchers.IO) {
//    val user= getUserInfo()
//    logX(user)
//}
/**
 * 自定义协程
 */

val mySingleDispatcher = Executors.newSingleThreadExecutor {
    Thread(it, "MySingleThread").apply { isDaemon = true }
}.asCoroutineDispatcher()

//fun main() = runBlocking (mySingleDispatcher){
//    val user= getUserInfo()
//    logX(user)
//}
//fun ExecutorService.asCoroutineDispatcher():ExecutorCoroutineDispatcher=ExecutorCoroutineDispatcherI


/**
 * CoroutineScope最大的作用
 */

//fun main() = runBlocking {
//    val scope = CoroutineScope(Job())
//    scope.launch {
//        logX("First start!")
//        delay(1000L)
//        logX("First end!")
//    }
//    scope.launch {
//        logX("Second start!")
//        delay(1000L)
//        logX("Second end!")
//    }
//    scope.launch {
//        logX("Third start!")
//        delay(1000L)
//        logX("Third end!")
//    }
//
//    delay(500L)
//    scope.cancel()
//    delay(1000L)
//}

//@OptIn(ExperimentalStdlibApi::class)
//fun main() = runBlocking {
//    val scope = CoroutineScope(Job() + mySingleDispatcher)
//    scope.launch {
//        logX(coroutineContext[CoroutineDispatcher] = mySingleDispatcher)
//    delay(1000L)
//        logX("First end!")
//    }
//    delay(500L)
//    scope.cancel()
//    delay(10000L)
//}


/**
 * 关键字operator 表示支持操作符重载
 */





































