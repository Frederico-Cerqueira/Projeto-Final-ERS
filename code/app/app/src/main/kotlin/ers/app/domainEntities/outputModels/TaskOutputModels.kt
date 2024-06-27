package ers.app.domainEntities.outputModels

import ers.app.repo.dtos.TaskDto
import kotlinx.serialization.Serializable


data class TaskOutputModel(val name: String, val userId: Int, val robotId: Int, val status: String)
data class TaskIDOutputModel(val id: Int)
data class TasksOutputModel(val tasks: List<TaskDto>)

@Serializable
data class TaskStartOutputModel(
    val id: Int,
    val status: String,
    val name: String,
    val areaId: Int,
    val height: Int,
    val width: Int,
    val timeId: Int,
    val weekDay: String,
    val startTime: String,
    val endTime: String
)