/**
 * Kotlin中的Object关键字的使用方式：
 * 1、匿名内部类
 * 2、伴生对象
 * 3、单例模式
 */
class KotlinObject {

}

interface Walks {
    fun toWalk()
}

interface Run {
    fun toRun()
}

class Cat() {

}

abstract class Dog() {
    abstract fun eat()
}


fun main() {
    //Kotlin中匿名内部类使用object关键字定义，继承抽象类的同时还可以继承多个接口。
    //如下：
    val mObject = object : Dog(), Walks, Run {
        override fun eat() {
            TODO("Not yet implemented")
        }

        override fun toWalk() {
            TODO("Not yet implemented")
        }

        override fun toRun() {
            TODO("Not yet implemented")
        }

    }
    Activity.InnerActivity.onCreate()
    Activity2.onCreate()
    UserManager2.getInstance("Grace")
}

/**
 * Object 单例类
 * 缺点：不支持懒加载
 *      不支持传参构造函数
 */
object UserManager {
    fun login() {}
}

/**
 * Object 伴生对象
 *在伴生对象的内部，如果存在"@JvaStatic"修饰的方法或属性，会被挪到伴生对象的类当中，变成静态成员
 */
class Activity() {
    object InnerActivity {
        //加上@JvmStatic关键字，能实现类似Java中的静态方法。调用的时候一样
        @JvmStatic
        fun onCreate() {
        }
    }
}

/**
 * companion object 在Kotlin中被称作伴生对象
 */
class Activity2() {
    companion object InnerActivity {
        fun onCreate() {}
    }
}


/**
 * 利用伴生对象实现工厂模式
 */
class User private constructor(val name: String) {
    companion object {
        @JvmStatic
        fun create(name: String): User? {
            return User(name)
        }
    }
}

/**
 * 懒加载委托
 */
object UserManager1 {
    //user变量变成类懒加载，只要user变量没有被使用，它就不会触发loadUser()的逻辑
    val user by lazy { loadUser() }
    private fun loadUser(): User? {
        return User.create("tom")
    }

    fun login() {}
}

/**
 * 伴生对象 Double Check
 */
class UserManager2 private constructor(name: String) {
    companion object {
        @Volatile
        private var INSTANCE: UserManager2? = null
        fun getInstance(name: String): UserManager2 = INSTANCE ?: synchronized(this) {
            INSTANCE ?: UserManager2(name).also { INSTANCE = it }
        }
    }
}

class PersonManager private constructor(name: String) {
    companion object {
        @Volatile
        private var INSTANCE: PersonManager? = null
        fun getInstance(name: String) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: PersonManager(name).also { INSTANCE = it }
        }
    }
}

/**
 * 抽象模版
 *
 * 伴生对象本质上还是嵌套类，它仍然是一个类，那么它就具备类的特性"继承其他的类"。
 * 那么我们就可以让伴生对象继承BaseSingleton这个抽象类
 */
abstract class BaseSingleton<in P, out T> {
    @Volatile
    private var INSTANCE: T? = null
    protected abstract fun creator(param: P): T
    fun getInstance(param: P) = INSTANCE ?: synchronized(this) {
        INSTANCE ?: creator(param).also { INSTANCE = it }
    }
}

class PersonManager1 private constructor(name: String) {
    companion object : BaseSingleton<String, PersonManager1>() {
        override fun creator(param: String): PersonManager1 = PersonManager1(param)

    }
}

class UserManager3 private constructor(name: String) {
    companion object : BaseSingleton<String, UserManager3>() {
        override fun creator(param: String): UserManager3 = UserManager3(param)

    }
}

/**
 * 接口模版
 * 缺点：1、instance无法使用private修饰（不符合单例的规范）
 *      2、instance无法使用@Volatile修饰（会引发出多线程同步问题）
 */

interface ISingleton<P, T> {
    var instance: T?
    fun creator(param: P): T
    fun getInstance(param: P): T = instance ?: synchronized(this) {
        instance ?: creator(param).also { instance = it }
    }
}












