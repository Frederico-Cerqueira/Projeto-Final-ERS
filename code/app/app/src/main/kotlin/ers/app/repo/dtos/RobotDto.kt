package ers.app.repo.dtos

/**
 * Data transfer object for Robot.
 */
data class RobotDto (
    val id : Int,
    val name : String,
    val status : String,
    val characteristics: String
)