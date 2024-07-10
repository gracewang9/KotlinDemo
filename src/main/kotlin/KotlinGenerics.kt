/**
 * Kotlin 的泛型
 * 泛型作为参数的时候用in；
 * 泛型作为返回值的时候用out.
 */

class TV {
    open fun tunOn() {}
    open fun tunOff() {}
}

//声明处逆变
open class Controller<in T> {

}

class XiaoMiTV1 {
    //使用处逆变
    fun buy(controller: Controller<in XiaoMiTV1>) {

    }
}

open class Food {}
class KFC : Food() {}
class Restaurant<out T:Food>{
    fun orderFood():T?{
        println("来单啦")
        /*..*/
        return null
    }
}

fun orderFood(restaurant: Restaurant<Food>){
    val food=restaurant.orderFood()
    println(food)
}

fun findRestaurant(): Restaurant<*> {
    return Restaurant<KFC>()
}


/**
 *
 */
class Controller2<in T>{
    fun turnOn(tv:T){}
}
class Restaurant2<out T>{
//    fun orderFood():T{}
}
fun main() {
//val kfc=Restaurant<KFC>()
//   orderFood(kfc)

    val restaurant=findRestaurant()
    val food:Food?=restaurant.orderFood()
}

/**
 * obj::class 类引用
 */
fun readMembers(obj:Any){
//    obj::class.memberProperties.forEach{
//
//    }
}