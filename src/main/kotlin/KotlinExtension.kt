/**
 * Kotlin的扩展
 * 1、扩展属性
 * 2、扩展函数
 *
 * 由于JVM不理解Kotlin的扩展语法，所以Kotlin编译器会将扩展函数转换成对应的静态方法，
 * 而扩展函数调用处的代码会被转换成静态方法的调用。
 *
 * 在Kotlin中，几乎所有的类都可以被扩展，包括普通类，单例类，密封类，枚举类，伴生对象，第三方提供的Java类。
 * 匿名类内部类，不可以扩展。
 * Kotlin的扩展主要用途是用来取代Java当中的各种工具类。
 *
 *
 */

/**
 * 这个函数是直接定义在Kotlin文件里的，而不是定义在某个类当中的。这种扩展函数叫做顶层函数
 * "fun" 在Kotlin中所有的函数都需要fun关键字
 * "String."代表我们的扩展函数是String这个类定义的。名字叫做接收者，是扩展函数的接收方。
 * "lastElement" 是定义的扩展函数名称
 * "Char?" 是扩展函数的返回值
 * "this."是"具体的XX对象"
 */
fun String.lastElement(): Char? {
    if (this.isEmpty())
        return null
    return this[length - 1]
}
//fun String?.lastElement():Char?{
//    if (this?.isEmpty() == true)
//        return null
//    return this?.get(length - 1)
//}
/**
 * 扩展属性
 */
val String.lastElement: Char?
    get() = if (isEmpty())
        null
    else get(length - 1)

/**
 * Kotlin扩展不能做的：
 * 1、Kotlin扩展不是真正的成员，它无法被它的子类重写
 * 2、扩展属性无法存储状态
 * 3、扩展的作用域有限，无法访问私有成员。
 */
open class Person3 {
    var name: String = ""
    var age: Int = 0
   open fun setEat() {}
}

val Person3.isAdult: Boolean get() = age >= 18
fun Person3.walk() {
    println("walk")
}

class Tom() : Person3() {
    override fun setEat() {
        super.setEat()
    }
}


fun main() {
    val msg = "Hello world"
    val last = msg.lastElement()
    val last2 = msg.lastElement
    println("last : $last ; $last2")
    msg.trim()
    String
}