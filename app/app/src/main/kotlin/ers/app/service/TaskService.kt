package ers.app.service

import ers.app.domainEntities.*
import ers.app.repo.dtos.TaskDto
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.Error
import org.springframework.stereotype.Component


data class TaskOutputModel(val name: String, val userId: Int, val robotId: Int)
data class TaskIDOutputModel(val id: Int)
data class TasksOutputModel(val tasks: List<TaskDto>)

@Component
class TaskService(private val transactionManager: TransactionManager) {

    fun createTask(name: String, userId: Int, robotId: Int): TaskResult =
        transactionManager.run {
            try {
                val task = it.taskData.createTask(name, userId, robotId)
                success(TaskOutputModel(task.name, task.userId, task.robotId))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }


    fun getTaskByID(id: Int): TaskResult =
        transactionManager.run {
            try {
                val task = it.taskData.getTaskById(id)
                if (task != null) {
                    success(TaskOutputModel(task.name, task.userId, task.robotId))
                } else {
                    failure(Error.TaskNotFound)
                }
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }


    fun updateTask(id: Int, status: String): TaskResult =
        transactionManager.run {
            try {
                if (it.taskData.getTaskById(id) == null)
                    failure(Error.TaskNotFound)
                val task = it.taskData.updateTask(id, status)
                success(TaskOutputModel(task.name, task.userId, task.robotId))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun deleteTask(id: Int): TaskIDResult =
        transactionManager.run {
            try {
                if (it.taskData.getTaskById(id) == null)
                    failure(Error.TaskNotFound)
                it.taskData.deleteTask(id)
                success(TaskIDOutputModel(id))
            }
            catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getTasksByUserId(offset: Int, limit: Int, id: Int):TasksResult =
        transactionManager.run {
            try {
                val user = it.usersData.getUserById(id)
                if (user == null)
                    failure(Error.UserNotFound)
                val tasks = it.taskData.getTasksByUserId(offset, limit, id)
                success(TasksOutputModel(tasks))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getTasksByRobotId(offset: Int, limit: Int, id: Int):TasksResult =
        transactionManager.run {
            try {
                val user = it.robotData.getRobotById(id)
                if (user == null)
                    failure(Error.RobotNotFound)
                val tasks = it.taskData.getTasksByRobotId(offset, limit, id)
                success(TasksOutputModel(tasks))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
}