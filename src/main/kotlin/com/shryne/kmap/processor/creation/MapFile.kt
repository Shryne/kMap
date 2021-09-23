package com.shryne.kmap.processor.creation

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeName
import javax.annotation.processing.Filer

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
    private val source: TypeName,
    private val target: TypeName,
    private val mappings: Iterable<Pair<String, String>>
) {
    fun writeTo(filer: Filer) {
        FileSpec.builder("", "${source}Mapping")
            .addFunction(
            MapMethod(source, target, mappings).asFun()
        ).build().apply {
            writeTo(System.out)
            writeTo(filer)
        }
    }
}