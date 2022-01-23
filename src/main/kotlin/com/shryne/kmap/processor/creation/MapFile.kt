package com.shryne.kmap.processor.creation

import com.shryne.kmap.annotation.MapPartner
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeName
import javax.annotation.processing.Filer
import javax.lang.model.element.TypeElement

/**
 * The file that will contain the mapping methods. Example (pseudocode):
 * ```
 * MapFile("Person", "Teacher", [("name", "firstName")]) =>
 *
 * // PersonMapping.kt
 * fun Person.toTeacher() =
 *      Teacher().also {
 *          it.firstName = name
 *      }
 * ```
 * @param source The type that is going to be mapped to the target.
 * @param target The target of the mapping.
 * @param mappings The names of the properties that will be mapped. [Pair.first]
 *  needs to contain the property of source and [Pair.second] the property of
 *  target.
 */
class MapFile(
    private val source: TypeElement,
    private val target: TypeElement,
    private val mapPartner: MapPartner,
    private val statements: Iterable<String>
) {
    fun writeTo(filer: Filer) {
        // TODO: What if two classes have the same name and the default package is
        //  used? => Collision
        FileSpec.builder(mapPartner.packageName, "${source.simpleName}Mapping")
            .addFunction(
            MapMethod(source, target, statements).asFun()
        ).build().apply {
            writeTo(System.out)
            writeTo(filer)
        }
    }
}