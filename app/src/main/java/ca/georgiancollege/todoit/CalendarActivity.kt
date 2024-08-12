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
 * Filename: CalendarActivity.kt
 */

package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.todoit.databinding.ActivityCalendarViewBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * CalendarActivity displays tasks scheduled for today and handles user interactions with a calendar view.
 * Users can select a date to view tasks scheduled for that specific day.
 *
 * @property binding The view binding for the calendar activity layout, providing access to UI elements.
 * @property viewModel The ViewModel instance for managing and observing task-related data.
 * @property dataManager A singleton instance of DataManager for managing task data interactions.
 * @property formattedDate A string representing the currently selected date in "MMMM dd, yyyy" format.
 */
class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarViewBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var dataManager: DataManager
    private var formattedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize our Firestore and DataManager
        FirebaseFirestore.setLoggingEnabled(true)
        dataManager = DataManager.instance()

        // Adapter for the RecyclerView, with a click listener to open the DetailsActivity
        val adapter = TaskAdapter({ task: Task ->
            val intent = Intent(this, DetailsActivity::class.java).apply {
                putExtra("taskId", task.id)
            }
            startActivity(intent)
        }, viewModel)

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = adapter

        // Observe the LiveData from the ViewModel
        viewModel.tasksByDueDate.observe(this) { tasks ->
            adapter.submitList(tasks)
        }

        // Get the current date and load tasks for it
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        formattedDate = dateFormat.format(currentDate)

        // Load all Tasks rom the database manager via viewModel
        viewModel.loadTasksByDueDate(formattedDate)

        // Set an OnDateChangeListener on the CalendarView
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time
            formattedDate = dateFormat.format(selectedDate)

            // Load tasks for the selected date
            viewModel.loadTasksByDueDate(formattedDate)
        }

        // TODO: Fix the calendar header text color
        setupEventHandlers()
    }

    /**
     * Called when the activity resumes from a paused state.
     * Reloads the tasks for the current date to ensure the data is up-to-date.
     */
    override fun onResume() {
        super.onResume()
        viewModel.loadTasksByDueDate(formattedDate)
    }

    /**
     * Sets up event handlers for the SearchView and the menu bar buttons.
     * Handles navigation to other activities based on user interactions.
     */
    private fun setupEventHandlers() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val intent = Intent(this@CalendarActivity, ListActivity::class.java).apply {
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