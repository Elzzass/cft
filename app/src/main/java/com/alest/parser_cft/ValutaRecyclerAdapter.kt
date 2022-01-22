package com.alest.parser_cft

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Создаем класс CarsRecyclerAdapter и наследуем его от класса RecyclerView.Adapter
//class CarsRecyclerAdapter(context: Context, private val cars: List<Car>)
class ValutaRecyclerAdapter(context: Context, private val cars: List<Valute>)
    : RecyclerView.Adapter<ValutaRecyclerAdapter.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    //Создаем элемент списка который отображается на экране
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.cell, parent, false)
        return ViewHolder(view)
    }

    //Задаем значения для элемента списка
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cars[position])
    }

    //Получаем количество элементов в списке
    override fun getItemCount(): Int {
        return cars.size
    }

    class ViewHolder constructor(view: View): RecyclerView.ViewHolder(view) {
        val charCodeView: TextView = view.findViewById(R.id.valute_char_code_textview)
        val valueView: TextView = view.findViewById(R.id.valute_value_textview)
        val nameView: TextView = view.findViewById(R.id.valute_name_textview)

//        fun bind(car: Car) {
        fun bind(valute: Valute) {
            charCodeView.text = valute.CharCode
            valueView.text = valute.NumCode
            nameView.text = valute.Name
        }
    }
}