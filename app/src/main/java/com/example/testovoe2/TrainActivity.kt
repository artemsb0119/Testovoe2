package com.example.testovoe2

import JsonDays
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*

class TrainActivity : AppCompatActivity() {

    private lateinit var textViewDayWeek: TextView
    private lateinit var imageBack: ImageView
    private lateinit var imageViewFon5: ImageView

    private lateinit var imageUrl: String
    private lateinit var fullDescription: String

    private lateinit var url: String
    private lateinit var firstFragment: FirstFragment
    private var isDataLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train)

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US)

        imageBack = findViewById(R.id.imageBack)
        textViewDayWeek = findViewById(R.id.textViewDayWeek)
        imageViewFon5 = findViewById(R.id.imageViewFon5)

        Glide.with(this)
            .load("http://135.181.248.237/2/fon5.png")
            .into(imageViewFon5)

        textViewDayWeek.text = dayOfWeek

        url = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "http://135.181.248.237/2/monday.json"
            Calendar.TUESDAY -> "http://135.181.248.237/2/tuesday.json"
            Calendar.WEDNESDAY -> "http://135.181.248.237/2/wednesday.json"
            Calendar.THURSDAY -> "http://135.181.248.237/2/thursday.json"
            Calendar.FRIDAY -> "http://135.181.248.237/2/friday.json"
            Calendar.SATURDAY -> "http://135.181.248.237/2/monday.json"
            Calendar.SUNDAY -> "http://135.181.248.237/2/tuesday.json"
            else -> ""
        }

        loadData()

        imageBack.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (isDataLoaded) {
            firstFragment = FirstFragment.newInstance(
                imageUrl,
                fullDescription
            )

            // Отображаем первый фрагмент
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, firstFragment)
                .commit()
        }
    }

    private fun loadData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.d("TableActivity1", "Start loading data...")
                val data = URL("http://135.181.248.237/2/thursday.json").readText()
                val gson = Gson()
                val tableData = gson.fromJson(data, Array<JsonDays>::class.java)

                for (table in tableData) {
                    imageUrl = table.img
                    fullDescription = table.text
                }

                isDataLoaded = true

                runOnUiThread {
                    firstFragment = FirstFragment.newInstance(
                        imageUrl,
                        fullDescription
                    )

                    // Отображаем первый фрагмент
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, firstFragment)
                        .commit()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("TableActivity1", "Error loading data", e)
            }
        }
    }
}