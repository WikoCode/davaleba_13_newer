package com.example.myapplication

import android.content.Context
import android.util.Log.d
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class JsonParser(private val context: Context) {

    fun parseJsonFromAssets(fileName: String): List<List<FieldsData>>? {
        val json: String? = try {
            val inputStream: InputStream = context.assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }

        return json?.let {
            val gson = GsonBuilder().create()
            val listType = object : TypeToken<List<List<FieldsData>>>() {}.type
            val fieldsDataList: List<List<FieldsData>> = gson.fromJson(it, listType)

            // Set viewType based on fieldType
            fieldsDataList.flatten().forEach { field ->
                field.viewType = when (field.fieldType) {
                    "input" -> FieldsAdapter.VIEW_TYPE_INPUT
                    "chooser" -> FieldsAdapter.VIEW_TYPE_CHOOSER
                    else -> d("Unexpected fieldType:", "${field.fieldType}")
                }
            }

            fieldsDataList
        }
    }
}

