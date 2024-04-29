package ers.app.repo.dtos

/**
 * Data transfer object for Area.
 */
data class AreaDto (
    val id : Int,
    val taskId: Int,
    val height: Int,
    val width: Int,
    val name: String,
    val description: String
)