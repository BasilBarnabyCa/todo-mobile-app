
/** Author: Basil Barnaby
 * Student Number: 200540109
 * Course: COMP3025 - Mobile and Pervasive Computing
 * Assignment: 3 - Todo App Prototype
 * Date: July 22, 2024
 * Description: This is a todo app that allows users to add, edit, and delete tasks.
 * App Name: Todo.iT
 * Target Device: Google Pixel 8 Pro
 * Version: 0.1
 */

package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.todoit.databinding.ActivityMainBinding

/**
 * MainActivity serves as the entry point of the Todo.iT application.
 * It displays pinned and upcoming tasks, and handles navigation to other activities.
 *
 * @property binding The view binding for the main activity layout.
 * @property upcomingTasks Array of Task objects representing upcoming tasks.
 * @property pinnedTasks Array of Task objects representing pinned tasks.
 */
class MainActivity : AppCompatActivity(), TaskAdapter.OnTaskClickListener, PinnedTaskAdapter.OnTaskClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var upcomingTasks: Array<Task>
    private lateinit var pinnedTasks: Array<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sample data for pinned tasks
        pinnedTasks = arrayOf(
            Task("Personal", "Grocery Shopping", "Buy groceries for the week", "Complete", "", "July 10, 2024"),
            Task("School", "Mobile Assignment 4", "Complete the design document and code for Todo app", "Not Started", "July 24, 2024", "July 1, 2024"),
            Task("Work", "Complete Database Backups", "Revise DB back up schedule and perform backups", "In Progress", "July 25, 2024", "June 20, 2024")
        )

        // Create and set the Pinned tasks adapter
        val pinnedTaskAdapter = PinnedTaskAdapter(pinnedTasks, this)

        // Set the adapter and layout manager for the Pinned tasks RecyclerView
        binding.pinnedTasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = pinnedTaskAdapter
        }

        // Sample data for Upcoming tasks
        upcomingTasks = arrayOf(
            Task("Fitness", "Morning Run", "Complete a 5km run in the park", "Not Started", "", "July 1, 2024"),
            Task("Work", "Project Planning Meeting", "Discuss project milestones and deliverables with the team", "In Progress", "July 25, 2024", "June 20, 2024"),
            Task("Personal", "Doctor's Appointment", "Annual physical check-up with Dr. Smith", "Complete", "", "July 10, 2024"),
            Task("School", "Draft Research Paper", "Complete the draft for the research paper on environmental science", "Not Started", "July 27, 2024", "July 5, 2024"),
        )

        // Create and set the adapter for the Upcoming tasks adapter
        val upcomingTaskAdapter = TaskAdapter(upcomingTasks, this)

        // Set the adapter and layout manager for the Upcoming tasks RecyclerView
        binding.tasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = upcomingTaskAdapter
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

    /**
     * Called when a task card is clicked in the Pinned tasks RecyclerView.
     *
     * @param position The position of the clicked task card in the RecyclerView.
     */
    override fun onTaskCardClick(position: Int) {
        Log.d("TaskAdapter", "Task card clicked at position: $position")

        val task = pinnedTasks[position]

        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra("category", task.category)
            putExtra("title", task.name)
            putExtra("notes", task.notes)
            putExtra("dueDate", task.dueDate)
        }

        startActivity(intent)
    }

    /**
     * Called when a task is clicked in the Upcoming tasks RecyclerView.
     *
     * @param position The position of the clicked task in the RecyclerView.
     */
    override fun onTaskClick(position: Int) {
        Log.d("TaskAdapter", "Task clicked at position: $position")

        val task = upcomingTasks[position]

        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra("category", task.category)
            putExtra("title", task.name)
            putExtra("notes", task.notes)
            putExtra("status", task.status)
            putExtra("dueDate", task.dueDate)
            putExtra("createDate", task.createDate)
        }

        startActivity(intent)
    }
}