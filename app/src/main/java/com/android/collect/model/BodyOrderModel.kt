package com.android.collect.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class BodyOrderModel(
    var order: String? = "",
    var price: String? = "",
    var qty: String? = ""
) : Serializable