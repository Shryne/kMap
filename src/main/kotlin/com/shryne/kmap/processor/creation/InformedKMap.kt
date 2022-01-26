package com.shryne.kmap.processor.creation

import com.shryne.kmap.annotation.KMap
import com.shryne.kmap.annotation.MapPartner
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element
import javax.lang.model.util.Types

class InformedKMap(
    private val sourceProperty: Element,
    private val sourceClass: Element,
    private val targetClass: Element,
    private val types: Types
) {
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
    fun sourceToTargetAssignment(): String {
        val assignment = if (hasMapPartner()) {
            "$sourceGet.to${
                targetClass.enclosedElements.find {
                it.simpleName.toString() == targetGet
            }!!.asType()!!.run {
                    types.asElement(this)?.simpleName
                }}()"
        } else {
            sourceGet
        }
        return when (targetSet.isMethod()) {
            true -> "it.${targetSet.substringBefore("(")}($assignment)"
            false -> "it.$targetSet = $assignment"
        }
    }


    fun targetToSourceAssignment(): String {
        val assignment = if (hasMapPartner()) {
            "$targetGet.to${
                sourceClass.enclosedElements.find {
                    it.simpleName.toString() == sourceGet
                }!!.asType()!!.run {
                    types.asElement(this)?.simpleName
                }}()"
        } else {
            targetGet
        }

        return when (sourceSet.isMethod()) {
            true -> "it.${sourceSet.substringBefore("(")}($assignment)"
            false -> "it.$sourceSet = $assignment"
        }
    }

    private fun String.isMethod(): Boolean = endsWith("()")

    private fun hasMapPartner(): Boolean =
        sourceClass.enclosedElements.find {
            it.simpleName.toString() == sourceGet
        }?.asType()?.run {
            types.asElement(this)?.getAnnotation(MapPartner::class.java) != null
        } ?: false
}
