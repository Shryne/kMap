package com.shryne.kmap.processor

import org.junit.jupiter.api.Test

/**
 * Tests the mapping between properties with an accessor that has another name. The `value`, `othersSet` or `othersGet`
 * parameters of `@KMap` are used in this case.
 *
 * Note that only two of these parameters can be used together, because the third wouldn't have an
 * effect. Example: `@KMap(value = "a", othersGet = "b", othersSet = "c")`. `value` wouldn't have any effect because
 * the getter and the setter are already defined.
 *
 * Additionally: To differentiate properties from methods, the `()` suffix is necessary. Otherwise, it would be impossible
 * to tell what is meant here:
 *
 * ```kotlin
 * @MapPartner(User::class)
 * class Client(
 *      @KMap(othersGet = "x")
 *      var y: Int
 * )
 *
 * class User(var x: Int) {
 *      fun x(): Int = 0
 * }
 * ```
 *
 */
class DifferentAccessorTest {
    @Test
    fun differentAccessorOtherGet() {
        assertMappingFiles("/different_accessor/other_get", Kotlin("User"), Kotlin("Client"))
    }

    @Test
    fun differentAccessorOtherSet() {
        assertMappingFiles("/different_accessor/other_set", Kotlin("User"), Kotlin("Client"))
    }

    @Test
    fun differentAccessorOtherGetSet() {
        assertMappingFiles("/different_accessor/other_get_set", Kotlin("User"), Kotlin("Client"))
    }

    @Test
    fun differentAccessorOtherGetSetJava() {
        assertMappingFiles("/different_accessor/other_get_set_java", Kotlin("Client"), Java("User"))
    }
}