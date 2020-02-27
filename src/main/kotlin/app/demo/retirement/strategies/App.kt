package app.demo.retirement.strategies

import app.demo.retirement.strategies.inflation.InflationChart

fun main(args: Array<String>) {
    val ic = InflationChart()
    ic.buildYearlyInflationChart("charts/inflation_yearly_1950_2019.png")
    ic.buildCumulativeInflationChartLogYScale("charts/inflation_cumulative_logY_1950_2019.png")
    ic.buildCumulativeInflationChart("charts/inflation_cumulative_1950_2019.png")
}
