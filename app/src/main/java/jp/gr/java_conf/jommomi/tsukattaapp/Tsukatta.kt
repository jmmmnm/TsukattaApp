package jp.gr.java_conf.jommomi.tsukattaapp

import android.graphics.Bitmap
import java.io.Serializable
import java.util.Date
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Tsukatta : RealmObject(),Serializable {
    var date: Date = Date()     //日時
    var day:String = ""
    var payment: String = ""    //支払い
    var price: Int = 0          //値段
    var image: ByteArray? = null//画像

    // id をプライマリーキーとして設定
    @PrimaryKey
    var id: Int = 0
}