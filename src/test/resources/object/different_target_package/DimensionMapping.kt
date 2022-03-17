package `object`.different_target_package

public fun Dimension.toSize(): Size =
    Size().also {
        it.w = width.toScalar()
        it.h = height.toScalar()
    }