package app.demo.retirement.strategies.chart.flat

import app.demo.retirement.strategies.chart.ChartFactory
import app.demo.retirement.strategies.chart.DataSource
import app.demo.retirement.strategies.chart.GoodPurchasingPowerCalc
import app.demo.retirement.strategies.chart.Series
import org.knowm.xchart.style.Styler

class FlatChart {
    private val calc = GoodPurchasingPowerCalc(DataSource.retrieveYearToFlatValue())

    fun buildYearlyPurchasingPowerChart(outputImageName: String) {
        val yearToPurchasingPower = calc.calcYearly()

        val chart = ChartFactory.buildXYChart(
                title = "Siła nabywcza, 1m² pow. b. miesz, ${calc.years()}",
                xAxisTitle = "rok",
                yAxisTitle = "siła nabywcza (poprzedni rok = 100.0)",
                xSeries = yearToPurchasingPower.keys.toList(),
                ySeries = listOf(Series("1m²\n" +
                        "powierzchni\n" +
                        "uzytkowej\n" +
                        "budynku\n" +
                        "mieszkalnego", yearToPurchasingPower.values.toList())),
                yAxisDecimalPattern = "###.##")

        ChartFactory.save(outputImageName, chart)
    }

    fun buildTotalPurchasingPowerChart(outputImageName: String) {
        val yearToTotalPurchasingPower = calc.calcTotal()

        val chart = ChartFactory.buildXYChart(
                title = "Siła nabywcza, 1m² pow. b. miesz, ${calc.years()}",
                xAxisTitle = "rok",
                yAxisTitle = "siła nabywcza (w roku ${calc.zeroYearForTotal()} = 100.0)",
                xSeries = yearToTotalPurchasingPower.keys,
                ySeries = listOf(Series("1m²\n" +
                        "powierzchni\n" +
                        "uzytkowej\n" +
                        "budynku\n" +
                        "mieszkalnego", yearToTotalPurchasingPower.values.toList())),
                legendPosition = Styler.LegendPosition.InsideSE,
                yAxisDecimalPattern = "###.##")

        ChartFactory.save(outputImageName, chart)
    }
}