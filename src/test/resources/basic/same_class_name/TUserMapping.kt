package basic.same_name

import basic.same_name.other.User

public fun basic.same_name.User.toClient(): User =
    User().also {
        it.age = age
        it.name = name
        it.hobbies = hobbies
    }