package inner_mapping.both

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(value = Value::class, packageName = "inner_mapping.target")
class Scalar {
    @KMap
    var x = Wrapper()
}