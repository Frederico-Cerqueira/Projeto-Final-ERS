package ers.app.repo.data.robotData

import ers.app.repo.dtos.RobotDto

/**
 * Interface that defines the operations related to the robot table in the database.
 */
interface RobotDataI{
    fun getRobotByID(id: Int): RobotDto?
    fun getRobots(offset: Int, limit: Int): List<RobotDto>
    fun createRobot(name: String, characteristics: String): RobotDto
    fun updateRobotStatus(id: Int, status: String): RobotDto
    fun deleteRobot(id: Int)
    fun getRobotByUserID(offset: Int, limit: Int, userID: Int): List<RobotDto>
}