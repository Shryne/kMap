package com.shryne.kmap.processor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.assertj.core.api.Assertions
import java.io.File
import kotlin.test.assertEquals

interface Source {
    val name: String
    val fileName: String
}

fun fileFromResources(path: String): File =
    File(Source::class.java.getResource(path).toURI())

fun sourceCodeFromResources(path: String): String =
    fileFromResources(path).readText()

class Java(override val name: String) : Source {
    override val fileName: String = "$name.java"
}

class Kotlin(override val name: String) : Source {
    override val fileName: String = "$name.kt"
}

fun assertMappingFiles(
    sourceFolder: String,
    vararg sources: Source,
    nonMappingSources: Iterable<Source> = emptyList(),
): Unit =
    assertMappingFiles(sourceFolder, listOf(*sources), nonMappingSources)

fun assertMappingFiles(
    sourceFolder: String,
    sources: Iterable<Source>,
    nonMappingSources: Iterable<Source> = emptyList(),
): Unit =
    assertMappingFiles(sourceFolder, sources.map { "${it.name}Mapping" }, sources = sources, nonMappingSources)

fun assertMappingFiles(
    sourceFolder: String,
    sourceMapFiles: Iterable<String>,
    vararg sources: Source,
    nonMappingSources: Iterable<Source> = emptyList(),
) = assertMappingFiles(sourceFolder, sourceMapFiles, listOf(*sources), nonMappingSources)

fun assertMappingFiles(
    sourceFolder: String,
    sourceMapFiles: Iterable<String>,
    sources: Iterable<Source>,
    nonMappingSources: Iterable<Source> = emptyList(),
) {
    val result = KotlinCompilation().also {
        it.sources = (sources + nonMappingSources).map {
            SourceFile.fromPath(
                fileFromResources("$sourceFolder/${it.fileName}")
            )
        }
        it.annotationProcessors = listOf(MapPartnerProcessor())
        it.inheritClassPath = true
        it.messageOutputStream = System.out
    }.compile()

    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

    sourceMapFiles.forEach { file ->
        println("File: $file.")
        Assertions.assertThat(
            result.sourcesGeneratedByAnnotationProcessor.find {
                it.name == "$file.kt"
            }!!.readText()
        ).containsIgnoringWhitespaces(
            sourceCodeFromResources(
                "$sourceFolder/$file.kt"
            )
        )
    }
}
