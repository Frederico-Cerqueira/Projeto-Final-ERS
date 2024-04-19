package ers.app.repo.data.robotData

import ers.app.repo.dtos.RobotDto

/**
 * Interface that defines the operations related to the robot table in the database.
 */
interface RobotDataI{
    fun getRobotById(id: Int): RobotDto?
    fun createRobot(name: String, characteristics: String): RobotDto
    fun updateRobotStatus(id: Int, status: String): RobotDto
    fun deleteRobot(id: Int)
    fun getRobotByUserId(offset: Int, limit: Int, userId: Int): List<RobotDto>
}