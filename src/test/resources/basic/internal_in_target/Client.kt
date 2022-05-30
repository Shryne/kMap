package basic.internal_in_target

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(User::class, packageName = "basic.internal_in_target")
class Client {
    @KMap("age.value")
    var age: Int = 0
}