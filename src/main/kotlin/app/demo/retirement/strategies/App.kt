package app.demo.retirement.strategies

import app.demo.retirement.strategies.chart.gold.GoldChart
import app.demo.retirement.strategies.chart.inflation.InflationChart
import app.demo.retirement.strategies.chart.usd.UsdChart

fun main(args: Array<String>) {
    val ic = InflationChart()
    ic.buildYearlyInflationChart("charts/inflation_yearly_1950_2019.png")
    ic.buildCumulativeInflationChartLogYScale("charts/inflation_cumulative_logY_1950_2019.png")
    ic.buildCumulativeInflationChart("charts/inflation_cumulative_1950_2019.png")
    val usdC = UsdChart()
    usdC.buildUsdYearlyChart("charts/usd_yearly_1984_2019.png")
    usdC.buildUsdYearlyChartInflationAdjusted("charts/usd_inflation_yearly_1985_2019.png")
    usdC.buildUsdCumulativeChartInflationAdjusted("charts/usd_inflation_cumulative_1985_2019.png")
    val gC = GoldChart()
    gC.buildGoldYearlyChart("charts/gold_yearly_1984_2019.png")
    gC.builGoldYearlyChartInflationAdjusted("charts/gold_inflation_yearly_1985_2019.png")
    gC.buildGoldCumulativeChartInflationAdjusted("charts/gold_inflation_cumulative_1985_2019.png")
}
