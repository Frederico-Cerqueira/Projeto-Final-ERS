package ers.app.repo.data.usersData

import ers.app.repo.dtos.UserDto

/**
 * Interface that defines the operations related to the user table in the database.
 */
interface UsersDataI {
    fun createUser(name: String, email: String, password: String) : UserDto?
    fun getUserById(id: Int) : UserDto?
    fun getUserByToken(token : String) : UserDto?
    fun loginUser(email: String, password: String) : UserDto?
}