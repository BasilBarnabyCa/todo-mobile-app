package ca.georgiancollege.todoit

/**
 * Data class representing a task.
 *
 * @property category The category of the task (e.g., Work, School, Personal).
 * @property title The title of the task.
 * @property notes A brief section of the notes for the task.
 * @property status The status of the task (e.g., Not Started, In Progress, Complete).
 * @property dueDate The date when the task is due or scheduled.
 * @property createDate The date when the task was created.
 */
data class Task(
    val category: String,
    val title: String,
    val notes: String,
    var status: String,
    val dueDate: String,
    val createDate: String
)
