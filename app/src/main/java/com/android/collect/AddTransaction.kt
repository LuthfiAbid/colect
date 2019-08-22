package com.android.collect

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.collect.data.Pref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_transaction.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val order_view = ArrayList<View>()

class AddTransaction : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    lateinit var pref: Pref
    private lateinit var fAuth: FirebaseAuth
    var idUser = ""
    var idKasir = ""
    var dateOrder = ""
    var cashback = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        fAuth = FirebaseAuth.getInstance()
        pref = Pref(this)

        val date = LocalDateTime.now()
        val formatdate = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS")
        val formatted = date.format(formatdate)
        dateOrder = formatted

        if (intent.extras != null) {
            idUser = intent.getStringExtra("idUser")
            transaction_tv_date.text = formatted
        } else {
            Toast.makeText(this, "Input User id!!", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

        btn_proccess.setOnClickListener {
            dialogInputCashback()
        }

        add_button.setOnClickListener {
            Add_Line()
        }

        FirebaseDatabase.getInstance().getReference("dataUser/${fAuth.uid}")
            .child("nama").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    idKasir = p0.value.toString()
                    transaction_tv_kasir.text = idKasir
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    private fun Add_Line() {
        val vi = this
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = vi.inflate(R.layout.layout_order, null)
        linearlayout_order.addView(v)
        order_view.add(v)
    }

    private fun getAnswer() {
        val options = ArrayList<HashMap<String, String>>()

        for (view in order_view) {
            val map = HashMap<String, String>()
            map.put("order", view.findViewById<EditText>(R.id.transaction_et_order_name).text.toString())
            map.put("price", view.findViewById<EditText>(R.id.transaction_et_order_harga).text.toString())
            map.put("qty", view.findViewById<EditText>(R.id.transaction_et_order_qty).text.toString())
            options.add(map)
        }

//        for (option in options) {
//            option.get("price")
//        }


        dbRef = FirebaseDatabase.getInstance().getReference("dataOrder/$idUser")

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                var i = 1
                if (data.exists()) {
                    data.children.indexOfLast {
                        i = it.key!!.toInt() + 1
                        true
                    }
                }
                dbRef.child("/$i/idKasir").setValue(idKasir)
                dbRef.child("/$i/idUser").setValue(idUser)
                dbRef.child("/$i/tanggal").setValue(dateOrder)
                dbRef.child("/$i/cashback").setValue(cashback)
                dbRef.child("/$i/detailOrder").setValue(options)
                dbRef.push()
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

//        dbRef.child("/total").setValue(password)


        Toast.makeText(this, options.get(0).get("order"), Toast.LENGTH_SHORT).show()
    }

    fun dialogInputCashback() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("With EditText")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_input_iduser, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            cashback = editText.text.toString()
            getAnswer()
        }
        builder.show()
    }
}
