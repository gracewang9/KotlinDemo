/**
 * 普通类
 */
//class Person(val name: String) {
//    //    val isAdult get()= age >= 18
//    var age: Int = 0
//        set(value) {
//            if (value < 18) {
//                println("$value")
//            } else {
//                field = value
//            }
//        }
//}
/**
 * 抽象类
 */
abstract class Person(val name: String, val age: Int) {
    abstract fun walk()
    val isAdult=age>18
    val isYouth get()=age>18
}

/**
 * 接口，接口名后面不能有括号
 */
interface Walk {
    fun walk()
}

class Bob : Walk {
    override fun walk() {
        TODO("Not yet implemented")
    }
}

/**
 * 普通类继承普通类需要加上open关键字 ，想要重写函数、属性也要修饰open关键字。
 * 在Kotlin中默认是不允许继承；被open修饰的普通类，它内部的方法和属性默认也是不允许被重写。
 */
open class Fruits() {
    open fun eat() {}
    open val color: Int = 0
}

class Apple() : Fruits() {
    val colors: Int = color
    override val color: Int
        get() = 2

    override fun eat() {
        super.eat()
    }
}

/**
 * 接口 ，实现
 * Kotlin中的接口、接口实现和Java中的接口、接口实现的语法是一样的。
 * 区别：Kotlin中的接口可以有属性，方法中可以有默认实现代码。
 */
interface FruitsStyle {
    fun color() {
        println("红色")
    }
    //不能设置初始值，val 只能重写get,var可以重写get,set方法
    val name: String
}

class Banana() : FruitsStyle {
    override fun color() {
        TODO("Not yet implemented")
    }

    override val name: String
        get() = "香蕉"

}

/**
 * 嵌套
 * Kotlin 中的普通嵌套类，它的本质是静态的。如果想在内部类中访问普通类的成员，需要在内部类前面加上inner 关键字
 * inner关键字代表B class是A class的内部的类。
 */
class A {
    val name: String = ""
    fun foo() {

    }

    //   ↓
    inner class B {
        val aName = name
        fun aFoo() {
            foo()
        }
    }
}

/**
 * 数据类,就是在类前面加data关键字
 * 在Kotlin中，编译器会为数据类自动生成一些有用的方法。
 * 它们是：
 * equals()
 * hashCode()
 * toString()
 * componentN()函数
 * copy()
 */
data class Pear(val name: String, val color: String, val size: Int)

/**
 * 密封类
 * 密封类用来表示某种受到限制的继承结构（更强大的枚举类）
 * （枚举就是一组有限的数量的值）
 */
sealed class Result<out R> {
    data class Success<out T>(val data: T, val message: String = "") : Result<T>()
    data class Error(val exception: java.lang.Exception) : Result<Nothing>()
    data class Loading(val time: Long = System.currentTimeMillis()) : Result<Nothing>()
}
fun display(data: Result<*>) =when(data){
    is Result.Success -> ""
    is Result.Error ->""
    is Result.Loading ->""
}

fun main() {
    val xueli = Pear("雪梨", "黄色", 1)
    val qingli = Pear("青梨", "绿色", 2)
    println("equals : ${xueli.equals(qingli)}")
    println("hashCode : ${xueli.hashCode()}")
    println("toString : ${xueli.toString()}")

    val (name, color, size) = xueli//数据类的解构声明，快速通过数据类来创建一连chuann
    println("toString : $name ; $color ; $size")
    val gaoshanxueli = xueli.copy(name = "高山雪梨")
    println(gaoshanxueli)


    val a:Long=1L
    val b:Long?=2L

    var c:Long=3L
    var d:Long?=null
    d=6L
    val e:Long?=null

}
