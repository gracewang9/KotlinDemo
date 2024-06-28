package kotlin

class KotlinClass {

    fun main() {
        /**
         * 声明变量
         */
        val num = 0
        var str = "string"

        //空安全
        val dub: String?
        val d: String? = null

        var price: Int = 100

        price = 200
        val num2 = 0
//        num2 = 2 编译报错
        /**
         * val 声明的变量是不可变变量，初始化后就不能修改；类似java中的final修饰的变量。
         * var 声明的变量是可变变量；类似java中的普通变量。
         */
        //Kotlin的类型转换
        val dou: Double = price.toDouble()

        //Kotlin 中的空安全
//        val nu:String =null 这样编译器报错
        var nu: String? = null//编译通过
        nu = dou.toString()

        //Kotlin中不存在隐式转换；如：
        val l = 100
//        val m:Long=l 这样编译报错
        val m: Long = l.toLong()//编译通过

        /**
         * 布尔类型
         * & 与运算
         * | 或运算
         * ！非运算
         * && 并且，||或者
         */

        val ii = 1
        val jj = 2
        val kk = 3
        val iTrue: Boolean = ii > jj && jj < kk

        /**
         * Char类型
         */
        val char: Char = 'A'
        val c: Int = char.toInt()

        /**
         * String 类型
         */
        val string: String = "Hello World"
        //Kotlin提供了非常友好的字符串模版
        print("Hello $string")
        print(
            """
            今天是星期五，
            明天就周末啦！
        """.trimIndent()
        )

        /**
         * 数组
         * Kotlin中使用arrayOf()来创建数组
         * Kotlin中获取数组长度和List获取的长度一样都是用size
         */
        val arrayInt = arrayOf(1, 1, 2, 3, 5)
        val arrayString = arrayOf("apple", "pear", "banner")
        print("arrayInt size :${arrayInt.size}")
        /**
         * 函数声明关键字fun
         *
         */
//        fun function(name:String):String{
//            return "hello $name"
//        }
        //以上代码等价以下代码
//        fun function(name:String):String="hello $name"//这种写法称为单一表达式函数
        fun function(name: String) = "hello $name"//其实我们也可以把返回类型也去掉

        //函数调用
        function("hello")
        //Kotlin新提供的命名参数
        function(name = "hello")

        fun userInfo(name: String, age: Int = 20, gender: Int = 1, height: Int = 1, likeCount: Long = 0) {

        }
        userInfo(name = "grace", 20, 1, 50, 1)
        userInfo(name = "grace", age = 20, gender = 1, height = 50, likeCount = 1)
        userInfo(name = "grace")//有默认值的参数我们可以不传
        //在java中要实现这样的功能，要实现多个userInfo函数，或者使用Builder设计模式
        /**
         * 控制流程：if ,when, for,while
         */
        //if语句 在程序中主要是用于逻辑判断
        val i = 1
        if (i > 0) {
            print("Big")
        } else {
            print("Small")
        }
        //还可以这样来用
        val message = if (i > 0) "Big" else "Small"
        val text: String? = "string"
        val size = text?.length ?: 0//Elvis表达式

        /**
         * when 语句 当分支大于两个时就可以使用when语句
         *
         */
        val number = 10
        when (number) {
            1 -> print("")
            2 -> print("")
            3 -> print("")
        }
        when (number) {
            1 -> print("")
            2 -> print("")
            else -> print("")
        }
        //Kotlin 的when 语句可以作为表达式为变量赋值；但是作为赋值时else不可以省略
        val messager = when (number) {
            1 -> 2
            2 -> 3
            else -> 4
        }

        /**
         * 循环语句：while 和for
         */
        //Kotlin中的while语句和Java中的while语句使用没什么区别
        var count = 0
        while (count < 100) {
            print("count $count ")
            count++
        }
        var count2 = 0
        do {
            print("count2 $count2")
            count2++
        } while (count2 <= 10)
        /**
         * for 语句
         */
        val array= arrayOf(1,2,3)
        for (i in array){
            println(i)
        }
        //使用..表示区间 如：1..10
        val oo=1..10
        for (i in oo){
            print(i)
        }
        //逆序迭代
        for (i in 10 downTo 0 step 2){
            println("逆序迭代 $i")
        }
        /**
         * 集合使用
         */
        val arrayList:ArrayList<String> = arrayListOf()
        val list:MutableList<String> = mutableListOf()
        arrayList.add("a")
        list.add("b")
        arrayList.addAll(list)
    }

}