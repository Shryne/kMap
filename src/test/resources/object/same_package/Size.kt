package `object`

import com.shryne.kmap.annotation.KMap
import com.shryne.kmap.annotation.MapPartner

@MapPartner(Dimension::class, packageName = "object")
class Size {
    @KMap("width")
    var w = Scalar()

    @KMap("height")
    var h = Scalar()
}