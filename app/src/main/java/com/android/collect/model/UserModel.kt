package com.android.collect.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserModel(
    val order: List<OrderModel>
)