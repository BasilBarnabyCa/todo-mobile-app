package ca.georgiancollege.todoit

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Data class representing a task.
 *
 * @property category The category of the task (e.g., Work, School, Personal).
 * @property name The title of the task.
 * @property notes A brief section of the notes for the task.
 * @property status The status of the task (e.g., Not Started, In Progress, Complete).
 * @property isComplete A flag indicating whether the task is completed or not.
 * @property hasDueDate A flag indicating whether the task has a due date or not.
 * @property dueDate The date when the task is due or scheduled.
 * @property createDate The date when the task was created.
 */
@IgnoreExtraProperties
data class Task(
    @DocumentId val id: String = "",
    val category: String,
    val name: String,
    val notes: String,
    var status: String,
    val isComplete: Boolean,
    val hasDueDate: Boolean,
    val dueDate: String,
    val createDate: String
)
{
    // No-argument constructor required by Firestore deserialization
    constructor() : this("", "", "", "", "", false, false, "", "")
}
