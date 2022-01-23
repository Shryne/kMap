package com.shryne.kmap.annotation

import kotlin.reflect.KClass

/**
 * Defines the class that the annotated class is needed to be mapped with.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class MapPartner(
    /**
     * The class to be mapped with.
     */
    val value: KClass<*>,

    /**
     * The package name of the generated mapping files.
     */
    val packageName: String = "com.shryne.kmap.mapping"
)
