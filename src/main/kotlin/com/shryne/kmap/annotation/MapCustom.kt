package com.shryne.kmap.annotation

/**
 * Defines a custom mapping method for the annotated property. Example:
 * ```kotlin
 * @MapPartner(B::class)
 * class A(
 *      @KMap
 *      @MapCustom("abs")
 *      var x: Int = 0
 * ) {
 *      fun abs(x: Int): Int = x.absoluteValue
 * }
 *
 * // one of the resulting mapping methods:
 * fun A.toB(): B =
 *      B().also {
 *          it.x = abs(x)
 *      }
 * ```
 */
annotation class MapCustom(
    val value: String = "",

    /**
     * The name of the mapping method that shall be called. Note that this method needs to have
     * a parameter for the annotated property and the return type must be suitable. The package
     * name may be included here.
     */
    val mapToOther: String = "",

    val mapToThis: String = ""
)
