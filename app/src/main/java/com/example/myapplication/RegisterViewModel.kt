package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    val fieldValuesMap = mutableMapOf<Int, MutableLiveData<String>>()

    fun initFieldLiveData(fieldId: Int) {
        fieldValuesMap[fieldId] = MutableLiveData()
    }
}