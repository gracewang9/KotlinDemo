package calculator

import kotlin.system.exitProcess

val EXIT = "exit"

class CalculatorV2 {
    fun start() {
        while (true) {
            println(helper)
            val input = readLine() ?: continue
            val result = calculate(input)
            if (result == null) {
                println("输入格式不正确。")
                continue
            } else {
                println("$input = $result")
            }
        }
    }

    fun calculate(input: String): String? {
        if (shouldExit(input)) return exitProcess(0)
        val exp = parseExpression(input) ?: return null
        val left = exp.left
        val operation = exp.operator
        val right = exp.right
        return when (operation) {
            Operation.ADD -> addString(left, right)
            Operation.MINUS -> minusString(left, right)
            Operation.MULTI -> multiString(left, right)
            Operation.DIVI -> diviString(left, right)
        }
    }

    //    fun addString(left: String, right: String): String {
//        val result = left.toInt() + right.toInt()
//        return result.toString()
//    }
    fun addString(leftNum: String, rightNum: String): String {
        val result = StringBuilder()
        var leftIndex = leftNum.length - 1
        var rightIndex = rightNum.length - 1
        var carry = 0
        while (leftIndex >= 0 || rightIndex >= 0) {
            //digitToInt char 转换数字
            val leftVal = if (leftIndex >= 0) leftNum[leftIndex].digitToInt() else 0
            val rightVal = if (rightIndex >= 0) rightNum[rightIndex].digitToInt() else 0
            val sum = leftVal + rightVal + carry
            carry = sum / 10
            result.append(sum % 10)
            leftIndex--
            rightIndex--
        }
        if (carry != 0) {
            result.append(carry)
        }
        return result.reverse().toString()
    }

    fun minusString(left: String, right: String): String {
        val result = left.toInt() - right.toInt()
        return result.toString()
    }

    fun multiString(left: String, right: String): String {
        val result = left.toInt() * right.toInt()
        return result.toString()
    }

    fun diviString(left: String, right: String): String {
        val result = left.toInt() / right.toInt()
        return result.toString()
    }

    fun shouldExit(input: String) = input == EXIT
    fun parseExpression(input: String): Expression? {
        //解析操作符
        val operation = parseOperator(input) ?: return null
        //用操作符分割算式
        val list = input.split(operation.value)
        if (list.size != 2) return null
        return Expression(list[0].trim(), operation, list[1].trim())
    }

    fun parseOperator(input: String): Operation? {
        Operation.values().forEach {
            if (input.contains(it.value))
                return it
        }
        return null
    }
}

data class Expression(val left: String, val operator: Operation, val right: String)

fun main() {
    val calculatorV2 = CalculatorV2()
    calculatorV2.start()

}