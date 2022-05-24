package different_accessor.other_get_set

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(
    User::class,
    packageName = "different_accessor.other_get_set"
)
class Client {
    @KMap(othersGet = "theAge()")
    var age: Int = 0

    @KMap
    var name: String = ""
}