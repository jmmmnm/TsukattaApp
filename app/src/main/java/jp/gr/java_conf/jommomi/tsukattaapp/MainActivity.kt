package jp.gr.java_conf.jommomi.tsukattaapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import io.realm.RealmChangeListener
import io.realm.Sort
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import kotlinx.android.synthetic.main.content_input.*
import java.util.*

const val EXTRA_TSUKATTA = "jp.gr.java_conf.jommomi.tsukattaapp.TSUKATTA"

class MainActivity : AppCompatActivity() {
    private lateinit var mRealm: Realm
    private val mRealmListener = object : RealmChangeListener<Realm> {
        override fun onChange(element: Realm) {
            reloadListView()
        }
    }
    private  lateinit var mTsukattaAdaper: TsukattaAdapter


    var calendar = Calendar.getInstance()
    var mYear = calendar.get(Calendar.YEAR)
    var mMonth = calendar.get(Calendar.MONTH)
    var mDate = calendar.get(Calendar.DAY_OF_MONTH)

    var mDays:String =mYear.toString() + "-" + String.format("%02d", mMonth + 1) + "-" + String.format("%02d", mDate)
    var mWeeks:String = mYear.toString()+ "-" + calendar.get(Calendar.WEEK_OF_YEAR).toString()
    var mMonths:String = mYear.toString()+ "-" +calendar.get(Calendar.MONTH).toString()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, InputActivity::class.java)
            startActivity(intent)
        }
        //
        kino.setOnClickListener{ view ->
            val intent = Intent(this@MainActivity, Main2Activity::class.java)
            mDays = mYear.toString() + "-" + String.format("%02d", mMonth + 1) + "-" + String.format("%02d", mDate-1)
            intent.type = "text/plain"
            intent.putExtra("value1", "days")
            intent.putExtra("value2",mDays)
            startActivity(intent)
        }
        ototoi.setOnClickListener{ view ->
            val intent = Intent(this@MainActivity, Main2Activity::class.java)
            mDays = mYear.toString() + "-" + String.format("%02d", mMonth + 1) + "-" + String.format("%02d", mDate-2)
            intent.type = "text/plain"
            intent.putExtra("value1", "days")
            intent.putExtra("value2",mDays)
            startActivity(intent)
        }
        senshu.setOnClickListener{ view ->
            val intent = Intent(this@MainActivity, Main2Activity::class.java)
            mWeeks= mYear.toString()+ "-" + (calendar.get(Calendar.WEEK_OF_YEAR)-1).toString()
            intent.type = "text/plain"
            intent.putExtra("value1", "weeks")
            intent.putExtra("value2",mWeeks)
            startActivity(intent)
        }
        sensenshu.setOnClickListener{ view ->
            val intent = Intent(this@MainActivity, Main2Activity::class.java)
            mWeeks= mYear.toString()+ "-" + (calendar.get(Calendar.WEEK_OF_YEAR)-2).toString()
            intent.type = "text/plain"
            intent.putExtra("value1", "weeks")
            intent.putExtra("value2",mWeeks)
            startActivity(intent)
        }
        kongetsu.setOnClickListener{ view ->
            val intent = Intent(this@MainActivity, Main2Activity::class.java)
            mMonths = mYear.toString()+ "-" + (calendar.get(Calendar.MONTH)+1).toString()
            intent.type = "text/plain"
            intent.putExtra("value1", "months")
            intent.putExtra("value2",mMonths)
            startActivity(intent)
        }
        sengetsu.setOnClickListener{ view ->
            val intent = Intent(this@MainActivity, Main2Activity::class.java)
            mMonths = mYear.toString()+ "-" +calendar.get(Calendar.MONTH).toString()
            intent.type = "text/plain"
            intent.putExtra("value1", "months")
            intent.putExtra("value2",mMonths)
            startActivity(intent)
        }

        // Realmの設定
        mRealm = Realm.getDefaultInstance()
        mRealm.addChangeListener(mRealmListener)

        // ListViewの設定
        mTsukattaAdaper = TsukattaAdapter(this@MainActivity)

        // ListViewをタップしたときの処理
        listView1.setOnItemClickListener { parent, _, position, _ ->
            // 入力・編集する画面に遷移させる
            val tsukatta = parent.adapter.getItem(position) as Tsukatta
            val intent = Intent(this@MainActivity, InputActivity::class.java)
            intent.putExtra(EXTRA_TSUKATTA, tsukatta.id)
            startActivity(intent)
        }

        // ListViewを長押ししたときの処理
        listView1.setOnItemLongClickListener { parent, _, position, _ ->
            // タスクを削除する
            val tsukatta = parent.adapter.getItem(position) as Tsukatta

            // ダイアログを表示する
            val builder = AlertDialog.Builder(this@MainActivity)

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
        reTitle()
    }


    override fun onRestart() {
        super.onRestart()
        reTitle()
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

    private fun reTitle() {

        var kyoTsukattaRealmResults = mRealm.where(Tsukatta::class.java).equalTo("days", mDays).findAll()
        var kyoTsukattaArray: Array<Tsukatta>?
        kyoTsukattaArray = kyoTsukattaRealmResults.toTypedArray()
        var kyoTsukatta:Int=0

        for(i in kyoTsukattaArray.indices){
            kyoTsukatta=kyoTsukatta+kyoTsukattaArray[i].price
        }
        supportActionBar?.title = "今日は "+kyoTsukatta+"円つかった"
    }

    override fun onDestroy() {
        super.onDestroy()

        mRealm.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}