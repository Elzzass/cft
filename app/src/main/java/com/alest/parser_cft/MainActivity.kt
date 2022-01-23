package com.alest.parser_cft

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
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
        initSpinner()


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

    private fun initSpinner() {
        var codeValutaList: MutableList<String> = ArrayList()
        //        valuteList.forEach{ it -> codeValutaList.add(it.CharCode)}
        valuteList.forEach { it -> codeValutaList.add(it.Name) }
        // Create an ArrayAdapter
        /*   val adapter = ArrayAdapter.createFromResource(
            this,
//            codeValutaList, android.R.layout.simple_spinner_item
            R.array.city_list, android.R.layout.simple_spinner_item
        )*/
        val catAdapter: ArrayAdapter<String>
        //        catAdapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, codeValutaList)
        catAdapter = ArrayAdapter<String>(this, R.layout.spinner_layout, codeValutaList)

        spinner.adapter = catAdapter
    }

    fun getValues(view: View) {

        var sumRub = findViewById<TextView>(R.id.enter_rubles_edit_text).text
        Log.d("Tag", "getValues 0: ${sumRub}")
        Log.d("Tag", "getValues sumRub.isNotBlank(): ${sumRub.isNotBlank()}")
        Log.d("Tag", "getValues sumRub.isNullOrEmpty(): ${!sumRub.isNullOrEmpty()}")
        Log.d("Tag", "getValues umRub.isDigitsOnly(): ${sumRub.isDigitsOnly()}")

//        if (sumRub.isNotBlank() && sumRub.isNullOrEmpty() && sumRub.isDigitsOnly()) {
        if (sumRub.isNotBlank() && !sumRub.isNullOrEmpty() && spinner.selectedItem != null && !valuteList.isNullOrEmpty() ) {
            try {
                Log.d("Tag", "getValues 1: ${sumRub}")

                var res = sumRub.toString().toFloat() * (valuteList.find { it.Name == spinner.selectedItem.toString() }?.Nominal
                    ?: 0f).toFloat() / (valuteList.find { it.Name == spinner.selectedItem.toString() }?.Value
                    ?: 0f)
                Log.d("Tag", "getValues res: ${res}")
//                String.format("%.3f", number)
//                findViewById<TextView>(R.id.convert_result_textview).text = res.toString()
                findViewById<TextView>(R.id.convert_result_textview).text = String.format("%.4f", res)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Tag", "getValues err: ${sumRub}")

                Toast.makeText(
                    this, "Введена не корректная сумма в рублях" , Toast.LENGTH_LONG
//                    this, "Spinner 1 " + spinner.selectedItem.toString(), Toast.LENGTH_LONG
                ).show()
            }
        }
//        findViewById<TextView>(R.id.convert_choice_textview).text = "Выбрана валюта: 2" +
//                valuteList.find { it.CharCode.equals(spinner.selectedItem.toString()) }
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
            initSpinner()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //закрываем подключение к БД и курсору
//        database?.close()
//        userCursor?.close()
    }
}