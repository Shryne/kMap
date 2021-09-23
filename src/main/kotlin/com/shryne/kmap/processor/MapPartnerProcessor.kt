package com.shryne.kmap.processor

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.kMap
import com.shryne.kmap.processor.creation.MapFile
import com.squareup.kotlinpoet.asTypeName
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
@SupportedSourceVersion(SourceVersion.RELEASE_8)
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
                    try {
                        source.getAnnotation(
                            MapPartner::class.java
                        ).value
                        // Getting class values is a little bit complicated,
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

                        val sourceType = source.asType().asTypeName()
                        val targetType = target.asType().asTypeName()

                        val mappings = source.enclosedElements.filter {
                            it.getAnnotation(kMap::class.java) != null
                        }.map {
                            it.getAnnotation(
                                kMap::class.java
                            ).value.run {
                                if (this == "") {
                                    it.simpleName.toString()
                                } else {
                                    this
                                }
                            } to it.simpleName.toString()
                        }
                        MapFile(sourceType, targetType, mappings).writeTo(
                            processingEnv.filer
                        )
                        MapFile(targetType, sourceType, mappings).writeTo(
                            processingEnv.filer
                        )
                    }
                }
            }
        return false
    }

}