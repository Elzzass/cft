package com.alest.parser_cft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.httpGet


class MainActivity : AppCompatActivity() {

//    val URL = "http://jsonplaceholder.typicode.com/users";
    val URL = "https://www.cbr-xml-daily.ru/daily_json.js"
    var People = ArrayList<ResponseData>()
    private lateinit var textView: TextView
    var str: String = ""
    var valuteArray: MutableList<Valute> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.okhttp_response)
        //добавляем обработчик для кнопки вызова функции request()
        request()

        findViewById<Button>(R.id.okhttp_request).setOnClickListener {
            request()

        }
    }

    private fun initRecycler() {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val adapter = CarsRecyclerAdapter(this, valuteArray) //Создаем экземпляр класса CarsRecyclerAdapter
        recyclerView.adapter = adapter //устанавливаем наш адаптер в качестве адаптера для нашего RecyclerView
    }
    private fun request() {
        //при помощи расширения httpGet() выполняем наш запрос и получаем данные в функции responseString
        URL.httpGet().responseObject(ResponseData.Deserializer()) { request, response, result ->
            val (dataFromResponse, err) = result

            Log.d("Tag", "responseObject result: ${result}")
            //Add to ArrayList
           /* response?.forEach { person ->
                People.add(person)
                Log.d("Tag", "responseObject person: ${person}")
                str += person.toString()
            }*/
            dataFromResponse?.Valute?.values?.forEach{valute ->
                valuteArray.add(valute)
            }
            Log.d("Tag", "responseObject valuteArray: ${valuteArray}")
            Log.d("Tag", "responseObject err: ${err}")
            Log.d("Tag", "responseObject dataFromResponse: ${dataFromResponse}")
            Log.d("Tag", "responseObject People: ${People}")
            Log.d("Tag", "responseObject str: ${str}")
//            textView.post { textView.text = str }
            textView.post { textView.text = dataFromResponse.toString() }
            initRecycler()

        }
    }

}