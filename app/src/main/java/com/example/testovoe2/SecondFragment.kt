package com.example.testovoe2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class SecondFragment : Fragment() {
    // Свойство для передачи параметра фрагменту
    private lateinit var imageUrl: String
    private lateinit var fullDescription: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        // Находим View элементы
        val imageViewTwo = view.findViewById<ImageView>(R.id.imageViewTwo)
        val descriptionTextView = view.findViewById<TextView>(R.id.full_description_text_view)
        imageUrl = arguments?.getString("imageUrl") ?: ""
        fullDescription = arguments?.getString("fullDescription") ?: ""

        Glide.with(this)
            .load(imageUrl)
            .into(imageViewTwo)
        // Отображаем полное описание
        descriptionTextView.text = fullDescription

        return view
    }

    // Создаем новый экземпляр фрагмента с передачей параметра
    companion object {
        fun newInstance(imageUrl: String, fullDescription: String): SecondFragment {
            val fragment = SecondFragment()
            val args = Bundle()
            args.putString("imageUrl", imageUrl)
            args.putString("fullDescription", fullDescription)
            fragment.arguments = args
            return fragment
        }
    }
}