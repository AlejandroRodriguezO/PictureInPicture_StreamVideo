package com.example.rcn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val video_rcn = "https://stream-01.nyc.dailymotion.com/sec(0HE75RGSqqnhnSdEMl12ZSWf-nWuGd6tNfrHQk0_HUYbvknVolsy0J7xblPHK3z-)/dm/3/x7vyv0z/s/live-2.m3u8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rcn.setOnClickListener {
            playVideo(video_rcn)
        }
    }

    private fun playVideo(url:String){

        val intent = Intent()
        intent.setClass(this, RcnActivity::class.java)
        intent.putExtra("VideoUrl", url)
        startActivity(intent)
    }
}