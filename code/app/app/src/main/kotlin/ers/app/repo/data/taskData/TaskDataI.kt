package ers.app.repo.data.taskData

import ers.app.repo.dtos.TaskDto

/**
 * Interface that defines the operations related to the task table in the database.
 */
interface TaskDataI {
    fun createTask(name: String, userID: Int, robotId: Int) : TaskDto
    fun getTaskByID(id : Int) : TaskDto?
    fun updateTask(id: Int, status: String) : TaskDto
    fun deleteTask(id: Int)
    fun getTasksByUserID(offset: Int, limit: Int, userID: Int) : List<TaskDto>//paginação
    fun getTasksByRobotID(offset: Int, limit: Int, robotID: Int) : List<TaskDto>//paginação
}