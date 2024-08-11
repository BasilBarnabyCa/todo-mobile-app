package ca.georgiancollege.todoit

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
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
 * Activity for editing existing tasks with details including category, notes, due date, and creation date.
 */
class EditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTaskBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var dataManager: DataManager
    private var taskId: String? = null
    private var isPinned: Boolean = false
    private var currentTask: Task? = null
    private var createDate: String = ""
    private var calendar = Calendar.getInstance()

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
                    val inputFormat = SimpleDateFormat(
                        "MM/dd/yyyy",
                        Locale.getDefault()
                    )
                    val parsedDate = inputFormat.parse(task.dueDate)
                    val formattedDate = parsedDate?.let { inputFormat.format(it) } ?: ""
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

        bindMenuBarButtons()
        initializeFormElements()
    }

    private fun initializeFormElements() {
        // Set click listener for select date button
        binding.selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }

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
        val statusOptions = arrayOf("Not complete", "In progress", "Complete")

        // Create an ArrayAdapter using the custom spinner item layout and dropdown layout
        val statusAdapter = ArrayAdapter(this, R.layout.spinner_item, statusOptions)
        statusAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        // Apply the adapter to the spinner
        binding.statusSpinner.adapter = statusAdapter

        // Toggle visibility of selected date layout based on switch state
        binding.selectedDateLinearLayout.visibility = View.GONE

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
    }

    private fun bindMenuBarButtons() {
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

    private fun updateTask() {
        taskId?.let {
            val name = binding.nameEditTextView.text.toString()
            val notes = binding.notesEditTextView.text.toString()
            val status = binding.statusSpinner.selectedItem.toString()
            val category = binding.categorySpinner.selectedItem.toString()
            val hasDueDate = binding.dueDateToggleSwitch.isChecked
            val dueDate = if (hasDueDate) binding.selectedDateLabelTextView.text.toString() else ""
            val isCompleted = status == "Complete"

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