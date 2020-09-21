package ru.kucegreev.neh

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.round


class MainActivity : AppCompatActivity() {
    val CAMERA_REQUEST_CODE = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }
        button2.setOnClickListener {
            val task = GetImageTask()
            val resp = JSONObject(task.execute().get())
            val bla = resp.getJSONObject("main").getDouble("temp") - 273
            textView.text = "Темпаратура в Смоленске сейчас ${round(bla)}"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {

                if (resultCode == Activity.RESULT_OK && data != null) {
                    imageView.setImageBitmap(data.extras?.get("data") as Bitmap)
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT)
            }
        }
    }

    @Suppress("DEPRECATION")
    class GetImageTask() : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String? {
            val str =
                "http://api.openweathermap.org/data/2.5/weather?q=Smolensk,ru&APPID=ebb2b252ab81cb6fa6010f60f4c5bf0e"
            val con: HttpURLConnection = URL(str).openConnection() as HttpURLConnection
            val `in` = BufferedReader(InputStreamReader(con.inputStream))
            val buf = StringBuffer()
            `in`.forEachLine { buf.append(it) }
            return buf.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }
    }
}

