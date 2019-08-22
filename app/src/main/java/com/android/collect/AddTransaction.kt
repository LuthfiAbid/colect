package com.android.collect

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_transaction.*

class AddTransaction : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        if (intent.extras != null) {
            val idUser: String = intent.getStringExtra("idUser")
            transaction_tv_kasir.text = idUser
        } else {
            Toast.makeText(this, "Input User id!!", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
}
