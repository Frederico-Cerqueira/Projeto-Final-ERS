package ers.app.service

import ers.app.domainEntities.outputModels.TimeIDOutputModel
import ers.app.domainEntities.outputModels.TimeOutputModel
import ers.app.domainEntities.outputModels.TimesOutputModel
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.*
import ers.app.utils.errors.*
import org.springframework.stereotype.Component
import java.sql.Time


@Component
class TimeService(private val transactionManager: TransactionManager) {

    fun createTime(taskId: Int, startTime: String, endTime: String, weekDay: String, description: String): Result<TimeOutputModel> {
        if (startTime > endTime || startTime.isEmpty() || endTime.isEmpty() || weekDay.isEmpty() || description.isEmpty())
            return failure(InvalidInput)
        if (weekDay !in setOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"))
            return failure(InvalidInput)
        if (description.length > 255)
            return failure(InputTooLong)
        return Handler().servicesHandler {
            transactionManager.run {
                val time =
                    it.timeData.createTime(taskId, Time.valueOf(startTime), Time.valueOf(endTime), weekDay, description)
                return@run success(
                    TimeOutputModel(
                        time.id,
                        time.taskId,
                        time.weekDay,
                        time.startTime,
                        time.endTime,
                        time.description
                    )
                )
            }
        }
    }


    fun getTimeByID(id: Int): Result<TimeOutputModel> =
        Handler().servicesHandler {
            transactionManager.run {
                val time = it.timeData.getTimeByID(id) ?: return@run failure(TimeNotFound)
                return@run success(
                    TimeOutputModel(
                        time.id,
                        time.taskId,
                        time.weekDay,
                        time.startTime,
                        time.endTime,
                        time.description
                    )
                )
            }
        }

    fun updateTime(id: Int, startTime: String, endTime: String, weekDay: String): Result<TimeOutputModel> {
        if (startTime.isEmpty() || endTime.isEmpty() || weekDay.isEmpty())
            return failure(InvalidInput)
        if (startTime > endTime)
            return failure(InvalidInput)
        if (weekDay !in setOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"))
            return failure(InvalidInput)
        return Handler().servicesHandler {
            transactionManager.run {
                it.timeData.getTimeByID(id) ?: return@run failure(TimeNotFound)
                val time = it.timeData.updateTime(id, Time.valueOf(startTime), Time.valueOf(endTime), weekDay)
                return@run success(
                    TimeOutputModel(
                        time.id,
                        time.taskId,
                        time.weekDay,
                        time.startTime,
                        time.endTime,
                        time.description
                    )
                )
            }
        }
    }


    fun updateTimeDescription(id: Int, description: String): Result<TimeOutputModel> {
        if (description.isEmpty())
            return failure(InvalidInput)
        if (description.length > 255)
            return failure(InputTooLong)
        return Handler().servicesHandler {
            transactionManager.run {
                it.timeData.getTimeByID(id) ?: return@run failure(TimeNotFound)
                val time = it.timeData.updateTimeDescription(id, description)
                return@run success(
                    TimeOutputModel(
                        time.id,
                        time.taskId,
                        time.startTime,
                        time.endTime,
                        time.weekDay,
                        time.description
                    )
                )
            }
        }
    }

    fun deleteTime(id: Int): Result<TimeIDOutputModel> =
        Handler().servicesHandler {
            transactionManager.run {
                it.timeData.getTimeByID(id) ?: return@run failure(TimeNotFound)
                it.timeData.deleteTime(id)
                return@run success(TimeIDOutputModel(id))
            }
        }

    fun getTimesByTaskId(offset: Int = 0, limit: Int = 0, taskId: Int): Result<TimesOutputModel> {
        if (offset < 0 || limit < 0)
            return failure(InvalidInput)
        return Handler().servicesHandler {
            transactionManager.run {
                it.taskData.getTaskByID(taskId) ?: return@run failure(TaskNotFound)
                val times = it.timeData.getTimesByTaskID(offset, limit, taskId)
                return@run success(TimesOutputModel(times))
            }
        }
    }

}