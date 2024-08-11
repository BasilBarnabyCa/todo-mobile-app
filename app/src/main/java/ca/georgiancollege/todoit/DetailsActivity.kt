package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.todoit.databinding.ActivityDetailsBinding

/**
 * DetailsActivity displays task details and handles user interactions.
 *
 * @property binding The view binding for the details activity layout.
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

        if (taskId != null) {
            viewModel.loadTaskById(taskId!!)
        } else {
            Toast.makeText(this, "Invalid task ID", Toast.LENGTH_SHORT).show()
        }

        // Observe the LiveData from the ViewModel
        viewModel.task.observe(this) { task ->
            task?.let {
                val status = task.status
                val category = task.category
                val dueDate = task.dueDate

                binding.detailsTitleTextView.text = task.name
                binding.notesTextView.text = task.notes
                binding.createdDateTextView.text = task.createDate

                if (dueDate.isEmpty()) {
                    binding.dueDateTextView.text = getString(R.string.due_date_not_set_text)
                } else {
                    binding.dueDateTextView.text = dueDate
                }

                when (status) {
                    "Not Started" -> {
                        binding.statusTextView.setTextColor(getColor(R.color.light_gray))
                    }
                    "In Progress" -> {
                        binding.statusTextView.setTextColor(getColor(R.color.sky))
                    }
                    "Complete" -> {
                        binding.statusTextView.setTextColor(getColor(R.color.emerald))
                    }
                    else -> {
                        Log.e("DetailsActivity", "Invalid status: $status")
                    }
                }
                binding.statusTextView.text = status

                when (category) {
                    "Fitness" -> {
                        binding.categoryImageView.setImageResource(R.drawable.ic_power_lifting)
                    }
                    "School" -> {
                        binding.categoryImageView.setImageResource(R.drawable.ic_book)
                    }
                    "Work" -> {
                        binding.categoryImageView.setImageResource(R.drawable.ic_briefcase)
                    }
                    "Personal" -> {
                        binding.categoryImageView.setImageResource(R.drawable.ic_organic)
                    }
                    else -> {
                        Log.e("DetailsActivity", "Invalid category: $category")
                    }
                }
                binding.categoryTextView.text = category
            }
        }

        bindActionButtons()
        bindMenuBarButtons()
    }

    private fun bindActionButtons() {
        // Set click listeners for status buttons
        binding.notStartedIconButton.setOnClickListener {
            binding.statusTextView.text = getString(R.string.not_started_text)
            binding.statusTextView.setTextColor(getColor(R.color.light_gray))
            Toast.makeText(this, "Status changed to Not Started!", Toast.LENGTH_SHORT).show()
        }

        binding.inProgressIconButton.setOnClickListener {
            binding.statusTextView.text = getString(R.string.in_progress_text)
            binding.statusTextView.setTextColor(getColor(R.color.sky))
            Toast.makeText(this, "Status changed to In Progress!", Toast.LENGTH_SHORT).show()
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
        }

        binding.deleteIconButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.task.value?.let {
                        viewModel.deleteTask(it)
                        Toast.makeText(this, "Task Deleted!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ListActivity::class.java))
                        finish()
                    }
                }
                .setNegativeButton("No", null)
                .show()
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
}