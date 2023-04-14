package com.example.testovoe2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import java.time.LocalDateTime

class MenuActivity : AppCompatActivity() {

    private lateinit var buttonTraining: AppCompatButton
    private lateinit var buttonAnalitic: AppCompatButton
    private lateinit var buttonQuestion: AppCompatButton
    private lateinit var buttonSettings: AppCompatButton
    private lateinit var imageViewFon2: ImageView

    private lateinit var textViewNow: TextView
    private lateinit var progressBarMenu: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        buttonTraining = findViewById(R.id.buttonTraining)
        buttonAnalitic = findViewById(R.id.buttonAnalitic)
        buttonQuestion = findViewById(R.id.buttonQuestion)
        buttonSettings = findViewById(R.id.buttonSettings)
        progressBarMenu = findViewById(R.id.progressBarMenu)
        textViewNow = findViewById(R.id.textViewNow)
        imageViewFon2 = findViewById(R.id.imageViewFon2)

        Glide.with(this)
            .load("http://135.181.248.237/2/fon5.png")
            .into(imageViewFon2)

        progressBarMenu.max = 25000

        val currentDateTime = LocalDateTime.now()
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val lastUpdatedDate = sharedPreferences.getString("last_updated_date", null)?.let {
            LocalDateTime.parse(it)
        }
        if (lastUpdatedDate == null || lastUpdatedDate.toLocalDate() != currentDateTime.toLocalDate()) {
            val editor = getSharedPreferences("my_preferences", Context.MODE_PRIVATE).edit()
            editor.putInt("points", 0)
            editor.putString("last_updated_date", currentDateTime.toString())
            editor.apply()
        }

        var points = sharedPreferences.getInt("points",0)

        progressBarMenu.progress = points
        textViewNow.text = points.toString()

        buttonTraining.setOnClickListener {
            val intent = Intent(this, TrainActivity::class.java)
            startActivity(intent)
        }
        buttonAnalitic.setOnClickListener {
            val intent = Intent(this, AnalitikActivity::class.java)
            startActivity(intent)
            finish()
        }
        buttonQuestion.setOnClickListener {
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
        }
        buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}