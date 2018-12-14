package com.ines.myproject2

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Projecttype : RealmObject() {
    @PrimaryKey
    var PROTYPE_CODE: String = ""
    var PROTYPE_NAME: String = ""
}