package com.alest.parser_cft

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ValutaRecyclerAdapter(context: Context, private val valutaList: List<Valute>)
    : RecyclerView.Adapter<ValutaRecyclerAdapter.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    //Создаем элемент списка который отображается на экране
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.cell, parent, false)
        return ViewHolder(view)
    }

    //Задаем значения для элемента списка
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(valutaList[position])
    }

    //Получаем количество элементов в списке
    override fun getItemCount(): Int {
        return valutaList.size
    }

    class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        val charCodeView: TextView = view.findViewById(R.id.valute_char_code_textview)
        val numCodeView: TextView = view.findViewById(R.id.valute_num_code_textview)
        val valueView: TextView = view.findViewById(R.id.valute_value_textview)
        val prevValueView: TextView = view.findViewById(R.id.valute_prev_value_textview)
        val nameView: TextView = view.findViewById(R.id.valute_name_textview)
        val idView: TextView = view.findViewById(R.id.valute_id_textview)
        val nominalView: TextView = view.findViewById(R.id.valute_nominal_textview)

        //        fun bind(car: Car) {
        fun bind(valute: Valute) {
            nameView.text = valute.Name
            charCodeView.text = valute.CharCode
            nominalView.text = valute.Nominal.toString()
            valueView.text = valute.Value.toString()
            prevValueView.text = valute.Previous.toString()
            idView.text = valute.ID
            numCodeView.text = valute.NumCode
        }
    }
}