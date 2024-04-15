package ers.app.repo.data.taskData

import ers.app.repo.dtos.TaskDto
import ers.app.repo.mappers.TaskDtoMapper
import org.jdbi.v3.core.Handle

/**
 * Class that implements the TaskDataI interface and is responsible for the operations related to the task table in the database.
 */
class TaskData(private val handle: Handle) : TaskDataI {

    //PROVAVELMENTE NÃO IRÁ RETORNAR UM DTO MAS UMA TASK NO FUTURO

    /**
     * Function that creates a task in the database.
     * @param name the name of the task.
     * @param userId the id of the user that created the task.
     * @param robotId the id of the robot that will execute the task.
     * @return the task created.
     */
    override fun createTask(name: String, userId: Int, robotId: Int): TaskDto? {
        val newTask =
            handle.createUpdate("INSERT INTO task (name, status, userId, robotId) VALUES (:name, 'pending', :userId, :robotId)")
                .bind("name", name)
                .bind("userId", userId)
                .bind("robotId", robotId)
                .executeAndReturnGeneratedKeys()
                .map(TaskDtoMapper())
                .singleOrNull()
        return newTask
    }

    /**
     * Function that gets a task by its id.
     * @param id the id of the task.
     * @return the task with the id passed as parameter.
     */
    override fun getTaskById(id: Int): TaskDto? {
        val task = handle.createQuery("SELECT * FROM task WHERE id = :id")
            .bind("id", id)
            .map(TaskDtoMapper())
            .singleOrNull()
        return task
    }

    /**
     * Function that updates the status of a task.
     * @param id the id of the task.
     * @param status the new status of the task.
     * @return the task with the updated status.
     */
    override fun updateTask(id: Int, status: String): TaskDto? {
        val task = handle.createUpdate("UPDATE task SET status = :status WHERE id = :id")
            .bind("status", status)
            .bind("id", id)
            .executeAndReturnGeneratedKeys()
            .map(TaskDtoMapper())
            .singleOrNull()
        return task
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
     * @param userId the id of the user.
     * @return the list of tasks of the user with the id passed as parameter.
     */
    override fun getTasksByUserId(offset: Int, limit: Int, userId: Int): List<TaskDto> {
        val tasks = handle.createQuery("SELECT * FROM task WHERE userId = :userId LIMIT :limit OFFSET :offset")
            .bind("userId", userId)
            .bind("limit", limit)
            .bind("offset", offset)
            .map(TaskDtoMapper())
            .list()
        return tasks
    }

    /**
     * Function that gets the tasks of a robot with pagination.
     * @param offset the offset of the pagination.
     * @param limit the limit of the pagination.
     * @param robotId the id of the robot.
     * @return the list of tasks of the robot with the id passed as parameter.
     */
    override fun getTasksByRobotId(offset: Int, limit: Int, robotId: Int): List<TaskDto> {
        val tasks = handle.createQuery("SELECT * FROM task WHERE robotId = :robotId LIMIT :limit OFFSET :offset")
            .bind("robotId", robotId)
            .bind("limit", limit)
            .bind("offset", offset)
            .map(TaskDtoMapper())
            .list()
        return tasks
    }

}