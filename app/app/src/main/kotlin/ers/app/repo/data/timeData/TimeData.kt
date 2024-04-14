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
     * @return TimeDto? Returns the time created
     */
    override fun createTime(taskId: Int, startTime: Time, endTime: Time, weekDay: String): TimeDto? {
        val newTime = handle.createUpdate("INSERT INTO time (taskId, start_time, end_time, weekDay) VALUES (:taskId, :startTime, :endTime, :weekDay)")
            .bind("taskId", taskId)
            .bind("startTime", startTime)
            .bind("endTime", endTime)
            .bind("weekDay", weekDay)
            .executeAndReturnGeneratedKeys()
            .map(TimeDtoMapper())
            .singleOrNull()
        return newTime
    }

    /**
     * Method to update the time of a task
     * @param taskId Id of the task
     * @param startTime Start time of the task
     * @param endTime End time of the task
     * @param weekDay Week day of the task
     * @return TimeDto? Returns the time updated
     */
    override fun updateTime(taskId: Int, startTime: Time, endTime: Time, weekDay: String): TimeDto? {
        val time = handle.createUpdate("UPDATE time SET start_time = :startTime, end_time = :endTime, weekDay = :weekDay WHERE taskId = :taskId")
            .bind("taskId", taskId)
            .bind("startTime", startTime)
            .bind("endTime", endTime)
            .bind("weekDay", weekDay)
            .executeAndReturnGeneratedKeys()
            .map(TimeDtoMapper())
            .singleOrNull()
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
     * Method to get a time by its task id
     * @param taskId Id of the task
     * @return TimeDto? Returns the time found
     */
    override fun getTimeByTaskId(taskId: Int): TimeDto? {
        val time = handle.createUpdate("SELECT * FROM time WHERE taskId = :taskId")
            .bind("taskId", taskId)
            .executeAndReturnGeneratedKeys()
            .map(TimeDtoMapper())
            .singleOrNull()
        return time
    }

}