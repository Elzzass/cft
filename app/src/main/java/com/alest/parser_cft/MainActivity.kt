package com.alest.parser_cft

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    val URL = "https://www.cbr-xml-daily.ru/daily_json.js"
    var People = ArrayList<ResponseData>()
    private lateinit var textView: TextView
    private lateinit var dateTextView: TextView
    var str: String = ""
    var valuteList: MutableList<Valute> = ArrayList()
    var databaseHelper: DatabaseHelper? = null
    lateinit var adapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences: SharedPreferences = getPreferences(Context.MODE_PRIVATE);
        textView = findViewById(R.id.okhttp_response_textview)
        dateTextView= findViewById(R.id.date_textview)

        databaseHelper = DatabaseHelper(applicationContext)
        initRecycler()
        initSpinner()
        dateTextView.text = "Дата: ${preferences.getString("Date", "Нет данных для отображения")}"

        var dateFromResponse = preferences.getString("Date", "Нет данных для отображения")
        Log.d("Tag", "date dateFromResponse: ${dateFromResponse}")

        findViewById<Button>(R.id.http_request_button).setOnClickListener {
            request()
        }
        findViewById<Button>(R.id.delete_db).setOnClickListener {
            clearTable()
        }
    }

    private fun initSpinner() {
        var codeValutaList: MutableList<String> = ArrayList()
        valuteList.forEach { codeValutaList.add(it.Name) }
        val spinnerAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.spinner_layout, codeValutaList)
        spinner.adapter = spinnerAdapter
    }

    fun getValues(view: View) {

        var sumRub = findViewById<TextView>(R.id.enter_rubles_edit_text).text
        Log.d("Tag", "getValues 0: ${sumRub}")
        Log.d("Tag", "getValues sumRub.isNotBlank(): ${sumRub.isNotBlank()}")
        Log.d("Tag", "getValues sumRub.isNullOrEmpty(): ${!sumRub.isNullOrEmpty()}")
        Log.d("Tag", "getValues umRub.isDigitsOnly(): ${sumRub.isDigitsOnly()}")

//        if (sumRub.isNotBlank() && sumRub.isNullOrEmpty() && sumRub.isDigitsOnly()) {
        if (sumRub.isNotBlank() && !sumRub.isNullOrEmpty() && spinner.selectedItem != null && !valuteList.isNullOrEmpty()) {
            try {
                Log.d("Tag", "getValues 1: ${sumRub}")

                var res = sumRub.toString()
                    .toFloat() * (valuteList.find { it.Name == spinner.selectedItem.toString() }?.Nominal
                    ?: 0f).toFloat() / (valuteList.find { it.Name == spinner.selectedItem.toString() }?.Value
                    ?: 0f)
                Log.d("Tag", "getValues res: ${res}")
//                String.format("%.3f", number)
//                findViewById<TextView>(R.id.convert_result_textview).text = res.toString()
                findViewById<TextView>(R.id.convert_result_textview).text = String.format(
                    "%.4f",
                    res
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Tag", "getValues err: ${sumRub}")

                Toast.makeText(
                    this, "Введена не корректная сумма в рублях", Toast.LENGTH_LONG
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


    fun getCurrentDate(): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        dateFormat.timeZone = TimeZone.getDefault()
        val today = Calendar.getInstance().time
        return dateFormat.format(today)
    }

    private fun initRecycler() {
        valuteList.clear()
        valuteList.addAll(databaseHelper?.getAllFromDB()!!)
        adapter = ValutaRecyclerAdapter(this, valuteList)

        findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter
        adapter.notifyDataSetChanged()
        Log.d("Tag", "initRecycler databaseHelper?.getAllFromDB()!!: ${databaseHelper?.getAllFromDB()!!}")
        Log.d("Tag", "initRecycler adapter: ${adapter}")
        Log.d("Tag", "initRecycler valuteList: ${valuteList}")
    }

    private fun request() {
        URL.httpGet().responseObject(ResponseData.Deserializer()) { request, response, result ->
            val (dataFromResponse, err) = result
            Log.d("Tag", "responseObject result: ${result}")
            valuteList.clear()
            dataFromResponse?.Valute?.values?.forEach { valute ->
                Log.d("Tag", "request valute: ${valute}")
                databaseHelper?.addValuta(valute)
                valuteList.add(valute)
            }
            getPreferences(Context.MODE_PRIVATE).edit().putString("Date", dataFromResponse?.Date).apply()

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
            dateTextView.post { dateTextView.text = "Дата: " + dataFromResponse?.Date }

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