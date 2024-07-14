package com.sangavi.gendigital.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sangavi.gendigital.ui.user.model.UserListUIData

object SharedPrefManager {

    private const val PREF_NAME = "MySharedPrefs"
    private const val USER_LIST_UI_DATA_KEY = "UserListUIData"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserListUIData(context: Context, userListUIData: UserListUIData) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = gson.toJson(userListUIData)

        editor.putString(USER_LIST_UI_DATA_KEY, json)
        editor.apply()
    }

    fun getUserListUIData(context: Context): UserListUIData? {
        val sharedPreferences = getSharedPreferences(context)
        val gson = Gson()
        val json = sharedPreferences.getString(USER_LIST_UI_DATA_KEY, null)

        return if (json != null) {
            gson.fromJson(json, UserListUIData::class.java)
        } else {
            null
        }
    }

    fun getUserID(context: Context) : Int? {
        val userListUIData = getUserListUIData(context)
        return userListUIData?.id
    }

    fun clearUserListUIData(context: Context) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()

        editor.remove(USER_LIST_UI_DATA_KEY)
        editor.apply()
    }
}
