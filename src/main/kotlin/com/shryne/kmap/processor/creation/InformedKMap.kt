package com.shryne.kmap.processor.creation

import com.shryne.kmap.annotation.KMap
import javax.lang.model.element.Element

class InformedKMap(private val sourceProperty: Element, private val targetClass: Element) {
    private val kMap: KMap = sourceProperty.getAnnotation(KMap::class.java).apply {
        requireNotNull(this) { "The given source property must be annotated with KMap." }
        require(value == "" || othersGet == "" || othersSet == "") {
            "KMap.value is redundant when KMap.othersGet and KMap.othersSet are set."
        }
    }

    val sourceGet: String = sourceProperty.simpleName.toString()
    val sourceSet: String = sourceProperty.simpleName.toString()

    val targetGet: String = when {
        kMap.value == "" -> if (kMap.othersGet == "") {
            sourceGet
        } else {
            kMap.othersGet
        }
        kMap.othersGet == "" -> if (kMap.value == "") {
            sourceGet
        } else {
            kMap.value
        }
        else -> sourceGet
    }

    val targetSet: String = when {
        kMap.value == "" -> if (kMap.othersSet == "") {
            sourceSet
        } else {
            kMap.othersSet
        }
        kMap.othersSet == "" -> if (kMap.value == "") {
            sourceSet
        } else {
            kMap.value
        }
        else -> sourceSet
    }

    // target = source
    fun sourceToTargetAssignment(): String =
        when (targetSet.isMethod()) {
            true -> "it.${targetSet.substringBefore("(")}($sourceGet)"
            false -> "it.$targetSet = $sourceGet"
        }


    fun targetToSourceAssignment(): String =
        when (sourceSet.isMethod()) {
            true -> "it.${sourceSet.substringBefore("(")}($targetGet)"
            false -> "it.$sourceSet = $targetGet"
        }

    private fun String.isMethod(): Boolean = endsWith("()")
}

/*
@KMap(value = "x", othersGet = "abc()")

...
var x: Int

abc
 */
