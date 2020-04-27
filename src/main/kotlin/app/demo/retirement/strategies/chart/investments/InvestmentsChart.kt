package app.demo.retirement.strategies.chart.investments

import app.demo.retirement.strategies.chart.ChartFactory
import app.demo.retirement.strategies.chart.DataSource
import app.demo.retirement.strategies.chart.Series
import org.knowm.xchart.style.Styler
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.YearMonth

class InvestmentsChart {

    companion object {
        const val HEADER_OFFSET = 1
        const val NO_DATA = "n/d"
        val TAX_FACTOR = "0.1900".toBigDecimal()
    }

    fun buildInvestmentsCompareCharts() {
        val inf = DataSource.retrieveYearToInflationValue().filter { it.key > 2004 }
                .values
                .map { it / "100.0000".toBigDecimal() }
                .fold(listOf(BigDecimal.ONE)) {acc, v -> acc + acc.last() * v}
                .map { it * "100.0000".toBigDecimal() }
                .mapIndexed { i, v -> (2005 + i) to v  }
                .toMap()

        val tdYearToInterestRate = File("src/main/resources/investments/time_deposit_6m_to_1y_avg_interest_rate_2005_2019.csv")
                .readLines()
                .drop(HEADER_OFFSET)
                .filter { it.contains(NO_DATA).not()  }
                .map { YearMonth.parse(it.split(";")[0]) to it.split(";")[1].toBigDecimal().setScale(4) }
                .groupBy { it.first.year }
                .map { it.key to it.value.first().second }
                .toMap()

        val td = tdYearToInterestRate.mapValues { it.value * (BigDecimal.ONE - TAX_FACTOR) }
                .values
                .fold(listOf("100.0000".toBigDecimal())) { acc, interestRate
                    -> acc + (acc.last() * (BigDecimal.ONE + (interestRate / "100.0000".toBigDecimal()))).setScale(4, RoundingMode.UP) }
                .mapIndexed {i, v -> (2005 + i) to v }
                .toMap()

        val gbYearToInterestRatePeriod1 = File("src/main/resources/investments/edo0115_interest_rate.csv")
                .readLines()
                .drop(HEADER_OFFSET)
                .map { it.split(";")[0].toInt() to it.split(";")[1].toBigDecimal().setScale(4)}
                .toMap()

        val gbP1 = mapOf(2005 to "100.0000".toBigDecimal()) +
                gbYearToInterestRatePeriod1
                        .values
                        .fold(listOf(BigDecimal.ONE)) { acc, interestRate
                            -> acc + (acc.last() * (BigDecimal.ONE + (interestRate / "100.0000".toBigDecimal()))).setScale(4, RoundingMode.HALF_UP) }
                        .drop(1)
                        .map { ((BigDecimal.ONE + ((it - BigDecimal.ONE) * (BigDecimal.ONE - TAX_FACTOR))) * "100.0000".toBigDecimal()).setScale(4)  }
                        .mapIndexed {i, v -> (2006 + i) to v }
                        .toMap()

        val gbYearToInterestRatePeriod2 = File("src/main/resources/investments/edo0125_interest_rate.csv")
                .readLines()
                .drop(HEADER_OFFSET)
                .map { it.split(";")[0].toInt() to it.split(";")[1].toBigDecimal().setScale(4)}
                .toMap()

        val prevPeriodPercentage = (gbP1.values.last() / "100.0000".toBigDecimal()).setScale(4)

        val gbP2 = gbYearToInterestRatePeriod2
                .values
                .fold(listOf(BigDecimal.ONE)) { acc, interestRate
                    -> acc + (acc.last() * (BigDecimal.ONE + (interestRate / "100.0000".toBigDecimal()))).setScale(4, RoundingMode.HALF_UP) }
                .drop(1)
                .map { ((prevPeriodPercentage * ((BigDecimal.ONE + ((it - BigDecimal.ONE) * (BigDecimal.ONE - TAX_FACTOR))))).setScale(4, RoundingMode.HALF_UP) * "100.0000".toBigDecimal()).setScale(4)  }
                .mapIndexed {i, v -> (2016 + i) to v }
                .toMap()

        val gb = gbP1 + gbP2

        val wig40YearToValue = File("src/main/resources/investments/mwig40_y.csv")
                .readLines()
                .drop(HEADER_OFFSET)
                .map { (it.split(";")[0].dropLast(6).toInt() + 1) to it.split(";")[4].toBigDecimal().setScale(4)}
                .toMap()

        val wig40PointZeroValue = wig40YearToValue.values.first()
        val wig40 = wig40YearToValue.values
                .map { it * "100.0000".toBigDecimal() / wig40PointZeroValue }
                .map { when {
                    it > "100.0000".toBigDecimal() -> {
                        val interest = (it / "100.0000".toBigDecimal()) - BigDecimal.ONE
                        val interestAfterTax = interest * (BigDecimal.ONE - TAX_FACTOR)

                        ("100.0000".toBigDecimal() * (BigDecimal.ONE + interestAfterTax))
                    }
                    else -> it
                } }
                .mapIndexed {i, v -> (2005 + i) to v }
                .toMap()


        val chart = ChartFactory.buildXYChart(
                title = "Inflacja vs lokaty vs obligacje skarbowe vs ETF mWIG40",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (w roku 2005 = 100.0)",
                xSeries = inf.keys,
                ySeries = listOf(
                        Series("inflacja", inf.values.toList()),
                        Series("lokata", td.values.toList()),
                        Series("obligacje skarbowe", gb.values.toList()),
                        Series("ETF mWIG40", wig40.values.toList())),
                legendPosition = Styler.LegendPosition.InsideSE,
                yAxisDecimalPattern = "###.##")

        ChartFactory.save("charts/investment_strategies_compare_2005_2020.png", chart)
    }
}