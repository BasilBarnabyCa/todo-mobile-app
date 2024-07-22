package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.todoit.databinding.ActivityListBinding

class ListActivity : AppCompatActivity(), TaskAdapter.OnTaskClickListener {

    private lateinit var binding: ActivityListBinding
    private lateinit var allTasks: Array<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sample data for Upcoming tasks
        allTasks = arrayOf(
            Task("School", "Research Paper", "Draft the introduction and literature review for the research paper", "Not Started", "July 24, 2024", "July 1, 2024"),
            Task("Work", "Team Meeting", "Discuss project milestones and deliverables with the team", "In Progress", "July 25, 2024", "June 20, 2024"),
            Task("Personal", "Doctor's Appointment", "Annual physical check-up with Dr. Smith", "Complete", "July 26, 2024", "July 10, 2024"),
            Task("Fitness", "Morning Run", "Complete a 5km run in the park", "Not Started", "July 27, 2024", "July 5, 2024"),
            Task("School", "Research Paper", "Draft the introduction and literature review for the research paper", "In Progress", "July 28, 2024", "June 21, 2024"),
            Task("Work", "Team Meeting", "Discuss project milestones and deliverables with the team", "Complete", "July 29, 2024", "June 22, 2024"),
            Task("Personal", "Doctor's Appointment", "Annual physical check-up with Dr. Smith", "Not Started", "July 30, 2024", "July 11, 2024"),
            Task("Fitness", "Morning Run", "Complete a 5km run in the park", "In Progress", "July 31, 2024", "July 6, 2024"),
            Task("School", "Research Paper", "Draft the introduction and literature review for the research paper", "Complete", "August 1, 2024", "June 23, 2024"),
            Task("Work", "Team Meeting", "Discuss project milestones and deliverables with the team", "Not Started", "August 2, 2024", "June 24, 2024"),
            Task("Personal", "Doctor's Appointment", "Annual physical check-up with Dr. Smith", "In Progress", "August 3, 2024", "July 12, 2024"),
            Task("Fitness", "Morning Run", "Complete a 5km run in the park", "Complete", "August 4, 2024", "July 7, 2024")
        )

        // Create and set the adapter for the Upcoming tasks adapter
        val taskAdapter = TaskAdapter(allTasks, this)

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

            startActivity(Intent(this, UserProfileActivity::class.java))
            finish()
        }
    }

    override fun onTaskClick(position: Int) {
        Log.d("TaskAdapter", "Task clicked at position: $position")
        val task = allTasks[position]

        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra("category", task.category)
            putExtra("title", task.title)
            putExtra("notes", task.notes)
            putExtra("status", task.status)
            putExtra("dueDate", task.dueDate)
            putExtra("createDate", task.createDate)
        }

        startActivity(intent)
        finish();
    }
}