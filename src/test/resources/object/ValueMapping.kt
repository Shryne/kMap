package `object`

public fun Value.toScalar(): Scalar =
    Scalar().also {
        it.x = x
    }