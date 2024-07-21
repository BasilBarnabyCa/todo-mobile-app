package ca.georgiancollege.todoit

/**
 * Data class representing a task.
 *
 * @property category The category of the task (e.g., Work, School, Personal).
 * @property title The title of the task.
 * @property notes A brief section of the notes for the task.
 * @property date The date when the task is due or scheduled.
 * @property time The time when the task is due or scheduled.
 */
data class Task(
    val category: String,
    val title: String,
    val notes: String,
    val date: String
//    val time: String
)
