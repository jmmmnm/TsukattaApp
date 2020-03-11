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


    private val payList = arrayOf("cash","id","pasmo","","famipay","paypay","","d_card","gold_point_card+","ing_fanV_card")
    private var payStr:String = ""
    private var priStr:String = ""

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
        buttonHyouji()

        button_date.setOnClickListener(mOnDateClickListener)
        button_times.setOnClickListener(mOnTimeClickListener)


        button_1.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}1"
            priStr += "1"
        }
        button_2.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}2"
            priStr += "2"
        }
        button_3.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}3"
            priStr += "3"
        }
        button_4.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}4"
            priStr += "4"
        }
        button_5.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}5"
            priStr += "5"
        }
        button_6.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}6"
            priStr += "6"
        }
        button_7.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}7"
            priStr += "7"
        }
        button_8.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}8"
            priStr += "8"
        }
        button_9.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}9"
            priStr += "9"
        }

        button_add.setOnClickListener(){
            addTsukatta()
            finish()
        }
        button_10.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}0"
            priStr += "0"
        }
        button_del.setOnClickListener(){
            payStr=""
            payment_text_view.text =""
            priStr=""
            price_text_view.text=""
            buttonHyouji()
        }


        button_11.setOnClickListener(){
            payment_text_view.text = payList[1]
            payStr = payList[1]
            buttonHyouji()
        }
        button_12.setOnClickListener(){
            payment_text_view.text = payList[2]
            payStr = payList[2]
            buttonHyouji()
        }
        button_13.setOnClickListener(){
            payment_text_view.text = payList[3]
            payStr = payList[3]
            buttonHyouji()
        }
        button_14.setOnClickListener(){
            payment_text_view.text = payList[4]
            payStr = payList[4]
            buttonHyouji()
        }
        button_15.setOnClickListener(){
            payment_text_view.text = payList[5]
            payStr = payList[5]
            buttonHyouji()
        }
        button_16.setOnClickListener(){
            payment_text_view.text = payList[6]
            payStr = payList[6]
            buttonHyouji()
        }
        button_17.setOnClickListener(){
            payment_text_view.text = payList[7]
            payStr = payList[7]
            buttonHyouji()
        }
        button_18.setOnClickListener(){
            payment_text_view.text = payList[8]
            payStr = payList[8]
            buttonHyouji()
        }
        button_19.setOnClickListener() {
            payment_text_view.text = payList[9]
            payStr = payList[9]
            buttonHyouji()
        }
        button_non.setOnClickListener() {

        }
        button_20.setOnClickListener() {
            payment_text_view.text = payList[0]
            payStr =payList[0]
            buttonHyouji()
        }
        button_back.setOnClickListener() {
            finish()
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
            payment_text_view.setText(mTsukatta!!.payment)
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

        val payment = payment_text_view.text.toString()
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

    private fun buttonHyouji() {
        button_11.text = payList[1]
        button_12.text = payList[2]
        button_13.text = payList[3]
        button_14.text = payList[4]
        button_15.text = payList[5]
        button_16.text = payList[6]
        button_17.text = payList[7]
        button_18.text = payList[8]
        button_19.text = payList[9]
        button_20.text = payList[0]

        if(payStr!="") {
            button_11.setVisibility(View.GONE)
            button_12.setVisibility(View.GONE)
            button_13.setVisibility(View.GONE)
            button_14.setVisibility(View.GONE)
            button_15.setVisibility(View.GONE)
            button_16.setVisibility(View.GONE)
            button_17.setVisibility(View.GONE)
            button_18.setVisibility(View.GONE)
            button_19.setVisibility(View.GONE)
            button_non.setVisibility(View.GONE)
            button_20.setVisibility(View.GONE)
            button_back.setVisibility(View.GONE)
        }else{
            button_11.setVisibility(View.VISIBLE)
            button_12.setVisibility(View.VISIBLE)
            button_13.setVisibility(View.VISIBLE)
            button_14.setVisibility(View.VISIBLE)
            button_15.setVisibility(View.VISIBLE)
            button_16.setVisibility(View.VISIBLE)
            button_17.setVisibility(View.VISIBLE)
            button_18.setVisibility(View.VISIBLE)
            button_19.setVisibility(View.VISIBLE)
            button_non.setVisibility(View.VISIBLE)
            button_20.setVisibility(View.VISIBLE)
            button_back.setVisibility(View.VISIBLE)
        }
    }
}