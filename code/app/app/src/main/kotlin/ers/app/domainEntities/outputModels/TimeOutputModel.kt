package ers.app.domainEntities.outputModels

import ers.app.repo.dtos.TimeDto

data class TimeOutputModel(
    val id: Int,
    val taskId: Int,
    val weekDay: String,
    val startTime: String,
    val endTime: String,
    val description: String
)

data class TimeIDOutputModel(val id: Int)
data class TimesOutputModel(val times: List<TimeDto>)