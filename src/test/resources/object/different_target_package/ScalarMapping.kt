package `object`.different_target_package

public fun Scalar.toValue(): Value =
    Value().also {
        it.x = x
    }