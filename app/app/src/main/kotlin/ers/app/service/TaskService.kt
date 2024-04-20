package ers.app.service

import ers.app.domainEntities.*
import ers.app.domainEntities.outputModels.TaskIDOutputModel
import ers.app.domainEntities.outputModels.TaskOutputModel
import ers.app.domainEntities.outputModels.TasksOutputModel
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.Error
import org.springframework.stereotype.Component

@Component
class TaskService(private val transactionManager: TransactionManager) {

    fun createTask(name: String, userID: Int, robotID: Int): TaskResult =
        transactionManager.run {
            try {
                if (name.isEmpty())
                    failure(Error.InvalidInput)
                if (name.length > 255)
                    failure(Error.InputTooLong)
                val task = it.taskData.createTask(name, userID, robotID)
                success(TaskOutputModel(task.name, task.userId, task.robotId))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }


    fun getTaskByID(id: Int): TaskResult =
        transactionManager.run {
            try {
                val task = it.taskData.getTaskById(id)
                if (task != null)
                    success(TaskOutputModel(task.name, task.userId, task.robotId))
                else
                    failure(Error.TaskNotFound)
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }


    fun updateTask(id: Int, status: String): TaskResult =
        transactionManager.run {
            try {
                it.taskData.getTaskById(id) ?: failure(Error.TaskNotFound)
                if (status !in setOf("pending", "in progress", "completed"))
                    failure(Error.InvalidInput)
                val task = it.taskData.updateTask(id, status)
                success(TaskOutputModel(task.name, task.userId, task.robotId))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun deleteTask(id: Int): TaskIDResult =
        transactionManager.run {
            try {
                it.taskData.getTaskById(id) ?: failure(Error.TaskNotFound)
                it.taskData.deleteTask(id)
                success(TaskIDOutputModel(id))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getTasksByUserID(offset: Int = 0, limit: Int = 0, id: Int): TasksResult =
        transactionManager.run {
            try {
                it.usersData.getUserById(id) ?: failure(Error.UserNotFound)
                if (offset < 0 || limit < 0)
                    failure(Error.InvalidInput)
                val tasks = it.taskData.getTasksByUserId(offset, limit, id)
                success(TasksOutputModel(tasks))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getTasksByRobotID(offset: Int = 0, limit: Int = 0, id: Int): TasksResult =
        transactionManager.run {
            try {
                it.robotData.getRobotById(id) ?: failure(Error.RobotNotFound)
                if (offset < 0 || limit < 0)
                    failure(Error.InvalidInput)
                val tasks = it.taskData.getTasksByRobotId(offset, limit, id)
                success(TasksOutputModel(tasks))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
}