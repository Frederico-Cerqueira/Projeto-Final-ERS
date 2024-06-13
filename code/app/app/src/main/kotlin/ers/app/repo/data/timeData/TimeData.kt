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

    /**
     * Method to create a new time in the database
     * @param taskID ID of the task
     * @param startTime Start time of the task
     * @param endTime End time of the task
     * @param weekDay Week day of the task
     * @param description Description of the task
     * @return TimeDto? Returns the time created
     */
    override fun createTime(
        taskID: Int,
        startTime: Time,
        endTime: Time,
        weekDay: String,
        description: String
    ): TimeDto {
        return handle.createUpdate("INSERT INTO time (taskId, start_time, end_time, weekDay, description) VALUES (:taskId, :startTime, :endTime, :weekDay, :description)")
            .bind("taskId", taskID)
            .bind("startTime", startTime)
            .bind("endTime", endTime)
            .bind("weekDay", weekDay)
            .bind("description", description)
            .executeAndReturnGeneratedKeys()
            .map(TimeDtoMapper())
            .first()
    }

    /**
     * Method to update the time of a task
     * @param id ID of the time
     * @param startTime Start time of the task
     * @param endTime End time of the task
     * @param weekDay Week day of the task
     * @return TimeDto? Returns the time updated
     */
    override fun updateTime(id: Int, startTime: Time, endTime: Time, weekDay: String): TimeDto {
        return handle.createUpdate("UPDATE time SET start_time = :startTime, end_time = :endTime, weekDay = :weekDay WHERE id = :id")
            .bind("id", id)
            .bind("startTime", startTime)
            .bind("endTime", endTime)
            .bind("weekDay", weekDay)
            .executeAndReturnGeneratedKeys()
            .map(TimeDtoMapper())
            .first()
    }

    /**
     * Method to update the description of a time
     * @param id ID of the time
     * @param description Description of the task
     * @return TimeDto? Returns the time updated
     */
    override fun updateTimeDescription(id: Int, description: String): TimeDto {
        return handle.createUpdate("UPDATE time SET description = :description WHERE id = :id")
            .bind("id", id)
            .bind("description", description)
            .executeAndReturnGeneratedKeys()
            .map(TimeDtoMapper())
            .first()
    }

    /**
     * Method to delete a time by its id
     * @param id ID of the time
     */
    override fun deleteTime(id: Int) {
        handle.createUpdate("DELETE FROM time WHERE id = :id")
            .bind("id", id)
            .execute()
    }

    /**
     * Method to get a time by its id
     * @param id ID of the time
     * @return TimeDto? Returns the time found
     */
    override fun getTimeByID(id: Int): TimeDto? {
        return handle.createQuery("SELECT * FROM time WHERE ID = :id")
            .bind("id", id)
            .map(TimeDtoMapper())
            .singleOrNull()
    }

    /**
     * Method to get the times of a task with pagination
     * @param taskID ID of the task
     * @param limit Limit of the pagination
     * @param offset Offset of the pagination
     */
    override fun getTimesByTaskID(offset: Int, limit: Int, taskID: Int): List<TimeDto> {
        return handle.createQuery("SELECT * FROM time WHERE taskId = :taskId ORDER BY id LIMIT :limit OFFSET :offset")
            .bind("taskId", taskID)
            .bind("limit", limit)
            .bind("offset", offset)
            .map(TimeDtoMapper())
            .list()
    }

}