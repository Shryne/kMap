package com.shryne.kmap.processor

import org.junit.jupiter.api.Test

/**
 * Tests the mapping for properties that are defined in the constructor. Example:
 * ```kotlin
 * @MapPartner(...)
 * class A(@KMap var value: Int)
 * ```
 */
class ConstructorTest {
    @Test
    fun bothConstructorSameName() {
        assertMappingFiles(
            "/constructor/both_constructor_same_name",
            Kotlin("Scalar"),
            Kotlin("Value")
        )
    }
}