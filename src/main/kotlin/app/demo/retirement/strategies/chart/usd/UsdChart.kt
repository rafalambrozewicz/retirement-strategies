package app.demo.retirement.strategies.chart.usd

import app.demo.retirement.strategies.chart.ChartFactory
import app.demo.retirement.strategies.chart.DataSource.Companion.retrieveYearToInflationValue
import app.demo.retirement.strategies.chart.DataSource.Companion.retrieveYearToUsdValue
import app.demo.retirement.strategies.chart.Series
import java.math.BigDecimal
import java.math.RoundingMode

class UsdChart {

    fun buildUsdYearlyChart(outputImageName: String) {

        val yearToUsdValue = retrieveYearToUsdValue()
        val chart = ChartFactory.buildXYChart(
                title = "USD/PLN 1984-2019 (zakładając, że denominacja nastapiła w 1984)",
                xAxisTitle = "rok",
                yAxisTitle = "wartość w pln",
                xSeries = yearToUsdValue.keys.toList(),
                ySeries = listOf(Series("1 usd", yearToUsdValue.values.toList())),
                yAxisDecimalPattern = "###.##"
        )

        ChartFactory.save(outputImageName, chart)
    }

    fun buildUsdYearlyChartInflationAdjusted(outputImageName: String) {
        val yearToUsdValue = retrieveYearToUsdValue()
        val inflation = retrieveYearToInflationValue()
                .filter { it.key > yearToUsdValue.keys.first() }
                .values

        val usdToPlnInflation = yearToUsdValue.values
                .windowed(2, 1)
                .map { it.first() * "100".toBigDecimal() / it.last() }

        val usdInflation = inflation.zip(usdToPlnInflation) { dInf, dUsd ->
            ((dInf / "100".toBigDecimal()) * (dUsd / "100".toBigDecimal())) * "100".toBigDecimal() }

        val chart = ChartFactory.buildXYChart(
                title = "Inflacja \"USD\", coroczna, 1985-2019",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (poprzedni rok = 100.0)",
                xSeries = yearToUsdValue.keys.toList().drop(1),
                ySeries = listOf(Series("usd", usdInflation)),
                yAxisDecimalPattern = "###.##"
        )

        ChartFactory.save(outputImageName, chart)
    }

    fun buildUsdCumulativeChartInflationAdjusted(outputImageName: String) {

        val yearToUsdValue = retrieveYearToUsdValue()
        val inflation = retrieveYearToInflationValue()
                .filter { it.key > yearToUsdValue.keys.first() }
                .values

        val usdToPlnInflation = yearToUsdValue.values
                .windowed(2, 1)
                .map { it.first() * "100".toBigDecimal() / it.last() }

        val usdInflation = inflation.zip(usdToPlnInflation) { dInf, dUsd ->
            ((dInf / "100".toBigDecimal()) * (dUsd / "100".toBigDecimal())) * "100".toBigDecimal() }

        val cumulativeUsdInflation = usdInflation.fold(listOf<BigDecimal>(), { acc, v ->  when {
            acc.isEmpty() -> acc + v
            else -> acc + (((acc.last() / "100".toBigDecimal().setScale(4)) *
                    (v / "100".toBigDecimal().setScale(4))) * "100".toBigDecimal().setScale(4))
                    .setScale(4, RoundingMode.HALF_UP)
        }})

        val chart = ChartFactory.buildXYChart(
                title = "Inflacja \"USD\", skumulowana, 1985-2019",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (w roku 1984 = 100.0)",
                xSeries = yearToUsdValue.keys.toList().drop(1),
                ySeries = listOf(Series("usd", cumulativeUsdInflation)),
                yAxisDecimalPattern = "###.##"
        )

        ChartFactory.save(outputImageName, chart)
    }
}