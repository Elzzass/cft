package com.alest.parser_cft

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val URL = "https://www.cbr-xml-daily.ru/daily_json.js"
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
        if (sumRub.isNotBlank() && !sumRub.isNullOrEmpty() && spinner.selectedItem != null && !valuteList.isNullOrEmpty()) {
                var res = sumRub.toString().toFloat() *
                        (valuteList.find { it.Name == spinner.selectedItem.toString() }?.Nominal ?: 0f).toFloat() /
                        (valuteList.find { it.Name == spinner.selectedItem.toString() }?.Value ?: 0f)
                findViewById<TextView>(R.id.convert_result_textview).text = String.format("%.4f", res)
        }
    }

    private fun initRecycler() {
        valuteList.clear()
        databaseHelper?.getAllFromDB()?.let { valuteList.addAll(it) }
        adapter = ValutaRecyclerAdapter(this, valuteList)
        findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun request() {
        URL.httpGet().responseObject(ResponseData.Deserializer()) { request, response, result ->
            val (dataFromResponse, err) = result
            if (response.statusCode == 200) {
                getPreferences(Context.MODE_PRIVATE).edit().putString("Date", dataFromResponse?.Date).apply()
                valuteList.clear()
                dataFromResponse?.Valute?.values?.forEach { valute ->
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
}