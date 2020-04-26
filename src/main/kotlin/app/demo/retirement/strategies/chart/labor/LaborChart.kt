package app.demo.retirement.strategies.chart.labor

import app.demo.retirement.strategies.chart.ChartFactory
import app.demo.retirement.strategies.chart.DataSource
import app.demo.retirement.strategies.chart.GoodPurchasingPowerCalc
import app.demo.retirement.strategies.chart.Series
import org.knowm.xchart.style.Styler

class LaborChart {
    val calc = GoodPurchasingPowerCalc(DataSource.retrieveYearToLaborValue())

    fun buildYearlyPurchasingPowerChart(outputImageName: String) {
        val yearToPurchasingPower = calc.calcYearly()

        val chart = ChartFactory.buildXYChart(
                title = "Siła nabywcza, praca, ${calc.years()}",
                xAxisTitle = "rok",
                yAxisTitle = "siła nabywcza (poprzedni rok = 100.0)",
                xSeries = yearToPurchasingPower.keys.toList(),
                ySeries = listOf(Series("praca (przeciętne\nwynagrodzenie w\ngospodarce narodowej)", yearToPurchasingPower.values.toList())),
                legendPosition = Styler.LegendPosition.InsideSE,
                yAxisDecimalPattern = "###.##")

        ChartFactory.save(outputImageName, chart)
    }

    fun buildTotalPurchasingPowerChart(outputImageName: String) {
        val yearToTotalPurchasingPower = calc.calcTotal()

        val chart = ChartFactory.buildXYChart(
                title = "Siła nabywcza, praca, ${calc.years()}",
                xAxisTitle = "rok",
                yAxisTitle = "siła nabywcza (w roku ${calc.zeroYearForTotal()} = 100.0)",
                xSeries = yearToTotalPurchasingPower.keys,
                ySeries = listOf(Series("praca (przeciętne\nwynagrodzenie w\ngospodarce narodowej)", yearToTotalPurchasingPower.values.toList())),
                legendPosition = Styler.LegendPosition.InsideSE,
                yAxisDecimalPattern = "###.##")

        ChartFactory.save(outputImageName, chart)
    }
}