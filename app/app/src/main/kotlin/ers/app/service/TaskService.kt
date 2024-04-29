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

    fun createTask(name: String, userID: Int, robotID: Int): TaskResult {
        if (name.isEmpty())
            return failure(Error.InvalidInput)
        if (name.length > 255)
            return failure(Error.InputTooLong)
        return transactionManager.run {
            try {
                val task = it.taskData.createTask(name, userID, robotID)
                success(TaskOutputModel(task.name, task.userId, task.robotId))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
    }

    fun getTaskByID(id: Int): TaskResult =
        transactionManager.run {
            try {
                val task = it.taskData.getTaskByID(id)
                if (task != null)
                    success(TaskOutputModel(task.name, task.userId, task.robotId))
                else
                    failure(Error.TaskNotFound)
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }


    fun updateTask(id: Int, status: String): TaskResult {
        if (status !in setOf("pending", "in progress", "completed"))
            return failure(Error.InvalidInput)
        return transactionManager.run {
            try {
                it.taskData.getTaskByID(id) ?: failure(Error.TaskNotFound)
                val task = it.taskData.updateTask(id, status)
                success(TaskOutputModel(task.name, task.userId, task.robotId))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
    }

    fun deleteTask(id: Int): TaskIDResult =
        transactionManager.run {
            try {
                it.taskData.getTaskByID(id) ?: failure(Error.TaskNotFound)
                it.taskData.deleteTask(id)
                success(TaskIDOutputModel(id))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getTasksByUserID(offset: Int = 0, limit: Int = 0, id: Int): TasksResult {
        if (offset < 0 || limit < 0)
            return failure(Error.InvalidInput)
        return transactionManager.run {
            try {
                it.usersData.getUserByID(id) ?: failure(Error.UserNotFound)
                val tasks = it.taskData.getTasksByUserID(offset, limit, id)
                success(TasksOutputModel(tasks))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
    }


    fun getTasksByRobotID(offset: Int = 0, limit: Int = 0, id: Int): TasksResult {
        if (offset < 0 || limit < 0)
            return failure(Error.InvalidInput)
        return transactionManager.run {
            try {
                it.robotData.getRobotByID(id) ?: failure(Error.RobotNotFound)
                val tasks = it.taskData.getTasksByRobotID(offset, limit, id)
                success(TasksOutputModel(tasks))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
    }
}