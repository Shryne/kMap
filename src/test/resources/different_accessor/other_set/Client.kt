package different_accessor.other_set

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(
    User::class,
    packageName = "different_accessor.other_set"
)
class Client {
    @KMap(othersSet = "theAge()")
    var age: Int = 0

    @KMap
    var name: String = ""
}