package ers.app.repo.dtos

/**
 * Represents a task data transfer object.
 */
data class TaskDto (
    val id: Int,
    val name: String,
    val userId: Int,
    val robotId: Int,
    val status: String,
)