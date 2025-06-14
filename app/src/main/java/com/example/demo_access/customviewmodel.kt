package com.example.demo_access

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class customviewmodel @Inject constructor(val demoRepo: DemoRepo):ViewModel() {
    val mutableLiveData=MutableLiveData<sealedclass>()
    val liveData:LiveData<sealedclass> =mutableLiveData


    fun fetch_result(context: Context){
        try {
            viewModelScope.launch {
                mutableLiveData.value=sealedclass.loading
                delay(2000)

                mutableLiveData.value=sealedclass.suceess(demoRepo.getUsers(context))
            }
        }
        catch (e:Exception){

            Log.d("Error", "Error->${e.message} ")
        }

    }

    //add user function
    fun addUser(name:String,course:String,context: Context){

        val value= ContentValues().apply {
            put("Name",name)
            put("Course",course)
        }
        context.contentResolver.insert(demoRepo.uri,value)

        mutableLiveData.value=sealedclass.suceess(demoRepo.getUsers(context))
    }


//delete user function

    fun deleteUser(context: Context,id:Int){
        context.contentResolver.delete(demoRepo.uri,"id=?", arrayOf("$id"))

        mutableLiveData.value=sealedclass.suceess(demoRepo.getUsers(context))
    }
}