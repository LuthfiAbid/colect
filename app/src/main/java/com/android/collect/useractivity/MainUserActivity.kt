package com.android.collect.useractivity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.collect.R
import com.android.collect.data.Pref
import com.android.collect.kasiractivity.Login
import com.android.collect.kasiractivity.ProfileActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import kotlinx.android.synthetic.main.nav_header_main_user.*

class MainUserActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var pref: Pref
    private lateinit var fAuth: FirebaseAuth
    var idProfile = ""
    internal var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        fAuth = FirebaseAuth.getInstance()
        pref = Pref(this)
        setSupportActionBar(toolbar)

        FirebaseDatabase.getInstance().getReference("dataUser/dataAuth/${fAuth.uid}")
            .child("id").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    idProfile = p0.value.toString()
                    FirebaseDatabase.getInstance().getReference("dataUser/$idProfile")
                        .child("nama").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                tv_nav_user.text = p0.value.toString()
                                FirebaseDatabase.getInstance()
                                    .getReference("dataUser/$idProfile")
                                    .addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(p0: DataSnapshot) {
                                            Glide.with(this@MainUserActivity)
                                                .load(p0.child("profile").value.toString())
                                                .centerCrop()
                                                .error(R.drawable.ic_launcher_background)
                                                .into(imageViewUser)
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

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            dialogInputId()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_user, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home_user -> {
//                start
            }
            R.id.nav_profile_user -> {
                startActivity(Intent(this, ProfileUserActivity::class.java))
            }
            R.id.nav_logout_user -> {
                pref.setStatus(false)
                fAuth.signOut()
                startActivity(
                    Intent(this, Login::class.java)
                )
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun dialogInputId() {
        FirebaseDatabase.getInstance().getReference("dataUser/dataAuth/${fAuth.uid}")
            .child("id").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    idProfile = p0.value.toString()
                    bitmap = TextToImageEncode(idProfile)
                    val builder = AlertDialog.Builder(this@MainUserActivity)
                    val inflater = layoutInflater
                    builder.setTitle("Scan me!")
                    val dialogLayout = inflater.inflate(R.layout.alert_dialog_barcode, null)
                    val barcode = dialogLayout.findViewById<ImageView>(R.id.img_barcode_user)
                    builder.setView(dialogLayout)
                    builder.create().show()
                    barcode.setImageBitmap(bitmap)
                }

                @Throws(WriterException::class)
                private fun TextToImageEncode(Value: String): Bitmap? {
                    val bitMatrix: BitMatrix
                    try {
                        bitMatrix = MultiFormatWriter().encode(
                            Value,
                            BarcodeFormat.QR_CODE,
                            ProfileActivity.QRcodeWidth,
                            ProfileActivity.QRcodeWidth, null
                        )

                    } catch (Illegalargumentexception: IllegalArgumentException) {

                        return null
                    }

                    val bitMatrixWidth = bitMatrix.width

                    val bitMatrixHeight = bitMatrix.height

                    val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

                    for (y in 0 until bitMatrixHeight) {
                        val offset = y * bitMatrixWidth

                        for (x in 0 until bitMatrixWidth) {

                            pixels[offset + x] = if (bitMatrix.get(x, y))
                                resources.getColor(R.color.black)
                            else
                                resources.getColor(R.color.white)
                        }
                    }
                    val bitmap =
                        Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)

                    bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
                    return bitmap
                }
            })
    }
}

