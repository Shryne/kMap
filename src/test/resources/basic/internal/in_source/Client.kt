package basic.internal.in_source

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(User::class, packageName = "basic.internal.in_source")
class Client {
    @KMap(thisValue = "age.value")
    var age = Wrapper()
}