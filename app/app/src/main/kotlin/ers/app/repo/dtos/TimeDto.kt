package ers.app.repo.dtos

data class TimeDto (
    val id: Int,
    val taskId: Int,
    val weekDay : String,
    val startHour : String,
    val endHour : String,
)