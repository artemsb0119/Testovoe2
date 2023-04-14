package com.example.testovoe2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide

class AnalitikActivity : AppCompatActivity() {

    private lateinit var editTextLength: EditText
    private lateinit var editTextPrised: EditText
    private lateinit var textViewNow: TextView
    private lateinit var imageViewFon4: ImageView

    private lateinit var progressBarAnalitik: ProgressBar

    private lateinit var buttonInsert: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analitik)

        editTextLength = findViewById(R.id.editTextLength)
        editTextPrised = findViewById(R.id.editTextPrised)
        buttonInsert = findViewById(R.id.buttonInsert)
        progressBarAnalitik = findViewById(R.id.progressBarAnalitik)
        textViewNow = findViewById(R.id.textViewNow)
        imageViewFon4 = findViewById(R.id.imageViewFon4)

        Glide.with(this)
            .load("http://135.181.248.237/2/fon5.png")
            .into(imageViewFon4)

        progressBarAnalitik.max = 25000

        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val weight = sharedPreferences.getInt("weight",0)
        var points = sharedPreferences.getInt("points",0)
        textViewNow.text = points.toString()
        progressBarAnalitik.progress = points

        buttonInsert.setOnClickListener {
            if (TextUtils.isEmpty(editTextLength.text)) {
                editTextLength.setText("0")
            }
            if (TextUtils.isEmpty(editTextPrised.text)) {
                editTextPrised.setText("0")
            }
            val lengthText = editTextLength.text.toString()
            val length = lengthText.toIntOrNull()

            val prisedText = editTextPrised.text.toString()
            val prised = prisedText.toIntOrNull()


            points += ((length!! *1000+ prised!! *10)*weight/10).toInt()
            val editor = getSharedPreferences("my_preferences", Context.MODE_PRIVATE).edit()
            editor.putInt("points", points)
            editor.apply()
            textViewNow.text = points.toString()
            progressBarAnalitik.progress = points
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
        return super.onKeyDown(keyCode, event)
    }
}