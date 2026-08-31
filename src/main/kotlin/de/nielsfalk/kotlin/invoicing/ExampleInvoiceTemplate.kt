package de.nielsfalk.kotlin.invoicing

import org.intellij.lang.annotations.Language
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.time.YearMonth

object ExampleInvoiceTemplate : InvoiceTemplate {


    override fun formatAdoc(invoice: Invoice): String = invoice.run {
        val serviceMonth = YearMonth.from(invoiceDate.minusDays(20))
        val hourRate = "120.00".toBigDecimal()

        val vatGroups = timesheet.groupBy {
            serviceMonth.atDay(it.day.coerceIn(1, serviceMonth.lengthOfMonth()))
                .getGermanInvoiceRate()
        }.map { (vat, rows) ->
            object {
                val vat = vat
                val hours = rows.sumHours()
                val net = (hours * hourRate).setScale(2, HALF_UP)
                val vatAmount = (net * vat.rate).setScale(2, HALF_UP)
            }
        }

        val netTotal = vatGroups.fold(BigDecimal.ZERO) { acc, g -> acc + g.net }
        val vatTotal = vatGroups.fold(BigDecimal.ZERO) { acc, g -> acc + g.vatAmount }
        val grossTotal = netTotal + vatTotal

        val formattedNetTotal = netTotal.toGermanDecimal()
        val formattedGrossTotal = grossTotal.toGermanDecimal()
        val formattedDueDate = invoiceDate.plusDays(30).format(localDateFormatter)

        val tableRows = vatGroups.mapIndexed { i, g ->
            "| ${i + 1} | Java-Programmierung und Software-Architektur | ${g.hours.toGermanDecimal()} Stunden | ${hourRate.toGermanDecimal()} € | ${g.net.toGermanDecimal()}"
        }.joinToString("\n        ")

        val vatSummary = vatGroups.joinToString("\n        ") { g ->
            "| *zzgl. ${g.vat.percentage.toGermanDecimal()}% Mehrwertsteuer* | *${g.vatAmount.toGermanDecimal()} €*"
        }

        val timesheetRows = timesheet.joinToString("\n        ") {
            "| ${yearMonth.atDay(it.day).format(localDateFormatter)} | ${it.start.format(timeFormatter)} | ${it.end.format(timeFormatter)} | ${it.duration.formatHoursMinutes()}"
        }

        @Language("AsciiDoc")
        """
        :noheader:
        :nofooter:
        :sectnums!:
        
        [frame=none, grid=none, cols="3,>1"]
        |===
        a|
        Niels Falk +
        Heidefalterweg 10 +
        12683 Berlin
        
        {empty}
        {empty}
        
        Muster Technology GmbH +
        Musterstraße 12 +
        12345 Berlin
        
        {empty}
        
        | ${localDateFormatter.format(invoiceDate)}
        
        |===
        
        [discrete]
        == Rechnung
        
        Rechnung-Nr $invoiceNumber +
        (bei Zahlung bitte angeben)
        
        [.subtitle]*Dienstleistungen im ${longMonthFormatter.format(serviceMonth)}*
        
        [frame=none, grid=none, stripes=none, cols="<3,<12,<3,>5,>4", width="100%", options="header"]
        |===
        | Lfd. Nr. | Bezeichnung | Menge | Stundensatz | Euro
        $tableRows
        |   |   |   |   |   
        |===
        
        [.small]
        [frame=none, grid=none, cols="3,>1"]
        |===
        | *Summe Netto* | *$formattedNetTotal €*
        $vatSummary
        | *Gesamtbetrag* | *$formattedGrossTotal €*
        
        |===
        
        {empty}
        
        Brutto-Rechnungsbetrag fällig am: $formattedDueDate
        
        *Bankverbindung*
        
        Kontoinhaber: Niels Falk +
        Kontonummer: *********** +
        Bankleitzahl: 43060967 +
        IBAN: DE4943060967*********** +
        BIC: GENODEM1GLS +
        Geldinstitut: GLS Gemeinschaftsbank eG
        
        USt.IDNr.: DE***********
        
        <<<
        
        [discrete]
        == Zeiterfassung
        
        [cols="1,1,1,1", options="header"]
        |===
        | Datum | Beginn | Ende | Dauer
        $timesheetRows
        |===""".trimIndent()
    }

    override val filenamePart = "example"
}
