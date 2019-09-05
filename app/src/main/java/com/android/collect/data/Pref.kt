package com.android.collect.data

import android.content.Context
import android.content.SharedPreferences

class Pref {
    val USER_ID = "uidx"
    val COUNTER_ID = "counter"
    val statusLogin = "STATUS"
    val statusUser = "STATUS_USER"
    val dest = "DESTINATION"


    var mContext: Context
    var sharedSet: SharedPreferences

    constructor(ctx: Context) {
        mContext = ctx
        sharedSet = mContext.getSharedPreferences("APLIKASITESDB", Context.MODE_PRIVATE)
    }
    fun saveUID(uid: String) {
        val edit = sharedSet.edit()
        edit.putString(USER_ID, uid)
        edit.apply()
    }
    fun getUIDD(): String? {
        return sharedSet.getString(USER_ID, " ")
    }
    fun getUID(): String? {
        return sharedSet.getString(USER_ID, " ")
    }

    fun getCounterId(): Int {
        return sharedSet.getInt(COUNTER_ID, 1)
    }

    fun setStatus(status: Boolean) {
        val edit = sharedSet.edit()
        edit.putBoolean(statusLogin, status)
        edit.apply()
    }

    fun setStatusUser(status: Boolean) {
        val edit = sharedSet.edit()
        edit.putBoolean(statusUser, status)
        edit.apply()
    }
    fun cekStatus(): Boolean? {
        return sharedSet.getBoolean(statusLogin, false)
    }

    fun cekStatusUser(): Boolean? {
        return sharedSet.getBoolean(statusUser, false)
    }
}