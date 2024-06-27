package ers.app.service

import ers.app.domainEntities.outputModels.*
import ers.app.repo.transaction.TransactionManager
import ers.app.service.http.RequestTask
import ers.app.utils.*
import ers.app.utils.errors.*
import org.springframework.stereotype.Component

@Component
class TaskService(private val transactionManager: TransactionManager) {

    val request = RequestTask()

    fun createTask(name: String, userID: Int, robotID: Int): Result<TaskOutputModel> {
        if (name.isEmpty())
            return failure(InvalidInput)
        if (name.length > 255)
            return failure(InputTooLong)
        return Handler().servicesHandler {
            transactionManager.run {
                val task = it.taskData.createTask(name, userID, robotID)
                return@run success(TaskOutputModel(task.name, task.userId, task.robotId, task.status))
            }
        }
    }

    fun getTaskByID(id: Int): Result<TaskOutputModel> =
        Handler().servicesHandler {
            transactionManager.run {
                val task = it.taskData.getTaskByID(id) ?: return@run failure(TaskNotFound)
                return@run success(TaskOutputModel(task.name, task.userId, task.robotId, task.status))
            }
        }


    fun updateTask(id: Int, status: String): Result<TaskOutputModel> {
        if (status !in setOf("pending", "in progress", "completed"))
            return failure(InvalidInput)
        return Handler().servicesHandler {
            transactionManager.run {
                it.taskData.getTaskByID(id) ?: return@run failure(TaskNotFound)
                val task = it.taskData.updateTask(id, status)
                return@run success(TaskOutputModel(task.name, task.userId, task.robotId, task.status))
            }
        }
    }

    fun deleteTask(id: Int): Result<TaskIDOutputModel> =
        Handler().servicesHandler {
            transactionManager.run {
                it.taskData.getTaskByID(id) ?: return@run failure(TaskNotFound)
                it.taskData.deleteTask(id)
                return@run success(TaskIDOutputModel(id))
            }
        }

    fun getTasksByUserID(offset: Int = 0, limit: Int = 0, id: Int): Result<TasksOutputModel> {
        if (offset < 0 || limit < 0)
            return failure(InvalidInput)
        return Handler().servicesHandler {
            transactionManager.run {
                it.usersData.getUserByID(id) ?: return@run failure(UserNotFound)
                val tasks = it.taskData.getTasksByUserID(offset, limit, id)
                return@run success(TasksOutputModel(tasks))
            }
        }
    }


    fun getTasksByRobotID(offset: Int = 0, limit: Int = 0, id: Int): Result<TasksOutputModel> {
        if (offset < 0 || limit < 0)
            return failure(InvalidInput)
        return Handler().servicesHandler {
            transactionManager.run {
                it.robotData.getRobotByID(id) ?: return@run failure(RobotNotFound)
                val tasks = it.taskData.getTasksByRobotID(offset, limit, id)
                return@run success(TasksOutputModel(tasks))
            }
        }
    }

    fun startTask(id: Int): Result<TaskOutputModel> =
        Handler().servicesHandler {
            transactionManager.run {
                val task = it.taskData.getTaskByID(id) ?: return@run failure(TaskNotFound)
                if (task.status != "pending")
                    return@run failure(InvalidInput)

                val startTask = it.taskData.startTask(id)
                val area = startTask.first ?: return@run failure(InvalidInput)
                val time = startTask.second ?: return@run failure(InvalidInput)
                val taskUpdated = it.taskData.updateTask(id, "in progress")
                val startTaskOutput = TaskStartOutputModel(
                    id = id,
                    status = "in progress",
                    name = task.name,
                    areaId = area.id,
                    height = area.height,
                    width = area.width,
                    timeId = time.id,
                    weekDay = time.weekDay,
                    startTime = time.startTime,
                    endTime = time.endTime
                )
                request.startTask(startTaskOutput)

                return@run success(TaskOutputModel(taskUpdated.name, taskUpdated.userId, taskUpdated.robotId, taskUpdated.status))
            }
        }

    fun stopTask(id: Int): Result<TaskOutputModel> =
        Handler().servicesHandler {
            transactionManager.run {
                val task = it.taskData.getTaskByID(id) ?: return@run failure(TaskNotFound)
                if (task.status != "in progress")
                    return@run failure(InvalidInput)

                val taskUpdated= it.taskData.updateTask(id, "pending")
                request.stopTask(id)
                return@run success(TaskOutputModel(taskUpdated.name, taskUpdated.userId, taskUpdated.robotId, taskUpdated.status))
            }
        }
}