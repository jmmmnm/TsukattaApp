package jp.gr.java_conf.jommomi.tsukattaapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
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
        val view: View = convertView ?: mLayoutInflater.inflate(android.R.layout.simple_list_item_2, null)

        val textView1 = view.findViewById<TextView>(android.R.id.text1)
        val textView2 = view.findViewById<TextView>(android.R.id.text2)

        textView1.text = tsukattaList[position].price.toString()
        
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE)
        val date = tsukattaList[position].date
        textView2.text = simpleDateFormat.format(date)

        return view
    }

}