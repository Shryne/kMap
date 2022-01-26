package com.shryne.kmap.processor

import org.junit.jupiter.api.Test

class ObjectTest {
    @Test
    fun map() {
        assertMappingFiles(
            "/object",
            Kotlin("Dimension"),
            Kotlin("Size"),
            Kotlin("Scalar"),
            Kotlin("Value")
        )
    }
}