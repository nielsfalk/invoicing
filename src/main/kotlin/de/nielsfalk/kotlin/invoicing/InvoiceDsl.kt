package de.nielsfalk.kotlin.invoicing

import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

private typealias TsRow = Row<Int, String, String>

val timeFormatter = DateTimeFormatter.ofPattern("H:mm")!!
var localDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")!!

interface InvoiceTemplate {
    val filenamePart: String
}

data class Invoice(
    val yearMonth: YearMonth,
    val invoiceTemplate: InvoiceTemplate,
    val invoiceDate: LocalDate,
    val invoiceNumber: String,
    val timesheet: List<TimeSheetRow>
) {
    val filename: String get() = "Niels Falk ${invoiceTemplate.filenamePart} Rechnung-Nr $invoiceNumber.pdf"

    companion object {
        operator fun invoke(
            year: Int,
            month: Int,
            invoiceTemplate: InvoiceTemplate,
            invoiceDate: String = localDateFormatter.format(
                YearMonth.of(year, month)
                    .plusMonths(1)
                    .atDay(1)
                    .minusDays(1)
            ),
            timesheet: List<TsRow>
        ) =
            Invoice(
                YearMonth.of(year, month),
                invoiceTemplate,
                LocalDate.parse(invoiceDate, localDateFormatter),
                "${month.toString().padStart(4, padChar = '0')}-$year",
                timesheet.map { it.toTimeSheetRow() }
            )
    }

    override fun toString(): String {
        return """
            Invoice(
                yearMonth=$yearMonth, 
                invoiceDate=$invoiceDate, 
                invoiceNumber='$invoiceNumber', 
                timesheet=
                    ${timesheet.joinToString("\n                    ") { it.prettyString() }}
            )""".trimIndent()
    }
}

private fun TsRow.toTimeSheetRow(): TimeSheetRow =
    TimeSheetRow(
        day,
        LocalTime.parse(start, timeFormatter),
        LocalTime.parse(stop, timeFormatter)
    )

data class TimeSheetRow(
    val day: Int,
    val start: LocalTime,
    val end: LocalTime,
    val duration: Duration = Duration.between(start, end).let {
        if (it.isPositive) it else it.plusHours(24)
    },
) {
    fun prettyString(): String =
        "${day.toString().padStart(2)} | $start | $end | ${duration.formatHoursMinutes()}"
}

fun Duration.formatHoursMinutes(): String {
    val totalMinutes = toMinutes()
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return "%02d:%02d".format(hours, minutes)
}

fun main() {
    val prettySheet = invoices.maxBy { it.yearMonth }.run {
        """
            ${invoiceTemplate::class.simpleName}
            $yearMonth
            
            day | start | end | duration:
            ${
            timesheet.joinToString(
                separator = "\n            ",
                transform = TimeSheetRow::prettyString
            )
        }
            
        """.trimIndent()
    }
    println("prettySheet = ${prettySheet}")

}

