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
 * Filename: Task.kt
 */

package ca.georgiancollege.todoit

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Data class representing a task.
 *
 * @property id The unique identifier for the task, automatically assigned by Firestore.
 * @property category The category of the task (e.g., Work, School, Personal).
 * @property name The title of the task.
 * @property notes A brief description or notes associated with the task.
 * @property status The current status of the task (e.g., Not started, In progress, Complete).
 * @property pinned A flag indicating whether the task is pinned (e.g., marked as important).
 * @property completed A flag indicating whether the task is completed.
 * @property hasDueDate A flag indicating whether the task has a due date.
 * @property dueDate The date by which the task is due, formatted as a string (e.g., "yyyy-MM-dd").
 * @property createDate The date when the task was created, formatted as a string (e.g., "yyyy-MM-dd").
 */
@IgnoreExtraProperties
data class Task(
    @DocumentId val id: String = "",
    val category: String,
    val name: String,
    val notes: String,
    var status: String,
    var pinned: Boolean,
    var completed: Boolean,
    val hasDueDate: Boolean,
    val dueDate: String,
    val createDate: String
)
{
    /**
     * No-argument constructor required by Firestore deserialization.
     * Initializes all properties with default values.
     */
    constructor() : this("", "", "", "", "", false, false, false, "", "")
}
