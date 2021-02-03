package top.harumill.getto.bot


import java.lang.Exception
import java.util.*

object Calculator {

    private val operators = arrayListOf('+','-','*','/')
    private val level = mapOf(
        '*' to 2,
        '/' to 2,
        '+' to 1,
        '-' to 1
    )

    fun getResult(input: String): String {
        val resultRPN = toReversePolishNotation(input)
        val resultPN = toPolishNotation(input)
        return if(resultPN == resultRPN)
            resultRPN.toString()
        else "error"
    }

    @Throws(IllegalArgumentException::class, NumberFormatException::class)
    private fun toPolishNotation(inputStr: String):Int {
        val input = inputStr.replace(" ","")
        val len = input.length
        var c: Char
        var tempChar: Char
        val s1 = Stack<Char>()
        val s2 = Stack<Double>()
        val expression = Stack<Any>()
        var number: Double
        var lastIndex: Int
        var i = len - 1
        while (i >= 0) {
            c = input[i]
            if (Character.isDigit(c)) {
                lastIndex = readDoubleReverse(input, i)
                number = input.substring(lastIndex, i + 1).toDouble()
                s2.push(number)
                i = lastIndex
                if (number == number)expression.push(number.toInt()) else expression.push(number)

            } else if (c in operators) {
                while (!s1.isEmpty()
                    && s1.peek() != ')'
                    && level[c]!! < level[s1.peek()]!!
                ) {
                    expression.push(s1.peek())
                    s2.push(calc(s2.pop(), s2.pop(), s1.pop()))
                }
                s1.push(c)
            } else if (c == ')') {
                s1.push(c)
            } else if (c == '(') {
                while (s1.pop().also { tempChar = it } != ')') {
                    expression.push(tempChar)
                    s2.push(calc(s2.pop(), s2.pop(), tempChar))
                    require(!s1.isEmpty()) { "bracket doesn't match, missing right bracket ')'." }
                }
            } else if (c == ' ') {
                // ignore
            } else {
                throw IllegalArgumentException(
                    "wrong character '$c'"
                )
            }
            --i
        }
        while (!s1.isEmpty()) {
            tempChar = s1.pop()
            expression.push(tempChar)
            s2.push(calc(s2.pop(), s2.pop(), tempChar))
        }
        while (!expression.isEmpty()) {
            print(expression.pop().toString() + " ")
        }
        val result = s2.pop()
        require(s2.isEmpty()) { "input is a wrong expression." }
//        println()
        return result.toInt()
    }

    @Throws(IllegalArgumentException::class, NumberFormatException::class)
    private fun toReversePolishNotation(inputStr: String) :Int{
        val input = inputStr.replace(" ","")
        val len = input.length
        var c: Char
        var tempChar: Char
        val s1 = Stack<Char>()
        val s2 = Stack<Double>()
        var number: Double
        var lastIndex: Int
        var i = 0
        while (i < len) {
            c = input[i]
            if (c.isDigit() || c == '.') {
                lastIndex = readDouble(input, i)
                number = input.substring(i, lastIndex).toDouble()
                s2.push(number)
                i = lastIndex - 1
                print("$number ")
            } else if (c in operators) {
                while (!s1.isEmpty()
                    && s1.peek() != '('
                    && level[c]!! <= level[s1.peek()]!!
                ) {
                    print(s1.peek().toString() + " ")
                    val num1 = s2.pop()
                    val num2 = s2.pop()
                    s2.push(calc(num2, num1, s1.pop()))
                }
                s1.push(c)
            } else if (c == '(') {
                s1.push(c)
            } else if (c == ')') {
                while (s1.pop().also { tempChar = it } != '(') {
                    print("$tempChar ")
                    val num1 = s2.pop()
                    val num2 = s2.pop()
                    s2.push(calc(num2, num1, tempChar))
                    require(!s1.isEmpty()) { "缺失(" }
                }
            } else {
                throw IllegalArgumentException(
                    "检测到非法字符 '$c'"
                )
            }
            ++i
        }
        while (!s1.isEmpty()) {
            tempChar = s1.pop()
            print("$tempChar ")
            val num1 = s2.pop()
            val num2 = s2.pop()
            s2.push(calc(num2, num1, tempChar))
        }
        val result = s2.pop()
        require(s2.isEmpty()) { "表达式错误" }
        println()
        println("结果是 "+ result.toInt())

        return  result.toInt()
    }

    @Throws(IllegalArgumentException::class)
    private fun calc(num1: Double, num2: Double, op: Char): Double {
        return when (op) {
            '+' -> num1 + num2
            '-' -> num1 - num2
            '*' -> num1 * num2
            else -> {
                require(num2 != 0.0) { "除数不能为0" }
                num1 / num2
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun readDoubleReverse(input: String, start: Int): Int {
        var dotIndex = -1
        var c: Char
        for (i in start downTo 0) {
            c = input[i]
            if (c == '.') {
                dotIndex = if (dotIndex != -1) throw IllegalArgumentException(
                    "there have more than 1 dots in the number."
                ) else i
            } else if (!Character.isDigit(c)) {
                return i + 1
            } else if (i == 0) {
                return 0
            }
        }
        throw IllegalArgumentException("not a number.")
    }

    @Throws(IllegalArgumentException::class)
    private fun readDouble(input: String, start: Int): Int {
        val len = input.length
        var dotIndex = -1
        var c: Char
        for (i in start until len) {
            c = input[i]
            if (c == '.') {
                dotIndex = when {
                    dotIndex != -1 -> throw IllegalArgumentException(
                        "检测到小数点过多，请重新输入"
                    )
                    i == len - 1 -> throw IllegalArgumentException(
                        "检测到小数不完整，请重新输入"
                    )
                    else -> i
                }
            } else if (!c.isDigit()) {
                return if (dotIndex == -1 || i - dotIndex > 1) i else throw IllegalArgumentException(
                    "检测到小数不完整，请重新输入"
                )
            } else if (i == len - 1) {
                return len
            }
        }
        throw IllegalArgumentException("not a number.")
    }
}