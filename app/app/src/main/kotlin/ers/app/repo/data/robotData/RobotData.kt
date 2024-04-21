package ers.app.repo.data.robotData

import ers.app.repo.dtos.RobotDto
import ers.app.repo.mappers.RobotDtoMapper
import org.jdbi.v3.core.Handle

/**
 * This class is responsible for handling the data of the robot
 * @param handle: Handle to connect to the database
 */
class RobotData(private val handle: Handle) : RobotDataI {

    //PROVAVELMENTE NÃO IRÁ RETORNAR UM DTO MAS UM ROBOT NO FUTURO

    /**
     * Method to get a robot by its id
     * @param id Id of the robot
     * @return RobotDto? Returns the robot found
     */
    override fun getRobotByID(id: Int): RobotDto? {
        val robot = handle.createUpdate("SELECT * FROM robot WHERE id = :id")
            .bind("id", id)
            .executeAndReturnGeneratedKeys()
            .map(RobotDtoMapper())
            .singleOrNull()
        return robot
    }

    /**
     * Method to create a new robot in the database
     * @param name Name of the robot
     * @param characteristics Characteristics of the robot
     * @return RobotDto? Returns the robot created
     */
    override fun createRobot(name: String, characteristics: String): RobotDto {
        val newRobot =
            handle.createUpdate("INSERT INTO robot (name, characteristics, status) VALUES (:name, :characteristics, 'available')")
                .bind("name", name)
                .bind("characteristics", characteristics)
                .executeAndReturnGeneratedKeys()
                .map(RobotDtoMapper())
                .first()
        return newRobot
    }

    /**
     * Method to update the status of a robot
     * @param id Id of the robot
     * @param status New status of the robot
     * @return RobotDto? Returns the robot updated
     */
    override fun updateRobotStatus(id: Int, status: String): RobotDto {
        val robot = handle.createUpdate("UPDATE robot SET status = :status WHERE id = :id")
            .bind("status", status)
            .bind("id", id)
            .executeAndReturnGeneratedKeys()
            .map(RobotDtoMapper())
            .first()
        return robot
    }

    /**
     * Method to delete a robot from the database
     * @param id Id of the robot
     */
    override fun deleteRobot(id: Int) {
        handle.createUpdate("DELETE FROM robot WHERE id = :id")
            .bind("id", id)
            .execute()
    }

    /**
     * Function that gets the robots of a user with pagination.
     * @param offset the offset of the pagination.
     * @param limit the limit of the pagination.
     * @param userID the id of the user.
     * @return the list of robots of the user with the id passed as parameter.
     */
    override fun getRobotByUserID(offset: Int, limit: Int, userID: Int): List<RobotDto> {
        val robots =
            handle.createQuery("SELECT robot.id, robot.name, robot.status, characteristics FROM robot JOIN TASK ON task.robotid == robot.id WHERE userid = :userId LIMIT :limit OFFSET :offset")
                .bind("userId", userID)
                .bind("limit", limit)
                .bind("offset", offset)
                .map(RobotDtoMapper())
                .list()
        return robots
    }
}
