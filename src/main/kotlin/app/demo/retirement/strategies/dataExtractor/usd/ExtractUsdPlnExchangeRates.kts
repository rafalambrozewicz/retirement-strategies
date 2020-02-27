import app.demo.retirement.strategies.dataExtractor.usd.model.CurrencyCode
import app.demo.retirement.strategies.dataExtractor.usd.model.CurrencyRow
import java.io.File
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val csvFilesOldFormat = listOf(
        File("data/csv/archiwum_tab_a_1984.csv"),
        File("data/csv/archiwum_tab_a_1985.csv"),
        File("data/csv/archiwum_tab_a_1986.csv"),
        File("data/csv/archiwum_tab_a_1987.csv"),
        File("data/csv/archiwum_tab_a_1988.csv"),
        File("data/csv/archiwum_tab_a_1989.csv"),
        File("data/csv/archiwum_tab_a_1990.csv"),
        File("data/csv/archiwum_tab_a_1991.csv"),
        File("data/csv/archiwum_tab_a_1992.csv"))

val csvFilesNewFormat = listOf(
        File("data/csv/archiwum_tab_a_1993.csv"),
        File("data/csv/archiwum_tab_a_1994.csv"),
        File("data/csv/archiwum_tab_a_1995.csv"),
        File("data/csv/archiwum_tab_a_1996.csv"),
        File("data/csv/archiwum_tab_a_1997.csv"),
        File("data/csv/archiwum_tab_a_1998.csv"),
        File("data/csv/archiwum_tab_a_1999.csv"),
        File("data/csv/archiwum_tab_a_2000.csv"),
        File("data/csv/archiwum_tab_a_2001.csv"),
        File("data/csv/archiwum_tab_a_2002.csv"),
        File("data/csv/archiwum_tab_a_2003.csv"),
        File("data/csv/archiwum_tab_a_2004.csv"),
        File("data/csv/archiwum_tab_a_2005.csv"),
        File("data/csv/archiwum_tab_a_2006.csv"),
        File("data/csv/archiwum_tab_a_2007.csv"),
        File("data/csv/archiwum_tab_a_2008.csv"),
        File("data/csv/archiwum_tab_a_2009.csv"),
        File("data/csv/archiwum_tab_a_2010.csv"),
        File("data/csv/archiwum_tab_a_2011.csv"),
        File("data/csv/archiwum_tab_a_2012.csv"),
        File("data/csv/archiwum_tab_a_2013.csv"),
        File("data/csv/archiwum_tab_a_2014.csv"),
        File("data/csv/archiwum_tab_a_2015.csv"),
        File("data/csv/archiwum_tab_a_2016.csv"),
        File("data/csv/archiwum_tab_a_2017.csv"),
        File("data/csv/archiwum_tab_a_2018.csv"),
        File("data/csv/archiwum_tab_a_2019.csv"))

val rows =
        (csvFilesOldFormat.fold(listOf<CurrencyRow>(), { acc, el -> acc + extractOld(el,  "USA") }) +
                csvFilesNewFormat.fold(listOf<CurrencyRow>(), { acc, el -> acc + extractNew(el, "USD") }))
                .sortedBy { it.date }
                .map { it.copy(
                        date = it.date,
                        from = it.from,
                        to = CurrencyCode.PLN,
                        quantity = 1,
                        value = it.value /
                                (if (it.to == CurrencyCode.PLZ) "10000".toBigDecimal() else "1".toBigDecimal()) /
                                it.quantity.toBigDecimal())
                }

val content = "#date;pln (assuming denomination happened in 1984, source: https://www.nbp.pl/home.aspx?f=/kursy/arch_a.html, 2020.02.22)\n" +
        rows.map { "${it.date};${it.value}" }.joinToString("\n")

File("usd_pln_exchange_rates_1984_2019.csv").writeText(content)

fun extractOld(file: File, countryName: String): List<CurrencyRow> {
    val formatter = DateTimeFormatter.ofPattern("d.MM.yyyy")
    val countriesRowIndex = 0
    val currenciesRowIndex = 1
    val descriptionRowsOffset = 2

    val dateIndex = 0

    val lines = file.readLines()

    val currencyIndex = lines[countriesRowIndex]
            .split(";")
            .findIndex { it.contains(countryName) }!!

    val currencyQuantity = lines[currenciesRowIndex]
            .split(";")[currencyIndex]
            .filter { it.isDigit() }
            .trim()
            .toInt()

    return lines.drop(descriptionRowsOffset).map {
        val elements = it.split(";")

        val date = LocalDate.parse(elements[dateIndex], formatter)

        val value = elements[currencyIndex]
                .replace(",", ".")
                .toBigDecimal()
                .setScale(4, RoundingMode.HALF_UP)

        CurrencyRow(date = date,
                from = CurrencyCode.USD,
                to = CurrencyCode.PLZ,
                quantity = currencyQuantity,
                value = value)
    }
}

fun extractNew(file: File, currencyCode: String): List<CurrencyRow> {

    val firstFormatter = DateTimeFormatter.ofPattern("d.MM.yyyy")
    val secondFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    val currenciesRowIndex = 0
    val descriptionRowsOffset = 1

    val dateIndex = 0

    val lines = file.readLines()

    val currencyIndex = lines[currenciesRowIndex]
            .split(";")
            .findIndex { it.contains(currencyCode) }!!

    val currencyQuantity = lines[currenciesRowIndex]
            .split(";")[currencyIndex]
            .filter { it.isDigit() }
            .trim()
            .toInt()


    return lines.drop(descriptionRowsOffset).map { s ->
        val elements = s.split(";")

        val firstDate: LocalDate? = try { LocalDate.parse(elements[dateIndex], firstFormatter) } catch (e: Throwable) { null }
        val secondDate: LocalDate? = try { LocalDate.parse(elements[dateIndex], secondFormatter) } catch (e: Throwable) { null }
        val date = firstDate ?: secondDate ?: throw Exception("Unknown date format")

        val value = elements[currencyIndex]
                .replace(",", ".")
                .toBigDecimal()
                .setScale(4, RoundingMode.HALF_UP)

        CurrencyRow(date = date,
                from = CurrencyCode.USD,
                to = when (date.year) {
                    in 1995..2019 -> CurrencyCode.PLN
                    in 1984..1994 -> CurrencyCode.PLZ
                    else -> throw Exception("Row from unsupported date")
                },
                quantity = currencyQuantity,
                value = value)
    }
}

fun <T> Collection<T>.findIndex(predicate: (T) -> Boolean): Int? {
    for (i in 0..this.size) { if (predicate(this.elementAt(i))) { return i } }

    return null
}