package de.nielsfalk.kotlin.invoicingimport

import de.nielsfalk.kotlin.invoicing.invoices
import io.kotest.core.spec.style.FreeSpec
import java.time.YearMonth

class InvoiceDslTest : FreeSpec({
    val invoice = invoices.firstOrNull {
        it.yearMonth == YearMonth.of(2025, 1)
    }

    "invoice exists" {
        assert(invoice != null)
    }

    invoice!!

    "invoice exists and has times" {
        assert(invoice.timesheet.size == 21)
    }

    "invoice filename" {
        assert(invoice.filename == "Niels Falk example Rechnung-Nr 0001-2025.pdf")
    }

    "format adoc" {
        assert(invoice.formatAdoc().trimStart().startsWith(":noheader:"))
    }
})
