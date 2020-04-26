package app.demo.retirement.strategies.chart

import java.math.BigDecimal
import java.math.RoundingMode

class GoodPurchasingPowerCalc(private val yearToGoodValue: Map<Int, BigDecimal>) {

    private fun calc(): Map<Int, BigDecimal> {
        val firstYearWhenGoodValueIsKnown = yearToGoodValue.keys.first()

        val inflation = DataSource.retrieveYearToInflationValue()
                .filter { it.key > firstYearWhenGoodValueIsKnown }
                .values

        val goodPricesRelativeToPrevYearWhenValueWas100 = yearToGoodValue.values
                .windowed(2, 1)
                .map { it.last() * "100.0000".toBigDecimal() / it.first() }

        val goodPurchasingPower = inflation.zip(goodPricesRelativeToPrevYearWhenValueWas100) { dInf, dGood ->
            dInf * "100".toBigDecimal() / dGood
        }.map { BigDecimal("10000.00") / it }

        return  yearToGoodValue.filterKeys { it != firstYearWhenGoodValueIsKnown }
                .keys
                .mapIndexed { i, v -> v to goodPurchasingPower[i] }
                .toMap()
    }

    fun calcYearly(): Map<Int, BigDecimal> {
        return calc()
    }

    fun zeroYearForTotal(): String = "${yearToGoodValue.keys.first()}"

    fun years(): String = "${yearToGoodValue.keys.first()+1}-${yearToGoodValue.keys.last()}"

    fun calcTotal(): Map<Int, BigDecimal> {
        val yearly = calc()
        val cumulativeValues = yearly.values.fold(listOf<BigDecimal>(), { acc, v ->  when {
            acc.isEmpty() -> acc + v
            else -> acc + (((acc.last() / "100".toBigDecimal().setScale(4)) *
                    (v / "100".toBigDecimal().setScale(4))) * "100".toBigDecimal().setScale(4))
                    .setScale(4, RoundingMode.HALF_UP)
        }})

        return yearly.keys
                .mapIndexed { i, v -> v to cumulativeValues[i] }
                .toMap()
    }
}