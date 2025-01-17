package ers.app.repo.dtos

/**
 * Represents a time data transfer object.
 */
data class TimeDto (
    val id: Int,
    val taskId: Int,
    val weekDay : String,
    val startTime : String,
    val endTime : String,
    val description : String
)