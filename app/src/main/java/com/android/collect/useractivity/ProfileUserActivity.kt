package com.android.collect.useractivity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.collect.R
import com.android.collect.data.Pref
import com.android.collect.kasiractivity.ProfileActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import kotlinx.android.synthetic.main.activity_profile.generationImageView
import kotlinx.android.synthetic.main.activity_profile.profilePic
import kotlinx.android.synthetic.main.activity_profile.toolbar
import kotlinx.android.synthetic.main.activity_profile_user.*

class ProfileUserActivity : AppCompatActivity() {
    lateinit var pref: Pref
    private lateinit var fAuth: FirebaseAuth
    var idProfile = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user)
        fAuth = FirebaseAuth.getInstance()
        pref = Pref(this)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "PROFILE USER"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        FirebaseDatabase.getInstance().getReference("dataUser/dataAuth/${fAuth.uid}")
            .child("id").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    idProfile = p0.value.toString()
                    FirebaseDatabase.getInstance().getReference("dataUser/$idProfile")
                        .child("nama").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                profileNameUser.text = p0.value.toString()
                                FirebaseDatabase.getInstance().getReference("dataUser/$idProfile")
                                    .child("phone")
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(p0: DataSnapshot) {
                                            nomor_telepon_user.text = p0.value.toString()
                                            FirebaseDatabase.getInstance()
                                                .getReference("dataUser/$idProfile")
                                                .addListenerForSingleValueEvent(object :
                                                    ValueEventListener {
                                                    override fun onDataChange(p0: DataSnapshot) {
                                                        Glide.with(this@ProfileUserActivity)
                                                            .load(p0.child("profile").value.toString())
                                                            .centerCrop()
                                                            .error(R.drawable.ic_launcher_background)
                                                            .into(profilePic)
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

        bt_editProf_user.setOnClickListener {
            startActivity(Intent(this, EditUserProfile::class.java))
        }
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
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }
}
