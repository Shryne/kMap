package different_accessor.other_get_set_java

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(
    User::class,
    packageName = "different_accessor.other_get_set_java"
)
class Client {
    @KMap(othersGet = "theAge()", othersSet = "setAge()")
    var age: Int = 0

    @KMap
    var name: String = ""
}