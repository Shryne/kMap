package constructor.same_name

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(value = Value::class, packageName = "basic.constructor")
class Scalar(@KMap var x: Int)