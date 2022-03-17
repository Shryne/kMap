package `object`.different_target_package

public fun Value.toScalar(): Scalar =
    Scalar().also {
        it.x = x
    }