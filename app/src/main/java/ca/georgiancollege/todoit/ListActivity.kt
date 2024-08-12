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
 * Filename: ListActivity.kt
 */

package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.todoit.databinding.ActivityListBinding
import com.google.firebase.firestore.FirebaseFirestore

/**
 * ListActivity displays all tasks in a RecyclerView and provides search functionality to filter tasks.
 *
 * @property binding The view binding for the list activity layout, providing access to UI elements.
 * @property viewModel The ViewModel instance for managing and observing task data.
 * @property dataManager A singleton instance of DataManager for managing task data interactions.
 */
class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var dataManager: DataManager

    /**
     * Called when the activity is first created.
     * Initializes the UI components, sets up the RecyclerView adapter, and loads tasks based on search query or all tasks if no query is present.
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
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
        }, viewModel, this)

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = adapter

        // Get the search query from the intent
        val searchQuery = intent.getStringExtra("searchQuery")
        if (searchQuery != null) {
            viewModel.searchTasks(searchQuery)
        } else {
            viewModel.loadAllTasks()
        }

        // Observe the LiveData from the ViewModel
        viewModel.tasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }

        setupEventHandlers()
    }

    /**
     * Called when the activity resumes from a paused state.
     * Reloads all tasks if no search query is present to ensure the data is up-to-date.
     */
    override fun onResume() {
        super.onResume()
        val searchQuery = intent.getStringExtra("searchQuery")
        if (searchQuery == null) {
            viewModel.loadAllTasks()
        }
    }

    /**
     * Sets up event handlers for various UI elements, including the SearchView and menu bar buttons.
     */
    private fun setupEventHandlers() {
        // Set up the SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchTasks(it)
                }
                return true
            }

            // Handle live changes in the search query
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isEmpty()) {
                        viewModel.loadAllTasks()
                    } else {
                        viewModel.searchTasks(it)
                    }
                }
                return true
            }
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
}