package ers.app.service

import ers.app.domainEntities.outputModels.RobotIDOutputModel
import ers.app.domainEntities.outputModels.RobotOutputModel
import ers.app.domainEntities.outputModels.RobotsOutputModel
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.*
import ers.app.utils.errors.*
import org.springframework.stereotype.Component

@Component
class RobotService(private val transactionManager: TransactionManager) {

    fun createRobot(name: String, characteristics: String): Result<RobotOutputModel> {
        if (name.isEmpty() || characteristics.isEmpty())
            return failure(InvalidInput)
        if (name.length > 255 || characteristics.length > 255)
            return failure(InputTooLong)
        return Handler().servicesHandler {
            transactionManager.run {
                val robot = it.robotData.createRobot(name, characteristics)
                return@run success(RobotOutputModel(robot.id, robot.name, robot.status, robot.characteristics))
            }
        }
    }

    fun getRobots(offset: Int, limit: Int): Result<RobotsOutputModel> {
        if (offset < 0 || limit < 0)
            return failure(InvalidInput)
        return Handler().servicesHandler {
            transactionManager.run {
                val robots = it.robotData.getRobots(offset, limit)
                return@run success(RobotsOutputModel(robots))
            }
        }
    }

    fun getRobotByID(id: Int): Result<RobotOutputModel> =
        Handler().servicesHandler {
            transactionManager.run {
                val robot = it.robotData.getRobotByID(id) ?: return@run failure(RobotNotFound)
                return@run success(RobotOutputModel(robot.id, robot.name, robot.status, robot.characteristics))
            }
        }

    fun updateRobotStatus(id: Int, status: String): Result<RobotOutputModel> {
        if (status !in setOf("available", "busy", "maintenance", "unavailable", "charging", "error"))
            return failure(InvalidInput)
        return Handler().servicesHandler {
            transactionManager.run {
                it.robotData.getRobotByID(id) ?: return@run failure(RobotNotFound)
                val robot = it.robotData.updateRobotStatus(id, status)
                return@run success(RobotOutputModel(robot.id, robot.name, robot.status, robot.characteristics))
            }
        }
    }


    fun deleteRobot(id: Int): Result<RobotIDOutputModel> =
        Handler().servicesHandler {
            transactionManager.run {
                it.robotData.getRobotByID(id) ?: return@run failure(RobotNotFound)
                it.robotData.deleteRobot(id)
                return@run success(RobotIDOutputModel(id))
            }
        }


    fun getRobotByUserID(offset: Int = 0, limit: Int = 0, id: Int): Result<RobotsOutputModel> {
        if (offset < 0 || limit < 0)
            return failure(InvalidInput)

        return Handler().servicesHandler {
            transactionManager.run {
                it.usersData.getUserByID(id) ?: return@run failure(UserNotFound)
                val robot = it.robotData.getRobotByUserID(offset, limit, id)
                return@run success(RobotsOutputModel(robot))
            }
        }
    }

}