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
 * Filename: TaskComparator.kt
 */

package ca.georgiancollege.todoit

import androidx.recyclerview.widget.DiffUtil

/**
 * TaskComparator is a utility class used to compare Task items in a RecyclerView.
 * It determines whether two Task items are the same and whether their contents are the same.
 */
class TaskComparator: DiffUtil.ItemCallback<Task>() {
    /**
     * Checks if two Task items represent the same entity by comparing their IDs.
     *
     * @param oldItem The old Task item.
     * @param newItem The new Task item.
     * @return True if the items have the same ID, false otherwise.
     */
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * Checks if the contents of two Task items are the same by comparing their properties.
     *
     * @param oldItem The old Task item.
     * @param newItem The new Task item.
     * @return True if all properties of the items are the same, false otherwise.
     */
    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}