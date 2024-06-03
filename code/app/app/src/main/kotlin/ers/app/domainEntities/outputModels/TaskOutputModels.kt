package ers.app.domainEntities.outputModels

import ers.app.repo.dtos.TaskDto

data class TaskOutputModel(val name: String, val userId: Int, val robotId: Int, val status: String)
data class TaskIDOutputModel(val id: Int)
data class TasksOutputModel(val tasks: List<TaskDto>)