package com.android.collect.useractivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.collect.R
import com.android.collect.data.ReceiptAdapter
import com.android.collect.model.BodyOrderModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class ReceiptActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    lateinit var dbRef: DatabaseReference
    private var list: MutableList<BodyOrderModel> = ArrayList()
    var idUser = ""
    private var orderAdapter: ReceiptAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "RECEIPT"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.rc_receipt)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.setHasFixedSize(true)
        idUser = intent.getStringExtra("id_user")
        dbRef = FirebaseDatabase.getInstance().getReference("dataOrder/$idUser")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(data: DataSnapshot) {
                list = ArrayList()
                for (dataSnapshot in data.children) {
                    dbRef = FirebaseDatabase.getInstance().getReference("detailOrder")
                    dbRef.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            for (dataSnapshot in data.children) {
                                val addDataAll = dataSnapshot.getValue(BodyOrderModel::class.java)
                                list.add(addDataAll!!)
//                                e("TAG_DETAILTAG_DETAIL",addDataAll.price)
                            }
                        }
                    })
                }
                orderAdapter = ReceiptAdapter(this@ReceiptActivity, list)
                recyclerView!!.adapter = orderAdapter
            }
        })
    }
}