package ers.app.repo.dtos

data class UserDto (
    val id: Int,
    val name: String,
    val email: String,
    val password: String, //hash of the password
    val token: String,
)

