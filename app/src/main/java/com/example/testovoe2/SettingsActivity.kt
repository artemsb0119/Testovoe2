package com.example.testovoe2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class SettingsActivity : AppCompatActivity() {

    private lateinit var textViewClear: TextView
    private lateinit var imageViewFon5: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        textViewClear = findViewById(R.id.textViewClear)
        imageViewFon5 = findViewById(R.id.imageViewFon5)

        Glide.with(this)
            .load("http://135.181.248.237/2/fon5.png")
            .into(imageViewFon5)

        textViewClear.setOnClickListener {
            val editor = getSharedPreferences("my_preferences", Context.MODE_PRIVATE).edit()
            editor.putBoolean("is_first_start", true)
            editor.putInt("points", 0)
            editor.putString("allText","")
            editor.apply()
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}