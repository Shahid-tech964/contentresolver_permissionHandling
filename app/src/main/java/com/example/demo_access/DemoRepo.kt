package com.example.demo_access

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import javax.inject.Inject


class  DemoRepo @Inject constructor() {
    val uri = Uri.parse("content://com.example.contentproivder/users")

    @SuppressLint("SuspiciousIndentation")
    fun getUsers(context: Context): ArrayList<modelClass> {

        val cursor = context.contentResolver.query(uri, null, null, null, null)
        val userList = arrayListOf<modelClass>()


        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(0)
                val name = it.getString(1)
                val course = it.getString(2)
                userList.add(modelClass(id, name, course))
            }
        }
        return userList
    }
}

