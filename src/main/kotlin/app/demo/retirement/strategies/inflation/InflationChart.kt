package app.demo.retirement.strategies.inflation

import app.demo.retirement.strategies.chart.ChartBuilder
import app.demo.retirement.strategies.chart.Series
import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.XYChart
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode

const val HEADER_OFFSET = 1

class InflationChart {

    fun buildYearlyInflationChart(outputImageName: String) {
        val yearToInflationValue = retrieveYearToInflationValue()

        val years = yearToInflationValue.keys.toList()
        val inflationValues = yearToInflationValue.values.toList()

        val chart = ChartBuilder.buildXYChart(
                title = "Inflacja coroczna 1950-2019",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (poprzedni rok = 100.0)",
                xSeries = years,
                ySeries = listOf(Series("inflacja", inflationValues))
        )

        save(outputImageName, chart)
    }

    private fun retrieveYearToInflationValue(): Map<Int, BigDecimal> =
            File("src/main/resources/inflation_yearly_1950_2019.csv")
                    .readLines()
                    .drop(HEADER_OFFSET)
                    .map { it.split(";")[0].toInt() to it.split(";")[1].toBigDecimal().setScale(4) }
                    .toMap()

    private fun save(name: String, chart: XYChart) =
            BitmapEncoder.saveBitmap(
                    chart,
                    name,
                    BitmapEncoder.BitmapFormat.PNG);

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

        val chart = ChartBuilder.buildXYChart(
                title = "Inflacja skumulowana 1950-2019",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (w roku 1949 = 100.0)",
                xSeries = years,
                ySeries = listOf(Series("inflacja", cumulativeInflationValues)),
                yAxisLogarithmic = true)

        save(outputImageName, chart)
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

        val chart = ChartBuilder.buildXYChart(
                title = "Inflacja skumulowana 1950-2019",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (w roku 1949 = 100.0)",
                xSeries = years,
                ySeries = listOf(Series("inflacja", cumulativeInflationValues)))

        save(outputImageName, chart)
    }
}