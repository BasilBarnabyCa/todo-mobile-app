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
 * Filename: PinnedTaskAdapter.kt
 */

package ca.georgiancollege.todoit

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.todoit.databinding.TaskCardBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * PinnedTaskAdapter is a RecyclerView adapter for displaying pinned tasks in a RecyclerView.
 * It binds task data to the task card layout and handles click events on individual tasks.
 *
 * @param listener A lambda function that handles task click events.
 */
class PinnedTaskAdapter(private val listener: (Task) -> Unit) :
    ListAdapter<Task, PinnedTaskAdapter.ViewHolder>(TaskComparator()) {

    /**
     * ViewHolder class that holds the view binding for each task card.
     * It initializes the click listener for the root view of the task card.
     *
     * @param binding The view binding for the task card layout.
     */
    inner class ViewHolder(val binding: TaskCardBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener(getItem(position))
                }
            }
        }
    }

    /**
     * Called when the RecyclerView needs a new ViewHolder to represent an item.
     * This method inflates the task card layout and creates a ViewHolder for it.
     *
     * @param viewGroup The parent view that the new view will be attached to.
     * @param viewType The view type of the new view (not used here as all views are the same type).
     * @return A new ViewHolder that holds a view of the task card layout.
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TaskCardBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method binds the task data to the appropriate views within the task card layout.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val task = getItem(position)
        viewHolder.binding.taskCardCategoryTextView.text = task.category
        viewHolder.binding.taskCardTitleTextView.text = task.name

// Limit the number of characters for notes
        val maxNotesLength = 60
        val notesText = if (task.notes.length > maxNotesLength) {
            task.notes.substring(0, maxNotesLength) + "..."
        } else {
            task.notes
        }
        viewHolder.binding.taskCardNotesTextView.text = notesText

// Date formatting
        val incomingDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val displayDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().time

        if (task.dueDate.isNotEmpty()) {
            val parsedDueDate = incomingDateFormat.parse(task.dueDate)
            val dueDateText = if (parsedDueDate != null) {
                displayDateFormat.format(parsedDueDate)
            } else {
                task.dueDate
            }

            val spannable = SpannableStringBuilder()
            spannable.append(dueDateText)

            if (parsedDueDate != null && parsedDueDate.before(currentDate)) {
                // Add "LATE" in bold below the date if the due date is in the past
                spannable.append("\n")
                val start = spannable.length
                spannable.append("LATE")
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    start,
                    spannable.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            viewHolder.binding.taskCardDateTextView.text = spannable
        } else {
            viewHolder.binding.taskCardDateTextView.text = ""
        }

        when (task.category) {
            "Fitness" -> viewHolder.binding.taskCardView.setBackgroundColor(
                ContextCompat.getColor(
                    viewHolder.itemView.context,
                    R.color.emerald
                )
            )

            "Work" -> viewHolder.binding.taskCardView.setBackgroundColor(
                ContextCompat.getColor(
                    viewHolder.itemView.context,
                    R.color.orange
                )
            )

            "School" -> viewHolder.binding.taskCardView.setBackgroundColor(
                ContextCompat.getColor(
                    viewHolder.itemView.context,
                    R.color.purple
                )
            )

            "Personal" -> viewHolder.binding.taskCardView.setBackgroundColor(
                ContextCompat.getColor(
                    viewHolder.itemView.context,
                    R.color.sky
                )
            )

            else -> viewHolder.binding.taskCardView.setBackgroundColor(
                ContextCompat.getColor(
                    viewHolder.itemView.context,
                    R.color.dark_slate
                )
            )
        }
    }
}
