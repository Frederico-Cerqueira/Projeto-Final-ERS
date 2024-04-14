package ers.app.repo.data.taskData

import ers.app.repo.dtos.TaskDto

/**
 * Interface that defines the operations related to the task table in the database.
 */
interface TaskDataI {
    fun createTask(name : String, status : String, userId : Int, robotId : Int) : TaskDto?
    fun getTaskById(id : Int) : TaskDto?
    fun updateTask(id: Int, status: String) : TaskDto?
    fun deleteTask(id: Int)
    fun getTasksByUserId(offset : Int, limit: Int, userId : Int) : List<TaskDto>//paginação
}