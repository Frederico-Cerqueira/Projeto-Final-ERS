package ers.app.domainEntities.outputModels

import ers.app.repo.dtos.AreaDto

data class AreaOutputModel(
    val id: Int,
    val height: Int,
    val width: Int,
    val taskId: Int,
    val name: String,
    val description: String
)

data class AreaIDOutputModel(val id: Int)
data class AreasOutputModel(val areas: List<AreaDto>)