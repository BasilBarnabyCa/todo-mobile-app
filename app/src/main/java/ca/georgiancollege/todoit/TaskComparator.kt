package ca.georgiancollege.todoit

import androidx.recyclerview.widget.DiffUtil

class TaskComparator: DiffUtil.ItemCallback<Task>() {
    // Checks if the items are the same - if two items represent the same entity by comparing the IDs
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    // Checks if the contents are the same - if two items are teh same by comparing the properties
    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}