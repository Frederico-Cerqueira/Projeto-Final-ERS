package ers.app.repo.data.timeData

import ers.app.repo.dtos.TimeDto
import ers.app.repo.mappers.TimeDtoMapper
import org.jdbi.v3.core.Handle
import java.sql.Time

/**
 * This class is responsible for handling the data of the time
 * @param handle: Handle to connect to the database
 */
class TimeData (private val handle: Handle) : TimeDataI {

    //PROVAVELMENTE NÃO IRÁ RETORNAR UM DTO MAS UM TIME NO FUTURO

    /**
     * Method to create a new time in the database
     * @param taskId Id of the task
     * @param startTime Start time of the task
     * @param endTime End time of the task
     * @param weekDay Week day of the task
     * @param description Description of the task
     * @return TimeDto? Returns the time created
     */
    override fun createTime(
        taskId: Int,
        startTime: Time,
        endTime: Time,
        weekDay: String,
        description: String
    ): TimeDto {
        val newTime = handle.createUpdate("INSERT INTO time (taskId, start_time, end_time, weekDay, description) VALUES (:taskId, :startTime, :endTime, :weekDay, :description)")
            .bind("taskId", taskId)
            .bind("startTime", startTime)
            .bind("endTime", endTime)
            .bind("weekDay", weekDay)
            .bind("description", description)
            .executeAndReturnGeneratedKeys()
            .map(TimeDtoMapper())
            .first()
        return newTime
    }

    /**
     * Method to update the time of a task
     * @param id Id of the time
     * @param startTime Start time of the task
     * @param endTime End time of the task
     * @param weekDay Week day of the task
     * @return TimeDto? Returns the time updated
     */
    override fun updateTime(id: Int, startTime: Time, endTime: Time, weekDay: String): TimeDto {
        val time = handle.createUpdate("UPDATE time SET start_time = :startTime, end_time = :endTime, weekDay = :weekDay WHERE id = :id")
            .bind("taskId", id)
            .bind("startTime", startTime)
            .bind("endTime", endTime)
            .bind("weekDay", weekDay)
            .executeAndReturnGeneratedKeys()
            .map(TimeDtoMapper())
            .first()
        return time
    }

    /**
     * Method to update the description of a time
     * @param id Id of the time
     * @param description Description of the task
     * @return TimeDto? Returns the time updated
     */
    override fun updateTimeDescription(id: Int, description: String): TimeDto {
        val time = handle.createUpdate("UPDATE time SET description = :description WHERE id = :id")
            .bind("taskId", id)
            .bind("description", description)
            .executeAndReturnGeneratedKeys()
            .map(TimeDtoMapper())
            .first()
        return time
    }

    /**
     * Method to delete a time by its id
     * @param id Id of the time
     */
    override fun deleteTime(id: Int) {
        handle.createUpdate("DELETE FROM time WHERE id = :id")
            .bind("id", id)
            .execute()
    }

    /**
     * Method to get a time by its id
     * @param id Id of the time
     * @return TimeDto? Returns the time found
     */
    override fun getTimeById(id: Int): TimeDto? {
        val time = handle.createUpdate("SELECT * FROM time WHERE id = :id")
            .bind("id", id)
            .executeAndReturnGeneratedKeys()
            .map(TimeDtoMapper())
            .singleOrNull()
        return time
    }

    /**
     * Method to get the times of a task with pagination
     * @param taskId Id of the task
     * @param limit Limit of the pagination
     * @param offset Offset of the pagination
     */
    override fun getTimesByTaskId(offset: Int, limit: Int, taskId: Int): List<TimeDto> {
        val time = handle.createUpdate("SELECT * FROM time WHERE taskId = :taskId LIMIT :limit OFFSET :offset")
            .bind("taskId", taskId)
            .bind("limit", limit)
            .bind("offset", offset)
            .executeAndReturnGeneratedKeys()
            .map(TimeDtoMapper())
            .list()
        return time
    }

}