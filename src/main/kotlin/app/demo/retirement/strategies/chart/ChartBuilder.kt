package app.demo.retirement.strategies.chart

import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler

class ChartBuilder {

    companion object {
        fun <V, T : Number> buildXYChart(
                title: String,
                xAxisTitle: String,
                yAxisTitle: String,
                xSeries: Collection<V>,
                ySeries: Collection<Series<T>>,
                yAxisLogarithmic: Boolean = false): XYChart {
            val xyChart = XYChartBuilder()
                    .width(800)
                    .height(600)
                    .title(title)
                    .xAxisTitle(xAxisTitle)
                    .yAxisTitle(yAxisTitle)
                    .theme(Styler.ChartTheme.GGPlot2)
                    .build()

            xyChart.apply {
                this.styler.setDecimalPattern("#")

                this.styler.setYAxisLogarithmic(yAxisLogarithmic)

                ySeries.forEach {
                    this.addSeries(it.name, xSeries.toList(), it.values.toList())
                }
            }

            return xyChart
        }
    }
}

 data class Series<T : Number>(
         val name: String,
         val values: Collection<T>)