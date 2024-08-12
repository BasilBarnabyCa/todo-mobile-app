/** Author: Basil Barnaby
 * Student Number: 200540109
 * Course: COMP3025 - Mobile and Pervasive Computing
 * Assignment: 4 - Todo App
 * Date: August 11, 2024
 * Description: This is a todo app that allows users to add, edit, and delete tasks.
 * App Name: Todo.iT
 * Target Device: Google Pixel 8 Pro
 * Version: 1.0
 *
 * Filename: UserProfileActivity.kt
 */

package ca.georgiancollege.todoit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.todoit.databinding.ActivityUserProfileBinding

/**
 * UserProfileActivity displays user profile information and handles user interactions.
 *
 * @property binding The view binding for the user profile activity layout, providing access to UI elements.
 */
class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    /**
     * Called when the activity is first created.
     * Initializes the UI components by inflating the layout and setting the content view.
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * Sets up event handlers for user interactions with the menu bar buttons.
     * Handles navigation to different activities like MainActivity, CalendarActivity, AddTaskActivity, and ListActivity.
     */
    private fun setupEventHandlers() {
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
}