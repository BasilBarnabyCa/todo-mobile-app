package ca.georgiancollege.todoit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.todoit.databinding.TaskCardBinding

/**
 * A control class and wrapper for displaying pinned tasks in a RecyclerView.
 *
 * @param dataSet An array of Task objects to be displayed.
 */
class PinnedTaskAdapter(private val dataSet: Array<Task>, private val listener: OnTaskClickListener) :
    RecyclerView.Adapter<PinnedTaskAdapter.ViewHolder>() {

    /**
     * ViewHolder class that holds the view binding for each task card.
     *
     * @param binding The view binding for the task card.
     */
    inner class ViewHolder(val binding: TaskCardBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onTaskCardClick(position)
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
        viewHolder.binding.taskCardCategoryTextView.text = dataSet[position].category
        viewHolder.binding.taskCardTitleTextView.text = dataSet[position].name
        viewHolder.binding.taskCardNotesTextView.text = dataSet[position].notes
        viewHolder.binding.taskCardDateTextView.text = dataSet[position].dueDate

        when (dataSet[position].category) {
            "Fitness" -> viewHolder.binding.taskCardView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.emerald))
            "Work" -> viewHolder.binding.taskCardView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.orange))
            "School" -> viewHolder.binding.taskCardView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.purple))
            "Personal" -> viewHolder.binding.taskCardView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.sky))
            else -> viewHolder.binding.taskCardView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.dark_slate))
        }
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
        fun onTaskCardClick(position: Int)
    }
}