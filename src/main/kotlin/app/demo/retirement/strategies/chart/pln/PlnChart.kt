package app.demo.retirement.strategies.chart.pln

import app.demo.retirement.strategies.chart.ChartFactory
import app.demo.retirement.strategies.chart.DataSource.Companion.retrieveYearToInflationValue
import app.demo.retirement.strategies.chart.Series
import org.knowm.xchart.style.Styler
import java.math.BigDecimal
import java.math.RoundingMode

class PlnChart {

    fun buildYearlyPurchasingPowerChart(outputImageName: String) {
        val yearToPurchasingPower = retrieveYearToInflationValue()
                .mapValues { keyToValue -> BigDecimal("10000.00") / keyToValue.value }

        val years = yearToPurchasingPower.keys.toList()
        val purchasingPowerValues = yearToPurchasingPower.values.toList()

        val chart = ChartFactory.buildXYChart(
                title = "Siła nabywcza PLN (PLZ) 1950-2019",
                xAxisTitle = "rok",
                yAxisTitle = "siła nabywcza (poprzedni rok = 100.0)",
                legendPosition = Styler.LegendPosition.InsideSE,
                xSeries = years,
                ySeries = listOf(Series("PLN (PLZ)", purchasingPowerValues))
        )

        ChartFactory.save(outputImageName, chart)
    }

    fun buildTotalPurchasingPowerChartLogYScale(outputImageName: String) {
        val yearToPurchasingPower = retrieveYearToInflationValue()
                .mapValues { keyToValue -> BigDecimal("10000.00") / keyToValue.value }

        val years = yearToPurchasingPower.keys.toList()
        val cumulativePurchasingPowerValues = yearToPurchasingPower.values.toList()
                .fold(listOf<BigDecimal>(), { acc, v ->  when {
                    acc.isEmpty() -> acc + v
                    else -> acc + (((acc.last() / "100".toBigDecimal().setScale(25)) *
                            (v / "100".toBigDecimal().setScale(25))) * "100".toBigDecimal().setScale(25))
                            .setScale(25, RoundingMode.HALF_UP)
                }})

        val chart = ChartFactory.buildXYChart(
                title = "Siła nabywcza PLN (PLZ) 1950-2019",
                xAxisTitle = "rok",
                yAxisTitle = "siła nabywcza (w roku 1949 = 100.0)",
                yAxisDecimalPattern = "#.####",
                xSeries = years,
                ySeries = listOf(Series("PLN (PLZ)", cumulativePurchasingPowerValues)),
                yAxisLogarithmic = true)

        ChartFactory.save(outputImageName, chart)
    }
}