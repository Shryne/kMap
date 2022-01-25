package `object`

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(value = Value::class, packageName = "object")
class Scalar {
    @KMap
    var x: Int = 15
}