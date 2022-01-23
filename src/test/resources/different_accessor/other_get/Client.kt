package different_accessor.other_get

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(
    User::class,
    packageName = "different_accessor.other_get"
)
class Client {
    @KMap(othersGet = "theAge()", othersSet = "theAge()")
    var age: Int = 0

    @KMap
    var name: String = ""
}