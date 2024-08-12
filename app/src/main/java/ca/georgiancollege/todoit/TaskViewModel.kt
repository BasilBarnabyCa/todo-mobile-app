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
 * Filename: TaskViewModel.kt
 */

package ca.georgiancollege.todoit

import androidx.lifecycle.*
import kotlinx.coroutines.launch

/**
 * TaskViewModel is responsible for managing the data and business logic of the Todo.iT application.
 * It interacts with the DataManager to load, save, update, and delete tasks, and exposes LiveData to observe changes in the task data.
 */
class TaskViewModel : ViewModel() {
    private val dataManager = DataManager.instance()

    private val m_tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = m_tasks

    private val m_pinnedTasks = MutableLiveData<List<Task>>()
    val pinnedTasks: LiveData<List<Task>> get() = m_pinnedTasks

    private val m_upcomingTasks = MutableLiveData<List<Task>>()
    val upcomingTasks: LiveData<List<Task>> get() = m_upcomingTasks

    private val m_taskByDueDate = MutableLiveData<List<Task>>()
    val tasksByDueDate: LiveData<List<Task>> get() = m_taskByDueDate

    private val m_task = MutableLiveData<Task?>()
    val task: LiveData<Task?> get() = m_task

    /**
     * Loads all tasks from the database
     */
    fun loadAllTasks() {
        viewModelScope.launch {
            m_tasks.value = dataManager.getAllTasks()
        }
    }

    /**
     * Loads pinned tasks from the database
     */
    fun loadPinnedTasks() {
        viewModelScope.launch {
            m_pinnedTasks.value = dataManager.getPinnedTasks()
        }
    }

    /**
     * Loads upcoming tasks from the database
     */
    fun loadUpcomingTasks() {
        viewModelScope.launch {
            m_upcomingTasks.value = dataManager.getUpcomingTasks()
        }
    }

    /**
     * Loads tasks by due date from the database
     */
    fun loadTasksByDueDate(dueDate: String) {
        viewModelScope.launch {
            m_taskByDueDate.value = dataManager.getTasksByDueDate(dueDate)
        }
    }

    /**
     * Loads a task by its ID from the database
     */
    fun loadTaskById(id: String) {
        viewModelScope.launch {
            m_task.value = dataManager.getTaskById(id)
        }
    }

    /**
     * Searches for tasks based on the provided query
     */
    fun searchTasks(query: String) {
        viewModelScope.launch {
            val allTasks = dataManager.getAllTasks()
            val filteredTasks = allTasks.filter {
                it.name.contains(query, ignoreCase = true) || it.notes.contains(query, ignoreCase = true)
            }
            m_tasks.value = filteredTasks
        }
    }

    /**
     * Saves a Task to the database manager
     */
    fun saveTask(task: Task) {
        viewModelScope.launch {
            if (task.id.isEmpty()) {
                dataManager.insert(task)
            } else {
                dataManager.update(task)
            }
            loadAllTasks()
        }
    }

    /**
     * Deletes a Task in the database manager
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            dataManager.delete(task)
            loadAllTasks()
        }
    }
}