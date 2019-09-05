package com.android.collect.data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.collect.R
import com.android.collect.model.HeadOrderModel
import com.android.collect.useractivity.ReceiptActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    var mCtx: Context
    var itemOrder: List<HeadOrderModel>
    lateinit var pref: Pref
    lateinit var dbRef: DatabaseReference
    lateinit var fauth: FirebaseAuth

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderModel: HeadOrderModel = itemOrder.get(position)
        holder.tv_toko.text = orderModel.namaToko
        holder.tv_harga.text = orderModel.total.toString()
        holder.tv_tanggal.text = orderModel.tanggal.toString()
        holder.ll.setOnClickListener {
            val intent = Intent(mCtx, ReceiptActivity::class.java)
            intent.putExtra("id_user", orderModel.idUser)
            mCtx.startActivity(intent)
        }
    }

    constructor (mCtx: Context, list: List<HeadOrderModel>) {
        this.mCtx = mCtx
        this.itemOrder = list
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrderViewHolder {
        val view: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.card_history, p0, false)
        val orderViewHolder = OrderViewHolder(view)
        return orderViewHolder
    }

    override fun getItemCount(): Int {
        return itemOrder.size
    }


    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ll: LinearLayout
        var tv_toko: TextView
        var tv_harga: TextView
        var tv_tanggal: TextView

        init {
            ll = itemView.findViewById(R.id.ll)
            tv_toko = itemView.findViewById(R.id.card_tv_toko)
            tv_harga = itemView.findViewById(R.id.card_tv_harga)
            tv_tanggal = itemView.findViewById(R.id.card_tv_tanggal)
        }
    }
}