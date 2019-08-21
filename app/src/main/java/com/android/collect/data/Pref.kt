package com.android.collect.data

import android.content.Context
import android.content.SharedPreferences

class Pref {
    val USER_ID = "uidx"
    val COUNTER_ID = "counter"
    val statusLogin = "STATUS"
    val statusUserSlur = "STATUS_USER"
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

    fun saveUIDD(uidd: String) {
        val edit = sharedSet.edit()
        edit.putString(USER_ID, uidd)
        edit.apply()
    }

    fun getUID(): String? {
        return sharedSet.getString(USER_ID, " ")
    }

    fun setStatusUser(statusUser: Boolean) {
        val edit = sharedSet.edit()
        edit.putBoolean(statusUserSlur, statusUser)
        edit.apply()
    }

    fun saveCounterId(counter: Int) {
        val edit = sharedSet.edit()
        edit.putInt(COUNTER_ID, counter)
        edit.apply()
    }

    fun getCounterId(): Int {
        return sharedSet.getInt(COUNTER_ID, 1)
    }

    fun getCountId(): Int {
        return sharedSet.getInt(COUNTER_ID, 0)
    }


    fun setEmail(email: String) {
        val editor: SharedPreferences.Editor = sharedSet.edit()
        editor.putString("EMAIL", email)
        editor.apply()
    }

    fun setStatusInput(status: Boolean) {
        val editor: SharedPreferences.Editor = sharedSet.edit()
        editor.putBoolean("STATUS", status)
        editor.apply()
    }

    fun setStatus(status: Boolean) {
        val edit = sharedSet.edit()
        edit.putBoolean(statusLogin, status)
        edit.apply()
    }

    fun cekStatus(): Boolean? {
        return sharedSet.getBoolean(statusLogin, false)
    }

    fun setStartDate(startdate: Boolean) {
        val edit = sharedSet.edit()
        edit.putBoolean("START DATE", startdate)
        edit.apply()
    }

    fun cekStartDate(): Boolean? {
        return sharedSet.getBoolean("START DATE", false)
    }

    fun setNama(nama: String) {
        val edit = sharedSet.edit()
        edit.putString("nama", nama)
        edit.apply()
    }

    fun getNama(): String? {
        return sharedSet.getString("nama", "")
    }

    fun setStatusGoogle(statusGoogle: Boolean) {
        val edit = sharedSet.edit()
        edit.putBoolean("sGoogle", statusGoogle)
        edit.apply()
    }

    fun cekStatusGoogle(): Boolean? {
        return sharedSet.getBoolean("sGoogle", false)
    }
}