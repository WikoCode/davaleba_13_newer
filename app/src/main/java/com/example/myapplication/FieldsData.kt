package com.example.myapplication

data class FieldsData(
    val fieldId: Int,
    val fieldType: String?,
    val hint: String,
    val icon: String,
    val isActive: Boolean,
    val keyboard: String?,
    val required: Boolean,
    var viewType: Int

)
