package coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class KotlinCoroutine {

}

//fun main() {
//    println("main : ${Thread.currentThread().name}")
//    thread {
//        println("thread : ${Thread.currentThread().name}")
//        Thread.sleep(100)
//    }
//    Thread.sleep(1000L)


//    repeat(1000_000_000){
//        thread {
//            Thread.sleep(1000000)
//        }
//    }
//    Thread.sleep(10000L)


//}


//fun main()= runBlocking {
////    println("main : ${Thread.currentThread().name}")
////    launch {
////        println("launch : ${Thread.currentThread().name}")
////        delay(100L)
////    }
////    Thread.sleep(1000L)
//
////    repeat(1000_000_000){
////        launch {
////            delay(1000000)
////        }
////    }
////    delay(10000L)
//
//    repeat(3){
//        Thread.sleep(1000L)
//        println("Print-1:${Thread.currentThread().name}")
//    }
//    repeat(3){
//        Thread.sleep(900L)
//        println("Print-2:${Thread.currentThread().name}")
//    }
//}

/**
 * 线程的sleep之所以是阻塞式的，是因为它会阻挡后续Task的执行。
 * 而协程是非阻塞式的，是因为它是可以支持挂起和恢复。
 */
fun main() = runBlocking {
    launch {
        repeat(3) {
            delay(1000L)
            println("Print-1:${Thread.currentThread().name}")
        }
    }

    launch {
        repeat(3){
            delay(900L)
            println("Print-2:${Thread.currentThread().name}")
        }
    }
    delay(3000L)
}

//fun main() = runBlocking(Dispatchers.IO) {
//    repeat(3){
//        launch { repeat(3){
//            println(Thread.currentThread().name)
//            delay(100)
//        } }
//    }
//    delay(5000L)
//}