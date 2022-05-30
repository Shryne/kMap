package com.shryne.kmap.processor

import org.junit.jupiter.api.Test

/**
 * Tests for basic mapping. Basic mapping is the mapping between properties with the same name and the same type.
 * `@KMap` is used without any arguments.
 */
class BasicTest {
    @Test
    fun onePropertySameName() {
        assertMappingFiles("/basic/one_property_same_name", Kotlin("Scalar"), Kotlin("Value"))
    }

    @Test
    fun javaPartner() {
        assertMappingFiles("/basic/java_partner", Kotlin("Scalar"), Java("Value"))
    }

    @Test
    fun accessorBased() {
        assertMappingFiles("/basic/accessor_based", Kotlin("Scalar"), Java("Value"))
    }

    @Test
    fun multiplePropertiesSameName() {
        assertMappingFiles("/basic/multiple_properties_same_name", Kotlin("Client"), Kotlin("User"))
    }

    @Test
    fun sameClassName() {
        assertMappingFiles("/basic/same_class_name", listOf("SUserMapping", "TUserMapping"), Kotlin("/other/User"), Kotlin("User"))
    }

    @Test
    fun internalInTarget() {
        assertMappingFiles("/basic/internal_in_target", listOf(Kotlin("Client"), Kotlin("User")), listOf(Kotlin("Wrapper")))
    }
}
