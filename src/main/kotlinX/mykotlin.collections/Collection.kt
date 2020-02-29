package mykotlin.collections

import java.math.BigDecimal

fun Collection<BigDecimal>.avg() : BigDecimal {
    val sum = this.reduce { acc, v -> acc + v }
    val count = this.size.toBigDecimal()
    return sum / count
}