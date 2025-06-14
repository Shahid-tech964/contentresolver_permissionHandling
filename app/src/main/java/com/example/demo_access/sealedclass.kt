package com.example.demo_access

sealed class sealedclass() {
    data object loading:sealedclass()
    data class suceess(val mess:ArrayList<modelClass>):sealedclass()
   data class error(val error:String):sealedclass()
}