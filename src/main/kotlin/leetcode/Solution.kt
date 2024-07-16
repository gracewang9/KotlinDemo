package leetcode


fun removeVowels(s: String) = s.filter { it !in setOf('a', 'o', 'e', 'i', 'u') }

fun compareVersion(version1: String, version2: String): Int {
    val v1 = version1.split('.').map { it.toInt() }
    val v2 = version2.split('.').map { it.toInt() }
    for (i in 0 until (maxOf(v1.size, v2.size))) {
        val diff = (v1.getOrElse(i) { 0 } - v2.getOrElse(i) { 0 })
        if (diff != 0) return if (diff > 0) 1 else -1
    }
    return 0
}

fun compareVersion1(version1: String, version2: String): Int {
    val list1 = version1.split(".")
    val list2 = version2.split(".")
    var i = 0
    do {
        val v1 = list1.getOrNull(i)?.toInt() ?: 0
        val v2 = list2.getOrNull(i)?.toInt() ?: 0
        if (v1 != v2) {
            return v1.compareTo(v2)
        }
        i++
    } while (i < list1.size || i < list2.size)

    return 0
}

fun compareVersion2(version1: String, version2: String): Int {
    val size1 = version1.length
    val size2 = version2.length
    var i = 0
    var j = 0
    while (i < size1 || j < size2) {
        var x = 0
        while (i < size1 && version1[i] != '.') {
            x = x * 10 + version1[i].toInt() - '0'.toInt()
            i++
        }
        i++
        var y = 0
        while (j < size2 && version2[j] != '.') {
            y = y * 10 + version2[j].toInt() - '0'.toInt()
            j++
        }
        j++

        if (x != y) {
            return x.compareTo(y)
        }
    }
    return 0
}

//fun compareVersion3(version1: String, version2: String) {
//    version1.split(".").zipLongest(version2.split("."), "0")
//        .onEach {
////            with(it){
////                if (first != second){
////                    return@with first.compareTo(second)
////                }
////            }
//        }.run { return@run 0 }
//}

//private fun Iterable<String>.zipLongest(
//    other: Iterable<String>,
//    deflater: String
//): List<Pair<Int, Int>> {
//    val first = iterator()
//    val second = other.iterator()
//    val list = ArrayList<Pair<Int, Int>>(minOf(collectionSizeOrDefault(10), other.collectionSizeOrDefault(10)))
//    while (first.hasNext() || second.hasNext()) {
//        val v1 = (first.nextOrNull() ?: deflater).toInt()
//        val v2=(second.nextOrNull() ?: deflater).toInt()
//        list.add(Pair(v1, v2))
//    }
//    return list
//}
//
//private fun <T> Iterable<T>.collectionSizeOrDefault(deflater: Int): Int =
//    if (this is Collection<*>) this.size else deflater
//
//private fun <T> Iterable<T>.nextOrNull(): T? = if (hasNext()) next() else null
//
//private data class Pair<out A, out B>(val first: A, val second: B) : java.io.Serializable {
//    public override fun toString(): String = "($first,$second)"
//}


/**
 * 640. 求解方程
 * 求解一个给定的方程，将x以字符串 "x=#value" 的形式返回。该方程仅包含 '+' ， '-' 操作，变量 x 和其对应系数。
 * 如果方程没有解或存在的解不为整数，请返回 "No solution" 。如果方程有无限解，则返回 “Infinite solutions” 。
 * 题目保证，如果方程中只有一个解，则 'x' 的值是一个整数。
 *
 * 示例 1：
 * 输入: equation = "x+5-3+x=6+x-2"
 * 输出: "x=2"
 *
 * 命令式实现
 */
fun solveEquation(equation: String): String {
    //分割等号
    val list = equation.split("=")

    var leftSum = 0
    var rightSum = 0
    //遍历左边的等式，移项，合并同类项
    val leftList = splitByOperator(list[0])
    val rightList = splitByOperator(list[1])
    leftList.forEach {
        if (it.contains("x")) {
            leftSum += xToInt(it)
        } else {
            rightSum -= it.toInt()
        }
    }
    rightList.forEach {
        if (it.contains("x")) {
            leftSum -= xToInt(it)
        } else {
            rightSum += it.toInt()
        }
    }
    //遍历右边的等式，移项，合并同类项

    //系数化为一，返回结果
    return when {
        leftSum == 0 && rightSum == 0 -> "Infinite solutions"
        leftSum == 0 && rightSum != 0 -> "No solution"
        else -> "x = ${rightSum / leftSum}"
    }
}

