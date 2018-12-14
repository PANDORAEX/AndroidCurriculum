package com.ines.myproject2

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Project : RealmObject() {
    @PrimaryKey
    var PROJECT_ID: Long = 0
    var START_DATE: Date = Date()
    var PROJECT_NO: String = ""
    var PROJECT_NAME: String = ""
    var PROTYPE_CODE: String = ""
    var LANGUAGE_CODE: String = ""
    var SUMMARY: String = ""
    var STATUS_CODE: String = ""
    var CUSTOMER: String? = ""
    var CHARGE: String? = ""
    var REVIEWER: String? = ""
    var RELEASE_DATE: Date? = null
    var REMARKS: String? = ""
}