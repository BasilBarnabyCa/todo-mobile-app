package ca.georgiancollege.todoit

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {
    private val dataManager = DataManager.instance()

    private val m_tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = m_tasks

    private val m_task = MutableLiveData<Task?>()
    val task: LiveData<Task?> get() = m_task

    // Load all Tasks from the database manager
    fun loadAllTasks() {
        viewModelScope.launch {
            m_tasks.value = dataManager.getAllTasks()
        }
    }

    // Load a specific Task from the database manager
    fun loadTaskById(id: String) {
        viewModelScope.launch {
            m_task.value = dataManager.getTaskById(id)
        }
    }

    // Save or update a Task in the database manager
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

    // Delete a Task from the database manager
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            dataManager.delete(task)
            loadAllTasks()
        }
    }
}