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
 * Filename: MainActivity.kt
 */

package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.todoit.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

/**
 * MainActivity serves as the entry point of the Todo.iT application.
 * It displays pinned and upcoming tasks, and handles navigation to other activities.
 *
 * @property binding The view binding for the main activity layout, providing access to UI elements.
 * @property viewModel The ViewModel instance for managing and observing task-related data.
 * @property dataManager A singleton instance of DataManager for managing task data interactions.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize our Firestore and DataManager
        FirebaseFirestore.setLoggingEnabled(true)
        dataManager = DataManager.instance()

        // Adapter for the RecyclerView, with a click listener to open the DetailsActivity
        val taskAdapter = TaskAdapter({ task: Task ->
            val intent = Intent(this, DetailsActivity::class.java).apply {
                putExtra("taskId", task.id)
            }
            startActivity(intent)
        }, viewModel, this)

        val pinnedTaskAdapter = PinnedTaskAdapter { task: Task ->
            val intent = Intent(this, DetailsActivity::class.java).apply {
                putExtra("taskId", task.id)
            }
            startActivity(intent)
        }

        binding.pinnedTasksRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.pinnedTasksRecyclerView.adapter = pinnedTaskAdapter

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = taskAdapter

        // Observe the LiveData from the ViewModel
        viewModel.pinnedTasks.observe(this) { tasks ->
            Log.d("TasksTracker", "Pinned Tasks: $tasks")
            pinnedTaskAdapter.submitList(tasks)
        }

        viewModel.upcomingTasks.observe(this) { tasks ->
            Log.d("TasksTracker", "Upcoming Tasks: $tasks")
            taskAdapter.submitList(tasks)
        }

        // Load all Tasks rom the database manager via viewModel
        viewModel.loadPinnedTasks()
        viewModel.loadUpcomingTasks()

        setupEventHandlers()
    }

    /**
     * Called when the activity resumes from a paused state.
     * Reloads the pinned and upcoming tasks to ensure the data is up-to-date.
     */
    override fun onResume() {
        super.onResume()
        viewModel.loadPinnedTasks()
        viewModel.loadUpcomingTasks()
    }

    /**
     * Sets up event handlers for the SearchView and the menu bar buttons.
     * Handles navigation to other activities based on user interactions.
     */
    private fun setupEventHandlers() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val intent = Intent(this@MainActivity, ListActivity::class.java).apply {
                        putExtra("searchQuery", it)
                    }
                    startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        // Set click listeners for menu bar buttons
        binding.menuBar.calendarButton.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
            finish()
        }

        binding.menuBar.addTaskButton.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        binding.menuBar.listButton.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
            finish()
        }

        binding.menuBar.userProfileButton.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
            finish()
        }
    }
}