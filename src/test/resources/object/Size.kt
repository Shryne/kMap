package `object`

import com.shryne.kmap.annotation.KMap
import com.shryne.kmap.annotation.MapPartner

@MapPartner(Dimension::class)
class Size {
    @KMap("width")
    var w = Scalar()

    @KMap("height")
    var h = Scalar()
}