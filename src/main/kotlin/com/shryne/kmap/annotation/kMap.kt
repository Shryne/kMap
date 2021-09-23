package com.shryne.kmap.annotation

/**
 * Defines with what property the annotated property is to be mapped with.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class kMap(
    /**
     * The name of the property to be mapped with. The default value "" is used
     * when the annotated and the target of the mapping have the same name.
     */
    val value: String = ""
)
