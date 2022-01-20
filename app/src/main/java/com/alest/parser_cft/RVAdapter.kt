package com.alest.parser_cft

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parsejsonretrofitgsonexample.databinding.CellBinding

class RVAdapter(private val cell: ArrayList<ResponseData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class CellViewHolder(var viewBinding: CellBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CellViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as CellViewHolder
        itemViewHolder.viewBinding.valuteIdTextview.text = cell[position].Valute.values
        itemViewHolder.viewBinding.employeeNameTextview.text = cell[position].employeeName
        itemViewHolder.viewBinding.employeeSalaryTextview.text = cell[position].employeeSalary
        itemViewHolder.viewBinding.employeeAgeTextview.text = cell[position].employeeAge
    }

    override fun getItemCount(): Int {
        return cell.size
    }
}