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

    fun createTime(taskId: Int, startTime: String, endTime: String, weekDay: String, description: String): TimeResult =
        transactionManager.run {
            try {
                if (startTime > endTime || startTime.isEmpty() || endTime.isEmpty() || weekDay.isEmpty() || description.isEmpty())
                    failure(Error.InvalidInput)
                if (weekDay !in setOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"))
                    failure(Error.InvalidInput)
                if (description.length > 255)
                    failure(Error.InputTooLong)
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

    fun getTimeByID(id: Int): TimeResult =
        transactionManager.run {
            try {
                val time = it.timeData.getTimeById(id)
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

    fun updateTime(id: Int, startTime: String, endTime: String, weekDay: String): TimeResult =
        transactionManager.run {
            try {
                it.timeData.getTimeById(id) ?: failure(Error.TimeNotFound)
                if (startTime > endTime || startTime.isEmpty() || endTime.isEmpty() || weekDay.isEmpty())
                    failure(Error.InvalidInput)
                if (weekDay !in setOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"))
                    failure(Error.InvalidInput)
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

    fun updateTimeDescription(id: Int, description: String): TimeResult =
        transactionManager.run {
            try {
                it.timeData.getTimeById(id) ?: failure(Error.TimeNotFound)
                if (description.length > 255)
                    failure(Error.InputTooLong)
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

    fun deleteTime(id: Int): TimeIDResult =
        transactionManager.run {
            try {
                it.timeData.getTimeById(id) ?: failure(Error.TimeNotFound)
                it.timeData.deleteTime(id)
                success(TimeIDOutputModel(id))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getTimesByTaskId(offset: Int = 0, limit: Int = 0, taskId: Int): TimesResult =
        transactionManager.run {
            try {
                it.taskData.getTaskById(taskId) ?: failure(Error.TaskNotFound)
                if (offset < 0 || limit < 0)
                    failure(Error.InvalidInput)
                val times = it.timeData.getTimesByTaskId(offset, limit, taskId)
                success(TimesOutputModel(times))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
}