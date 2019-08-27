package com.android.collect

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.collect.data.Pref
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var dbRef: DatabaseReference
    lateinit var pref: Pref
    private lateinit var fAuth: FirebaseAuth
    var idToko = ""
    var idKasir = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fAuth = FirebaseAuth.getInstance()
        pref = Pref(this)

        getDataKasir()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            dialogInputId()
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val navView: NavigationView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    private fun getDataKasir() {
        FirebaseDatabase.getInstance().getReference("dataUser/${fAuth.uid}")
            .child("id").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    idKasir = p0.value.toString()
                    FirebaseDatabase.getInstance().getReference("dataUser/${idKasir}")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                tv_nama_user.text = p0.child("nama").value.toString()
                                Glide.with(this@MainActivity).load(p0.child("profile").value.toString())
                                    .centerCrop()
                                    .error(R.drawable.ic_launcher_background)
                                    .into(imageViewProfileDrawer)

                                FirebaseDatabase.getInstance().getReference("dataUser/${idKasir}")
                                    .child("toko").addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(p0: DataSnapshot) {
                                            idToko = p0.value.toString()
                                            FirebaseDatabase.getInstance().getReference("dataToko/${idToko}")
                                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                                    override fun onDataChange(p0: DataSnapshot) {
                                                        main_tv_cafename.text = p0.child("namaToko").value.toString()
                                                        main_tv_addrescafe.text = p0.child("alamat").value.toString()
                                                    }

                                                    override fun onCancelled(p0: DatabaseError) {

                                                    }
                                                })
                                        }

                                        override fun onCancelled(p0: DatabaseError) {

                                        }
                                    })
                            }

                            override fun onCancelled(p0: DatabaseError) {

                            }
                        })
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile-> {
                startActivity(Intent(this,ProfileActivity::class.java))
            }
            R.id.nav_saldo-> {

            }
            R.id.nav_logout -> {
                pref.setStatus(false)
                fAuth.signOut()
                startActivity(
                    Intent(
                        this, Login::class.java
                    )
                )
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun dialogInputId() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("With EditText")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_input_iduser, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            val intent = Intent(this, AddTransaction::class.java)
            intent.putExtra("idUser", editText.text.toString())
            startActivity(intent)
        }
        builder.show()
    }
}
