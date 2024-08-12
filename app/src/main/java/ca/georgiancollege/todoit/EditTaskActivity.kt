/** Author: Basil Barnaby
 * Student Number: 200540109
 * Course: COMP3025 - Mobile and Pervasive Computing
 * Assignment: 4 - Todo App
 * Date: August 11, 2024
 * Description: This is a todo app that allows users to add, edit, and delete tasks.
 * App Name: Todo.iT
 * Target Device: Google Pixel 8 Pro
 * Version: 1.0
 *
 * Filename: EditTaskActivity.kt
 */

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
import ca.georgiancollege.todoit.databinding.ActivityEditTaskBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * EditTaskActivity allows users to edit the details of an existing task, including the name, category, status, notes, due date, and pin status.
 *
 * @property binding The view binding for the edit task activity layout, providing access to UI elements.
 * @property viewModel The ViewModel instance for managing and observing the task data.
 * @property dataManager A singleton instance of DataManager for managing task data interactions.
 * @property taskId The ID of the task being edited, retrieved from the intent.
 * @property isPinned Boolean indicating whether the task is pinned.
 * @property createDate The creation date of the task, used when updating the task.
 * @property calendar Calendar instance used for date selection.
 */
class EditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTaskBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var dataManager: DataManager
    private var taskId: String? = null
    private var isPinned: Boolean = false
    private var createDate: String = ""
    private var calendar = Calendar.getInstance()

    /**
     * Called when the activity is first created.
     * Initializes the UI components, loads task details, and sets up event handlers.
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Alias for the DataManager singleton
        dataManager = DataManager.instance()

        taskId = intent.getStringExtra("taskId")

        // Load task details based on taskId
        taskId?.let {
            viewModel.loadTaskById(it)
        }

        // Observe the LiveData from the ViewModel
        viewModel.task.observe(this) { task ->
            task?.let {
                isPinned = task.pinned
                createDate = task.createDate
                binding.nameEditTextView.setText(task.name)
                binding.notesEditTextView.setText(task.notes)

                if (!task.hasDueDate) {
                    binding.selectedDateLabelTextView.text =
                        getString(R.string.due_date_not_set_text)
                } else {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                    val parsedDate = inputFormat.parse(task.dueDate)
                    val formattedDate = parsedDate?.let { outputFormat.format(it) } ?: ""
                    binding.selectedDateLabelTextView.text = formattedDate
                }

                // Set status spinner
                val statusAdapter = binding.statusSpinner.adapter as ArrayAdapter<String>
                binding.statusSpinner.setSelection(statusAdapter.getPosition(task.status))

                // Set category spinner
                val categoryAdapter = binding.categorySpinner.adapter as ArrayAdapter<String>
                binding.categorySpinner.setSelection(categoryAdapter.getPosition(task.category))

                // Set due date toggle switch and visibility
                binding.dueDateToggleSwitch.isChecked = task.hasDueDate
                binding.selectedDateLinearLayout.visibility =
                    if (task.hasDueDate) View.VISIBLE else View.GONE

                // Handle pin state
                togglePinState(task.pinned)
            }
        }

        initializeFormElements()
        setupEventHandlers()
    }

    /**
     * Initializes form elements such as spinners and handles initial UI setup.
     */
    private fun initializeFormElements() {
        // Handle pin icon click
        binding.pinImageView.setOnClickListener {
            isPinned = !isPinned
            togglePinState(isPinned)
        }

        // Selection options for category spinner
        val categories = arrayOf("Work", "Personal", "School", "Fitness")

        // Create an ArrayAdapter using the custom spinner item layout and dropdown layout
        val categoryAdapter = ArrayAdapter(this, R.layout.spinner_item, categories)
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        // Apply the adapter to the spinner
        binding.categorySpinner.adapter = categoryAdapter

        // Selection options for status spinner
        val statusOptions = arrayOf("Not started", "In progress", "Complete")

        // Create an ArrayAdapter using the custom spinner item layout and dropdown layout
        val statusAdapter = ArrayAdapter(this, R.layout.spinner_item, statusOptions)
        statusAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        // Apply the adapter to the spinner
        binding.statusSpinner.adapter = statusAdapter

        // Set visibility of selected date layout based onCreate
        binding.selectedDateLinearLayout.visibility = View.GONE
    }

    /**
     * Sets up event handlers for various UI elements, including the date picker, pin toggle, and update button.
     */
    private fun setupEventHandlers() {

        // Text watcher for name edit text
        binding.nameEditTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val isNameValid =
                    (s?.length ?: 0) >= 4 // Ensure that the length is at least 4 characters
                if (isNameValid) {
                    binding.updateButton.visibility = View.VISIBLE
                    binding.dueDateToggleLinearLayout.visibility = View.VISIBLE
                    binding.selectedDateLinearLayout.visibility = View.VISIBLE
                } else {
                    binding.updateButton.visibility = View.GONE
                    binding.dueDateToggleLinearLayout.visibility = View.GONE
                    binding.selectedDateLinearLayout.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Set click listener for select date button
        binding.selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        // Set click listener for due date toggle switch
        binding.dueDateToggleSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.selectedDateLinearLayout.visibility = View.VISIBLE
            } else {
                binding.selectedDateLinearLayout.visibility = View.GONE
                binding.selectedDateLabelTextView.text =
                    this.getString(R.string.select_a_date_label_text)
            }
        }

        binding.updateButton.setOnClickListener {
            updateTask()
        }

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

        // Set click listeners for cancel and update buttons
        binding.cancelButton.setOnClickListener {
            Log.d("CancelButton", "Cancel button clicked")

            finish()
        }
    }

    /**
     * Shows a date picker dialog for selecting the due date.
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

    /**
     * Toggles the pin state of the task and updates the UI accordingly.
     *
     * @param pinState Boolean indicating the current pin state of the task.
     */
    private fun togglePinState(pinState: Boolean) {
        if (pinState) {
            binding.pinImageView.alpha = 1.0f
            binding.pinImageView.setImageResource(R.drawable.ic_pinned)
            isPinned = true
        } else {
            binding.pinImageView.alpha = resources.getFloat(R.dimen.dimmed_image_alpha)
            binding.pinImageView.setImageResource(R.drawable.ic_unpinned)
        }
    }

    /**
     * Updates the task with the current values from the UI and saves it to the database.
     */
    private fun updateTask() {
        taskId?.let {
            val displayDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val storageDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val name = binding.nameEditTextView.text.toString()
            val notes = binding.notesEditTextView.text.toString()
            val status = binding.statusSpinner.selectedItem.toString()
            val category = binding.categorySpinner.selectedItem.toString()
            val hasDueDate = binding.dueDateToggleSwitch.isChecked
            val isCompleted = status == "Complete"

            val dueDate = if (hasDueDate && binding.selectedDateLabelTextView.text.toString().isNotEmpty()) {
                displayDateFormat.parse(binding.selectedDateLabelTextView.text.toString())?.let { date ->
                    storageDateFormat.format(date)
                } ?: ""
            } else {
                ""
            }

            val updatedTask = Task(
                id = it,
                name = name,
                notes = notes,
                status = status,
                category = category,
                hasDueDate = hasDueDate,
                dueDate = dueDate,
                pinned = isPinned,
                completed = isCompleted,
                createDate = createDate
            )

            viewModel.saveTask(updatedTask)
            Toast.makeText(this, "Task updated successfully!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("taskId", it)
            startActivity(intent)
            finish()
        } ?: run {
            Toast.makeText(this, "Error updating task", Toast.LENGTH_SHORT).show()
        }
    }



}