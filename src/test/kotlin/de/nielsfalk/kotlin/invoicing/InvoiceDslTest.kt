package de.nielsfalk.kotlin.invoicingimport

import de.nielsfalk.kotlin.invoicing.invoices
import io.kotest.core.spec.style.FreeSpec
import java.time.YearMonth

class InvoiceDslTest : FreeSpec({
    "invoice exists and has times" {
        val invoice = invoices.firstOrNull {
            it.yearMonth == YearMonth.of(2025, 1)
        }

        assert(invoice != null)
        assert(invoice?.timesheet?.size == 21)
    }
})
