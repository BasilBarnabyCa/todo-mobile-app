package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.todoit.databinding.ActivityListBinding

/**
 * ListActivity displays all tasks in a RecyclerView.
 *
 * @property binding The view binding for the list activity layout.
 * @property allTasks Array of Task objects representing all tasks.
 */
class ListActivity : AppCompatActivity(), TaskAdapter.OnTaskClickListener {

    private lateinit var binding: ActivityListBinding
    private lateinit var allTasks: Array<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sample data for Upcoming tasks
        allTasks = arrayOf(
            Task("Fitness", "Morning Run", "Complete a 5km run in the park", "Not Started", "", "July 1, 2024"),
            Task("Work", "Project Planning Meeting", "Discuss project milestones and deliverables with the team", "In Progress", "July 25, 2024", "June 20, 2024"),
            Task("Personal", "Doctor's Appointment", "Annual physical check-up with Dr. Smith", "Complete", "", "July 10, 2024"),
            Task("School", "Draft Research Paper", "Complete the draft for the research paper on environmental science", "Not Started", "July 27, 2024", "July 5, 2024"),
            Task("Fitness", "Yoga Session", "Attend a yoga session at the local gym", "In Progress", "", "June 21, 2024"),
            Task("Work", "Team Review Meeting", "Discuss project progress and address any issues", "Complete", "July 29, 2024", "June 22, 2024"),
            Task("Personal", "Dentist Appointment", "Regular dental check-up", "Not Started", "July 30, 2024", "July 11, 2024"),
            Task("School", "Literature Review", "Draft the literature review for the research paper", "In Progress", "", "July 6, 2024"),
            Task("Fitness", "Cycling", "Complete a 20km cycling session", "Complete", "August 1, 2024", "June 23, 2024"),
            Task("Work", "Client Meeting", "Discuss project requirements with the client", "Not Started", "", "June 24, 2024"),
            Task("School", "Final Research Paper Submission", "Submit the final draft of the research paper", "Complete", "August 3, 2024", "July 12, 2024"),
            Task("Personal", "Therapy Session", "Attend the scheduled therapy session", "In Progress", "August 4, 2024", "July 7, 2024")
        )

        // Create and set the adapter for the Upcoming tasks adapter
        val taskAdapter = TaskAdapter(allTasks, this)

        // Set the adapter and layout manager for the Upcoming tasks RecyclerView
        binding.tasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }

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

    /**
     * Handles the click event for tasks.
     *
     * @param position The position of the clicked task in allTasks array.
     */
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
    }
}