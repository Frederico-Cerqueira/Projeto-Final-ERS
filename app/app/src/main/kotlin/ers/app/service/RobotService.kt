package ers.app.service

import ers.app.domainEntities.*
import ers.app.repo.dtos.RobotDto
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.Error
import org.springframework.stereotype.Component

data class RobotOutputModel(
    val id: Int,
    val name: String,
    val status: String,
    val characteristics: String
)

data class RobotIDOutputModel(val id: Int)
data class RobotsOutputModel(val tasks: List<RobotDto>)

@Component
class RobotService(private val transactionManager: TransactionManager) {

    fun createRobot(name: String, characteristics: String): RobotResult =
        transactionManager.run {
            try {
                val robot = it.robotData.createRobot(name, characteristics)
                success(RobotOutputModel(robot.id, robot.name, robot.status, robot.characteristics))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }


    fun getRobotById(id: Int): RobotResult =
        transactionManager.run {
            try {
                val robot = it.robotData.getRobotById(id)
                if (robot != null) {
                    success(RobotOutputModel(robot.id, robot.name, robot.status, robot.characteristics))
                } else {
                    failure(Error.RobotNotFound)
                }
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun updateRobotStatus(id: Int, status: String): RobotResult =
        transactionManager.run {
            try {
                if (it.robotData.getRobotById(id) == null)
                    failure(Error.RobotNotFound)
                val robot = it.robotData.updateRobotStatus(id, status)
                success(RobotOutputModel(robot.id, robot.name, robot.status, robot.characteristics))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun deleteRobot(id: Int): RobotIDResult =
        transactionManager.run {
            try {
                if (it.robotData.getRobotById(id) == null)
                    failure(Error.RobotNotFound)
                it.robotData.deleteRobot(id)
                success(RobotIDOutputModel(id))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getRobotByUserId(offset: Int, limit: Int, id: Int): RobotsResult =
        transactionManager.run {
            try {
                val user = it.usersData.getUserById(id)
                if (user == null)
                    failure(Error.UserNotFound)
                val robot = it.robotData.getRobotByUserId(offset, limit, id)
                success(RobotsOutputModel(robot))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }


}