package ca.georgiancollege.todoit

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.todoit.databinding.TaskRowItemBinding

/**
 * A control class and wrapper for displaying pinned tasks in a RecyclerView.
 *
 * @param dataSet An array of Task objects to be displayed.
 */
class TaskAdapter(private val dataSet: Array<Task>, private val listener: OnTaskClickListener) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    /**
     * ViewHolder class that holds the view binding for each task card.
     *
     * @param binding The view binding for the task card.
     */
    inner class ViewHolder(val binding: TaskRowItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
            binding.statusImageView.setOnClickListener {
                cycleStatus(adapterPosition)
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onTaskClick(position)
            }
        }
    }

    /**
     * Called when the RecyclerView needs a new ViewHolder to represent an item.
     *
     * @param viewGroup The parent view that the new view will be attached to.
     * @param viewType The view type of the new view.
     * @return A new ViewHolder that holds a view of the given view type.
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout with view binding
        val binding =
            TaskRowItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.taskTitleTextView.text = dataSet[position].title
        viewHolder.binding.taskDateTimeTextView.text = buildString {
            append(dataSet[position].notes)
            append(" - ")
            append(dataSet[position].dueDate)
        }

        when (dataSet[position].category) {
            "Fitness" -> viewHolder.binding.categoryImageView.setImageResource(R.drawable.ic_power_lifting)
            "Work" -> viewHolder.binding.categoryImageView.setImageResource(R.drawable.ic_briefcase)
            "School" -> viewHolder.binding.categoryImageView.setImageResource(R.drawable.ic_book)
            "Personal" -> viewHolder.binding.categoryImageView.setImageResource(R.drawable.ic_organic)
            else -> viewHolder.binding.categoryImageView.setImageResource(R.drawable.ic_power_lifting)
        }

        updateStatus(viewHolder, position)
    }

    private fun updateStatus(viewHolder: ViewHolder, position: Int) {
        val task = dataSet[position]
        when (task.status) {
            "Not Started" -> {
                viewHolder.binding.statusImageView.setImageResource(R.drawable.ic_not_started)
                viewHolder.binding.taskTitleTextView.paintFlags = viewHolder.binding.taskTitleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                viewHolder.binding.taskDateTimeTextView.paintFlags = viewHolder.binding.taskDateTimeTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                viewHolder.binding.taskTitleTextView.alpha = 1.0f
                viewHolder.binding.taskDateTimeTextView.alpha = 1.0f
            }
            "In Progress" -> {
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
        val task = dataSet[position]
        task.status = when (task.status) {
            "Not Started" -> "In Progress"
            "In Progress" -> "Complete"
            "Complete" -> "Not Started"
            else -> "Not Started"
        }
        notifyItemChanged(position)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = dataSet.size

    /**
     * Interface definition for a callback to be invoked when a task is clicked.
     */
    public interface OnTaskClickListener {
        fun onTaskClick(position: Int)
    }
}