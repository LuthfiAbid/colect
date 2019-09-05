package com.android.collect.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class HeadOrderModel(
    var cashback: String? = "",
    var namaToko: String? = "",
    var total: Long? = 0,
    var detailOrder: ArrayList<BodyOrderModel>? = ArrayList(),
    var idUser: String? = "",
    var namaKasir: String? = "",
    var tanggal: String? = ""
) : Serializable