package com.shryne.kmap.processor.creation

import com.shryne.kmap.annotation.KMap
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asClassName
import javax.lang.model.element.TypeElement

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
class MapFunction(
    private val source: TypeElement,
    private val target: TypeElement,
    private val statements: Iterable<String>,
) {
    private val returnType: String = if (source.simpleName.toString() == target.simpleName.toString()) {
        target.asClassName().toString()
        target.simpleName.toString()
    } else {
        target.simpleName.toString()
    }

    fun asFun(): FunSpec =
        FunSpec.builder("to${target.simpleName}")
            .receiver(source.asClassName())
            .returns(target.asClassName())
            .beginControlFlow("return %T().also", target.asClassName())
            .apply {
                statements.forEach(::addStatement)
            }
            .endControlFlow()
            .build()
}