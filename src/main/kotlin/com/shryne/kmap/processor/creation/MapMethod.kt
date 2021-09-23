package com.shryne.kmap.processor.creation

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeName

/**
 * The method that is going to do the mapping. It will look similar to this:
 * ```
 * fun <source>.to<target>(): <target> =
 *      <target>().also {
 *          it.<mappings.first> = it.<mappings.second>
 *          ...
 *      }
 * ```
 * @param source
 */
class MapMethod(
    private val source: TypeName,
    private val target: TypeName,
    private val mappings: Iterable<Pair<String, String>>
) {
    fun asFun(): FunSpec =
        FunSpec.builder("to${target}")
            .receiver(source)
            .returns(target)
            .beginControlFlow("return $target().also")
            .apply {
                mappings.forEach {
                    addStatement("it.${it.first} = ${it.second}")
                }
            }
            .endControlFlow()
            .build()
}