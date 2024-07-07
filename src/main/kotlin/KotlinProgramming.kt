
/**
 * Kotlin编程式
 * 在Kotlin中函数是一等公民
 * 1、函数可以独立于类之外 ->Kotlin的顶层函数
 * 2、函数可以作为参数和返回值 ->Kotlin中高阶函数和Lambda
 * 3、函数可以像变量一样 ->Kotlin的函数引用
 */
class KotlinProgramming {
    /**
     * 命令式开发
     */

    fun foo(): List<Int> {
        val list = listOf(1, 2, 3)
        val result = mutableListOf<Int>()
        for (i in list) {
            if (i % 2 == 0) {
                result.add(i)
            }
        }
        return result
    }

    /**
     * 函数式开发 / 声明式
     */
    fun fp() = listOf(1, 2, 3).filter { it % 2 == 0 }

    /**
     * 函数式的循环
     * 命令式
     */
    fun loop(): Int {
        var result = 0
        for (i in 1..10) {
            result += i
        }
        return result
    }

    /**
     * 递归
     */
    fun recursionLoop(): Int {
        tailrec fun go(i: Int, sum: Int): Int =
            if (i > 10) sum else go(i, sum)
        return go(1, 0)
    }

    fun reduced() = (1..10).reduce { acc, i -> acc + i }
    fun sum() = (1..10).sum()
}

fun main() {
    val programming=KotlinProgramming()
    println("reduced : ${programming.reduced()}")
    println("sum : ${programming.sum()}")
}