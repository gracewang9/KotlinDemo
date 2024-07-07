import javax.swing.text.View

/**
 * Kotlin中高阶函数
 * 高阶函数是将函数用作参数或返回值的函数。
 * 一个函数的参数或是返回值，它们当中有一个是函数的情况下，这个函数就是高阶函数
 */
class KotlinFunction {
    //高阶函数
    fun setOnClickListener(l: (View) -> Unit) {
        //...
    }

}

/**
 * 函数类型：函数类型就是函数的类型
 * 将函数的"参数类型和返回值类型"抽象出来后，就得到类"函数类型"
 */

//(Int,Int)->Float这个就是add()函数的类型
fun add(a: Int, b: Int): Float {
    return (a + b).toFloat()
}

//  函数赋值给变量                函数引用
val function: (Int, Int) -> Float = ::add

//                        函数作为参数的高阶函数
fun setOnClickListener(l: (View) -> Unit) {}

/**
 * Lambda 表达式引发的 8 种写法
 */
fun onClick(v: View): Unit {}
//1）写法
//view.setOnClickListener(object:View.OnClickListener{
// override fun onClick(v:View?){
// ...
// }
// })

//2）写法
//view.setOnClickListener(View.OnClickListener{v:View? ->
// ...
// })

//3）写法
//view.setOnClickListener({v:View?->
// ...
// })

//4）写法
//view.setOnClickListener({v->
// ...
// })

//5）写法
//view.setOnClickListener({it->
// ...
// })

//6）写法
//view.setOnClickListener({
// getView(it)
// })

//7）写法
//view.setOnClickListener(){v->
//  getView(it)
// }

//8）写法
//view.setOnClickListener{v->
//  getView(it)
// }

/**
 * SAM转换
 */
fun setOnClickListener2(l: (View) -> Unit) {}
//fun setOnClickListener2(l: ((View) -> Unit)?) {}


fun User.applys(self: User, block: (self: User) -> Unit): User {

    return this
}

/**
 * creator函数修改成高阶属性
 */
abstract class BaseSingleton2<in P, out T> {
    @Volatile
    private var INSTANCE: T? = null

    protected abstract val creator: (P) -> T

    fun getInstance(param: P) = INSTANCE ?: synchronized(this) {
        INSTANCE ?: creator(param).also { INSTANCE = it }
    }
}
class PersonManager2 private constructor(private val name:String){
    companion object:BaseSingleton2<String,PersonManager2>(){
        override val creator = ::PersonManager2
    }
}


fun main() {
    setOnClickListener(::onClick)
    setOnClickListener { }
    val user = User.create("Job")
    user?.applys(self = user) { self: User ->
        self.name
    }

}
















