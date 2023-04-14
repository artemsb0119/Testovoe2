package com.example.testovoe2

import android.R.id.text2
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.util.*


class QuestionActivity : AppCompatActivity() {

    private lateinit var imageViewSend: ImageView
    private lateinit var editTextAsk: EditText
    private lateinit var textViewServerResponses: TextView
    private lateinit var imageBack: ImageView
    private lateinit var imageViewFon5: ImageView

    private val serverUrl = "http://84.38.181.162/ios"
    private var questionId: Int? = null

    private var allText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        imageBack = findViewById(R.id.imageBack)
        editTextAsk = findViewById(R.id.editTextAsk)
        imageViewSend = findViewById(R.id.imageViewSend)
        textViewServerResponses = findViewById(R.id.textViewServerResponses)
        imageViewFon5 = findViewById(R.id.imageViewFon5)

        Glide.with(this)
            .load("http://135.181.248.237/2/fon5.png")
            .into(imageViewFon5)

        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        textViewServerResponses.text = sharedPreferences.getString("allText","")

        imageBack.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageViewSend.setOnClickListener {
            val question = editTextAsk.text.toString().trim()
            if (question.isNotEmpty()) {
                sendQuestion(question)
            }
        }
    }

    private fun sendQuestion(question: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Отправляем вопрос на сервер
                val random = Random()
                val questionId = random.nextInt(100000)
                val response = URL("$serverUrl/response.php?id=$questionId").readText()
                withContext(Dispatchers.Main) {
                    // Если получили ответ
                    if (response.isNotEmpty()) {
                        // Парсим JSON, чтобы получить ответ в читаемом виде
                        val jsonResponse = JSONObject(response)
                        val answer = jsonResponse.getString("response")
                        // Добавляем вопрос и ответ в поле вывода
                        val message = "$question\n\nAnswer: $answer\n\n"
                        val spannable: Spannable = SpannableString(message)

                        spannable.setSpan(
                            ForegroundColorSpan(Color.WHITE),
                            question.length,
                            (message).length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        val newSpannable = SpannableStringBuilder(spannable)
                        newSpannable.append(textViewServerResponses.text)
                        textViewServerResponses.text = newSpannable
                        allText = textViewServerResponses.text.toString()
                        val editor = getSharedPreferences("my_preferences", Context.MODE_PRIVATE).edit()
                        editor.putString("allText", allText)
                        editor.apply()
                        editTextAsk.text.clear()
                    } else {
                        textViewServerResponses.text = "The response is being processed"
                        checkAnswer(question)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun checkAnswer(question: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Ждем ответ на вопрос
                val response = URL("$serverUrl/response.php?id=$questionId").readText()

                withContext(Dispatchers.Main) {
                    if (response.isNotEmpty()) {
                        // Если получили ответ, добавляем его к полю вывода
                        val jsonResponse = JSONObject(response)
                        val answer = jsonResponse.getString("response")
                        val currentMessage = textViewServerResponses.text.toString()
                        val newMessage = "$currentMessage\n\nAnswer: $answer\n\n"
                        val spannable: Spannable = SpannableString(newMessage)

                        spannable.setSpan(
                            ForegroundColorSpan(Color.WHITE),
                            currentMessage.length,
                            (newMessage).length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        val newSpannable = SpannableStringBuilder(spannable)
                        newSpannable.append(textViewServerResponses.text)
                        textViewServerResponses.text = newSpannable
                        allText = textViewServerResponses.text.toString()
                        val editor = getSharedPreferences("my_preferences", Context.MODE_PRIVATE).edit()
                        editor.putString("allText", allText)
                        editor.apply()
                    } else {
                        // Если ответа нет, продолжаем ждать
                        checkAnswer(question)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}