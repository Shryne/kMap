package basic.one_property_same_name

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(Value::class, packageName = "basic.one_property_same_name")
class Scalar {
    @KMap
    var x: Int = 15
}