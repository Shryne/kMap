package basic.internal_in_target

public fun Client.toUser(): User =
    User().also {
        it.age.value = age
    }