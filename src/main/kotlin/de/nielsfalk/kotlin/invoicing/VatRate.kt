package de.nielsfalk.kotlin.invoicing

import java.math.BigDecimal
import java.time.LocalDate

enum class GermanVatRate(
    val rate: BigDecimal,
    val from: LocalDate,
    val to: LocalDate
) {
    Standard19BeforeReduction(
        rate = BigDecimal("0.19"),
        from = LocalDate.of(2016, 1, 1),
        to = LocalDate.of(2020, 6, 30)
    ),

    Temporary16(
        rate = BigDecimal("0.16"),
        from = LocalDate.of(2020, 7, 1),
        to = LocalDate.of(2020, 12, 31)
    ),

    Standard19Since2021(
        rate = BigDecimal("0.19"),
        from = LocalDate.of(2021, 1, 1),
        to = LocalDate.MAX
    );

    val percentage: BigDecimal
        get() = rate.movePointRight(2)

    fun appliesTo(date: LocalDate): Boolean =
        !date.isBefore(from) && !date.isAfter(to)
}

fun LocalDate.getGermanInvoiceRate(): GermanVatRate =
    (GermanVatRate.entries.firstOrNull { it.appliesTo(this) }
        ?: throw IllegalArgumentException(
            "No German VAT rate configured for date $this"
        ))
