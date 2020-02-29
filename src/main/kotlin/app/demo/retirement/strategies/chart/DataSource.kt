package app.demo.retirement.strategies.chart

import mykotlin.collections.avg
import java.io.File
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DataSource {
    companion object {
        const val HEADER_OFFSET = 1
        val usdDf = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fun retrieveYearToInflationValue(): Map<Int, BigDecimal> =
                File("src/main/resources/inflation_yearly_1950_2019.csv")
                        .readLines()
                        .drop(HEADER_OFFSET)
                        .map { it.split(";")[0].toInt() to it.split(";")[1].toBigDecimal().setScale(4) }
                        .toMap()

        fun retrieveYearToUsdValue(): Map<Int, BigDecimal> =
                File("src/main/resources/usd_pln_exchange_rates_1984_2019.csv")
                        .readLines()
                        .drop(HEADER_OFFSET)
                        .map { LocalDate.parse(it.split(";")[0], usdDf) to it.split(";")[1].toBigDecimal().setScale(4) }
                        .groupBy { it.first.year }
                        .map { it.key to it.value.map { it.second }.avg() }
                        .toMap()
    }
}