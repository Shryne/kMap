package basic.multiple_properties_same_name

import com.shryne.kmap.annotation.MapPartner
import com.shryne.kmap.annotation.KMap

@MapPartner(User::class, packageName = "basic.multiple_properties_same_name")
class Client {
    @KMap
    var age: Int = 0

    @KMap
    var name: String = ""

    @KMap
    var hobbies: List<String> = listOf()
}