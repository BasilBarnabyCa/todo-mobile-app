package ca.georgiancollege.todoit

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.todoit.databinding.ActivityAddTaskBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Activity for adding new tasks with details including category, notes, due date, and creation date.
 */
class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private var calendar = Calendar.getInstance()
    private val context = this
    private val viewModel: TaskViewModel by viewModels()
    private var isPinned = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeFormElements()
        setupEventHandlers()
    }

    private fun initializeFormElements() {

        // Handle pin icon click
        binding.pinImageView.setOnClickListener {
            isPinned = !isPinned
            if (isPinned) {
                binding.pinImageView.alpha = 1.0f
                binding.pinImageView.setImageResource(R.drawable.ic_pinned)
            } else {
                binding.pinImageView.alpha = context.resources.getFloat(R.dimen.dimmed_image_alpha)
                binding.pinImageView.setImageResource(R.drawable.ic_unpinned)
            }
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
                binding.selectedDateLabelTextView.text =
                    context.getString(R.string.select_a_date_label_text)
            }
        }

        binding.saveButton.setOnClickListener {
            saveTask()
        }

        // Set click listener for select date button
        binding.selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setupEventHandlers() {

        // Text watcher for name edit text
        binding.nameEditTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val isNameValid =
                    (s?.length ?: 0) >= 4 // Ensure that the length is at least 4 characters
                if (isNameValid) {
                    binding.saveButton.visibility = View.VISIBLE
                    binding.dueDateLabelTextView.visibility = View.VISIBLE
                    binding.dueDateToggleSwitch.visibility = View.VISIBLE
                } else {
                    binding.saveButton.visibility = View.GONE
                    binding.dueDateLabelTextView.visibility = View.GONE
                    binding.dueDateToggleSwitch.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

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
    }

    /**
     * Shows a date picker dialog.
     */
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this, { _, year: Int, month: Int, day: Int ->
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

    private fun saveTask() {
        val displayDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val storageDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (binding.nameEditTextView.text.toString()
                .isEmpty() || binding.notesEditTextView.text.toString()
                .isEmpty() || binding.categorySpinner.selectedItem.toString().isEmpty()
        ) {
            Toast.makeText(this, "Name, category, and notes are required", Toast.LENGTH_SHORT)
                .show()
        } else {
            val dueDate =
                if (binding.selectedDateLabelTextView.text.toString() == "Please select a date") {
                    ""
                } else {
                    binding.selectedDateLabelTextView.text.toString()
                }

            val task = Task(
                id = UUID.randomUUID().toString(),
                category = binding.categorySpinner.selectedItem.toString(),
                name = binding.nameEditTextView.text.toString(),
                notes = binding.notesEditTextView.text.toString(),
                status = "Not started",
                completed = false,
                pinned = isPinned,
                hasDueDate = dueDate.isNotEmpty(),
                dueDate = if (dueDate.isEmpty()) "" else displayDateFormat.parse(dueDate)
                    ?.let { date -> storageDateFormat.format(date) } ?: "",
                createDate = storageDateFormat.format(Date()) // Store the current date in ISO format
            )

            viewModel.saveTask(task)

            Toast.makeText(this, "Task added successfully!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("taskId", task.id)
            startActivity(intent)
            finish()
        }
    }
}