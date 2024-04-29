package ers.app.domainEntities.outputModels

import ers.app.repo.dtos.RobotDto

data class RobotOutputModel(
    val id: Int,
    val name: String,
    val status: String,
    val characteristics: String
)

data class RobotIDOutputModel(val id: Int)
data class RobotsOutputModel(val robots: List<RobotDto>)
