package app.demo.retirement.strategies.chart.gold

import app.demo.retirement.strategies.chart.ChartFactory
import app.demo.retirement.strategies.chart.DataSource
import app.demo.retirement.strategies.chart.GoodPurchasingPowerCalc
import app.demo.retirement.strategies.chart.Series

class GoldChart {
    val calc = GoodPurchasingPowerCalc(DataSource.retrieveYearToGoldValue())

    fun buildYearlyPurchasingPowerChart(outputImageName: String) {
        val yearToPurchasingPower = calc.calcYearly()

        val chart = ChartFactory.buildXYChart(
                title = "Siła nabywcza, złoto, ${calc.years()}",
                xAxisTitle = "rok",
                yAxisTitle = "siła nabywcza (poprzedni rok = 100.0)",
                xSeries = yearToPurchasingPower.keys.toList(),
                ySeries = listOf(Series("złoto", yearToPurchasingPower.values.toList())),
                yAxisDecimalPattern = "###.##")

        ChartFactory.save(outputImageName, chart)
    }

    fun buildTotalPurchasingPowerChart(outputImageName: String) {
        val yearToTotalPurchasingPower = calc.calcTotal()

        val chart = ChartFactory.buildXYChart(
                title = "Siła nabywcza, złoto, ${calc.years()}",
                xAxisTitle = "rok",
                yAxisTitle = "siła nabywcza (w roku ${calc.zeroYearForTotal()} = 100.0)",
                xSeries = yearToTotalPurchasingPower.keys,
                ySeries = listOf(Series("złoto", yearToTotalPurchasingPower.values.toList())),
                yAxisDecimalPattern = "###.##")

        ChartFactory.save(outputImageName, chart)
    }
}