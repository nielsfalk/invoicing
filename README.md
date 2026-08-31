# Invoicing Tool

A Kotlin-based tool for generating professional invoices as PDFs using a Domain-Specific Language (DSL) and Asciidoctor PDF.

## Features

- **Invoicing DSL**: Easily define invoices and timesheets using a clean Kotlin DSL.
- **Automatic VAT Calculation**: Supports German VAT rates with automatic selection based on the service date (e.g., handling the temporary 16% reduction in 2020).
- **PDF Generation**: Uses Asciidoctor PDF to convert AsciiDoc templates into high-quality PDF documents.
- **Timesheet Support**: Integrated timesheet generation with automatic duration and total hours calculation.
- **Customizable Templating**: Define your own invoice layouts using AsciiDoc.
- **Theming**: Support for custom PDF themes via YAML configuration (`invoice-theme.yml`).
- **Concise Data Entry**: Uses a custom `datatable` plugin for readable timesheet data in code.

## Project Structure

- `src/main/kotlin`: Core logic and DSL definitions.
  - `InvoiceDsl.kt`: DSL for creating invoices and timesheet rows.
  - `VatRate.kt`: German VAT rate logic.
  - `InvoiceTempleting.kt`: Entry point for PDF conversion.
  - `InvoiceData.kt`: Sample data containing actual invoice definitions.
- `invoice-theme.yml`: Asciidoctor PDF theme configuration.
- `build.gradle.kts`: Project dependencies and build configuration.

## Getting Started

### Prerequisites

- JDK 21 or higher.
- Gradle (included via wrapper).

### Running the Tool

To generate invoices defined in `InvoiceData.kt`:

```bash
./gradlew run
```

Generated PDFs will be placed in the `out/` directory.

To force regeneration of existing invoices:

```bash
./gradlew run --args="--force"
```

## Example Usage

Invoices are defined in Kotlin using the DSL:

```kotlin
Invoice(
    year = 2026,
    month = 7,
    invoiceDate = "01.08.2026",
    invoiceTemplate = ExampleInvoiceTemplate,
    timesheet = 
      @Data("day", "start" , "stop") Row {
            1    ǀ "09:00" ǀ "12:00"
            1    ǀ "13:00" ǀ "17:00"
            2    ǀ "08:30" ǀ "16:30"
    }
)
```

## Technologies

- **Kotlin**: Primary programming language.
- **AsciidoctorJ**: Java bridge for Asciidoctor.
- **AsciidoctorJ PDF**: PDF converter for Asciidoctor.
- **Kotest**: Testing framework.
- **Kotlin Power Assert**: Kotlin compiler plugin for descriptive assertion messages.
- **io.github.nielsfalk.datatable**: Plugin for tabular data representation in Kotlin code.
