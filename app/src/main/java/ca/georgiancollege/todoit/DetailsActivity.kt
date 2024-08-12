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
 * Filename: DetailsActivity.kt
 */

package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.todoit.databinding.ActivityDetailsBinding
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * DetailsActivity displays detailed information about a specific task.
 * It allows users to view, edit, delete, and update the status of the task.
 *
 * @property binding The view binding for the details activity layout, providing access to UI elements.
 * @property viewModel The ViewModel instance for managing and observing the task data.
 * @property dataManager A singleton instance of DataManager for managing task data interactions.
 * @property taskId The ID of the task being displayed, retrieved from the intent.
 */
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var dataManager: DataManager
    private var taskId: String? = null

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Alias for the DataManager singleton
        dataManager = DataManager.instance()

        taskId = intent.getStringExtra("taskId")
        checkTaskId()

        // Observe the LiveData from the ViewModel
        viewModel.task.observe(this) { task ->
            task?.let {
                val incomingDateFormat =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val displayDateFormat =
                    SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

                val dueDate = if (task.hasDueDate && task.dueDate.isNotEmpty()) {
                    incomingDateFormat.parse(task.dueDate)
                        ?.let { date -> displayDateFormat.format(date) } ?: "Not set"
                } else {
                    "Not set"
                }

                val createDate = if (task.createDate.isNotEmpty()) {
                    incomingDateFormat.parse(task.createDate)
                        ?.let { date -> displayDateFormat.format(date) } ?: "Not set"
                } else {
                    "Not set"
                }

                binding.detailsTitleTextView.text = task.name
                binding.notesTextView.text = task.notes
                binding.createdDateTextView.text = createDate
                binding.dueDateTextView.text = dueDate

                when (task.status) {
                    "Not started" -> binding.statusTextView.setTextColor(getColor(R.color.light_gray))
                    "In progress" -> binding.statusTextView.setTextColor(getColor(R.color.sky))
                    "Complete" -> binding.statusTextView.setTextColor(getColor(R.color.emerald))
                    else -> Log.e("DetailsActivity", "Invalid status: ${task.status}")
                }
                binding.statusTextView.text = task.status

                when (task.category) {
                    "Fitness" -> binding.categoryImageView.setImageResource(R.drawable.ic_power_lifting)
                    "School" -> binding.categoryImageView.setImageResource(R.drawable.ic_book)
                    "Work" -> binding.categoryImageView.setImageResource(R.drawable.ic_briefcase)
                    "Personal" -> binding.categoryImageView.setImageResource(R.drawable.ic_organic)
                    else -> Log.e("DetailsActivity", "Invalid category: ${task.category}")
                }
                binding.categoryTextView.text = task.category
            }
        }


        setupEventHandlers()
    }

    private fun setupEventHandlers() {
        // Set click listeners for status buttons
        binding.notStartedIconButton.setOnClickListener {
            binding.statusTextView.text = getString(R.string.not_started_text)
            binding.statusTextView.setTextColor(getColor(R.color.light_gray))
            Toast.makeText(this, "Status changed to Not started!", Toast.LENGTH_SHORT).show()
        }

        binding.inProgressIconButton.setOnClickListener {
            binding.statusTextView.text = getString(R.string.in_progress_text)
            binding.statusTextView.setTextColor(getColor(R.color.sky))
            Toast.makeText(this, "Status changed to In progress!", Toast.LENGTH_SHORT).show()
        }

        binding.completeIconButton.setOnClickListener {
            binding.statusTextView.text = getString(R.string.complete_text)
            binding.statusTextView.setTextColor(getColor(R.color.emerald))
            Toast.makeText(this, "Status changed to Complete!", Toast.LENGTH_SHORT).show()
        }

        // Set click listeners for edit, delete, and back buttons
        binding.backIconButton.setOnClickListener {
            finish()
        }

        binding.editIconButton.setOnClickListener {
            val intent = Intent(this, EditTaskActivity::class.java).apply {
                putExtra("taskId", taskId)
            }
            startActivity(intent)
            finish()
        }

        binding.deleteIconButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.task.value?.let {
                        viewModel.deleteTask(it)
                        Toast.makeText(this, "Task Deleted!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .setNegativeButton("No", null)
                .show()
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

        binding.menuBar.addTaskButton.setOnClickListener {
            Log.d("MenuBar", "Add task button clicked")

            startActivity(Intent(this, AddTaskActivity::class.java))
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
    }

    override fun onResume() {
        super.onResume()
        taskId = intent.getStringExtra("taskId")
        checkTaskId()
    }

    private fun checkTaskId() {
        if (taskId != null) {
            viewModel.loadTaskById(taskId!!)
        } else {
            Toast.makeText(this, "Invalid task ID", Toast.LENGTH_SHORT).show()
        }
    }
}