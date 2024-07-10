import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 委托应用
 * 1、委托类
 * 2、委托属性
 * 3、Kotlin中的标准委托
 *（两个属性之间的直接委托，
 * by lazy 懒加载委托，
 * Delegates.observable观察者委托，
 * by map 映射委托）                           ·····
 */
interface DB {
    fun save()
}

class SqlDB() : DB {
    override fun save() {
        println("save to sql")
    }
}

class GreenDaoDB() : DB {
    override fun save() {
        println("save to greenDaoDB")

    }

}

//db参数 通过 by 将接口实现委托给db
class UniversalDB(db: DB) : DB by db

/**
 * 委托属性(直接委托）
 */
class Item {
    var count: Int = 0
    var total: Int by ::count
}

val data: String by lazy {
    request()
}

fun request(): String {
    println("网络请求！！")
    return "网络数据"
}

/**
 * 自定义委托
 */
class StringDelegate(private var s: String = "Hello") {
    operator fun getValue(thisRef: Owner, property: KProperty<*>): String {
        return s
    }

    operator fun setValue(thisRef: Owner, property: KProperty<*>, value: String) {
        s = value
    }
}

class Owner {
    var text: String by StringDelegate()
}

/**
 *  继承ReadWriteProperty自定义委托
 */
class StringDelegate2(private var s: String = "Hello") : ReadWriteProperty<Owner2, String> {
    override fun getValue(thisRef: Owner2, property: KProperty<*>): String {
        return s
    }

    override fun setValue(thisRef: Owner2, property: KProperty<*>, value: String) {
        s = value
    }
}

class SmartDelegate {
    operator fun provideDelegate(thisRef: Owner2, property: KProperty<*>): ReadWriteProperty<Owner2, String> {
        return if (property.name.contains("log")) {
            StringDelegate2("log")
        } else {
            StringDelegate2("normal")
        }
    }
}

class Owner2 {
    var normalText: String by SmartDelegate()
    var logText: String by SmartDelegate()
}

class Model {
    /**
     * 提供内部修改
     * MutableList是可变集合
     */
    private val _data: MutableList<String> = mutableListOf()

    /**
     * 提供外部调用
     * Kotlin中的List是不能修改的，没有add和remove方法。
     */
    val data: List<String> by ::_data //委托 
    private fun load() {
        _data.add("Hello")
    }
}


fun main() {
//    UniversalDB(SqlDB()).save()
//    UniversalDB(GreenDaoDB()).save()

//    println("开始")
//    println(data)
//    println(data)

    val owner = Owner2()
    println(owner.normalText)
    println(owner.logText)

    val model = Model()
}