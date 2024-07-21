package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.todoit.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sample data for Upcoming tasks
        val allTasks = arrayOf(
            Task("School", "Research Paper", "Draft the introduction and literature review for the research paper", "July 24, 2024"),
            Task("Work", "Team Meeting", "Discuss project milestones and deliverables with the team", "July 25, 2024"),
            Task("Personal", "Doctor's Appointment", "Annual physical check-up with Dr. Smith", "July 26, 2024"),
            Task("Fitness", "Morning Run", "Complete a 5km run in the park", "July 27, 2024"),
            Task("School", "Research Paper", "Draft the introduction and literature review for the research paper", "July 28, 2024"),
            Task("Work", "Team Meeting", "Discuss project milestones and deliverables with the team", "July 29, 2024"),
            Task("Personal", "Doctor's Appointment", "Annual physical check-up with Dr. Smith", "July 30, 2024"),
            Task("Fitness", "Morning Run", "Complete a 5km run in the park", "July 31, 2024"),
            Task("School", "Research Paper", "Draft the introduction and literature review for the research paper", "August 1, 2024"),
            Task("Work", "Team Meeting", "Discuss project milestones and deliverables with the team", "August 2, 2024"),
            Task("Personal", "Doctor's Appointment", "Annual physical check-up with Dr. Smith", "August 3, 2024"),
            Task("Fitness", "Morning Run", "Complete a 5km run in the park", "August 4, 2024")
        )

        // Create and set the adapter for the Upcoming tasks adapter
        val taskAdapter = TaskAdapter(allTasks)

        // Set the adapter and layout manager for the Upcoming tasks RecyclerView
        binding.tasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }

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
            finish()
        }

        binding.menuBar.userProfileButton.setOnClickListener {
            Log.d("MenuBar", "User profile button clicked")
        }
    }
}