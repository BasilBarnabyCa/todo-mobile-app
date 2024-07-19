package ca.georgiancollege.todoit

import android.os.Bundle
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

        // Create and set the adapter for the RecyclerView
        val pinnedTaskAdapter = PinnedTaskAdapter(pinnedTasks)

        // Set the adapter and layout manager for the RecyclerView
        binding.pinnedTasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = pinnedTaskAdapter
        }
    }
}