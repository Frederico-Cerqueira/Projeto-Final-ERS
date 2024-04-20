package ers.app.service

import ers.app.domainEntities.*
import ers.app.domainEntities.outputModels.RobotIDOutputModel
import ers.app.domainEntities.outputModels.RobotOutputModel
import ers.app.domainEntities.outputModels.RobotsOutputModel
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.Error
import org.springframework.stereotype.Component

@Component
class RobotService(private val transactionManager: TransactionManager) {

    fun createRobot(name: String, characteristics: String): RobotResult =
        transactionManager.run {
            try {
                if (name.length > 255 || characteristics.length > 255)
                    failure(Error.InputTooLong)
                if (name.isEmpty() || characteristics.isEmpty())
                    failure(Error.InvalidInput)
                val robot = it.robotData.createRobot(name, characteristics)
                success(RobotOutputModel(robot.id, robot.name, robot.status, robot.characteristics))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }


    fun getRobotByID(id: Int): RobotResult =
        transactionManager.run {
            try {
                val robot = it.robotData.getRobotById(id)
                if (robot != null)
                    success(RobotOutputModel(robot.id, robot.name, robot.status, robot.characteristics))
                else
                    failure(Error.RobotNotFound)
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun updateRobotStatus(id: Int, status: String): RobotResult =
        transactionManager.run {
            try {
                it.robotData.getRobotById(id) ?: failure(Error.RobotNotFound)
                if (status !in setOf("available", "busy", "maintenance", "unavailable", "charging", "error"))
                    failure(Error.InvalidInput)
                val robot = it.robotData.updateRobotStatus(id, status)
                success(RobotOutputModel(robot.id, robot.name, robot.status, robot.characteristics))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun deleteRobot(id: Int): RobotIDResult =
        transactionManager.run {
            try {
                it.robotData.getRobotById(id) ?: failure(Error.RobotNotFound)
                it.robotData.deleteRobot(id)
                success(RobotIDOutputModel(id))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getRobotByUserID(offset: Int = 0, limit: Int = 0, id: Int): RobotsResult =
        transactionManager.run {
            try {
                it.usersData.getUserById(id) ?: failure(Error.UserNotFound)
                if (offset < 0 || limit < 0)
                    failure(Error.InvalidInput)
                val robot = it.robotData.getRobotByUserId(offset, limit, id)
                success(RobotsOutputModel(robot))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
}