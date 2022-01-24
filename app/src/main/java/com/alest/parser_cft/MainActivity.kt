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
    private lateinit var dateTextView: TextView
    var valuteList: MutableList<Valute> = ArrayList()
    var databaseHelper: DatabaseHelper? = null
    lateinit var adapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences: SharedPreferences = getPreferences(Context.MODE_PRIVATE);
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

        if (sumRub.isNotBlank() && !sumRub.isNullOrEmpty() && spinner.selectedItem != null && !valuteList.isNullOrEmpty()) {
                Log.d("Tag", "getValues 1: ${sumRub}")
                var res = sumRub.toString().toFloat() *
                        (valuteList.find { it.Name == spinner.selectedItem.toString() }?.Nominal ?: 0f).toFloat() /
                        (valuteList.find { it.Name == spinner.selectedItem.toString() }?.Value ?: 0f)
                Log.d("Tag", "getValues res: ${res}")
                findViewById<TextView>(R.id.convert_result_textview).text = String.format("%.4f", res)

        }
    }

    private fun clearTable() {
        Log.d("Tag", "clear valuteArray: ${valuteList}")
        databaseHelper?.deleteDB()
        valuteList.clear()
        initRecycler()
    }

    private fun initRecycler() {
        valuteList.clear()
        databaseHelper?.getAllFromDB()?.let { valuteList.addAll(it) }
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
            Log.d("Tag", "responseObject result..component2(): ${result.component2()}")
            Log.d("Tag", "responseObject result..component1(): ${result.component1()}")

            Log.d("Tag", "request adapter: ${adapter}")
            Log.d("Tag", "responseObject valuteList: ${valuteList}")
            Log.d("Tag", "responseObject err.localizedMessage: ${err?.localizedMessage}")
            Log.d("Tag", "responseObject err.cause: ${err?.cause}")
            Log.d("Tag", "responseObject err.message: ${err?.message}")
            Log.d("Tag", "responseObject err.causedByInterruption: ${err?.causedByInterruption}")
            Log.d("Tag", "responseObject response.statusCode: ${response.statusCode}")
            Log.d("Tag", "responseObject dataFromResponse: ${dataFromResponse}")
            if (response.statusCode == 200) {
                getPreferences(Context.MODE_PRIVATE).edit().putString("Date", dataFromResponse?.Date).apply()

                valuteList.clear()
                dataFromResponse?.Valute?.values?.forEach { valute ->
                    Log.d("Tag", "request valute: ${valute}")
                    databaseHelper?.addValuta(valute)
                    valuteList.add(valute)
                }
                initRecycler()
                initSpinner()
                dateTextView.post { dateTextView.text = "Дата: " + dataFromResponse?.Date }
            } else {
                Toast.makeText(applicationContext, "Нет данных. Ошибка в сети!!!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //закрываем подключение к БД и курсору
//        database?.close()
//        userCursor?.close()
    }
}