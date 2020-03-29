package jp.gr.java_conf.jommomi.tsukattaapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class TsukattaAdapter(context: Context) : BaseAdapter() {
    private val mLayoutInflater: LayoutInflater
    var tsukattaList = mutableListOf<Tsukatta>()

    init{
        this.mLayoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return tsukattaList.size
    }

    override fun getItem(position: Int): Any {
        return tsukattaList[position]
    }

    override fun getItemId(position: Int): Long {
        return tsukattaList[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_tsukatta, parent, false)
        }

        val dateTextView = convertView!!.findViewById<View>(R.id.dateTextView) as TextView
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE)
        val date = tsukattaList[position].date
        dateTextView.text = simpleDateFormat.format(date)

        val paymentTextView = convertView.findViewById<View>(R.id.paymentTextView) as TextView
        paymentTextView.text = tsukattaList[position].payment

        val priceTextView = convertView.findViewById<View>(R.id.priceTextView) as TextView
        priceTextView.text = tsukattaList[position].price.toString()+" 円"

        val bytes = tsukattaList[position].image
        if (bytes != null && bytes!!.isNotEmpty()) {
            val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size).copy(Bitmap.Config.ARGB_8888, true)
            val imageView = convertView.findViewById<View>(R.id.imageView) as ImageView
            imageView.setImageBitmap(image)
        }

        val commentTextView = convertView.findViewById<View>(R.id.commentTextView) as TextView
        commentTextView.text = tsukattaList[position].comment

        val monthsTextView = convertView.findViewById<View>(R.id.monthsTextView) as TextView
        monthsTextView.text = tsukattaList[position].months

        val weeksTextView = convertView.findViewById<View>(R.id.weeksTextView) as TextView
        weeksTextView.text = tsukattaList[position].weeks


        return convertView

    }

}