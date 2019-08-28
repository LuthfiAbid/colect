package com.android.collect.kasiractivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.collect.R
import com.android.collect.data.FinalOrderAdapter
import com.android.collect.data.FinalOrderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_final_transaction.*

class FinalTransaction : AppCompatActivity() {

    var idTransaction = 0
    var idUser = ""
    var idToko = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_transaction)

        rc_view_order.layoutManager = LinearLayoutManager(this)

        idTransaction = intent.getIntExtra("idTransaction", 0)
        idUser = intent.getStringExtra("idUser")
        idToko = intent.getStringExtra("idToko")

        Toast.makeText(this, "data $idTransaction, $idUser, $idToko", Toast.LENGTH_LONG).show()

        getDataRecipt()

        back_btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    private fun getDataRecipt() {

        FirebaseDatabase.getInstance().getReference("dataToko/${idToko}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    recipt_tv_cafename.text = p0.child("namaToko").value.toString()
                    recipt_tv_address.text = p0.child("alamat").value.toString()
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

        FirebaseDatabase.getInstance().getReference("dataUser/${idUser}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    recipt_tv_user.text = p0.child("nama").value.toString()
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

        FirebaseDatabase.getInstance().getReference("dataOrder/${idUser}/${idTransaction}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    recipt_tv_date.text = p0.child("tanggal").value.toString()
                    recipt_tv_kasir.text = p0.child("namaKasir").value.toString()
                    recipt_tv_cashback.text = p0.child("cashback").value.toString()
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

        FirebaseDatabase.getInstance().getReference("dataOrder/${idUser}/${idTransaction}/detailOrder")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val datas = ArrayList<FinalOrderModel>()
                    for (data in p0.children) {
                        datas.add(
                            FinalOrderModel(
                                data.child("order").value.toString(),
                                data.child("price").value.toString(),
                                data.child("qty").value.toString()
                            )
                        )
//                        val model = FinalOrderModel(data.child("order"))
                    }
                    rc_view_order.adapter = FinalOrderAdapter(datas, this@FinalTransaction)
                }

            })
    }
}
