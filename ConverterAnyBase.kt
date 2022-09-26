package converter

import java.math.*
import kotlin.math.*

class NumberConvert(numberConvert: List<String>) {
    var integer = numberConvert[0]
    var fractional = if (numberConvert.size == 2) "0.${numberConvert[1]}" else ""
}

fun alphabet(): MutableList<Char> {
    val alphabet: MutableList<Char> = mutableListOf()
    for (i in '0'..'9') {
        alphabet.add(i)
    }
    for (i in 'a'..'z') {
        alphabet.add(i)
    }
    return alphabet
}

fun fromDecimal(x: String, y: String): String {
    var numberConvert = BigInteger(x)
    var result = ""
    if (numberConvert != BigInteger.ZERO) {
        while (numberConvert != BigInteger.ZERO) {
            result += "${alphabet()[(numberConvert % BigInteger(y)).toInt()]}"
            numberConvert /= BigInteger(y)
        }
    } else result = "0"
    return result.reversed()
}

fun fromDecimalFractional(x: String, y: String): String {
    var fractionalConvert = BigDecimal(x)
    var result = ""
    while (fractionalConvert != BigDecimal.ONE) {
        fractionalConvert -= fractionalConvert.setScale(0, RoundingMode.DOWN)
        result += "${alphabet()[(fractionalConvert * BigDecimal(y)).setScale(0, RoundingMode.DOWN).toInt()]}"
        fractionalConvert *= BigDecimal(y)
        if (result.length == 5) break
    }
    return result
}

fun toDecimal(x: String, y: String): String {
    val numberConvert = x.reversed()
    var result = BigInteger("0")
    for (i in numberConvert.indices) {
        result += BigInteger.valueOf(alphabet().indexOf(numberConvert[i]).toLong()) * BigInteger.valueOf(y.toDouble().pow(i).toLong())
    }
    return result.toString()
}

fun toDecimalFractional(x: String, y: String): String {
    val fractionalConvert = x.replace(".", "")
    var result = BigDecimal("0")
    for (i in fractionalConvert.indices) {
        result += BigDecimal.valueOf(alphabet().indexOf(fractionalConvert[i]).toLong()).setScale(6, RoundingMode.HALF_DOWN) / BigDecimal.valueOf(y.toDouble().pow(i).toLong())
    }
    result = result.setScale(6, RoundingMode.HALF_DOWN)
    return result.toString().substringAfter("0").substring(0, 6)
}

fun main() {
    do {
        print("\nEnter two numbers in format: {source base} {target base} (To quit type /exit) ")
        val request = readln()
        if (request != "/exit") {
            val (sourceBase, targetBase) = request.split(' ')
            print("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back) ")
            var numberConvert = readln()
            while (numberConvert != "/back") {
                val convert = NumberConvert(numberConvert.split("."))
                print("Conversion result: ")
                if (sourceBase == "10") {
                    if (convert.fractional == "") println(fromDecimal(convert.integer, targetBase)) else {
                        println(fromDecimal(convert.integer, targetBase) + "." + fromDecimalFractional(convert.fractional, targetBase))
                    }
                } else if (targetBase == "10") {
                    if (convert.fractional == "") println(toDecimal(convert.integer, sourceBase)) else {
                        println(toDecimal(convert.integer, sourceBase) + toDecimalFractional(convert.fractional, sourceBase))
                    }

                } else if (numberConvert == "0") println("0")
                else {
                    if (convert.fractional == "") println(fromDecimal(toDecimal(convert.integer, sourceBase), targetBase)) else {
                        println(fromDecimal(toDecimal(convert.integer, sourceBase), targetBase) + "." + fromDecimalFractional(toDecimalFractional(convert.fractional, sourceBase), targetBase))
                    }

                }
                print("\nEnter number in base $sourceBase to convert to base $targetBase (To go back type /back) ")
                numberConvert = readln()
            }
        }
    } while (request != "/exit")
}