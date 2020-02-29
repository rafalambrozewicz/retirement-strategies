package app.demo.retirement.strategies.chart

import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.internal.chartpart.Chart
import org.knowm.xchart.style.Styler

class ChartFactory {

    companion object {

        fun <ST : Styler, S : org.knowm.xchart.internal.series.Series> save(filename: String, c: Chart<ST, S>) {
            BitmapEncoder.saveBitmap(
                    c,
                    filename,
                    BitmapEncoder.BitmapFormat.PNG);
        }

        fun <V, T : Number> buildXYChart(
                title: String,
                xAxisTitle: String,
                yAxisTitle: String,
                xSeries: Collection<V>,
                ySeries: Collection<Series<T>>,
                xAxisDecimalPattern: String = "#",
                yAxisDecimalPattern: String = "#",
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
                this.styler.setXAxisDecimalPattern(xAxisDecimalPattern)
                this.styler.setYAxisDecimalPattern(yAxisDecimalPattern)

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