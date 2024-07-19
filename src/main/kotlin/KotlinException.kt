import kotlinx.coroutines.*
import java.util.concurrent.Executors

/**
 * Kotlin协程异常处理原则：
 * 1、不要用try-catch 直接包裹launch,async。
 * 2、灵活使用SupervisorJob,控制异常传播的范围。
 * 3、使用CoroutineExceptionHandler处理复杂结构的协程异常，它仅在顶层协程中起作用。
 * 4、协程的取消需要内部的配合。
 * 5、不要轻易打破协程的父子结构。
 * 6、捕获CancellationException后，要考虑是否应该重新抛出来。
 *
 */

//fun main() = runBlocking {
//    val job=launch (Dispatchers.Default){
//        var i=0
//        while (true){
//            Thread.sleep(500L)
//            i++
//            println("i = $i")
//        }
//    }
//    delay(2000L)
//    job.cancel()
//    job.join()
//    println("End")
//}
/**
 * 协程的取消需要内部的配合
 */
//fun main() = runBlocking {
//    val job=launch (Dispatchers.Default){
//        var i=0
//        while (isActive){//只有协程内部活跃的状态
//            Thread.sleep(500L)
//            i++
//            println("i = $i")
//        }
//    }
//    delay(2000L)
//    job.cancel()
//    job.join()
//    println("end")
//}

/**
 *协程的父子结构
 */
val fixedDispatcher=Executors.newFixedThreadPool(2){Thread(it,"MyFixedThread").apply {
    isDaemon=false
}}.asCoroutineDispatcher()

//fun main() = runBlocking {
//    val parentJob=launch(fixedDispatcher) {
//        launch {
//            var i=0
//            while (isActive){
//                Thread.sleep(500L)
//                i++
//                println("First i =$i")
//            }
//        }
//        launch {
//            var i=0
//            while (isActive){
//                Thread.sleep(500L)
//                i++
//                println("Second i =$i")
//            }
//        }
//
//    }
//
//    delay(2000L)
//    parentJob.cancel()
//    parentJob.join()
//    println("End")
//}

//fun main()= runBlocking {
//    val parentJob=launch (Dispatchers.Default){
//        launch {
//           var i=0
//            while (true){
//                try {
//                    delay(500L)
//                }catch (e:CancellationException){
//                    println("Catch CancellationException")
//                    throw e
//                }
//                i++
//                println("first i=$i")
//            }
//        }
//        launch {
//            var i=0
//            delay(500L)
//            i++
//            println("Second i=$i")
//        }
//    }
//    delay(2000L)
//    parentJob.cancel()
//    parentJob.join()
//    println("end")
//}
/**
 * SupervisorJob() 使用
 */
//fun main() = runBlocking {
//    val scope= CoroutineScope(SupervisorJob())
//    val deferred=scope.async {
//        delay(100L)
//        1/0
//    }
//    try {
//        deferred.await()
//    }catch (e:java.lang.ArithmeticException){
//        println("Catch : $e")
//    }
//    delay(500L)
//    println("End")
//}

/**
 * CoroutineExceptionHandler使用
 * 使用CoroutineExceptionHandler处理复杂结构的协程异常，它仅在顶层协程中起作用。
 */
fun main() = runBlocking {
    val myExceptionHandler= CoroutineExceptionHandler{_,throwable->
        println("Catch exception : $throwable")
    }

    val scope= CoroutineScope(coroutineContext+Job() +myExceptionHandler)
    scope.launch {
        async {
            delay(100L)
        }
        launch {
            delay(100L)
            launch {
                delay(100L)
                1/0
            }
        }
        delay(1000L)
    }
    delay(10000L)
    println("End")
}
















