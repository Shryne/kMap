package basic.accessor_based

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(value = Value::class, packageName = "basic.accessor_based")
class Scalar {
    @KMap
    var x: Int = 15
}