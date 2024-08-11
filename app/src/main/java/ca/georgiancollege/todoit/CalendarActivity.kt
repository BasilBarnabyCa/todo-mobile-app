package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.todoit.databinding.ActivityCalendarViewBinding

/**
 * CalendarActivity displays tasks scheduled for today and handles user interactions.
 *
 * @property binding The view binding for the calendar activity layout.
 * @property todayTasks Array of Task objects representing today's tasks.
 */
class CalendarActivity : AppCompatActivity(), TaskAdapter.OnTaskClickListener {

    private lateinit var binding: ActivityCalendarViewBinding
    private lateinit var todayTasks: Array<Task>

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Fix the calendar header text color

        // Sample data for Upcoming tasks
        todayTasks = arrayOf(
            Task(id = "1", category = "Work", name = "Team Review Meeting", notes = "Discuss project progress and address any issues", status = "Complete", isComplete = true, hasDueDate = true, dueDate = "July 22, 2024", createDate = "June 22, 2024"),
            Task(id = "2", category = "Personal", name = "Dentist Appointment", notes = "Regular dental check-up", status = "Not Started", isComplete = false, hasDueDate = true, dueDate = "July 22, 2024", createDate = "July 11, 2024"),
            Task(id = "3", category = "School", name = "Literature Review", notes = "Draft the literature review for the research paper", status = "In Progress", isComplete = false, hasDueDate = true, dueDate = "July 22, 2024", createDate = "July 6, 2024"),
            Task(id = "4", category = "Fitness", name = "Cycling", notes = "Complete a 20km cycling session", status = "Complete", isComplete = true, hasDueDate = true, dueDate = "July 22, 2024", createDate = "June 23, 2024"),
            Task(id = "5", category = "Work", name = "Client Meeting", notes = "Discuss project requirements with the client", status = "Not Started", isComplete = false, hasDueDate = true, dueDate = "July 22, 2024", createDate = "June 24, 2024"),
            Task(id = "6", category = "School", name = "Final Research Paper Submission", notes = "Submit the final draft of the research paper", status = "Complete", isComplete = true, hasDueDate = true, dueDate = "July 22, 2024", createDate = "July 12, 2024"),
            Task(id = "7", category = "Personal", name = "Therapy Session", notes = "Attend the scheduled therapy session", status = "In Progress", isComplete = false, hasDueDate = true, dueDate = "July 22, 2024", createDate = "July 7, 2024")
        )

        // Create and set the adapter for the Upcoming tasks adapter
        val taskAdapter = TaskAdapter(todayTasks, this)

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
     * Handles the click event for tasks.
     *
     * @param position The position of the clicked task in today's tasks array.
     */
    override fun onTaskClick(position: Int) {
        Log.d("TaskAdapter: Today", "Task clicked at position: $position")

        val task = todayTasks[position]

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