package com.shryne.kmap.processor.creation

import com.shryne.kmap.annotation.KMap
import com.shryne.kmap.annotation.MapPartner
import javax.lang.model.element.Element

class InformedMapPartner(
    private val sourceClass: Element,
    private val targetClass: Element,
) {
    private val mapPartner: MapPartner = sourceClass.getAnnotation(
        MapPartner::class.java
    ).apply {
        requireNotNull(this) {
            "The given class must be annotated with MapPartner."
        }
    }

    val sourcePackage: String = mapPartner.packageName
}