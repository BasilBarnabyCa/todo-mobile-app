package ca.georgiancollege.todoit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.todoit.databinding.TaskCardBinding

/**
 * A control class and wrapper for displaying pinned tasks in a RecyclerView.
 *
 * @param dataSet An array of Task objects to be displayed.
 */
class PinnedTaskAdapter(private val dataSet: Array<Task>) :
    RecyclerView.Adapter<PinnedTaskAdapter.ViewHolder>() {


    /**
     * ViewHolder class that holds the view binding for each task card.
     *
     * @param binding The view binding for the task card.
     */
    class ViewHolder(val binding: TaskCardBinding) : RecyclerView.ViewHolder(binding.root)

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
        viewHolder.binding.taskCardCategory.text = dataSet[position].category
        viewHolder.binding.taskCardTitle.text = dataSet[position].title
        viewHolder.binding.taskCardNotes.text = dataSet[position].notes
        viewHolder.binding.taskCardDate.text = dataSet[position].date
        // TODO: Set the time text
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = dataSet.size
}