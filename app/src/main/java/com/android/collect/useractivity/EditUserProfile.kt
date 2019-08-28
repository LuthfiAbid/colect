package com.android.collect.useractivity

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
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.collect.R
import com.android.collect.data.Pref
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit_user_profile.*
import java.io.IOException

class EditUserProfile : AppCompatActivity() {
    lateinit var dbRef: DatabaseReference
    private lateinit var fAuth: FirebaseAuth
    lateinit var preferences: Pref
    val REQUEST_CODE_IMAGE = 10002
    val PERMISSION_RC = 10003
    var value = 0.0
    lateinit var filePathImage: Uri
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var storageReference: StorageReference
    var idProfile = ""
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

    fun GetFileExtension(uri: Uri): String? {
        val contentResolver = this.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
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
                        .centerCrop().into(img_placeholder_EditProfile_user)
                } catch (x: IOException) {
                    x.printStackTrace()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_profile)
        preferences = Pref(this)
        fAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
//        supportActionBar!!.title = "EDIT PROFILE"
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        img_placeholder_EditProfile_user.setOnClickListener {
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

        FirebaseDatabase.getInstance().getReference("dataUser/dataAuth/${fAuth.uid}")
            .child("id").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    idProfile = p0.value.toString()
                    FirebaseDatabase.getInstance().getReference("dataUser/$idProfile")
                        .child("profile").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                Glide.with(this@EditUserProfile).load(p0.value.toString())
                                    .centerCrop()
                                    .error(R.drawable.ic_launcher_background)
                                    .into(img_placeholder_EditProfile_user)
                                FirebaseDatabase.getInstance().getReference("dataUser/$idProfile")
                                    .child("nama").addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(p0: DataSnapshot) {
                                            et_editnama_user.setText(p0.value.toString())
                                            FirebaseDatabase.getInstance().getReference("dataUser/$idProfile")
                                                .child("phone")
                                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                                    override fun onDataChange(p0: DataSnapshot) {
                                                        et_editnohp_user.setText(p0.value.toString())
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

        btn_save_user.setOnClickListener {
            val uidUser = fAuth.currentUser?.uid
            dbRef = FirebaseDatabase.getInstance().reference
//            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onCancelled(p0: DatabaseError) {
//                    Log.e("Error", p0.message)
//                }
//
//                override fun onDataChange(p0: DataSnapshot) {
//                    for (data in p0.children) {
            val eteditnamao = et_editnama_user.text.toString()
            val eto = et_editnohp_user.text.toString()
            try {
                FirebaseDatabase.getInstance().getReference("dataUser/dataAuth/${fAuth.uid}")
                    .child("id").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            idProfile = p0.value.toString()
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                val storageRef: StorageReference = storageReference
                    .child("profile/$uidUser/${preferences.getUIDD()}.${GetFileExtension(filePathImage)}")
                storageRef.putFile(filePathImage).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener {
                        dbRef.child("dataUser/$idProfile/profile").setValue(it.toString())
                    }
                }.addOnFailureListener {
                    Log.e("TAG_ERROR", it.message)
                }.addOnProgressListener { taskSnapshot ->
                    value = (100.0 * taskSnapshot
                        .bytesTransferred / taskSnapshot.totalByteCount)
                }
            } catch (e: UninitializedPropertyAccessException) {
                Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show()
            }
            dbRef.child("dataUser/$idProfile/nama").setValue(eteditnamao)
            dbRef.child("dataUser/$idProfile/phone").setValue(eto)
            Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show()
        }
    }
}
