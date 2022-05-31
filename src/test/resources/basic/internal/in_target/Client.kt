package basic.internal.in_target

import basic.internal.in_target.User
import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(User::class, packageName = "basic.internal.in_target")
class Client {
    @KMap("age.value")
    var age: Int = 0
}