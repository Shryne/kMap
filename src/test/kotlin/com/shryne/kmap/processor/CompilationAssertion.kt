package com.shryne.kmap.processor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.assertj.core.api.Assertions
import java.io.File
import kotlin.test.assertEquals

interface Source {
    val name: String
    val fileName: String

    fun fileFromResources(path: String): File =
        File(Source::class.java.getResource(path).toURI())

    fun sourceCodeFromResources(path: String): String =
        fileFromResources(path).readText()
}

class Java(override val name: String) : Source {
    override val fileName: String = "$name.java"
}

class Kotlin(override val name: String) : Source {
    override val fileName: String = "$name.kt"

    override fun fileFromResources(path: String): File =
        File(Source::class.java.getResource(path).toURI())

    override fun sourceCodeFromResources(path: String): String =
        fileFromResources(path).readText()
}

fun assertMappingFiles(sourceFolder: String, vararg sources: Source) {
    val sourceFiles = sources.map {
        SourceFile.fromPath(
            it.fileFromResources("$sourceFolder/${it.fileName}")
        )
    }

    val result = KotlinCompilation().also {
        it.sources = sourceFiles.toMutableList()
        it.annotationProcessors = listOf(MapPartnerProcessor())
        it.inheritClassPath = true
        it.messageOutputStream = System.out
    }.compile()

    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

    sources.forEach { source ->
        Assertions.assertThat(
            result.sourcesGeneratedByAnnotationProcessor.find {
                it.name == "${source.name}Mapping.kt"
            }!!.readText()
        ).containsIgnoringWhitespaces(
            source.sourceCodeFromResources(
                "$sourceFolder/${source.name}Mapping.kt"
            )
        )
    }
}
