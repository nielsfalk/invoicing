package de.nielsfalk.kotlin.invoicing

import org.asciidoctor.Asciidoctor
import org.asciidoctor.Attributes
import org.asciidoctor.Options
import org.asciidoctor.SafeMode
import java.io.File
import kotlin.use

fun main(args: Array<String>) {
    val force = args.contains("force") || args.contains("--force") || args.contains("-f")
    val outDir = File("out")
    outDir.mkdirs()

    Asciidoctor.Factory.create().use {
        invoices.forEach { invoice ->
            val file = File(outDir, invoice.filename)
            if (!file.exists() || force) {
                println("Generating ${invoice.filename}...")
                it.convert(
                    invoice.formatAdoc(),
                    Options.builder()
                        .backend("pdf")
                        .safe(SafeMode.UNSAFE)
                        .attributes(
                            Attributes.builder()
                                .attribute("pdf-themesdir", File(".").absolutePath)
                                .attribute("pdf-theme", "invoice")
                                .build()
                        )
                        .toFile(file)
                        .build()
                )
            } else {
                println("Skipping ${invoice.filename} (already exists).")
            }
        }
    }
}
