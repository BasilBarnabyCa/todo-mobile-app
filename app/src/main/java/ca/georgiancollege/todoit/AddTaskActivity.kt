package ca.georgiancollege.todoit

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.todoit.databinding.ActivityAddTaskBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private var calendar = Calendar.getInstance()
    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.menuBar.homeButton.setOnClickListener {
            Log.d("MenuBar", "Home button clicked")

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.menuBar.calendarButton.setOnClickListener {
            Log.d("MenuBar", "Calendar button clicked")

            startActivity(Intent(this, CalendarActivity::class.java))
            finish()
        }

        binding.menuBar.listButton.setOnClickListener {
            Log.d("MenuBar", "List button clicked")

            startActivity(Intent(this, ListActivity::class.java))
            finish()
        }

        binding.menuBar.userProfileButton.setOnClickListener {
            Log.d("MenuBar", "User profile button clicked")

            startActivity(Intent(this, UserProfileActivity::class.java))
            finish()
        }

        binding.cancelButton.setOnClickListener {
            Log.d("CancelButton", "Cancel button clicked")

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.saveButton.setOnClickListener {
            Log.d("SaveButton", "Save button clicked")

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        // Selection Options for spinner
        val categories = arrayOf("Work", "Personal", "School", "Fitness")

        // Create an ArrayAdapter using the custom spinner item layout and dropdown layout
        val adapter = ArrayAdapter(this, R.layout.spinner_item, categories)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        // Apply the adapter to the spinner
        binding.categorySpinner.adapter = adapter


        // Toggle visibility of selected date layout based on switch state
        binding.selectedDateLinearLayout.visibility = View.GONE

        binding.dueDateToggleSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.selectedDateLinearLayout.visibility = View.VISIBLE
            } else {
                binding.selectedDateLinearLayout.visibility = View.GONE
                binding.selectedDateLabelTextView.text = context.getString(R.string.select_a_date_label_text)
            }
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(this, {_,year: Int, month: Int, day: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day)
            val dateFormat = SimpleDateFormat("MM/dd/yyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            binding.selectedDateLabelTextView.text = formattedDate
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}