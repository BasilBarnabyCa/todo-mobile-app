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
 * Filename: DataManager.kt
 */

package ca.georgiancollege.todoit

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

/**
 * A Singleton class responsible for managing all CRUD operations related to tasks in Firestore.
 * It provides methods to create, read, update, and delete tasks, as well as retrieve specific task lists such as upcoming tasks or tasks by due date.
 *
 * @property db The Firestore database instance used for performing database operations.
 */
class DataManager private constructor() {
    private val db: FirebaseFirestore = Firebase.firestore

    companion object
    {
        private val TAG = "DataManager"

        @Volatile
        private var m_instance:DataManager? = null

        /**
         * Returns the singleton instance of DataManager, creating it if necessary.
         *
         * @return The singleton instance of DataManager.
         */
        fun instance(): DataManager
        {
            if(m_instance == null)
            {
                synchronized(this)
                {
                    if (m_instance == null) {
                        m_instance = DataManager()
                    }
                }
            }

            return m_instance!!
        }
    }

    /**
     * Inserts a new task into the Firestore database.
     *
     * @param task The Task object to be inserted.
     */
    suspend fun insert (task: Task) {
        try {
            db.collection("tasks").document(task.id).set(task).await()
        }
        catch (e: Exception) {
            Log.e(TAG, "Error inserting Task: ${e.message}", e)
        }
    }

    /**
     * Updates an existing task in the Firestore database.
     *
     * @param task The Task object to be updated.
     */
    suspend fun update (task: Task) {
        try {
            db.collection("tasks").document(task.id).set(task).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error updating Task: ${e.message}", e)
        }
    }

    /**
     * Deletes a task from the Firestore database.
     *
     * @param task The Task object to be deleted.
     */
    suspend fun delete (task: Task) {
        try {
            db.collection("tasks").document(task.id).delete().await()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting Task: ${e.message}", e)
        }
    }

    /**
     * Retrieves all tasks from the Firestore database.
     *
     * @return A list of Task objects representing all tasks in the database.
     */
    suspend fun getAllTasks() : List<Task> {
        return try {
            val tasks = db.collection("tasks").get().await()
            tasks?.toObjects(Task::class.java) ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting all Tasks: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Retrieves a limited list of upcoming tasks from the Firestore database, ordered by due date.
     *
     * @return A list of Task objects representing upcoming tasks.
     */
    suspend fun getUpcomingTasks(): List<Task> {
        return try {
            val tasks = db.collection("tasks")
                .orderBy("dueDate")
                .limit(4)
                .get()
                .await()
            tasks?.toObjects(Task::class.java) ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting upcoming tasks: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Retrieves a list of pinned tasks from the Firestore database.
     *
     * @return A list of Task objects representing pinned tasks.
     */
    suspend fun getPinnedTasks(): List<Task> {
        return try {
            val tasks = db.collection("tasks")
                .whereEqualTo("pinned", true)
                .limit(10)
                .get()
                .await()
            tasks?.toObjects(Task::class.java) ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting pinned Tasks: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Retrieves a list of tasks from the Firestore database that match a specific due date.
     *
     * @param dueDate The due date to filter tasks by.
     * @return A list of Task objects representing tasks with the specified due date.
     */
    suspend fun getTasksByDueDate(dueDate: String): List<Task> {
        return try {
            val tasks = db.collection("tasks")
                .whereEqualTo("dueDate", dueDate)
                .get()
                .await()
            tasks?.toObjects(Task::class.java) ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting tasks for due date $dueDate: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Retrieves a task by its ID from the Firestore database.
     *
     * @param id The ID of the task to retrieve.
     * @return The Task object if found, or null if not found or if an error occurs.
     */
    suspend fun getTaskById(id: String) : Task? {
        return try {
            val task = db.collection("tasks").document(id).get().await()
            task?.toObject(Task::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting Task with id ${id}: ${e.message}", e)
            null
        }
    }
}