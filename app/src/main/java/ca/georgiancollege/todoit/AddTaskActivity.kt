package ca.georgiancollege.todoit

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.todoit.databinding.ActivityAddTaskBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Activity for adding new tasks with details including category, notes, due date, and creation date.
 */
class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private var calendar = Calendar.getInstance()
    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listeners for menu bar buttons
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

        // Set click listeners for cancel and save buttons
        binding.cancelButton.setOnClickListener {
            Log.d("CancelButton", "Cancel button clicked")

            finish()
        }

        binding.saveButton.setOnClickListener {
            Log.d("SaveButton", "Save button clicked")

            val incomingDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

            if(binding.nameEditTextView.text.toString().isEmpty() || binding.notesEditTextView.text.toString().isEmpty() || binding.categorySpinner.selectedItem.toString().isEmpty()) {
                Toast.makeText(this, "Name, category, and notes are required", Toast.LENGTH_SHORT).show()
            } else {
                val dueDate = if (binding.selectedDateLabelTextView.text.toString() == "Please select a date") {
                    ""
                } else {
                    binding.selectedDateLabelTextView.text.toString()
                }

                // Create an intent to pass data to DetailsActivity
                val intent = Intent(this, DetailsActivity::class.java).apply {
                    putExtra("category", binding.categorySpinner.selectedItem.toString())
                    putExtra("title", binding.nameEditTextView.text.toString())
                    putExtra("notes", binding.notesEditTextView.text.toString())
                    putExtra("status", "Not started")
                    putExtra("dueDate",
                        incomingDateFormat.parse(dueDate)?.let { date -> dateFormat.format(date) })
                    putExtra("createDate", dateFormat.format(Date()))
                }

                Toast.makeText(this, "Task added successfully!", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }

        }

        // Set click listener for select date button
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

    /**
     * Shows a date picker dialog.
     */
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