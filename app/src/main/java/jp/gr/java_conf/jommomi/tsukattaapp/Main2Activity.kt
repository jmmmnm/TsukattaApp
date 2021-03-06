package jp.gr.java_conf.jommomi.tsukattaapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.*

class Main2Activity : AppCompatActivity() {
    private lateinit var mRealm: Realm
    private val mRealmListener = object : RealmChangeListener<Realm> {
        override fun onChange(element: Realm) {
            reloadListView()
        }
    }
    private  lateinit var mTsukattaAdaper: TsukattaAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        // Realmの設定
        mRealm = Realm.getDefaultInstance()
        mRealm.addChangeListener(mRealmListener)

        // ListViewの設定
        mTsukattaAdaper = TsukattaAdapter(this@Main2Activity)

        // ListViewをタップしたときの処理
        listView2.setOnItemClickListener { parent, _, position, _ ->
            // 入力・編集する画面に遷移させる
            val tsukatta = parent.adapter.getItem(position) as Tsukatta
            val intent = Intent(this@Main2Activity, InputActivity::class.java)
            intent.putExtra(EXTRA_TSUKATTA, tsukatta.id)
            startActivity(intent)
        }

        // ListViewを長押ししたときの処理
        listView2.setOnItemLongClickListener { parent, _, position, _ ->
            // タスクを削除する
            val tsukatta = parent.adapter.getItem(position) as Tsukatta

            // ダイアログを表示する
            val builder = AlertDialog.Builder(this@Main2Activity)

            builder.setTitle("削除")
            builder.setMessage(tsukatta.payment + "を削除しますか")

            builder.setPositiveButton("OK"){_, _ ->
                val results = mRealm.where(Tsukatta::class.java).equalTo("id", tsukatta.id).findAll()

                mRealm.beginTransaction()
                results.deleteAllFromRealm()
                mRealm.commitTransaction()

                reloadListView()
            }

            builder.setNegativeButton("CANCEL", null)

            val dialog = builder.create()
            dialog.show()

            true
        }
        reloadListView()
    }

    private fun reloadListView() {
        val intent = intent

        val value1 = intent.getStringExtra("value1")
        val value2 = intent.getStringExtra("value2")
        val value3 = intent.getStringExtra("value3")

        Log.d("kotlintest", "v1 + v2 = " + value1 +"  "+ value2)

        val tsukattaRealmResults = mRealm.where(Tsukatta::class.java).equalTo(value2, value3).findAll().sort("date", Sort.DESCENDING)

        // 上記の結果を、TsukattaList としてセットする
        mTsukattaAdaper.tsukattaList = mRealm.copyFromRealm(tsukattaRealmResults)

        // TsukattaのListView用のアダプタに渡す
        listView2.adapter = mTsukattaAdaper

        // 表示を更新するために、アダプターにデータが変更されたことを知らせる
        mTsukattaAdaper.notifyDataSetChanged()



        var tempTsukattaRealmResults = mRealm.where(Tsukatta::class.java).equalTo(value2, value3).findAll()
        var tempTsukattaArray: Array<Tsukatta>?
        tempTsukattaArray = tempTsukattaRealmResults.toTypedArray()

        var tempTsukatta:Int=0
        for(i in tempTsukattaArray.indices) {
            tempTsukatta = tempTsukatta + tempTsukattaArray[i].price
        }
        supportActionBar?.title = value1+"は "+tempTsukatta + "円つかった"
    }




}
