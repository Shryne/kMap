package com.shryne.kmap.processor

import org.junit.jupiter.api.Test

class ObjectTest {
    @Test
    fun samePackage() {
        assertMappingFiles(
            "/object/same_package",
            Kotlin("Dimension"),
            Kotlin("Size"),
            Kotlin("Scalar"),
            Kotlin("Value")
        )
    }

    @Test
    fun differentTargetPackage() {
        assertMappingFiles(
            "/object/different_target_package",
            Kotlin("Dimension"),
            Kotlin("Size"),
            Kotlin("Scalar"),
            Kotlin("Value")
        )
    }
}