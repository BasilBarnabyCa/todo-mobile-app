package ca.georgiancollege.todoit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.todoit.databinding.TaskCardBinding

/**
 * A control class and wrapper for displaying pinned tasks in a RecyclerView.
 *
 * @param listener A lambda function that handles task click events.
 */
class PinnedTaskAdapter(private val listener: (Task) -> Unit) :
    ListAdapter<Task, PinnedTaskAdapter.ViewHolder>(TaskComparator()) {

    /**
     * ViewHolder class that holds the view binding for each task card.
     *
     * @param binding The view binding for the task card.
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
     *
     * @param viewGroup The parent view that the new view will be attached to.
     * @param viewType The view type of the new view.
     * @return A new ViewHolder that holds a view of the given view type.
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TaskCardBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val task = getItem(position)
        viewHolder.binding.taskCardCategoryTextView.text = task.category
        viewHolder.binding.taskCardTitleTextView.text = task.name
        viewHolder.binding.taskCardNotesTextView.text = task.notes
        viewHolder.binding.taskCardDateTextView.text = task.dueDate

        when (task.category) {
            "Fitness" -> viewHolder.binding.taskCardView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.emerald))
            "Work" -> viewHolder.binding.taskCardView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.orange))
            "School" -> viewHolder.binding.taskCardView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.purple))
            "Personal" -> viewHolder.binding.taskCardView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.sky))
            else -> viewHolder.binding.taskCardView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.dark_slate))
        }
    }
}
