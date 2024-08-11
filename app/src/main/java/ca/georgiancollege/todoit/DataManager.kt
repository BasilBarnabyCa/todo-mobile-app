package ca.georgiancollege.todoit

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class DataManager private constructor() {
    private val db: FirebaseFirestore = Firebase.firestore

    companion object
    {
        private val TAG = "DataManager"

        @Volatile
        private var m_instance:DataManager? = null

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

    // CRUD Operations
    // Create Task in Firestore
    suspend fun insert (task: Task) {
        try {
            db.collection("tasks").document(task.id).set(task).await()
        }
        catch (e: Exception) {
            Log.e(TAG, "Error inserting Task: ${e.message}", e)
        }
    }

    // Update Task in Firestore
    suspend fun update (task: Task) {
        try {
            db.collection("tasks").document(task.id).set(task).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error updating Task: ${e.message}", e)
        }
    }

    // Delete Task from Firestore
    suspend fun delete (task: Task) {
        try {
            db.collection("tasks").document(task.id).delete().await()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting Task: ${e.message}", e)
        }
    }

    // Get All Tasks from Firestore
    suspend fun getAllTasks() : List<Task> {
        return try {
            val tasks = db.collection("tasks").get().await()
            tasks?.toObjects(Task::class.java) ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting all Tasks: ${e.message}", e)
            emptyList()
        }
    }

    // Get Upcoming Tasks from Firestore
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

    // Get Task by ID from Firestore
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