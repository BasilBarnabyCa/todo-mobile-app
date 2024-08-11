package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.todoit.databinding.ActivityCalendarViewBinding
import com.google.firebase.firestore.FirebaseFirestore

/**
 * CalendarActivity displays tasks scheduled for today and handles user interactions.
 *
 * @property binding The view binding for the calendar activity layout.
 * @property todayTasks Array of Task objects representing today's tasks.
 */
class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarViewBinding
//    private lateinit var todayTasks: Array<Task>
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var dataManager: DataManager

    // Adapter for the RecyclerView, with a click listener to open the DetailsActivity
    private val adapter = TaskAdapter {task: Task ->
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra("taskId", task.id)
        }
        startActivity(intent)
    }

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize our Firestore and DataManager
        FirebaseFirestore.setLoggingEnabled(true)
        dataManager = DataManager.instance()

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = adapter

        // Observe the LiveData from the ViewModel
        viewModel.tasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }

        // Load all Tasks rom the database manager via viewModel
        viewModel.loadAllTasks()

        // TODO: Fix the calendar header text color


        binding.menuBar.homeButton.setOnClickListener {
            Log.d("MenuBar", "Home button clicked")

            startActivity(Intent(this, MainActivity::class.java))
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