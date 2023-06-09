package ru.startandroid.develop.cameraintent

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import java.io.File

class MainActivity : AppCompatActivity() {
    private var directory: File? = null
    private val TYPE_PHOTO = 1
    private val TYPE_VIDEO = 2
    private val REQUEST_CODE_PHOTO = 1
    private val REQUEST_CODE_VIDEO = 2
    private val TAG = "myLogs"
    private var ivPhoto: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createDirectory()
        ivPhoto = findViewById<View>(R.id.ivPhoto) as ImageView
    }

    fun onClickPhoto(view: View?) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri(TYPE_PHOTO))
        startActivityForResult(intent, REQUEST_CODE_PHOTO)
    }

    fun onClickVideo(view: View?) {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri(TYPE_VIDEO))
        startActivityForResult(intent, REQUEST_CODE_VIDEO)
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        intent: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_CODE_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (intent == null) {
                    Log.d(TAG, "Intent is null")
                } else {
                    Log.d(TAG, "Photo uri: " + intent.data)
                    val bndl = intent.extras
                    if (bndl != null) {
                        val obj = intent.extras!!["data"]
                        if (obj is Bitmap) {
                            val bitmap = obj
                            Log.d(TAG, "bitmap " + bitmap.width + " x "
                                    + bitmap.height)
                            ivPhoto!!.setImageBitmap(bitmap)
                        }
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Canceled")
            }
        }
        if (requestCode == REQUEST_CODE_VIDEO) {
            if (resultCode == RESULT_OK) {
                if (intent == null) {
                    Log.d(TAG, "Intent is null")
                } else {
                    Log.d(TAG, "Video uri: " + intent.data)
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Canceled")
            }
        }
    }

    private fun generateFileUri(type: Int): Uri {
        var file: File? = null
        when (type) {
            TYPE_PHOTO -> file = File(directory!!.getPath() + "/" + "photo_"
                    + System.currentTimeMillis() + ".jpg")
            TYPE_VIDEO -> file = File(directory!!.getPath() + "/" + "video_"
                    + System.currentTimeMillis() + ".mp4")
        }
        Log.d(TAG, "fileName = $file")
        return Uri.fromFile(file)
    }

    private fun createDirectory() {
        directory = File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "MyFolder")
        if (!directory!!.exists()) directory!!.mkdirs()
    }
}