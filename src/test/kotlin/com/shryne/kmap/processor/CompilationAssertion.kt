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

class Java(override val name: String): Source {
    override val fileName: String = "$name.java"
}

class Kotlin(override val name: String): Source {
    override val fileName: String = "$name.kt"

    override fun fileFromResources(path: String): File =
        File(Source::class.java.getResource(path).toURI())

    override fun sourceCodeFromResources(path: String): String =
        fileFromResources(path).readText()
}

fun assertMappingFiles(sourceFolder: String, source1: Source, source2: Source) {
    val source1File = SourceFile.fromPath(
        source1.fileFromResources("$sourceFolder/${source1.fileName}".apply { println(this) })
    )
    val source2File = SourceFile.fromPath(
        source2.fileFromResources("$sourceFolder/${source2.fileName}".apply { println(this) })
    )

    val result = KotlinCompilation().apply {
        sources = listOf(source1File, source2File)
        annotationProcessors = listOf(MapPartnerProcessor())
        inheritClassPath = true
        messageOutputStream = System.out
    }.compile()

    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

    Assertions.assertThat(
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name == "${source1.name}Mapping.kt"
        }!!.readText()
    ).containsIgnoringWhitespaces(
        source1.sourceCodeFromResources(
            "$sourceFolder/${source1.name}Mapping.kt"
        )
    )

    Assertions.assertThat(
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name == "${source2.name}Mapping.kt"
        }!!.readText()
    ).containsIgnoringWhitespaces(
        source2.sourceCodeFromResources(
            "$sourceFolder/${source2.name}Mapping.kt"
        )
    )
}
