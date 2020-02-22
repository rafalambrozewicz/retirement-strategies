package app.demo.retirement.strategies.dataExtractor.usd.model

import java.math.BigDecimal
import java.time.LocalDate

enum class CurrencyCode {
    PLZ,
    PLN,
    USD
}

data class CurrencyRow (val date: LocalDate,
                        val from: CurrencyCode,
                        val to: CurrencyCode,
                        val quantity: Int,
                        val value: BigDecimal)