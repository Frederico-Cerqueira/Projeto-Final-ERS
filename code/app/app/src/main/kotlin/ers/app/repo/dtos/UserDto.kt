package ers.app.repo.dtos

/**
 * Represents a user data transfer object.
 */
data class UserDto (
    val id: Int,
    val name: String,
    val email: String,
    val password: String, //hash of the password
    val token: String,
)

