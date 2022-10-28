package com.example.rcn

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_rcn.*
import kotlinx.android.synthetic.main.activity_rcn.view.*

class RcnActivity : AppCompatActivity() {

    private val TAG: String = "PIP_TAG"

    private var videoUri: Uri? = null

    private var pictureInPictureParamsBuilder: PictureInPictureParams.Builder? = null

    private var actionBar: ActionBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rcn)

        actionBar = supportActionBar
        setVideoView(intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPictureParamsBuilder = PictureInPictureParams.Builder()
        }
        pipButton.setOnClickListener {
        pictureInPictureMode()
        }
    }

    private fun setVideoView(intent: Intent?) {
        val videoUrl = intent!!.getStringExtra("VideoUrl")
        Log.d(TAG, "setVideoView: $videoUrl")

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)

        videoUri = Uri.parse(videoUrl)

        videoView.setMediaController(mediaController)

        videoView.setVideoURI(videoUri)

        videoView.setOnPreparedListener { mp ->
            Log.d(TAG, "setVideoView: Video prepared, playing...")
            mp.start()
        }
    }

    private fun pictureInPictureMode() {
        Log.d(TAG, "pictureInPictureMode: try to enter in pip mode")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "pictureInPictureMode: Supports PIP")

            val aspectRatio = Rational(videoView.width, videoView.height)
            pictureInPictureParamsBuilder!!.setAspectRatio(aspectRatio).build()
            enterPictureInPictureMode((pictureInPictureParamsBuilder!!.build()))
        } else {
            Log.d(TAG, "pictureInPictureMode:  Doesn't support PIP")
            Toast.makeText(this, "Tu dispositivo no soporta Picture in Picture", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "onUserLeaveHint: was not in PIP")
            pictureInPictureMode()
        }else{
            Log.d(TAG, "onUserLeaveHint: already in PIP")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean,
                                               newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)

        if(isInPictureInPictureMode){
            Log.d(TAG, "onPictureInPictureModeChanged: Entered PIP")

            pipButton.visibility = View.GONE
            actionBar!!.hide()
        }else {
            Log.d(TAG, "onUserLeaveHint: exit PIP")
            pipButton.visibility = View.VISIBLE
            actionBar!!.show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Log.d(TAG, "onNewIntent: Play new Video")
        setVideoView(intent)
    }

    override fun onStop() {
        super.onStop()
        if(videoView.isPlaying){
            videoView.stopPlayback()
        }
    }
}