package com.example.myapplication

import android.os.Bundle
import android.util.Log.d
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentRegisterBinding

class RegisterFragment(inflate: Inflater<FragmentRegisterBinding>) :
    BaseFragment<FragmentRegisterBinding>(
        inflate
    ) {

    private var fieldsData: List<List<FieldsData>>? = null
    private lateinit var registerViewModel: RegisterViewModel



    override fun setupUI() {

    }

    override fun setupListeners() {
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]


        val jsonParser = JsonParser(requireContext())
        fieldsData = jsonParser.parseJsonFromAssets("fields.json")

        fieldsData?.let {
            d("Dataset Size", it.flatten().size.toString())

            it.flatten().take(7).forEachIndexed { index, field ->
                d("Field[$index]", field.toString())
            }

            it.flatten().forEach { field ->
                field.viewType = when (field.fieldType) {
                    "input" -> FieldsAdapter.VIEW_TYPE_INPUT
                    "chooser" -> FieldsAdapter.VIEW_TYPE_CHOOSER
                    else -> d("Unexpected fieldType:", "${field.fieldType}")

                }
            }

            val fieldsAdapter = FieldsAdapter(it)

            binding.rvFields.adapter = fieldsAdapter
            binding.rvFields.layoutManager = LinearLayoutManager(requireContext())

            binding.btnRegister.setOnClickListener {
                validateFields()
            }

        }
    }

    private fun validateFields() {
        var isValid = true

        fieldsData?.forEach { fieldList ->
            fieldList.forEach { field ->
                val value = registerViewModel.fieldValuesMap[field.fieldId]?.value ?: ""

                when (field.fieldType) {
                    "input" -> {
                        if (!validateField(value, field)) {
                            isValid = false
                            return@forEach
                        }
                    }
                    "chooser" -> {
                        if (!validateField(value, field)) {
                            isValid = false
                            return@forEach
                        }
                    }
                }
            }
        }
        if (isValid) {
            registerViewModel.fieldValuesMap.forEach { (_, valueLiveData) ->
                valueLiveData.value = ""
            }

            showToast("Fields are valid!")
        } else {
            showToast("Validation failed. Please check your input.")
        }
    }

    private fun validateField(value: String, field: FieldsData): Boolean {
        // Validation logic for each field type
        return when (field.hint) {
            "Email" -> Patterns.EMAIL_ADDRESS.matcher(value).matches()
            "phone" -> value.isNotEmpty()
            "FullName" -> value.isNotEmpty()
            else -> true
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}
