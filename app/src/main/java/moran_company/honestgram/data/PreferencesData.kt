package moran_company.honestgram.data

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.preference.PreferenceManager
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer
import com.google.gson.Gson

import moran_company.honestgram.HonestApplication
import moran_company.honestgram.utility.ObjectSerializer


object PreferencesData {

    private val KEY_PROFILE = "profilePrefs"
    private val KEY_DIALOGS = "profileDialogs"
    private val KEY_USERS = "profileUsers"


    private val applicationContext: Context
        get() = HonestApplication.getInstance()

    fun saveUser(user: Users) {
        var json = Gson().toJson(user)
        save(KEY_PROFILE, json)
    }

    fun getUser(): Users? = Gson().fromJson(getString(KEY_PROFILE, ""), Users::class.java)

    fun resetProfile() {
        save(KEY_PROFILE, null)
    }

    fun saveUserDialogs(listDialogs: ArrayList<List<Dialogs>>) {
        save(KEY_DIALOGS, ObjectSerializer.serialize(listDialogs))
    }

    fun getUserDialogs(): ArrayList<List<Dialogs>>? =
            ObjectSerializer
                    .deserialize(getString(KEY_DIALOGS,ObjectSerializer.serialize(ArrayList<List<Dialogs>>()))) as ArrayList<List<Dialogs>>?

    fun resetUserDialogs() {
        save(KEY_DIALOGS, null)
    }

    fun saveUsers(listDialogs: ArrayList<Users>) {
        save(KEY_USERS, ObjectSerializer.serialize(listDialogs))
    }

    fun getUsers(): ArrayList<Users>? =
            ObjectSerializer
                    .deserialize(getString(KEY_USERS,ObjectSerializer.serialize(ArrayList<Users>()))) as ArrayList<Users>?

    fun resetUsers() {
        save(KEY_USERS, null)
    }


    ////////////////////
    fun getInt(key: String, defValue: Int): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return prefs.getInt(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return prefs.getLong(key, defValue)
    }

    fun getString(key: String, defValue: String): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return prefs.getString(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return prefs.getBoolean(key, defValue)
    }

    fun save(key: String, value: String?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun save(key: String, value: Long) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = prefs.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun save(key: String, value: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun save(key: String, value: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

}