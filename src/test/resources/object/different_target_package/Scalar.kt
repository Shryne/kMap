package `object`.different_target_package

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(value = Value::class)
class Scalar {
    @KMap
    var x: Int = 15
}