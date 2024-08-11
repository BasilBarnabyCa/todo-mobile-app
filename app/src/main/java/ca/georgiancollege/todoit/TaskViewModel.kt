package ca.georgiancollege.todoit

import androidx.lifecycle.*
import kotlinx.coroutines.launch

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

    // Load all Tasks from the database manager
    fun loadAllTasks() {
        viewModelScope.launch {
            m_tasks.value = dataManager.getAllTasks()
        }
    }

    // Load Pinned Tasks from the database manager
    fun loadPinnedTasks() {
        viewModelScope.launch {
            m_pinnedTasks.value = dataManager.getPinnedTasks()
        }
    }

    // Load Upcoming Tasks from the database manager
    fun loadUpcomingTasks() {
        viewModelScope.launch {
            m_upcomingTasks.value = dataManager.getUpcomingTasks()
        }
    }

    // Load Today Tasks from the database manager
    fun loadTasksByDueDate(dueDate: String) {
        viewModelScope.launch {
            m_taskByDueDate.value = dataManager.getTasksByDueDate(dueDate)
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