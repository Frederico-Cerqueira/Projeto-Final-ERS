package ers.app.repo.data.taskData

import ers.app.repo.dtos.TaskDto
import ers.app.repo.mappers.TaskDtoMapper
import org.jdbi.v3.core.Handle

/**
 * Class that implements the TaskDataI interface and is responsible for the operations related to the task table in the database.
 */
class TaskData(private val handle: Handle) : TaskDataI {

    /**
     * Function that creates a task in the database.
     * @param name the name of the task.
     * @param userID the id of the user that created the task.
     * @param robotId the id of the robot that will execute the task.
     * @return the task created.
     */
    override fun createTask(name: String, userID: Int, robotId: Int): TaskDto {
        return handle.createUpdate("INSERT INTO task (name, status, userId, robotId) VALUES (:name, 'pending', :userId, :robotId)")
            .bind("name", name)
            .bind("userId", userID)
            .bind("robotId", robotId)
            .executeAndReturnGeneratedKeys()
            .map(TaskDtoMapper())
            .first()
    }

    /**
     * Function that gets a task by its id.
     * @param id the id of the task.
     * @return the task with the id passed as parameter.
     */
    override fun getTaskByID(id: Int): TaskDto? {
        return handle.createQuery("SELECT * FROM task WHERE id = :id")
            .bind("id", id)
            .map(TaskDtoMapper())
            .singleOrNull()
    }

    /**
     * Function that updates the status of a task.
     * @param id the id of the task.
     * @param status the new status of the task.
     * @return the task with the updated status.
     */
    override fun updateTask(id: Int, status: String): TaskDto {
        return handle.createUpdate("UPDATE task SET status = :status WHERE id = :id")
            .bind("status", status)
            .bind("id", id)
            .executeAndReturnGeneratedKeys()
            .map(TaskDtoMapper())
            .first()
    }

    /**
     * Function that deletes a task by its id.
     * @param id the id of the task.
     */
    override fun deleteTask(id: Int) {
        handle.createUpdate("DELETE FROM task WHERE id = :id")
            .bind("id", id)
            .execute()
    }

    /**
     * Function that gets the tasks of a user with pagination.
     * @param offset the offset of the pagination.
     * @param limit the limit of the pagination.
     * @param userID the id of the user.
     * @return the list of tasks of the user with the id passed as parameter.
     */
    override fun getTasksByUserID(offset: Int, limit: Int, userID: Int): List<TaskDto> {
        return handle.createQuery("SELECT * FROM task WHERE userId = :userId ORDER BY id LIMIT :limit OFFSET :offset")
            .bind("userId", userID)
            .bind("limit", limit)
            .bind("offset", offset)
            .map(TaskDtoMapper())
            .list()
    }

    /**
     * Function that gets the tasks of a robot with pagination.
     * @param offset the offset of the pagination.
     * @param limit the limit of the pagination.
     * @param robotID the id of the robot.
     * @return the list of tasks of the robot with the id passed as parameter.
     */
    override fun getTasksByRobotID(offset: Int, limit: Int, robotID: Int): List<TaskDto> {
        return handle.createQuery("SELECT * FROM task WHERE robotId = :robotId ORDER BY id LIMIT :limit OFFSET :offset")
            .bind("robotId", robotID)
            .bind("limit", limit)
            .bind("offset", offset)
            .map(TaskDtoMapper())
            .list()
    }

}