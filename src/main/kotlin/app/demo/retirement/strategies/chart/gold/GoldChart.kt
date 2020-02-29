package app.demo.retirement.strategies.chart.gold

import app.demo.retirement.strategies.chart.ChartFactory
import app.demo.retirement.strategies.chart.DataSource
import app.demo.retirement.strategies.chart.Series
import java.math.BigDecimal
import java.math.RoundingMode

class GoldChart {

    fun buildGoldYearlyChart(outputImageName: String) {

        val yearToGoldValue = DataSource.retrieveYearToGoldValue()
        val chart = ChartFactory.buildXYChart(
                title = "Złoto (1 uncja)/PLN (zakładając, że denominacja nastapiła w 1984)",
                xAxisTitle = "rok",
                yAxisTitle = "wartość w pln",
                xSeries = yearToGoldValue.keys.toList(),
                ySeries = listOf(Series("złoto,\n1 uncja", yearToGoldValue.values.toList())),
                yAxisDecimalPattern = "###.##"
        )

        ChartFactory.save(outputImageName, chart)
    }

    fun builGoldYearlyChartInflationAdjusted(outputImageName: String) {
        val yearToGoldValue = DataSource.retrieveYearToGoldValue()
        val inflation = DataSource.retrieveYearToInflationValue()
                .filter { it.key > yearToGoldValue.keys.first() }
                .values

        val goldToPlnInflation = yearToGoldValue.values
                .windowed(2, 1)
                .map { it.first() * "100".toBigDecimal() / it.last() }

        val goldInflation = inflation.zip(goldToPlnInflation) { dInf, dGold ->
            ((dInf / "100".toBigDecimal()) * (dGold / "100".toBigDecimal())) * "100".toBigDecimal() }

        val chart = ChartFactory.buildXYChart(
                title = "Inflacja \"Złoto\", coroczna, 1985-2019",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (poprzedni rok = 100.0)",
                xSeries = yearToGoldValue.keys.toList().drop(1),
                ySeries = listOf(Series("złoto", goldInflation)),
                yAxisDecimalPattern = "###.##"
        )

        ChartFactory.save(outputImageName, chart)
    }

    fun buildGoldCumulativeChartInflationAdjusted(outputImageName: String) {

        val yearToGoldValue = DataSource.retrieveYearToGoldValue()
        val inflation = DataSource.retrieveYearToInflationValue()
                .filter { it.key > yearToGoldValue.keys.first() }
                .values

        val goldToPlnInflation = yearToGoldValue.values
                .windowed(2, 1)
                .map { it.first() * "100".toBigDecimal() / it.last() }

        val goldInflation = inflation.zip(goldToPlnInflation) { dInf, dGold ->
            ((dInf / "100".toBigDecimal()) * (dGold / "100".toBigDecimal())) * "100".toBigDecimal() }

        val cumulativeGoldInflation = goldInflation.fold(listOf<BigDecimal>(), { acc, v ->  when {
            acc.isEmpty() -> acc + v
            else -> acc + (((acc.last() / "100".toBigDecimal().setScale(4)) *
                    (v / "100".toBigDecimal().setScale(4))) * "100".toBigDecimal().setScale(4))
                    .setScale(4, RoundingMode.HALF_UP)
        }})

        val chart = ChartFactory.buildXYChart(
                title = "Inflacja \"Złoto\", skumulowana, 1985-2019",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (w roku 1984 = 100.0)",
                xSeries = yearToGoldValue.keys.toList().drop(1),
                ySeries = listOf(Series("złoto", cumulativeGoldInflation)),
                yAxisDecimalPattern = "###.##"
        )

        ChartFactory.save(outputImageName, chart)
    }
}