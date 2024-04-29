package ers.app.service

import ers.app.domainEntities.*
import ers.app.domainEntities.outputModels.TaskIDOutputModel
import ers.app.domainEntities.outputModels.TaskOutputModel
import ers.app.domainEntities.outputModels.TasksOutputModel
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.Errors
import org.springframework.stereotype.Component

@Component
class TaskService(private val transactionManager: TransactionManager) {

    fun createTask(name: String, userID: Int, robotID: Int): TaskResult {
        if (name.isEmpty())
            return failure(Errors.InvalidInput)
        if (name.length > 255)
            return failure(Errors.InputTooLong)
        return transactionManager.run {
            try {
                val task = it.taskData.createTask(name, userID, robotID)
                success(TaskOutputModel(task.name, task.userId, task.robotId))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
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
                    failure(Errors.TaskNotFound)
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }


    fun updateTask(id: Int, status: String): TaskResult {
        if (status !in setOf("pending", "in progress", "completed"))
            return failure(Errors.InvalidInput)
        return transactionManager.run {
            try {
                it.taskData.getTaskByID(id) ?: failure(Errors.TaskNotFound)
                val task = it.taskData.updateTask(id, status)
                success(TaskOutputModel(task.name, task.userId, task.robotId))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }
    }

    fun deleteTask(id: Int): TaskIDResult =
        transactionManager.run {
            try {
                it.taskData.getTaskByID(id) ?: failure(Errors.TaskNotFound)
                it.taskData.deleteTask(id)
                success(TaskIDOutputModel(id))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }

    fun getTasksByUserID(offset: Int = 0, limit: Int = 0, id: Int): TasksResult {
        if (offset < 0 || limit < 0)
            return failure(Errors.InvalidInput)
        return transactionManager.run {
            try {
                it.usersData.getUserByID(id) ?: failure(Errors.UserNotFound)
                val tasks = it.taskData.getTasksByUserID(offset, limit, id)
                success(TasksOutputModel(tasks))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }
    }


    fun getTasksByRobotID(offset: Int = 0, limit: Int = 0, id: Int): TasksResult {
        if (offset < 0 || limit < 0)
            return failure(Errors.InvalidInput)
        return transactionManager.run {
            try {
                it.robotData.getRobotByID(id) ?: failure(Errors.RobotNotFound)
                val tasks = it.taskData.getTasksByRobotID(offset, limit, id)
                success(TasksOutputModel(tasks))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }
    }
}