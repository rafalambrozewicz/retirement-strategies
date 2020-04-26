package app.demo.retirement.strategies.chart

import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.internal.chartpart.Chart
import org.knowm.xchart.style.Styler
import org.knowm.xchart.style.markers.Marker
import org.knowm.xchart.style.markers.SeriesMarkers

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
                datePattern: String = "yyyy-MM",
                xAxisDecimalPattern: String = "#",
                yAxisDecimalPattern: String = "#",
                legendPosition: Styler.LegendPosition = Styler.LegendPosition.InsideNE,
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
                this.styler.setDatePattern(datePattern)
                this.styler.setXAxisDecimalPattern(xAxisDecimalPattern)
                this.styler.setYAxisDecimalPattern(yAxisDecimalPattern)

                this.styler.setLegendPosition(legendPosition);
                this.styler.setYAxisLogarithmic(yAxisLogarithmic)

                ySeries.forEach {
                    this.addSeries(it.name, xSeries.toList(), it.values.toList()).marker = it.marker
                }
            }

            return xyChart
        }
    }
}

 data class Series<T : Number>(
         val name: String,
         val values: Collection<T>,
         val marker: Marker = SeriesMarkers.CIRCLE)