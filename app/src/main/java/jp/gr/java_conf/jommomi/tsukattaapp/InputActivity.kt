package jp.gr.java_conf.jommomi.tsukattaapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import io.realm.Realm
import kotlinx.android.synthetic.main.content_input.*
import java.util.*

class InputActivity : AppCompatActivity() {

    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var mHour = 0
    private var mMinute = 0
    private var mTsukatta: Tsukatta? = null

    private val mOnDateClickListener = View.OnClickListener {
        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                mYear = year
                mMonth = month
                mDay = dayOfMonth
                val dateString = mYear.toString() + "/" + String.format("%02d", mMonth + 1) + "/" + String.format("%02d", mDay)
                button_date.text = dateString
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private val mOnTimeClickListener = View.OnClickListener {
        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                mHour = hour
                mMinute = minute
                val timeString = String.format("%02d", mHour) + ":" + String.format("%02d", mMinute)
                button_times.text = timeString
            }, mHour, mMinute, false)
        timePickerDialog.show()
    }

    private val mOnDoneClickListener = View.OnClickListener {
        addTsukatta()
        finish()
    }

    private var pStr:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        // ActionBarを設定する
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        // UI部品の設定
        button_date.setOnClickListener(mOnDateClickListener)
        button_times.setOnClickListener(mOnTimeClickListener)
        button_camera.setOnClickListener(mOnDoneClickListener)


        button_0.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}0"
            pStr += "0"
        }
        button_1.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}1"
            pStr += "1"
        }
        button_2.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}2"
            pStr += "2"
        }
        button_3.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}3"
            pStr += "3"
        }
        button_4.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}4"
            pStr += "4"
        }
        button_5.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}5"
            pStr += "5"
        }
        button_6.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}6"
            pStr += "6"
        }
        button_7.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}7"
            pStr += "7"
        }
        button_8.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}8"
            pStr += "8"
        }
        button_9.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}9"
            pStr += "9"
        }
        button_c.setOnClickListener(){
            price_text_view.text = ""
            pStr = ""
        }

        // EXTRA_TSUKATTA から Tsukatta の id を取得して、 id から Tsukatta のインスタンスを取得する
        val intent = intent
        val tsukattaId = intent.getIntExtra(EXTRA_TSUKATTA, -1)
        val realm = Realm.getDefaultInstance()
        mTsukatta = realm.where(Tsukatta::class.java).equalTo("id", tsukattaId).findFirst()
        realm.close()

        if (mTsukatta == null) {
            // 新規作成の場合
            val calendar = Calendar.getInstance()
            mYear = calendar.get(Calendar.YEAR)
            mMonth = calendar.get(Calendar.MONTH)
            mDay = calendar.get(Calendar.DAY_OF_MONTH)
            mHour = calendar.get(Calendar.HOUR_OF_DAY)
            mMinute = calendar.get(Calendar.MINUTE)

            val dateString = mYear.toString() + "/" + String.format("%02d", mMonth + 1) + "/" + String.format("%02d", mDay)
            val timeString = String.format("%02d", mHour) + ":" + String.format("%02d", mMinute)

            button_date.text = dateString
            button_times.text = timeString
        } else {
            // 更新の場合
            payment_edit_text.setText(mTsukatta!!.payment)
            price_text_view.setText(mTsukatta!!.price)

            val calendar = Calendar.getInstance()
            calendar.time = mTsukatta!!.date
            mYear = calendar.get(Calendar.YEAR)
            mMonth = calendar.get(Calendar.MONTH)
            mDay = calendar.get(Calendar.DAY_OF_MONTH)
            mHour = calendar.get(Calendar.HOUR_OF_DAY)
            mMinute = calendar.get(Calendar.MINUTE)

            val dateString = mYear.toString() + "/" + String.format("%02d", mMonth + 1) + "/" + String.format("%02d", mDay)
            val timeString = String.format("%02d", mHour) + ":" + String.format("%02d", mMinute)

            button_date.text = dateString
            button_times.text = timeString
        }
    }

    private fun addTsukatta() {
        val realm = Realm.getDefaultInstance()

        realm.beginTransaction()

        if (mTsukatta == null) {
            // 新規作成の場合
            mTsukatta = Tsukatta()

            val tsukattaRealmResults = realm.where(Tsukatta::class.java).findAll()

            val identifier: Int =
                if (tsukattaRealmResults.max("id") != null) {
                    tsukattaRealmResults.max("id")!!.toInt() + 1
                } else {
                    0
                }
            mTsukatta!!.id = identifier
        }

        val payment = payment_edit_text.text.toString()
        val price = price_text_view.text.toString()

        mTsukatta!!.payment = payment
        mTsukatta!!.price = price.toInt()
        val calendar = GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute)
        val date = calendar.time
        mTsukatta!!.date = date

        realm.copyToRealmOrUpdate(mTsukatta!!)
        realm.commitTransaction()

        realm.close()
    }
}