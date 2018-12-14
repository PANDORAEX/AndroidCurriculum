package com.ines.myproject2

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Status : RealmObject() {
    @PrimaryKey
    var STATUS_CODE: String = ""
    var STATUS_NAME: String = ""
}