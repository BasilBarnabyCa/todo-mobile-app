package ca.georgiancollege.todoit

// Model Class
data class Task(
    val category: String,
    val title: String,
    val description: String,
    val date: String,
    val time: String
)
