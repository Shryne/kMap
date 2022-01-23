package basic.java_partner

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(Value::class, packageName = "basic.java_partner")
class Scalar {
    @KMap
    var x: Int = 15
}