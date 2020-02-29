package app.demo.retirement.strategies.chart.flat

import app.demo.retirement.strategies.chart.ChartFactory
import app.demo.retirement.strategies.chart.DataSource
import app.demo.retirement.strategies.chart.Series
import java.math.BigDecimal
import java.math.RoundingMode

class FlatChart {
    fun buildFlatValueYearlyChart(outputImageName: String) {

        val yearToFlatValue = DataSource.retrieveYearToFlatValue()
        val chart = ChartFactory.buildXYChart(
                title = "1m² pow. b. miesz./PLN (zakładając, że denominacja nastapiła w 1984)",
                xAxisTitle = "rok",
                yAxisTitle = "wartość w pln",
                xSeries = yearToFlatValue.keys.toList(),
                ySeries = listOf(Series("1m²\npowierzchni\nuzytkowej\nbudynku\nmieszkalnego", yearToFlatValue.values.toList())),
                yAxisDecimalPattern = "###.##"
        )

        ChartFactory.save(outputImageName, chart)
    }

    fun buildFlatYearlyChartInflationAdjusted(outputImageName: String) {
        val yearToFlatValue = DataSource.retrieveYearToFlatValue()
        val inflation = DataSource.retrieveYearToInflationValue()
                .filter { it.key > yearToFlatValue.keys.first() }
                .values

        val flatToPlnInflation = yearToFlatValue.values
                .windowed(2, 1)
                .map { it.first() * "100".toBigDecimal() / it.last() }

        val flatInflation = inflation.zip(flatToPlnInflation) { dInf, dGold ->
            ((dInf / "100".toBigDecimal()) * (dGold / "100".toBigDecimal())) * "100".toBigDecimal() }

        val chart = ChartFactory.buildXYChart(
                title = "Inflacja \"Nieruchomość\", coroczna, 1966-2019",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (poprzedni rok = 100.0)",
                xSeries = yearToFlatValue.keys.toList().drop(1),
                ySeries = listOf(Series("nieruchomość", flatInflation)),
                yAxisDecimalPattern = "###.##"
        )

        ChartFactory.save(outputImageName, chart)
    }

    fun buildFlatCumulativeChartInflationAdjusted(outputImageName: String) {

        val yearToFlatValue = DataSource.retrieveYearToFlatValue()
        val inflation = DataSource.retrieveYearToInflationValue()
                .filter { it.key > yearToFlatValue.keys.first() }
                .values

        val flatToPlnInflation = yearToFlatValue.values
                .windowed(2, 1)
                .map { it.first() * "100".toBigDecimal() / it.last() }

        val flatInflation = inflation.zip(flatToPlnInflation) { dInf, dGold ->
            ((dInf / "100".toBigDecimal()) * (dGold / "100".toBigDecimal())) * "100".toBigDecimal() }

        val cumulativeFlatInflation = flatInflation.fold(listOf<BigDecimal>(), { acc, v ->  when {
            acc.isEmpty() -> acc + v
            else -> acc + (((acc.last() / "100".toBigDecimal().setScale(4)) *
                    (v / "100".toBigDecimal().setScale(4))) * "100".toBigDecimal().setScale(4))
                    .setScale(4, RoundingMode.HALF_UP)
        }})

        val chart = ChartFactory.buildXYChart(
                title = "Inflacja \"Nieruchomość\", skumulowana, 1966-2019",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (w roku 1965 = 100.0)",
                xSeries = yearToFlatValue.keys.toList().drop(1),
                ySeries = listOf(Series("nieruchomość", cumulativeFlatInflation)),
                yAxisDecimalPattern = "###.##"
        )

        ChartFactory.save(outputImageName, chart)
    }


}