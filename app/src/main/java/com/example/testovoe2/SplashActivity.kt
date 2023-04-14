package com.example.testovoe2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.usb.UsbDevice.getDeviceId
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.onesignal.OneSignal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.util.*
import java.net.HttpURLConnection

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SERVER_URL = "http://135.181.248.237/splash.php"
    }

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView
    private lateinit var imageViewFon1: ImageView

    private lateinit var whatDo: String
    private lateinit var answer: String
    private var progressStatus = 0
    private val handler = Handler(Looper.getMainLooper())

    private var isActivityDestroyed = false

    private lateinit var uniqueId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.textView)
        imageViewFon1 = findViewById(R.id.imageViewFon1)

        progressBar.max = 100
        textView.text = "0"

        Glide.with(this)
            .load("http://135.181.248.237/2/fon1.png")
            .into(imageViewFon1)

        Glide.with(this)
            .load("http://135.181.248.237/2/fon2.png")
            .preload()
        Glide.with(this)
            .load("http://135.181.248.237/2/fon5.png")
            .preload()

        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

        uniqueId = sharedPreferences.getString("uniqueId", "") ?: ""

        if (uniqueId.isEmpty()) {
            uniqueId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString("uniqueId", uniqueId).apply()
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val phoneName = getPhoneName()
                val locale = Locale.getDefault().language

                val response = URL("$SERVER_URL?phone_name=$phoneName&locale=$locale&unique=$uniqueId").readText()
                withContext(Dispatchers.Main) {
                    if (response.isNotEmpty()) {
                        val jsonResponse = JSONObject(response)
                        answer = jsonResponse.getString("url")
                        Log.d("AAAA",answer)
                        when (answer) {
                            "no" -> {
                                OneSignal.disablePush(false)
                                whatDo = "main"
                            }
                            "nopush" -> {
                                OneSignal.disablePush(true)
                                whatDo = "main"
                            }
                            else -> {
                                whatDo = "web"
                            }
                        }
                    }
                    else {
                        whatDo = ""
                        val builder = AlertDialog.Builder(getApplicationContext())
                        builder.setTitle("Check Your Internet Connection")
                        builder.setMessage("Please make sure your device is connected to the internet.")
                        builder.setPositiveButton("OK") { dialog, which ->
                            recreate()
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }
                }


            } catch (e: Exception) {
                whatDo = ""
            }
        }

        Handler().postDelayed({
            progressBar.visibility = View.GONE
            textView.visibility = View.GONE

            val isFirstStart = sharedPreferences.getBoolean("is_first_start", true)

            if (!isActivityDestroyed) {
                when(whatDo) {
                    "main" -> {
                        if (isFirstStart) {
                            val intent = Intent(this, RegistrationActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(this, MenuActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    "web" -> {
                        val intent = Intent(this, WebViewActivity::class.java)
                        intent.putExtra("url", answer)
                        startActivity(intent)
                        finish()
                    }
                    else -> {
                        val builder = AlertDialog.Builder(this,R.style.Theme_Testovoe2)
                        builder.setTitle("Check Your Internet Connection")
                        builder.setMessage("Please make sure your device is connected to the internet.")
                        builder.setPositiveButton("OK") { dialog, which ->
                            recreate()
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }
                }
            }
        }, 3000)

        Thread(Runnable {
            while (progressStatus < 100) {
                progressStatus += 1
                handler.post {
                    progressBar.progress = progressStatus
                    textView.text = "$progressStatus"
                }
                try {
                    Thread.sleep(30)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        isActivityDestroyed = true
    }

    private fun getPhoneName(): String {
        val manufacturer = android.os.Build.MANUFACTURER
        val model = android.os.Build.MODEL
        return "$manufacturer $model"
    }

}