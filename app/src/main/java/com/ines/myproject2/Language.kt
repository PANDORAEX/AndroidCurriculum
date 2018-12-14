package com.ines.myproject2

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Language : RealmObject() {
    @PrimaryKey
    var LANGUAGE_CODE: String = ""
    var LANGUAGE_NAME: String = ""
}