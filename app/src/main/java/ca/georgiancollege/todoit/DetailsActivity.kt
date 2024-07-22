package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get task data from intent
        val category = intent.getStringExtra("category")
        val title = intent.getStringExtra("title")
        val notes = intent.getStringExtra("notes")
        val status = intent.getStringExtra("status")
        val dueDate = intent.getStringExtra("dueDate")
        val createDate = intent.getStringExtra("createDate")

        // Set task data to text views with appropriate conditions
        binding.detailsTitleTextView.text = title
        binding.notesTextView.text = notes

        if (dueDate.isNullOrEmpty()) {
            binding.dueDateTextView.text = getString(R.string.due_date_not_set_text)
        } else {
            binding.dueDateTextView.text = dueDate
        }
        binding.createdDateTextView.text = createDate

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
            startActivity(Intent(this, EditTaskActivity::class.java))
        }

        binding.deleteIconButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes") { _, _ ->
                    Toast.makeText(this, "Task Deleted!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ListActivity::class.java))
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}