private fun xToInt(it: String): Int =
    when (it) {
        "x",
        "+x" -> 1
        "-x" -> -1
        else -> it.replace("x", "").toInt()
    }


private fun splitByOperator(list: String): List<String> {
    val result = mutableListOf<String>()
    var temp = ""
    list.forEach {
        if (it == '+' || it == '-') {
            if (temp.isNotEmpty())
                result.add(temp)

            temp = it.toString()
        } else {
            temp += it
        }
    }
    result.add(temp)
    return result
}

/**
 * 函数式实现
 */
fun solveEquation2(equation: String): String {
    val list = equation.replace("-", "+-").split("=")
    val leftList = list[0].split("+")
    val rightList = list[1].split("+")
    var leftSum = 0
    var rightSum = 0
    leftList
        .filter {
            it.hasX()
        }.map { xToInt(it) }
        .toMutableList()
        .apply {
            rightList
                .filter { it.hasX() }
                .map { xToInt(it).times(-1) }
                .let { addAll(it) }

        }.sum()
        .let { leftSum = it }

    rightList
        .filter { it.hasX() }
        .map { it.toInt() }
        .toMutableList()
        .apply {
            leftList
                .filter { it.isNumber() }
                .map { it.toInt().times(-1) }
                .let { addAll(it) }
        }.sum()
        .let { rightSum = it }

    return when {
        leftSum == 0 && rightSum == 0 -> "Infinite solution"
        leftSum == 0 && rightSum != 0 -> "No solution"
        else -> "x=${rightSum / leftSum}"
    }
}

private fun String.isNumber(): Boolean = this != "" && !this.contains("x")
private fun String.hasX(): Boolean = this != "" && !this.contains("x")


/**
 *592. 分数加减运算
 * 给定一个表示分数加减运算的字符串 expression ，你需要返回一个字符串形式的计算结果。
 * 这个结果应该是不可约分的分数，即最简分数。 如果最终结果是一个整数，
 * 例如 2，你需要将它转换成分数形式，其分母为 1。所以在上述例子中, 2 应该被转换为 2/1。
 *
 * 示例 1:
 * 输入: expression = "-1/2+1/2"
 * 输出: "0/1"
 */
fun fractionAddition(expression: String): String {
    //分割式字
    val list = expression.replace("-", "+-")
    val fractionList = list.split("+")
    val expressionList = mutableListOf<Expression>()
    //解析分数成Expression
    for (item in fractionList) {
        if (item.trim() != "") {
            expressionList.add(parseExpression(item))
        }
    }
    //计算所有分母的最小公倍数
    var lcm = 1
    for (exp in expressionList) {
        lcm = lcm(lcm, exp.denominator)
    }
    //将所有的分数都通分
    val commonDenominatorFractions = mutableListOf<Expression>()
    for (exp in expressionList) {
        commonDenominatorFractions.add(toCommonDenominatorExp(exp, lcm))

    }
    //将所有分子加起来进行计算，得到结果
    var numberator = 0
    for (fraction in commonDenominatorFractions) {
        numberator += fraction.number
    }
    //将结果化为"最简分数
    val result = Expression(numberator, lcm)
    val reducedFraction = result.reducedFraction()
    return reducedFraction.toString()
}

private fun parseExpression(expression: String): Expression {
    val list = expression.trim().split("/")
    if (list.size != 2)
        return Expression(0, 0)

    return Expression(list[0].toInt(), list[1].toInt())
}

private fun toCommonDenominatorExp(expression: Expression, lcm: Int): Expression {
    return Expression(number = expression.number * lcm / expression.denominator, denominator = lcm)
}

private fun Expression.reducedFraction(): Expression {
    val gcd = gcd(Math.abs(number), denominator)
    return Expression(number / gcd, denominator / gcd)
}

private fun lcm(a: Int, b: Int) = a * b / gcd(a, b)
private fun gcd(a: Int, b: Int): Int {
    var (big, small) = if (a > b) a to b else b to a
    while (small != 0) {
        val temp = small
        small = big % small
        big = temp
    }
    return big
}

data class Expression(val number: Int, val denominator: Int) {
    override fun toString(): String {
        return "$number/$denominator"
    }
}

fun main() {
//    val s="a,b,c,d,e,f,j,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z"
//    val vowels = removeVowels(s)
//    print(vowels)

//    val version1 = "1.2"
//    val version2 = "1.10"
//    val compareVersion = compareVersion2(version1, version2)
//    println(compareVersion)

    val s = "x+5-3+x=6+x-2"
    val solveEquation = solveEquation2(s)
    println(solveEquation)

}