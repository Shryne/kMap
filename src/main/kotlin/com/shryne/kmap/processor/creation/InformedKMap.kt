package com.shryne.kmap.processor.creation

import com.shryne.kmap.annotation.KMap
import com.shryne.kmap.annotation.MapPartner
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element
import javax.lang.model.util.Types

/**
 * A decorator for [KMap] to create the assignments for the annotated property.
 * Note that this class can be used both ways: annotated property -> property
 * and property -> annotated property.
 *
 * @param sourceProperty The property that contains the value.
 * @param sourceClass The class of the source property.
 * @param targetClass The class of the target property that will get the value.
 * @param types The utility class necessary to operate on Types.
 */
class InformedKMap(
    private val sourceProperty: Element,
    private val sourceClass: Element,
    private val targetClass: Element,
    private val types: Types
) {
    /**
     * The annotation containing the information about the mapping of a
     * property.
     */
    private val kMap: KMap = sourceProperty.getAnnotation(KMap::class.java).apply {
        requireNotNull(this) { "The given source property must be annotated with KMap." }
        if (thisValue != "") {
            require(!((thisGet != "") and (thisSet != ""))) {
                "KMap.thisValue is redundant when KMap.thisGet and KMap.thisSet are set."
            }
        }
        require(value == "" || othersGet == "" || othersSet == "") {
            "KMap.value is redundant when KMap.othersGet and KMap.othersSet are set."
        }
    }

    private val propertyName = sourceProperty.simpleName.toString()

    /**
     * The getter of the source property.
     */
    private val sourceGet: String = when {
        kMap.thisGet == "" ->
            if (kMap.thisValue == "") {
                propertyName
            } else {
                kMap.thisValue
            }
        else -> kMap.thisGet
    }

    /**
     * The setter of the source property.
     */
    private val sourceSet: String = when {
        kMap.thisSet == "" -> if (kMap.thisValue == "") {
            propertyName
        } else  {
            kMap.thisValue
        }
        else -> kMap.thisGet
    }
    /**
     * The getter of the target property.
     */
    private val targetGet: String = when {
        kMap.value == "" -> if (kMap.othersGet == "") {
            propertyName
        } else {
            kMap.othersGet
        }
        kMap.othersGet == "" -> if (kMap.value == "") {
            propertyName
        } else {
            kMap.value
        }
        else -> propertyName
    }

    /**
     * The setter of the target property.
     */
    private val targetSet: String = when {
        kMap.value == "" -> if (kMap.othersSet == "") {
            propertyName
        } else {
            kMap.othersSet
        }
        kMap.othersSet == "" -> if (kMap.value == "") {
            propertyName
        } else {
            kMap.value
        }
        else -> propertyName
    }

    private val importPackage: String? = mapPartner()?.packageName

    /**
     * The import necessary to apply the assignment or null if no one is
     * necessary.
     */
    val sourceToTargetImport: Pair<String, String>? = importPackage?.run {
        this to "to${
            targetClass.enclosedElements.find {
                it.simpleName.toString() == targetGet
            }!!.asType()!!.run {
                types.asElement(this)?.simpleName
            }}"
    }

    val targetToSourceImport: Pair<String, String>? = mapPartner()?.run {
        packageName to "to${
            sourceClass.enclosedElements.find {
                it.simpleName.toString() == sourceGet
            }!!.asType()!!.run {
                types.asElement(this)?.simpleName
            }}"
    }

    /**
     * @return The assignment from source property to the target property
     * (target = source).
     */
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

    /**
     * @return The assignment from the target property to the source property
     * (source = target).
     */
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

    /**
     * @return True if the source class is annotated with [MapPartner] and
     * otherwise false.
     */
    private fun hasMapPartner(): Boolean =
        sourceClass.enclosedElements.find {
            it.simpleName.toString() == sourceGet
        }?.asType()?.run {
            types.asElement(this)?.getAnnotation(MapPartner::class.java) != null
        } ?: false

    /**
     * @return The [MapPartner] annotation of the source class or null if it
     * isn't annotated with it.
     */
    private fun mapPartner(): MapPartner? =
        sourceClass.enclosedElements.find {
            it.simpleName.toString() == sourceGet
        }?.asType()?.run {
            types.asElement(this)?.getAnnotation(MapPartner::class.java)
        }
}
