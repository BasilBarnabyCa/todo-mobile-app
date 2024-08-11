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
            Task(id = "1", category = "Fitness", name = "Morning Run", notes = "Complete a 5km run in the park", status = "Not Started", isComplete = false, hasDueDate = false, dueDate = "", createDate = "July 1, 2024"),
            Task(id = "2", category = "Work", name = "Project Planning Meeting", notes = "Discuss project milestones and deliverables with the team", status = "In Progress", isComplete = false, hasDueDate = true, dueDate = "July 25, 2024", createDate = "June 20, 2024"),
            Task(id = "3", category = "Personal", name = "Doctor's Appointment", notes = "Annual physical check-up with Dr. Smith", status = "Complete", isComplete = true, hasDueDate = false, dueDate = "", createDate = "July 10, 2024"),
            Task(id = "4", category = "School", name = "Draft Research Paper", notes = "Complete the draft for the research paper on environmental science", status = "Not Started", isComplete = false, hasDueDate = true, dueDate = "July 27, 2024", createDate = "July 5, 2024"),
            Task(id = "5", category = "Fitness", name = "Yoga Session", notes = "Attend a yoga session at the local gym", status = "In Progress", isComplete = false, hasDueDate = false, dueDate = "", createDate = "June 21, 2024"),
            Task(id = "6", category = "Work", name = "Team Review Meeting", notes = "Discuss project progress and address any issues", status = "Complete", isComplete = true, hasDueDate = true, dueDate = "July 29, 2024", createDate = "June 22, 2024"),
            Task(id = "7", category = "Personal", name = "Dentist Appointment", notes = "Regular dental check-up", status = "Not Started", isComplete = false, hasDueDate = true, dueDate = "July 30, 2024", createDate = "July 11, 2024"),
            Task(id = "8", category = "School", name = "Literature Review", notes = "Draft the literature review for the research paper", status = "In Progress", isComplete = false, hasDueDate = false, dueDate = "", createDate = "July 6, 2024"),
            Task(id = "9", category = "Fitness", name = "Cycling", notes = "Complete a 20km cycling session", status = "Complete", isComplete = true, hasDueDate = true, dueDate = "August 1, 2024", createDate = "June 23, 2024"),
            Task(id = "10", category = "Work", name = "Client Meeting", notes = "Discuss project requirements with the client", status = "Not Started", isComplete = false, hasDueDate = false, dueDate = "", createDate = "June 24, 2024"),
            Task(id = "11", category = "School", name = "Final Research Paper Submission", notes = "Submit the final draft of the research paper", status = "Complete", isComplete = true, hasDueDate = true, dueDate = "August 3, 2024", createDate = "July 12, 2024"),
            Task(id = "12", category = "Personal", name = "Therapy Session", notes = "Attend the scheduled therapy session", status = "In Progress", isComplete = false, hasDueDate = true, dueDate = "August 4, 2024", createDate = "July 7, 2024")
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
            putExtra("title", task.name)
            putExtra("notes", task.notes)
            putExtra("status", task.status)
            putExtra("dueDate", task.dueDate)
            putExtra("createDate", task.createDate)
        }

        startActivity(intent)
    }
}