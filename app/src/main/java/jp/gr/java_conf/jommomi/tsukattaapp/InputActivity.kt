package jp.gr.java_conf.jommomi.tsukattaapp

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import io.realm.Realm
import kotlinx.android.synthetic.main.content_input.*
import kotlinx.android.synthetic.main.content_input.imageView
import kotlinx.android.synthetic.main.list_tsukatta.*
import java.io.ByteArrayOutputStream
import java.util.*

class InputActivity : AppCompatActivity() {
    companion object {
        private val PERMISSIONS_REQUEST_CODE = 100
        private val CHOOSER_REQUEST_CODE = 100
    }
    private var imageV:ByteArray?= null
    private var mPictureUri: Uri? = null

    private var mYear = 0
    private var mMonth = 0
    private var mDate = 0
    private var mHour = 0
    private var mMinute = 0
    private var mComment = ""
    private var mDays = ""
    private var mWeeks = ""
    private var mMonths = ""
    private var mTsukatta: Tsukatta? = null

    private val mOnDateClickListener = View.OnClickListener {
        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                mYear = year
                mMonth = month
                mDate = dayOfMonth
                val dateString = mYear.toString() + "-" + String.format("%02d", mMonth + 1) + "-" + String.format("%02d", mDate)
                button_date.text = dateString
            }, mYear, mMonth, mDate)
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



    private val payList = arrayOf("id","pasmo","famipay","paypay","dcard","ingfan","goldpoint","cash")
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

        buttonHyouji()

        // UI部品の設定

        button_date.setOnClickListener(mOnDateClickListener)
        button_times.setOnClickListener(mOnTimeClickListener)

        imageView.setOnClickListener(){
            // パーミッションの許可状態を確認する
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // 許可されている
                    showChooser()
                } else {
                    // 許可されていないので許可ダイアログを表示する
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
                }
            } else {
                showChooser()
            }
        }

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
        button_10.setOnClickListener(){
            price_text_view.text = "${price_text_view.text}0"
            priStr += "0"
        }
        button_11.setOnClickListener(){
            mComment = comment_text_view.text.toString()
            addTsukatta()
            finish()
        }
        button_12.setOnClickListener(){
            payStr=""
            payment_text_view.text =""
            priStr=""
            price_text_view.text=""
        }
        button_13.setOnClickListener(){
            payment_text_view.text = payList[7]
            payStr = payList[7]
        }
        button_14.setOnClickListener(){
            payment_text_view.text = payList[6]
            payStr = payList[6]
        }
        button_15.setOnClickListener(){
            payment_text_view.text = payList[5]
            payStr = payList[5]
        }
        button_16.setOnClickListener(){
            payment_text_view.text = payList[4]
            payStr = payList[4]
        }
        button_17.setOnClickListener(){
            payment_text_view.text = payList[3]
            payStr = payList[3]
        }
        button_18.setOnClickListener(){
            payment_text_view.text = payList[2]
            payStr = payList[2]
        }
        button_19.setOnClickListener() {
            payment_text_view.text = payList[1]
            payStr = payList[1]
        }
        button_20.setOnClickListener() {
            payment_text_view.text = payList[0]
            payStr = payList[0]
        }



        // EXTRA_TSUKATTA から Tsukatta の id を取得して、 id から Tsukatta のインスタンスを取得する
        val intent = intent
        val tsukattaId = intent.getIntExtra(EXTRA_TSUKATTA, -1)
        val realm = Realm.getDefaultInstance()
        mTsukatta = realm.where(Tsukatta::class.java).equalTo("id", tsukattaId).findFirst()
        realm.close()

        if (mTsukatta == null) {
            // 新規作成の場合
            supportActionBar?.title = "新規追加"
            val calendar = Calendar.getInstance()
            mYear = calendar.get(Calendar.YEAR)
            mMonth = calendar.get(Calendar.MONTH)
            mDate = calendar.get(Calendar.DAY_OF_MONTH)
            mHour = calendar.get(Calendar.HOUR_OF_DAY)
            mMinute = calendar.get(Calendar.MINUTE)

            val dateString = mYear.toString() + "-" + String.format("%02d", mMonth + 1) + "-" + String.format("%02d", mDate)
            val timeString = String.format("%02d", mHour) + ":" + String.format("%02d", mMinute)

            button_date.text = dateString
            button_times.text = timeString
        } else {
            // 更新の場合
            supportActionBar?.title = "変更"
            payment_text_view.setText(mTsukatta!!.payment)
            price_text_view.setText(mTsukatta!!.price.toString())
            comment_text_view.setText(mTsukatta!!.comment)

            val bytes = mTsukatta!!.image
            if (bytes != null && bytes!!.isNotEmpty()) {
                val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size).copy(Bitmap.Config.ARGB_8888, true)
                imageView.setImageBitmap(image)
            }

            val calendar = Calendar.getInstance()
            calendar.time = mTsukatta!!.date
            mYear = calendar.get(Calendar.YEAR)
            mMonth = calendar.get(Calendar.MONTH)
            mDate = calendar.get(Calendar.DAY_OF_MONTH)
            mHour = calendar.get(Calendar.HOUR_OF_DAY)
            mMinute = calendar.get(Calendar.MINUTE)

            val dateString = mYear.toString() + "-" + String.format("%02d", mMonth + 1) + "-" + String.format("%02d", mDate)
            val timeString = String.format("%02d", mHour) + ":" + String.format("%02d", mMinute)

            button_date.text = dateString
            button_times.text = timeString

            imageV=bytes
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
        val calendar = GregorianCalendar(mYear, mMonth, mDate, mHour, mMinute)
        val date = calendar.time
        mTsukatta!!.date = date

        mDays =mYear.toString() + "-" + String.format("%02d", mMonth + 1) + "-" + String.format("%02d", mDate)
        mWeeks = mYear.toString()+ "-" + calendar.get(Calendar.WEEK_OF_YEAR).toString()
        mMonths = mYear.toString()+ "-" + (calendar.get(Calendar.MONTH)+1).toString()

        mTsukatta!!.days = mDays
        mTsukatta!!.weeks = mWeeks
        mTsukatta!!.months = mMonths
        mTsukatta!!.comment = mComment
        mTsukatta!!.image = imageV

        realm.copyToRealmOrUpdate(mTsukatta!!)
        realm.commitTransaction()

        realm.close()
    }

    private fun showChooser() {
        // ギャラリーから選択するIntent
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)

        // カメラで撮影するIntent
        val filename = System.currentTimeMillis().toString() + ".jpg"
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        mPictureUri = contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPictureUri)

        // ギャラリー選択のIntentを与えてcreateChooserメソッドを呼ぶ
        val chooserIntent = Intent.createChooser(galleryIntent, "画像を取得")

        // EXTRA_INITIAL_INTENTS にカメラ撮影のIntentを追加
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

        startActivityForResult(chooserIntent, CHOOSER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CHOOSER_REQUEST_CODE) {

            if (resultCode != Activity.RESULT_OK) {
                if (mPictureUri != null) {
                    contentResolver.delete(mPictureUri!!, null, null)
                    mPictureUri = null
                }
                return
            }

            // 画像を取得
            val uri = if (data == null || data.data == null) mPictureUri else data.data

            // URIからBitmapを取得する
            val image: Bitmap
            try {
                val contentResolver = contentResolver
                val inputStream = contentResolver.openInputStream(uri!!)
                image = BitmapFactory.decodeStream(inputStream)
                inputStream!!.close()
            } catch (e: Exception) {
                return
            }

            // 取得したBimapの長辺を500ピクセルにリサイズする
            val imageWidth = image.width
            val imageHeight = image.height
            val scale = Math.min(120.toFloat() / imageWidth, 120.toFloat() / imageHeight) // (1)

            val matrix = Matrix()
            matrix.postScale(scale, scale)


            val resizedImage = Bitmap.createBitmap(image, 0, 0, imageWidth, imageHeight, matrix, true)

            // BitmapをImageViewに設定する
            imageView.setImageBitmap(resizedImage)

            imageV= BitmapToByte(resizedImage)

            mPictureUri = null
        }
    }

     fun BitmapToByte(bmp:Bitmap):ByteArray{
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun buttonHyouji() {
        button_13.text = payList[7]
        button_14.text = payList[6]
        button_15.text = payList[5]
        button_16.text = payList[4]
        button_17.text = payList[3]
        button_18.text = payList[2]
        button_19.text = payList[1]
        button_20.text = payList[0]
    }

}