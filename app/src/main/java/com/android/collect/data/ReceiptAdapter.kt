package com.android.collect.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.collect.R
import com.android.collect.model.BodyOrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class ReceiptAdapter : RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    var mCtx: Context
    var itemOrder: List<BodyOrderModel>
    lateinit var pref: Pref
    lateinit var dbRef: DatabaseReference
    lateinit var fauth: FirebaseAuth
    var idUser = ""

    constructor (mCtx: Context, list: List<BodyOrderModel>) {
        this.mCtx = mCtx
        this.itemOrder = list
    }


    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        val orderModel: BodyOrderModel = itemOrder.get(position)
        holder.tv_order_receipt.text = orderModel.order
        holder.tv_harga_receipt.text = orderModel.price
        holder.tv_qty_receipt.text = orderModel.qty
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_order, parent, false)
        val receiptViewHolder = ReceiptViewHolder(view)
        return receiptViewHolder
    }

    override fun getItemCount(): Int {
        return itemOrder.size
    }

    inner class ReceiptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_order_receipt: TextView
        var tv_harga_receipt: TextView
        var tv_qty_receipt: TextView

        init {
            tv_order_receipt = itemView.findViewById(R.id.tv_order_receipt)
            tv_harga_receipt = itemView.findViewById(R.id.tv_price_receipt)
            tv_qty_receipt = itemView.findViewById(R.id.tv_qty_receipt)
        }
    }
}