package com.android.collect.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.collect.R

class FinalOrderAdapter(val data: ArrayList<FinalOrderModel>, val context: Context) :
    RecyclerView.Adapter<FinalOrderAdapter.FinalOrderHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinalOrderHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_order, null)
        return FinalOrderHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FinalOrderHolder, position: Int) {
        holder.dataOrder.text = data.get(position).order
        holder.dataPrice.text = data.get(position).price
        holder.dataQty.text = data.get(position).qty
    }

    class FinalOrderHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dataOrder: TextView
        val dataPrice: TextView
        val dataQty: TextView

        init {
            dataOrder = view.findViewById(R.id.dataOrder)
            dataPrice = view.findViewById(R.id.dataPrice)
            dataQty = view.findViewById(R.id.dataQty)
        }
    }
}