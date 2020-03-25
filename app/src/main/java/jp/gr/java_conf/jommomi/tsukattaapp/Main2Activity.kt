package jp.gr.java_conf.jommomi.tsukattaapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val intent = intent

        val value1 = intent.getStringExtra(EXTRA_TSUKATTA)

        Log.d("kotlintest","TSUKATTA = "+value1)
    }
}
