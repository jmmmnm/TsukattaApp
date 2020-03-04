package jp.gr.java_conf.jommomi.tsukattaapp

import android.app.Application
import io.realm.Realm

class TsukattaApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}