package com.shryne.kmap.processor.creation

import com.shryne.kmap.annotation.MapPartner

class Source(
    val packageName: String,
    val name: String,
    val properties: Collection<AnnotatedProperty>,
    val partner: MapPartner
)