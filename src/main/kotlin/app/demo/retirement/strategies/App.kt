package app.demo.retirement.strategies

import app.demo.retirement.strategies.chart.flat.FlatChart
import app.demo.retirement.strategies.chart.gold.GoldChart
import app.demo.retirement.strategies.chart.labor.LaborChart
import app.demo.retirement.strategies.chart.pln.PlnChart
import app.demo.retirement.strategies.chart.usd.UsdChart

fun main(args: Array<String>) {
    val plnC = PlnChart()
    plnC.buildYearlyPurchasingPowerChart("charts/pp_pln_plz_yearly_1950_2019.png")
    plnC.buildTotalPurchasingPowerChartLogYScale("charts/pp_pln_plz_total_1950_2019_logY.png")

    val usdC = UsdChart()
    usdC.buildYearlyPurchasingPowerChart("charts/pp_usd_yearly_1985_2019.png")
    usdC.buildTotalPurchasingPowerChart("charts/pp_usd_total_1985_2019.png")

    val gC = GoldChart()
    gC.buildYearlyPurchasingPowerChart("charts/pp_gold_yearly_1985_2019.png")
    gC.buildTotalPurchasingPowerChart("charts/pp_gold_total_1985_2019.png")

    val fC = FlatChart()
    fC.buildYearlyPurchasingPowerChart("charts/pp_flat_yearly_1966_2019.png")
    fC.buildTotalPurchasingPowerChart("charts/pp_flat_total_1966_2019.png")

    val lC = LaborChart()
    lC.buildYearlyPurchasingPowerChart("charts/pp_labor_yearly_1951_2019.png")
    lC.buildTotalPurchasingPowerChart("charts/pp_labor_total_1951_2019.png")
}
