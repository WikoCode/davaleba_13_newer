package com.example.myapplication

import android.app.DatePickerDialog
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ChooserBinding
import com.example.myapplication.databinding.InputBinding
import java.util.Calendar

class FieldsAdapter(
    private val dataset: List<List<FieldsData>>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_INPUT -> {
                val inputBinding = InputBinding.inflate(inflater, parent, false)
                InputViewHolder(inputBinding)
            }

            VIEW_TYPE_CHOOSER -> {
                val chooserBinding = ChooserBinding.inflate(inflater, parent, false)
                ChooserViewHolder(chooserBinding)
            }

            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }


    override fun getItemCount(): Int {
        return dataset.flatten().size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.fieldType) {
            "input" -> (holder as InputViewHolder).bindInput(item)
            "chooser" -> (holder as ChooserViewHolder).bindChooser(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return dataset.flatten()[position].viewType
    }


    private fun getItem(position: Int): FieldsData {
        val flattenedList = dataset.flatten()
        return flattenedList[position]
    }

    companion object {
        const val VIEW_TYPE_INPUT = 1
        const val VIEW_TYPE_CHOOSER = 2
    }

    class InputViewHolder(private val binding: InputBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindInput(item: FieldsData) {
            binding.etInput.id = item.fieldId
            binding.etInput.hint = item.hint




            when (item.keyboard) {
                "text" -> binding.etInput.inputType = InputType.TYPE_CLASS_TEXT
                "number" -> binding.etInput.inputType = InputType.TYPE_CLASS_NUMBER
            }


        }
    }

    class ChooserViewHolder(private val binding: ChooserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindChooser(item: FieldsData) {
            binding.tvChooser.id = item.fieldId
            binding.tvChooser.text = item.hint




            binding.tvChooser.setOnClickListener {
                when (item.hint) {
                    "Birthday" -> {
                        handleBirthdayClick(item)
                    }

                    "Gender" -> {
                        handleGenderClick(item)
                    }
                }
            }
        }

        private fun handleGenderClick(item: FieldsData) {

            val genderOptions = arrayOf("Male", "Female", "Attack Helicopter")
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setTitle("Select Gender")
                .setItems(genderOptions) { _, which ->
                    val selectedGender = genderOptions[which]

                    binding.tvChooser.text = selectedGender
                }
            builder.create().show()

        }

        private fun handleBirthdayClick(item: FieldsData) {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                binding.root.context,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"

                    binding.tvChooser.text = selectedDate
                },
                currentYear,
                currentMonth,
                currentDay
            )

            datePickerDialog.show()
        }
    }



}