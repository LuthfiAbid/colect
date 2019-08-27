package com.android.collect

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_final_transaction.*

class FinalTransaction : AppCompatActivity() {

    var idTransaction = ""
    var idUser = ""
    var idToko = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_transaction)

        if (intent.extras != null) {
            idTransaction = intent.getStringExtra("idTransaction")
            idUser = intent.getStringExtra("idUser")
            idToko = intent.getStringExtra("idToko")
        } else {
            Toast.makeText(this, "Transaction Error", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }

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
            .child("toko").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    recipt_tv_date.text = p0.child("tanggal").value.toString()
                    recipt_tv_kasir.text = p0.child("namaKasir").value.toString()
                    recipt_tv_cashback.text = p0.child("cashback").value.toString()
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }
}
