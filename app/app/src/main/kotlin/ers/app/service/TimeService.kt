package ers.app.service

import ers.app.domainEntities.*
import ers.app.domainEntities.outputModels.TimeIDOutputModel
import ers.app.domainEntities.outputModels.TimeOutputModel
import ers.app.domainEntities.outputModels.TimesOutputModel
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.Error
import org.springframework.stereotype.Component
import java.sql.Time


@Component
class TimeService(private val transactionManager: TransactionManager) {

    fun createTime(taskId: Int, startTime: String, endTime: String, weekDay: String, description: String): TimeResult {
        if (startTime > endTime || startTime.isEmpty() || endTime.isEmpty() || weekDay.isEmpty() || description.isEmpty())
            return failure(Error.InvalidInput)
        if (weekDay !in setOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"))
            return failure(Error.InvalidInput)
        if (description.length > 255)
            return failure(Error.InputTooLong)
        return transactionManager.run {
            try {
                val time =
                    it.timeData.createTime(taskId, Time.valueOf(startTime), Time.valueOf(endTime), weekDay, description)
                success(
                    TimeOutputModel(
                        time.id,
                        time.taskId,
                        time.startTime,
                        time.endTime,
                        time.weekDay,
                        time.description
                    )
                )
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
    }


    fun getTimeByID(id: Int): TimeResult =
        transactionManager.run {
            try {
                val time = it.timeData.getTimeByID(id)
                if (time != null)
                    success(
                        TimeOutputModel(
                            time.id,
                            time.taskId,
                            time.startTime,
                            time.endTime,
                            time.weekDay,
                            time.description
                        )
                    )
                else
                    failure(Error.TimeNotFound)

            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun updateTime(id: Int, startTime: String, endTime: String, weekDay: String): TimeResult {
        if (startTime.isEmpty() || endTime.isEmpty() || weekDay.isEmpty())
            return failure(Error.InvalidInput)
        if (startTime > endTime)
            return failure(Error.InvalidInput)
        if (weekDay !in setOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"))
            return failure(Error.InvalidInput)
        return transactionManager.run {
            try {
                it.timeData.getTimeByID(id) ?: failure(Error.TimeNotFound)
                val time = it.timeData.updateTime(id, Time.valueOf(startTime), Time.valueOf(endTime), weekDay)
                success(
                    TimeOutputModel(
                        time.id,
                        time.taskId,
                        time.startTime,
                        time.endTime,
                        time.weekDay,
                        time.description
                    )
                )
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
    }


    fun updateTimeDescription(id: Int, description: String): TimeResult {
        if (description.isEmpty())
            return failure(Error.InvalidInput)
        if (description.length > 255)
            return failure(Error.InputTooLong)
        return transactionManager.run {
            try {
                it.timeData.getTimeByID(id) ?: failure(Error.TimeNotFound)
                val time = it.timeData.updateTimeDescription(id, description)
                success(
                    TimeOutputModel(
                        time.id,
                        time.taskId,
                        time.startTime,
                        time.endTime,
                        time.weekDay,
                        time.description
                    )
                )
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
    }

    fun deleteTime(id: Int): TimeIDResult =
        transactionManager.run {
            try {
                it.timeData.getTimeByID(id) ?: failure(Error.TimeNotFound)
                it.timeData.deleteTime(id)
                success(TimeIDOutputModel(id))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getTimesByTaskId(offset: Int = 0, limit: Int = 0, taskId: Int): TimesResult {
        if (offset < 0 || limit < 0)
            return failure(Error.InvalidInput)
        return transactionManager.run {
            try {
                it.taskData.getTaskByID(taskId) ?: failure(Error.TaskNotFound)
                val times = it.timeData.getTimesByTaskID(offset, limit, taskId)
                success(TimesOutputModel(times))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
    }

}