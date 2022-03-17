package `object`

public fun Scalar.toValue(): Value =
    Value().also {
        it.x = x
    }