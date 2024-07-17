import coroutines.logX
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Kotlin 中的Channel
 * Channel 有两个关键字：send()发送管道数据，receive()接收管道数据。
 * ⚠️ 直接使用receive()会导致各种问题，对于管道数据的接收应该尽可能地使用for循环、consumeEach{}
 */
class KotlinCoroutineChannel {

}

//fun main() = runBlocking {
//    val channel= Channel<Int> (capacity = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)
//    launch {
//        (1..3).forEach {
//            channel.send(it)
//            logX("Send: $it")
//        }
//        channel.close()
//    }
//    launch {
//        for (i in channel){
//            logX("Receive : $i")
//        }
//    }
//    logX("end")
//}
/**
 * produce{}高阶函数
 * produce{}会自动调用close()函数
 */
//fun main() = runBlocking {
//    val channel:ReceiveChannel<Int> = produce {
//        (1..3).forEach {
//            send(it)
//            logX("Send : $it")
//        }
//    }
//    launch {
//        for (i in channel){
//            logX("Receive : $i")
//        }
//    }
//    logX("end ")
//}

//fun main() = runBlocking {
//    val channel:ReceiveChannel<Int> = produce {
//        (1..3).forEach {
//            send(it)
//        }
//    }
//    channel.receive()
//    channel.receive()
//    channel.receive()
////    channel.receive()
//    logX("end")
//}

/**
 * 发送方可以使用"isClosedForSend"来判断当前的Channel是否关闭。
 * 接收方可以使用"isClosedForReceive"来判断当前的Channel是否关闭。
 */
//@OptIn(ExperimentalCoroutinesApi::class)
//fun main() = runBlocking {
//    val channel:ReceiveChannel<Int> = produce {
//        (1..3).forEach {
//            send(it)
//           println("Send $it")
//        }
//    }
//    while (!channel.isClosedForReceive){
//        val i=channel.receive()
//        println("Receive $i")
//    }
//    println("end")
//}

//fun main()= runBlocking {
//    val channel:ReceiveChannel<Int> = produce(capacity = 3) {
//        (1..300).forEach {
//            send(it)
//            println("Send : $it")
//        }
//    }
//    while (!channel.isClosedForReceive){
//        println("Receive : ${channel.receive()}")
//    }
//    logX("end")
//}

/**
 * channel.consumeEach{}
 */
//fun main() = runBlocking {
//    val channel:ReceiveChannel<Int> = produce (capacity = 3){
//        (1..400).forEach {
//            channel.send(it)
//            println("Send $it")
//        }
//    }
//    channel.consumeEach {
//        println("Recvice $it")
//    }
//    logX("end")
//}

/**
 * Channel 是"热"的,不管有没有接收方，发送方都会工作
 */

class ChannelModel{
    val channel:ReceiveChannel<Int> by ::_channel
   private val _channel:Channel<Int> = Channel()
    suspend fun init(){
        (1..3).forEach {
            _channel.send(it)
        }
    }
}
fun main() = runBlocking {
    val model=ChannelModel()
    launch {
        model.init()
    }
    model.channel.consumeEach {
        println("$it")
    }
}








