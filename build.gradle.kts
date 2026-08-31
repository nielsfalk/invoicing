plugins {
    kotlin("jvm") version "2.3.21"
    kotlin("plugin.power-assert") version "2.3.21"
    id("io.github.nielsfalk.datatable") version "0.2.1"
    application
}

group = "de.nielsfalk.kotlin.invoicing"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.asciidoctor:asciidoctorj:3.0.1")
    implementation("org.asciidoctor:asciidoctorj-pdf:2.3.23")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

tasks.named("compileKotlin") {
    dependsOn("generateDataTables")
}

application {
    mainClass = "de.nielsfalk.kotlin.invoicing.InvoiceTempletingKt"
}
