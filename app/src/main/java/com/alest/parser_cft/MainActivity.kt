package com.alest.parser_cft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result


class MainActivity : AppCompatActivity() {

    val URL = "http://jsonplaceholder.typicode.com/users";
    var People = ArrayList<Person>()
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
        URL.httpGet().responseObject(Person.Deserializer()) { request, response, result ->
            val (people, err) = result

            //Add to ArrayList
            people?.forEach { person ->
                People.add(person)
            }

            println(People)
        }
    }

}