package `object`

public fun Dimension.toSize(): Size =
    Size().also {
        it.w = width.toScalar()
        it.h = height.toScalar()
    }