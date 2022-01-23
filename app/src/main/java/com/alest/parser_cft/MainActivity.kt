package com.alest.parser_cft

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //    val URL = "http://jsonplaceholder.typicode.com/users";
    val URL = "https://www.cbr-xml-daily.ru/daily_json.js"
    var People = ArrayList<ResponseData>()
    private lateinit var textView: TextView
    var str: String = ""
    var valuteList: MutableList<Valute> = ArrayList()
    var databaseHelper: DatabaseHelper? = null
//    var database: SQLiteDatabase? = null

    lateinit var adapter: RecyclerView.Adapter<*>
    var userCursor: Cursor? = null
    var userAdapter: SimpleCursorAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
//        adapter = recyclerView?.adapter as RecyclerView.Adapter<*>
        //добавляем обработчик для кнопки вызова функции request()
//        request()
        textView = findViewById(R.id.okhttp_response_textview)

        //инициализируем наш DatabaseHelper
        databaseHelper = DatabaseHelper(applicationContext)
//        var valute = databaseHelper?.getAllFromDB()!!
//        valute.forEach { it -> valuteList.add(it) }
//        Collections.copy(valuteList, databaseHelper?.getAllFromDB()!!)
//        valuteList.addAll(databaseHelper?.getAllFromDB()!!)
//        valuteList.addAll(databaseHelper?.getAllFromDB()!!)
        initRecycler()
        var codeValutaList : MutableList<String> = ArrayList()
        valuteList.forEach{ it -> codeValutaList.add(it.CharCode)}
        // Create an ArrayAdapter
     /*   val adapter = ArrayAdapter.createFromResource(
            this,
//            codeValutaList, android.R.layout.simple_spinner_item
            R.array.city_list, android.R.layout.simple_spinner_item
        )*/
        val catAdapter: ArrayAdapter<String>
        catAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, codeValutaList)

        spinner.adapter = catAdapter


// Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
//        spinner.adapter = adapter
        // Spinner element
        // Spinner element
//        val spinner = findViewById(R.id.spinner) as Spinner

        findViewById<Button>(R.id.http_request_button).setOnClickListener {
            request()
        }
        findViewById<Button>(R.id.delete_db).setOnClickListener {
            clearTable()
        }
    }
    fun getValues(view: View) {
        Toast.makeText(
            this, "Spinner 1 " + spinner.selectedItem.toString(), Toast.LENGTH_LONG
        ).show()

    }
    private fun clearTable() {
        //открываем подключение к базе данных
//        database = databaseHelper?.readableDatabase

        //получаем данных в Cursor при помощи SQL запроса
//        userCursor = database?.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE}", null)

//        valuteArray.clear()
        Log.d("Tag", "clear valuteArray: ${valuteList}")

        databaseHelper?.deleteDB()
        valuteList.clear()
        initRecycler()

//        adapter.notifyDataSetChanged()
//        notifyRV()
//        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
//        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun notifyRV() {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter?.notifyDataSetChanged()
        adapter.notifyDataSetChanged()

    }

    private fun initRecycler() {
//        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
//        adapter = ValutaRecyclerAdapter(this, databaseHelper?.getAllFromDB()!!) //Создаем экземпляр класса CarsRecyclerAdapter
        valuteList.clear()
        valuteList.addAll(databaseHelper?.getAllFromDB()!!)
        adapter =
            ValutaRecyclerAdapter(this, valuteList) //Создаем экземпляр класса CarsRecyclerAdapter
//        recyclerView.adapter = adapter //устанавливаем наш адаптер в качестве адаптера для нашего RecyclerView
        findViewById<RecyclerView>(R.id.recycler_view).adapter =
            adapter //устанавливаем наш адаптер в качестве адаптера для нашего RecyclerView
        adapter.notifyDataSetChanged()
        Log.d(
            "Tag",
            "initRecycler databaseHelper?.getAllFromDB()!!: ${databaseHelper?.getAllFromDB()!!}"
        )
        Log.d("Tag", "initRecycler adapter: ${adapter}")
        Log.d("Tag", "initRecycler valuteList: ${valuteList}")

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
            valuteList.clear()
            dataFromResponse?.Valute?.values?.forEach { valute ->
//                valuteArray.add(valute)
                Log.d("Tag", "request valute: ${valute}")

                databaseHelper?.addValuta(valute)
//                notifyRV()
                valuteList.add(valute)
            }
            Log.d("Tag", "request adapter: ${adapter}")
//            adapter = ValutaRecyclerAdapter(this, valuteList) //Создаем экземпляр класса CarsRecyclerAdapter
//            adapter.notifyDataSetChanged()
            Log.d("Tag", "responseObject valuteList: ${valuteList}")
//            Log.d("Tag", "responseObject databaseHelper?.getAllFromDB(): ${databaseHelper?.getAllFromDB()!!}")
            Log.d("Tag", "responseObject err: ${err}")
            Log.d("Tag", "responseObject dataFromResponse: ${dataFromResponse}")
            Log.d("Tag", "responseObject People: ${People}")
            Log.d("Tag", "responseObject str: ${str}")
//            textView.post { textView.text = str }
            textView.post { textView.text = dataFromResponse.toString() }
//            valuteArray = databaseHelper?.getAllFromDB()!!
            initRecycler()
//            notifyRV()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //закрываем подключение к БД и курсору
//        database?.close()
//        userCursor?.close()
    }
}