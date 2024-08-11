package ca.georgiancollege.todoit

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.todoit.databinding.TaskRowItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * A control class and wrapper for displaying tasks in a RecyclerView.
 *
 * @param listener A lambda function that handles task click events.
 */
class TaskAdapter(private val listener: (Task) -> Unit, private val viewModel: TaskViewModel) :
    ListAdapter<Task, TaskAdapter.ViewHolder>(TaskComparator()) {

    /**
     * ViewHolder class that holds the view binding for each task row.
     *
     * @param binding The view binding for the task row.
     */
    inner class ViewHolder(val binding: TaskRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener(getItem(position)) // Pass the clicked Task to the lambda
                }
            }
            binding.statusImageView.setOnClickListener {
                cycleStatus(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TaskRowItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val task = getItem(position)
        viewHolder.binding.taskTitleTextView.text = task.name

        val incomingDateFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val displayDateFormat =
            SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

        if (task.dueDate.isNotEmpty() && task.dueDate != "Please select a date") {
            val dueDate = if (task.hasDueDate && task.dueDate.isNotEmpty()) {
                incomingDateFormat.parse(task.dueDate)
                    ?.let { date -> displayDateFormat.format(date) } ?: "Not set"
            } else {
                "Not set"
            }
            viewHolder.binding.taskDateTimeTextView.text = buildString {
                append(task.notes)
                append(" - ")
                append(dueDate)
            }
        } else {
            viewHolder.binding.taskDateTimeTextView.text = task.notes
        }

        when (task.category) {
            "Fitness" -> viewHolder.binding.categoryImageView.setImageResource(R.drawable.ic_power_lifting)
            "Work" -> viewHolder.binding.categoryImageView.setImageResource(R.drawable.ic_briefcase)
            "School" -> viewHolder.binding.categoryImageView.setImageResource(R.drawable.ic_book)
            "Personal" -> viewHolder.binding.categoryImageView.setImageResource(R.drawable.ic_organic)
            else -> viewHolder.binding.categoryImageView.setImageResource(R.drawable.ic_power_lifting)
        }

        updateStatus(viewHolder, position)
    }

    private fun updateStatus(viewHolder: ViewHolder, position: Int) {
        val task = getItem(position)
        when (task.status) {
            "Not started" -> {
                viewHolder.binding.statusImageView.setImageResource(R.drawable.ic_not_started)
                viewHolder.binding.taskTitleTextView.paintFlags = viewHolder.binding.taskTitleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                viewHolder.binding.taskDateTimeTextView.paintFlags = viewHolder.binding.taskDateTimeTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                viewHolder.binding.taskTitleTextView.alpha = 1.0f
                viewHolder.binding.taskDateTimeTextView.alpha = 1.0f
            }
            "In progress" -> {
                viewHolder.binding.statusImageView.setImageResource(R.drawable.ic_in_progress)
                viewHolder.binding.taskTitleTextView.paintFlags = viewHolder.binding.taskTitleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                viewHolder.binding.taskDateTimeTextView.paintFlags = viewHolder.binding.taskDateTimeTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                viewHolder.binding.taskTitleTextView.alpha = 1.0f
                viewHolder.binding.taskDateTimeTextView.alpha = 1.0f
            }
            "Complete" -> {
                viewHolder.binding.statusImageView.setImageResource(R.drawable.ic_complete)
                viewHolder.binding.taskTitleTextView.paintFlags = viewHolder.binding.taskTitleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                viewHolder.binding.taskDateTimeTextView.paintFlags = viewHolder.binding.taskDateTimeTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                viewHolder.binding.taskTitleTextView.alpha = 0.3f
                viewHolder.binding.taskDateTimeTextView.alpha = 0.3f
            }
            else -> {
                Log.e("TaskAdapter", "Invalid status: ${task.status}")
            }
        }
    }

    private fun cycleStatus(position: Int) {
        val task = getItem(position)
        task.status = when (task.status) {
            "Not started" -> "In progress"
            "In progress" -> "Complete"
            "Complete" -> "Not started"
            else -> "Not started"
        }

        task.completed = task.status == "Complete"

        notifyItemChanged(position)
        viewModel.saveTask(task)
    }
}
