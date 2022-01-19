package com.example.dependencies

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alest.parser_cft.R
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.io.IOException

class FuelActivity: AppCompatActivity() {
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.okhttp_response)

        //добавляем обработчик для кнопки вызова функции request()
        findViewById<Button>(R.id.okhttp_request).setOnClickListener {
            request()
        }
    }

    private fun request() {
        val city = "Ivanovo"

        val url: String = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=96130c474a1cba9f126dba69955d342e"

        //при помощи расширения httpGet() выполняем наш запрос и получаем данные в функции responseString
        url.httpGet().responseString { _, _, result ->
            when (result) {
                //обрабатываем ответ, если запрос завершился с ошибкой
                is Result.Failure -> {
                    textView.post { textView.text = result.getException().message }
                }

                //обрабатываем ответ, если запрос завершился успешно
                is Result.Success -> {
                    textView.post { textView.text = result.get() }
                }
            }
        }
    }
}