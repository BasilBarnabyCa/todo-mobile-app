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
            Task("Work", "Team Review Meeting", "Discuss project progress and address any issues", "Complete", "July 22, 2024", "June 22, 2024"),
            Task("Personal", "Dentist Appointment", "Regular dental check-up", "Not Started", "July 22, 2024", "July 11, 2024"),
            Task("School", "Literature Review", "Draft the literature review for the research paper", "In Progress", "July 22, 2024", "July 6, 2024"),
            Task("Fitness", "Cycling", "Complete a 20km cycling session", "Complete", "July 22, 2024", "June 23, 2024"),
            Task("Work", "Client Meeting", "Discuss project requirements with the client", "Not Started", "July 22, 2024", "June 24, 2024"),
            Task("School", "Final Research Paper Submission", "Submit the final draft of the research paper", "Complete", "July 22, 2024", "July 12, 2024"),
            Task("Personal", "Therapy Session", "Attend the scheduled therapy session", "In Progress", "July 22, 2024", "July 7, 2024")
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
            putExtra("title", task.title)
            putExtra("notes", task.notes)
            putExtra("status", task.status)
            putExtra("dueDate", task.dueDate)
            putExtra("createDate", task.createDate)
        }

        startActivity(intent)
    }
}