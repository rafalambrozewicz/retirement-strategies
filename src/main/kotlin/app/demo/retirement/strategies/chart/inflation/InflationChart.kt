package app.demo.retirement.strategies.chart.inflation

import app.demo.retirement.strategies.chart.ChartFactory
import app.demo.retirement.strategies.chart.DataSource.Companion.retrieveYearToInflationValue
import app.demo.retirement.strategies.chart.Series
import java.math.BigDecimal
import java.math.RoundingMode

class InflationChart {

    fun buildYearlyInflationChart(outputImageName: String) {
        val yearToInflationValue = retrieveYearToInflationValue()

        val years = yearToInflationValue.keys.toList()
        val inflationValues = yearToInflationValue.values.toList()

        val chart = ChartFactory.buildXYChart(
                title = "Inflacja coroczna 1950-2019",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (poprzedni rok = 100.0)",
                xSeries = years,
                ySeries = listOf(Series("inflacja", inflationValues))
        )

        ChartFactory.save(outputImageName, chart)
    }

    fun buildCumulativeInflationChartLogYScale(outputImageName: String) {
        val yearToInflationValue = retrieveYearToInflationValue()

        val years = yearToInflationValue.keys.toList()
        val cumulativeInflationValues = yearToInflationValue.values.toList()
                .fold(listOf<BigDecimal>(), { acc, v ->  when {
                    acc.isEmpty() -> acc + v
                    else -> acc + (((acc.last() / "100".toBigDecimal().setScale(4)) *
                            (v / "100".toBigDecimal().setScale(4))) * "100".toBigDecimal().setScale(4))
                            .setScale(4, RoundingMode.HALF_UP)
                }})

        val chart = ChartFactory.buildXYChart(
                title = "Inflacja skumulowana 1950-2019",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (w roku 1949 = 100.0)",
                xSeries = years,
                ySeries = listOf(Series("inflacja", cumulativeInflationValues)),
                yAxisLogarithmic = true)

        ChartFactory.save(outputImageName, chart)
    }

    fun buildCumulativeInflationChart(outputImageName: String) {
        val yearToInflationValue = retrieveYearToInflationValue()

        val years = yearToInflationValue.keys.toList()
        val cumulativeInflationValues = yearToInflationValue.values.toList()
                .fold(listOf<BigDecimal>(), { acc, v ->  when {
                    acc.isEmpty() -> acc + v
                    else -> acc + (((acc.last() / "100".toBigDecimal().setScale(4)) *
                            (v / "100".toBigDecimal().setScale(4))) * "100".toBigDecimal().setScale(4))
                            .setScale(4, RoundingMode.HALF_UP)
                }})

        val chart = ChartFactory.buildXYChart(
                title = "Inflacja skumulowana 1950-2019",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (w roku 1949 = 100.0)",
                xSeries = years,
                ySeries = listOf(Series("inflacja", cumulativeInflationValues)))

        ChartFactory.save(outputImageName, chart)
    }
}