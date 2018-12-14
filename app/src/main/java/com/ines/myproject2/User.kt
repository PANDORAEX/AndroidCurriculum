package com.ines.myproject2

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User : RealmObject() {
    @PrimaryKey
    var USERID: String = ""
    var PASSWORD: String = ""
}