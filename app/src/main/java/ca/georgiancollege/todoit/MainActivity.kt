package ca.georgiancollege.todoit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.todoit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sample data for pinned tasks
        val pinnedTasks = arrayOf(
            Task("School", "Mobile Assignment 4", "Complete the design document and code for Todo app", "2023-11-20", "10:00 AM"),
            Task("Work", "Complete Database Backups", "Revise DB back up schedule and perform backups", "2023-11-21", "09:00 AM"),
            Task("Personal", "Grocery Shopping", "Buy groceries for the week", "2023-11-22", "08:00 AM")
        )

        // Create and set the Pinned tasks adapter
        val pinnedTaskAdapter = PinnedTaskAdapter(pinnedTasks)

        // Set the adapter and layout manager for the Pinned tasks RecyclerView
        binding.pinnedTasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = pinnedTaskAdapter
        }

        // Sample data for Upcoming tasks
        val upcomingTasks = arrayOf(
            Task("School", "Research Paper", "Draft the introduction and literature review for the research paper", "2023-11-20", "10:00 AM"),
            Task("Work", "Team Meeting", "Discuss project milestones and deliverables with the team", "2023-11-21", "11:00 AM"),
            Task("Personal", "Doctor's Appointment", "Annual physical check-up with Dr. Smith", "2023-11-22", "02:00 PM"),
            Task("Fitness", "Morning Run", "Complete a 5km run in the park", "2023-11-23", "07:00 AM")
        )

        // Create and set the adapter for the Upcoming tasks adapter
        val upcomingTaskAdapter = TaskAdapter(upcomingTasks)

        // Set the adapter and layout manager for the Upcoming tasks RecyclerView
        binding.tasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = upcomingTaskAdapter
        }

        binding.menuBar.homeButton.setOnClickListener {
            Log.d("MenuBar", "Home button clicked")
        }

        binding.menuBar.calendarButton.setOnClickListener {
            Log.d("MenuBar", "Calendar button clicked")
        }

        binding.menuBar.addTaskButton.setOnClickListener {
            Log.d("MenuBar", "Add task button clicked")
        }

        binding.menuBar.listButton.setOnClickListener {
            Log.d("MenuBar", "List button clicked")
        }

        binding.menuBar.userProfileButton.setOnClickListener {
            Log.d("MenuBar", "User profile button clicked")
        }
    }
}