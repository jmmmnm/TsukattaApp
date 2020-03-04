package jp.gr.java_conf.jommomi.tsukattaapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import io.realm.RealmChangeListener
import io.realm.Sort
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mRealm: Realm
    private val mRealmListener = object : RealmChangeListener<Realm> {
        override fun onChange(element: Realm) {
            reloadListView()
        }
    }

    private  lateinit var mTsukattaAdaper: TsukattaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        // Realmの設定
        mRealm = Realm.getDefaultInstance()
        mRealm.addChangeListener(mRealmListener)

        // ListViewの設定
        mTsukattaAdaper = TsukattaAdapter(this@MainActivity)

        // ListViewをタップしたときの処理
        listView1.setOnItemClickListener { parent, view, posiiton, id ->
            //入力・編集する部面に遷移させる
        }

        // ListViewを長押ししたときの処理
        listView1.setOnItemLongClickListener { parent, view, position, id ->
            // タスクを削除する
            true
        }

        // アプリ起動時に表示テスト用のタスクを作成する
        addTsukattaForTest()

        reloadListView()
    }

    private fun reloadListView() {
        // Realmデータベースから、「全てのデータを取得して新しい日時順に並べた結果」を取得
        val tsukattaRealmResults = mRealm.where(Tsukatta::class.java).findAll().sort("date", Sort.DESCENDING)

        // 上記の結果を、TsukattaList としてセットする
        mTsukattaAdaper.tsukattaList = mRealm.copyFromRealm(tsukattaRealmResults)

        // TsukattaのListView用のアダプタに渡す
        listView1.adapter = mTsukattaAdaper

        // 表示を更新するために、アダプターにデータが変更されたことを知らせる
        mTsukattaAdaper.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()

        mRealm.close()
    }

    private fun addTsukattaForTest() {
        val tsukatta = Tsukatta()
        tsukatta.payment = "支払い方法"
        tsukatta.price = 0
        tsukatta.date = Date()
        tsukatta.image = null
        tsukatta.id = 0
        mRealm.beginTransaction()
        mRealm.copyToRealmOrUpdate(tsukatta)
        mRealm.commitTransaction()
    }
}