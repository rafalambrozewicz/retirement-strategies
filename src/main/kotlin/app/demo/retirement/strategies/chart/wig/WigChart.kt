package app.demo.retirement.strategies.chart.wig

import app.demo.retirement.strategies.chart.ChartFactory
import app.demo.retirement.strategies.chart.DataSource
import app.demo.retirement.strategies.chart.Series
import org.apache.commons.math3.ml.distance.EuclideanDistance
import org.knowm.xchart.style.markers.SeriesMarkers
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class WigChart  {

    fun buildWigCompareChart() {
        val wig = DataSource.retrieveDateToClosingPricesOfWIG().normalize()
        val wig20 = DataSource.retrieveDateToClosingPricesOfWIG20().normalize()
        val wig40 = DataSource.retrieveDateToClosingPricesOfmWIG40().normalize()
        val wig80 = DataSource.retrieveDateToClosingPricesOfsWIG80().normalize()

        val wigValues = wig.doubleArrayValues()
        val distWIGToWIG20 = EuclideanDistance().compute(wigValues, wig20.doubleArrayValues())
                .toBigDecimal().setScale(2, RoundingMode.UP)
        val distWIGTomWIG40 = EuclideanDistance().compute(wigValues, wig40.doubleArrayValues())
                .toBigDecimal().setScale(2, RoundingMode.UP)
        val distWIGTomsWIG80 = EuclideanDistance().compute(wigValues, wig80.doubleArrayValues())
                .toBigDecimal().setScale(2, RoundingMode.UP)

        val chart = ChartFactory.buildXYChart(
                title = "WIG / WIG20 / mWIG40 / sWIG80",
                xAxisTitle = "rok",
                yAxisTitle = "wartość (1998-01 = 100.0)",
                xSeries = wig.keys.toList(),
                ySeries = listOf(
                        Series("WIG", wig.values.toList(), marker = SeriesMarkers.NONE),
                        Series("WIG20 (odl. od WIG=$distWIGToWIG20)", wig20.values.toList(), marker = SeriesMarkers.NONE),
                        Series("mWIG40 (odl. od WIG=$distWIGTomWIG40)", wig40.values.toList(), marker = SeriesMarkers.NONE),
                        Series("sWIG80 (odl. od WIG=$distWIGTomsWIG80)", wig80.values.toList(), marker = SeriesMarkers.NONE)),
                yAxisDecimalPattern = "###.##")

        ChartFactory.save("charts/wig_compare_1998_2020.png", chart)
    }

    private fun Map<LocalDate, BigDecimal>.normalize(): Map<Date, BigDecimal> {
        val initVal = this.values.first()

        return this.map {
            (Date.from(it.key.atStartOfDay(ZoneId.of("Europe/Warsaw")).toInstant())) to (it.value * "100.0000".toBigDecimal() / initVal)
        }.toMap()
    }

    private fun Map<Date, BigDecimal>.doubleArrayValues(): DoubleArray =
            this.values.map { it.setScale(2, RoundingMode.UP).toDouble() }.toDoubleArray()
}