package de.nielsfalk.kotlin.invoicing

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
        val adoc = invoice.formatAdoc()
        assert(adoc.trimStart().startsWith(":noheader:"))
        assert(adoc.contains("<<<"))
        assert(adoc.contains("== Zeiterfassung"))
        assert(adoc.contains("| Datum | Beginn | Ende | Dauer"))
        
        val firstRow = invoice.timesheet.first()
        assert(adoc.contains("| ${invoice.yearMonth.atDay(firstRow.day).format(localDateFormatter)} |"))
    }
})
