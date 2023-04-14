package com.example.testovoe2

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class FirstFragment : Fragment() {

    private lateinit var imageUrl: String
    private lateinit var shortDescription: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        // Находим View элементы
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val shortDescriptionTextView = view.findViewById<TextView>(R.id.shortDescriptionTextView)
        val expandButton = view.findViewById<ImageView>(R.id.expandButton)
        imageUrl = arguments?.getString("imageUrl") ?: ""
        shortDescription = arguments?.getString("shortDescription") ?: ""

        // Загружаем картинку из imageUrl
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)
        // Отображаем короткое описание
        shortDescriptionTextView.text = shortDescription
        // Обработчик нажатия на кнопку "Развернуть"
        expandButton.setOnClickListener {
            // Создаем фрагмент с полным описанием
            val fullDescriptionFragment = SecondFragment.newInstance(imageUrl,shortDescription)

            // Заменяем текущий фрагмент на второй фрагмент
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, fullDescriptionFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        return view
    }

    companion object {
        fun newInstance(imageUrl: String, shortDescription: String): FirstFragment {
            val fragment = FirstFragment()
            val args = Bundle()
            args.putString("imageUrl", imageUrl)
            args.putString("shortDescription", shortDescription)
            fragment.arguments = args
            return fragment
        }
    }
}
