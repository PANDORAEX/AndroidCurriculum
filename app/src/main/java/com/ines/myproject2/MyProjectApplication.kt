package com.ines.myproject2

import android.app.Application
import io.realm.Realm

class MyProjectApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}