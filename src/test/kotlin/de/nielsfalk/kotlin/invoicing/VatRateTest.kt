package de.nielsfalk.kotlin.invoicing

import de.nielsfalk.dataTables.Data
import de.nielsfalk.kotlin.invoicingimport.VatRateRow
import de.nielsfalk.kotlin.invoicingimport.each
import io.kotest.core.spec.style.FreeSpec
import java.time.LocalDate

class VatRateTest : FreeSpec({
    "GermanVatRate applies correct rates for dates" {
        @Data("date"                     , "expectedRate"        , "expectedPercentage") VatRateRow {
              LocalDate.of(2020, 6, 30)  ǀ "0.19".toBigDecimal() ǀ "19".toBigDecimal()
              LocalDate.of(2020, 7, 1)   ǀ "0.16".toBigDecimal() ǀ "16".toBigDecimal()
              LocalDate.of(2020, 12, 31) ǀ "0.16".toBigDecimal() ǀ "16".toBigDecimal()
              LocalDate.of(2021, 1, 1)   ǀ "0.19".toBigDecimal() ǀ "19".toBigDecimal()
              LocalDate.of(2026, 8, 31)  ǀ "0.19".toBigDecimal() ǀ "19".toBigDecimal()
        }.each {
            val vatRate = date.getGermanInvoiceRate()

            assert(vatRate.rate == expectedRate)
            assert(vatRate.percentage == expectedPercentage)
        }
    }
})
