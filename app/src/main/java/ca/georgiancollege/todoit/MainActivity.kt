package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.todoit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TaskAdapter.OnTaskClickListener, PinnedTaskAdapter.OnTaskClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sample data for pinned tasks
        val pinnedTasks = arrayOf(
            Task("School", "Mobile Assignment 4", "Complete the design document and code for Todo app", "July 24, 2024"),
            Task("Work", "Complete Database Backups", "Revise DB back up schedule and perform backups", "July 25, 2024"),
            Task("Personal", "Grocery Shopping", "Buy groceries for the week", "July 26, 2024")
        )

        // Create and set the Pinned tasks adapter
        val pinnedTaskAdapter = PinnedTaskAdapter(pinnedTasks, this)

        // Set the adapter and layout manager for the Pinned tasks RecyclerView
        binding.pinnedTasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = pinnedTaskAdapter
        }

        // Sample data for Upcoming tasks
        val upcomingTasks = arrayOf(
            Task("School", "Research Paper", "Draft the introduction and literature review for the research paper", "July 27, 2024"),
            Task("Work", "Team Meeting", "Discuss project milestones and deliverables with the team", "July 28, 2024"),
            Task("Personal", "Doctor's Appointment", "Annual physical check-up with Dr. Smith", "July 29, 2024"),
            Task("Fitness", "Morning Run", "Complete a 5km run in the park", "July 30, 2024")
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
    }

    override fun onTaskClick(position: Int) {
        Log.d("TaskAdapter", "Task clicked at position: $position")
        startActivity(Intent(this, DetailsActivity::class.java))
        finish();
    }
}