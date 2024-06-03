package ers.app.service

import ers.app.domainEntities.*
import ers.app.domainEntities.outputModels.RobotIDOutputModel
import ers.app.domainEntities.outputModels.RobotOutputModel
import ers.app.domainEntities.outputModels.RobotsOutputModel
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.Errors
import org.springframework.stereotype.Component

@Component
class RobotService(private val transactionManager: TransactionManager) {

    fun createRobot(name: String, characteristics: String): RobotResult {
        if (name.isEmpty() || characteristics.isEmpty())
            return failure(Errors.InvalidInput)
        if (name.length > 255 || characteristics.length > 255)
            return failure(Errors.InputTooLong)
        return transactionManager.run {
            try {
                val robot = it.robotData.createRobot(name, characteristics)
                success(RobotOutputModel(robot.id, robot.name, robot.status, robot.characteristics))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }
    }

    fun getRobots(offset: Int, limit: Int): RobotsResult {
        if (offset < 0 || limit < 0)
            return failure(Errors.InvalidInput)
        return transactionManager.run {
            try {
                val robots = it.robotData.getRobots(offset, limit)
                success(RobotsOutputModel(robots))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }
    }

    fun getRobotByID(id: Int): RobotResult =
        transactionManager.run {
            try {
                val robot = it.robotData.getRobotByID(id)
                if (robot != null)
                    success(RobotOutputModel(robot.id, robot.name, robot.status, robot.characteristics))
                else
                    failure(Errors.RobotNotFound)
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }

    fun updateRobotStatus(id: Int, status: String): RobotResult {
        if (status !in setOf("available", "busy", "maintenance", "unavailable", "charging", "error"))
            return failure(Errors.InvalidInput)
        return transactionManager.run {
            try {
                it.robotData.getRobotByID(id) ?: return@run failure(Errors.RobotNotFound)
                val robot = it.robotData.updateRobotStatus(id, status)
                success(RobotOutputModel(robot.id, robot.name, robot.status, robot.characteristics))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }

    }


    fun deleteRobot(id: Int): RobotIDResult =
        transactionManager.run {
            try {
                it.robotData.getRobotByID(id) ?: failure(Errors.RobotNotFound)
                it.robotData.deleteRobot(id)
                success(RobotIDOutputModel(id))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }

    fun getRobotByUserID(offset: Int = 0, limit: Int = 0, id: Int): RobotsResult {
        if (offset < 0 || limit < 0)
            return failure(Errors.InvalidInput)
        return transactionManager.run {
            try {
                it.usersData.getUserByID(id) ?: failure(Errors.UserNotFound)
                val robot = it.robotData.getRobotByUserID(offset, limit, id)
                success(RobotsOutputModel(robot))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }
    }

}