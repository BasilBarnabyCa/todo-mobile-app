package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.todoit.databinding.ActivityListBinding
import com.google.firebase.firestore.FirebaseFirestore

/**
 * ListActivity displays all tasks in a RecyclerView.
 *
 * @property binding The view binding for the list activity layout.
 * @property allTasks Array of Task objects representing all tasks.
 */
class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize our Firestore and DataManager
        FirebaseFirestore.setLoggingEnabled(true)
        dataManager = DataManager.instance()

        val adapter = TaskAdapter ({task: Task ->
            val intent = Intent(this, DetailsActivity::class.java).apply {
                putExtra("taskId", task.id)
            }
            startActivity(intent)
        }, viewModel)

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = adapter

        // Observe the LiveData from the ViewModel
        viewModel.tasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }

        // Load all Tasks rom the database manager via viewModel
        viewModel.loadAllTasks()

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

        binding.menuBar.userProfileButton.setOnClickListener {
            Log.d("MenuBar", "User profile button clicked")

            startActivity(Intent(this, UserProfileActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAllTasks()
    }
}