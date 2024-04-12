package ers.app.repo.dtos

data class TaskDto (
    val id: Int,
    val name: String,
    val userId: Int,
    val robotId: Int,
    val status: String,
)