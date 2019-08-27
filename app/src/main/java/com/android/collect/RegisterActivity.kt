package com.android.collect

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.collect.data.Pref
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_register.*
import java.io.IOException
import java.util.*


class RegisterActivity : AppCompatActivity() {
    var value = 0.0
    val REQUEST_CODE_IMAGE = 10002
    val PERMISSION_RC = 10003
    lateinit var fAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    lateinit var helperPref: Pref
    lateinit var storageReference: StorageReference
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var filePathImage: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        fAuth = FirebaseAuth.getInstance()
        helperPref = Pref(this)
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
        tv_to_login.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
        btn_register.setOnClickListener {
            var nama = et_nama.text.toString()
            var nomor = et_nomor_telepon.text.toString()
            var email = et_email_reg.text.toString()
            var password = et_password_reg.text.toString()
            if (nama.isNotEmpty() || nomor.isNotEmpty() || email.isNotEmpty() || password.isNotEmpty()) {
                fAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            simpanToFirebase(nama, nomor, email, password)
                            Toast.makeText(this, "Daftar sukses!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, Login::class.java))
                        } else {
                            Toast.makeText(this, "Daftar gagal!", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Ada inputan yang kosong!", Toast.LENGTH_SHORT).show()
            }
        }
        imageProfileHolder.setOnClickListener {
            when {
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ), PERMISSION_RC
                        )
                    } else {
                        imageChooser()
                    }
                }
                else -> {
                    imageChooser()
                }
            }
        }
    }

    private fun imageChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select Image"),
            REQUEST_CODE_IMAGE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                filePathImage = data?.data!!
                try {
                    val bitmap: Bitmap = MediaStore
                        .Images.Media.getBitmap(
                        this.contentResolver, filePathImage
                    )
                    Glide.with(this).load(bitmap)
                        .override(250, 250)
                        .centerCrop().into(imageProfileHolder)
                } catch (x: IOException) {
                    x.printStackTrace()
                }
            }
        }
    }

    private fun simpanToFirebase(nama: String, nomor: String, email: String, password: String) {
        val uidUser = fAuth.currentUser?.uid
        val uid = helperPref.getUID()
        val nameXXX = UUID.randomUUID().toString()
        val counterId = helperPref.getCounterId()

        val storageRef: StorageReference = storageReference
            .child("profile/$uid/$nameXXX.${GetFileExtension(filePathImage)}")
        storageRef.putFile(filePathImage).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener {
                dbRef = FirebaseDatabase.getInstance().getReference("dataUser/")
                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(data: DataSnapshot) {
                        var i = 1
                        if (data.exists()) {
                            data.children.indexOfLast {
                                i = it.key!!.toInt() + 1
                                true
                            }
                        }
                        dbRef.child("/${fAuth.uid}").setValue(fAuth.uid)
                        dbRef.child("/${fAuth.uid}/id").setValue(i)
                        dbRef.child("/$i/id").setValue(uidUser)
                        dbRef.child("/$i/nama").setValue(nama)
                        dbRef.child("/$i/email").setValue(email)
                        dbRef.child("/$i/password").setValue(password)
                        dbRef.child("/$i/phone").setValue(nomor)
                        dbRef.child("/$i/role").setValue("user")
                        dbRef.child("/$i/profile").setValue(it.toString())
                    }
                })
            }
            progressReg.visibility = View.GONE
        }.addOnFailureListener {
            Log.e("TAG_ERROR", it.message)
        }.addOnProgressListener { taskSnapshot ->
            value = (100.0 * taskSnapshot
                .bytesTransferred / taskSnapshot.totalByteCount)
            progressReg.visibility = View.VISIBLE
        }
        startActivity(Intent(this, Login::class.java))
    }
    fun GetFileExtension(uri: Uri): String? {
        val contentResolver = this.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }
}
