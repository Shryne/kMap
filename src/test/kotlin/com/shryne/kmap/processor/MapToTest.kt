package com.shryne.kmap.processor

import com.shryne.kmap.annotation.kMap
import org.junit.jupiter.api.Test
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.assertEquals

/**
 * Tests whether [kMap] properties are used as expected.
 */
class MapToTest {
    @Test
    fun onePropertySameName() {
        val source1 = SourceFile.kotlin(
            "TestClass.kt",
            """
                import com.shryne.kmap.annotation.MapPartner 
                import com.shryne.kmap.annotation.kMap 

               @MapPartner(Target::class)
               class Source {
                    @kMap
                    var x: Int = 15
               }
            """
        )
        val source2 = SourceFile.kotlin(
            "Target.kt",
            """
               class Target {
                    var x: Int = 0
               }
            """
        )

        val result = KotlinCompilation().apply {
            sources = listOf(source1, source2)
            annotationProcessors = listOf(MapPartnerProcessor())
            inheritClassPath = true
            messageOutputStream = System.out
        }.compile()

        assertEquals(
            KotlinCompilation.ExitCode.OK,
            result.exitCode
        )

        //assertThat(result.messages).contains("My annotation processor was called")

        assertThat(
            result.sourcesGeneratedByAnnotationProcessor.find {
                it.name == "TargetMapping.kt"
            }!!.readText()
        ).containsIgnoringWhitespaces(
            """
                public fun Target.toSource(): Source = 
                    Source().also {
                        it.x = x
                    }
            """.trimIndent()
        )

        assertThat(
            result.sourcesGeneratedByAnnotationProcessor.find {
                it.name == "SourceMapping.kt"
            }!!.readText()
        ).containsIgnoringWhitespaces(
            """
                public fun Source.toTarget(): Target = 
                    Target().also {
                        it.x = x
                    }
            """.trimIndent()
        )
    }
}