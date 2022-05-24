package com.shryne.kmap.processor

import com.shryne.kmap.annotation.KMap
import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.processor.creation.InformedKMap
import com.shryne.kmap.processor.creation.MapFile
import com.shryne.kmap.processor.creation.Source
import com.squareup.kotlinpoet.asClassName
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.tools.Diagnostic

/**
 * Processor for [MapPartner].
 */
@SupportedSourceVersion(SourceVersion.RELEASE_15)
@SupportedAnnotationTypes("com.shryne.kmap.annotation.MapPartner")
class MapPartnerProcessor : AbstractProcessor() {
    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        roundEnv.getElementsAnnotatedWith(MapPartner::class.java)
            .forEach { source ->
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.NOTE,
                    "With MapPartner annotated element is: $source."
                )
                if (source is TypeElement) {
                    val mapPartner = source.getAnnotation(
                        MapPartner::class.java
                    )
                    try {
                        mapPartner.value
                        // Getting class values is a little complicated,
                        //  because at this point they don't exist. They are to be
                        //  compiled. Forcing a MirroredTypeException is a trick
                        //  to work with it. See https://area-51.blog/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor/
                    } catch (mte: MirroredTypeException) {
                        val target = processingEnv.typeUtils.asElement(
                            mte.typeMirror
                        ) as TypeElement

                        processingEnv.messager.printMessage(
                            Diagnostic.Kind.NOTE,
                            "Map partner of $source is $target."
                        )
                        processingEnv.messager.printMessage(
                            Diagnostic.Kind.NOTE,
                            "Source type is: ${source.simpleName}."
                        )
                        processingEnv.messager.printMessage(
                            Diagnostic.Kind.NOTE,
                            "Target type is: ${target.simpleName}"
                        )

                        val kMaps = source.enclosedElements.filter {
                            it.getAnnotation(KMap::class.java) != null
                        }.map {
                            InformedKMap(
                                it,
                                source,
                                target,
                                processingEnv.typeUtils
                            )
                        }

                        MapFile(
                            source,
                            target,
                            mapPartner,
                            kMaps.map { it.sourceToTargetAssignment() },
                            kMaps.filter { it.sourceToTargetImport != null }
                                .map { it.sourceToTargetImport!! }
                        ).writeTo(processingEnv.filer)

                        MapFile(
                            target,
                            source,
                            mapPartner,
                            kMaps.map { it.targetToSourceAssignment() },
                            kMaps.filter { it.targetToSourceImport != null }
                                .map { it.targetToSourceImport!! }
                        ).writeTo(processingEnv.filer)
                    }
                }
            }
        return false
    }

}