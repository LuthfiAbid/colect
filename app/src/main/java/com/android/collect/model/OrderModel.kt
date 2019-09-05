package com.android.collect.model

data class OrderModel(
    var key: String? = null,
    var idUser: String? = null,
    var idToko: String? = null,
    var detailOrder: UserModel? = null,
    var price: Int? = null,
    var qty: Int? = null
)